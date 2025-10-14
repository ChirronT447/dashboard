package com.gateway.dashboard.interviews.cs.jit;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A semaphore-based rate limiter that replenishes permits just-in-time,
 * without using a dedicated background thread pool.
 *
 * This implementation is thread-safe.
 */
public final class JustInTimeRateLimiter {

    private final Semaphore semaphore;
    private final int maxPermits;
    private final long replenishmentPeriodNanos;
    private final int permitsPerPeriod;
    private final Lock replenishmentLock = new ReentrantLock();

    private long lastReplenishmentTimeNanos;

    private JustInTimeRateLimiter(int maxPermits, int initialPermits, Rate rate) {
        this.semaphore = new Semaphore(initialPermits);
        this.maxPermits = maxPermits;
        this.replenishmentPeriodNanos = rate.getPeriodNanos();
        this.permitsPerPeriod = (int) rate.permits();
        this.lastReplenishmentTimeNanos = System.nanoTime();
    }

    /**
     * Attempts to acquire a single permit without blocking.
     *
     * @return true if a permit was acquired, false otherwise.
     */
    public boolean tryAcquire() {
        replenish();
        return semaphore.tryAcquire();
    }

    /**
     * Attempts to acquire a single permit, waiting up to the specified timeout.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return true if a permit was acquired, false if the waiting time elapsed.
     * @throws InterruptedException if the current thread is interrupted.
     */
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        replenish();
        return semaphore.tryAcquire(timeout, unit);
    }

    /**
     * Acquires a permit, blocking until one is available.
     *
     * @throws InterruptedException if the current thread is interrupted.
     */
    public void acquire() throws InterruptedException {
        replenish();
        semaphore.acquire();
    }

    /**
     * The core logic. Calculates how many permits should be added to the semaphore
     * based on the time elapsed since the last replenishment.
     * This method is thread-safe.
     */
    private void replenish() {
        replenishmentLock.lock();
        try {
            long now = System.nanoTime();
            long elapsedNanos = now - lastReplenishmentTimeNanos;

            if (elapsedNanos < replenishmentPeriodNanos) {
                return; // Not enough time has passed to add even one batch of permits
            }

            long periodsPassed = elapsedNanos / replenishmentPeriodNanos;
            long permitsToAdd = periodsPassed * permitsPerPeriod;

            if (permitsToAdd > 0) {
                int currentPermits = semaphore.availablePermits();
                // Cap the total permits at maxPermits
                long newPermits = Math.min((long) currentPermits + permitsToAdd, maxPermits);
                int permitsToRelease = (int) (newPermits - currentPermits);

                if (permitsToRelease > 0) {
                    semaphore.release(permitsToRelease);
                }

                // Advance the last replenishment time to prevent "drift"
                lastReplenishmentTimeNanos += periodsPassed * replenishmentPeriodNanos;
            }
        } finally {
            replenishmentLock.unlock();
        }
    }

    public int availablePermits() {
        // It's good practice to replenish before checking for an up-to-date value
        replenish();
        return semaphore.availablePermits();
    }

    // --- Builder for clean instantiation ---

    public static class Builder {
        private int maxPermits;
        private int initialPermits = 0;
        private Rate rate;

        public Builder withMaxPermits(int maxPermits) {
            if (maxPermits <= 0) throw new IllegalArgumentException("Max permits must be positive.");
            this.maxPermits = maxPermits;
            return this;
        }

        public Builder withInitialPermits(int initialPermits) {
            this.initialPermits = initialPermits;
            return this;
        }

        public Builder withRate(Rate rate) {
            this.rate = rate;
            return this;
        }

        public JustInTimeRateLimiter build() {
            if (maxPermits <= 0) throw new IllegalStateException("Max permits must be set.");
            if (initialPermits > maxPermits) throw new IllegalStateException("Initial permits cannot exceed max permits.");
            if (rate == null) throw new IllegalStateException("Rate must be set.");
            return new JustInTimeRateLimiter(maxPermits, initialPermits, rate);
        }
    }
}