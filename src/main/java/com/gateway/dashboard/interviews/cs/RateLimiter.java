package com.gateway.dashboard.interviews.cs;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * A rate limiter implemented using only the core Java library.
 * This class is thread-safe.
 *
 * It uses a token bucket algorithm, where a Semaphore represents the bucket
 * and a ScheduledExecutorService replenishes the tokens.
 */
public final class RateLimiter implements AutoCloseable {

    private final Semaphore semaphore;
    private final int maxPermits;
    private final ScheduledExecutorService replenisher;

    /**
     * Creates a RateLimiter.
     *
     * @param permits The number of permits allowed per replenishment period.
     * @param period  The time period after which permits are replenished.
     */
    private RateLimiter(int permits, Duration period) {
        if (permits <= 0) {
            throw new IllegalArgumentException("Number of permits must be positive.");
        }
        if (period.isNegative() || period.isZero()) {
            throw new IllegalArgumentException("Replenishment period must be positive.");
        }

        this.replenisher = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
        this.semaphore = new Semaphore(permits);
        this.maxPermits = permits;

        // Schedule the replenishment task.
        long periodMillis = period.toMillis();
        this.replenisher.scheduleAtFixedRate(this::replenish, periodMillis, periodMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Factory method to create a RateLimiter.
     * @param permits The number of permits allowed per time period.
     * @param period  The time period (e.g., Duration.ofSeconds(1)).
     * @return A new instance of RateLimiter.
     */
    public static RateLimiter create(int permits, Duration period) {
        return new RateLimiter(permits, period);
    }

    /**
     * Acquires a permit from the rate limiter, blocking until one is available.
     * This method is interruptible.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    /**
     * Attempts to acquire a permit without blocking.
     * @return true if a permit was acquired, false otherwise.
     */
    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    private void replenish() {
        // Calculate how many permits to add to avoid exceeding the max.
        int permitsToAdd = maxPermits - semaphore.availablePermits();
        if (permitsToAdd > 0) {
            semaphore.release(permitsToAdd);
        }
    }

    /**
     * Shuts down the background replenishment thread. This should be called to ensure graceful application shutdown.
     */
    @Override
    public void close() {
        replenisher.shutdown();
    }

    // --- Example Usage ---
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting rate limiter demo: 5 permits per second.");

        // Use try-with-resources to ensure the limiter is closed automatically.
        try (var rateLimiter = RateLimiter.create(5, Duration.ofSeconds(1))) {

            // Use a virtual thread executor to simulate many concurrent requests.
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                // Submit 20 tasks that each need a permit.
                for (int i = 0; i < 20; i++) {
                    final int taskNum = i;
                    executor.submit(() -> {
                        try {
                            System.out.printf("[Task %2d] Waiting for permit...%n", taskNum);
                            rateLimiter.acquire();
                            // The action being rate-limited would go here.
                            System.out.printf("[Task %2d] ...Permit acquired at %dms%n", taskNum, System.currentTimeMillis() % 10000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            }
            // The virtual thread executor is automatically closed here.
        }
        // The rate limiter's replenisher is automatically closed here.
        System.out.println("Demo finished.");
    }
}