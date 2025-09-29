package com.gateway.dashboard.interviews.cs;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * A per-user rate limiter implemented using only the core Java library.
 * It uses a token bucket algorithm for each user, where a Semaphore represents the bucket.
 * A single, shared ScheduledExecutorService replenishes tokens for all users.
 */
public final class MultiUserRateLimiter implements AutoCloseable {

    private final int maxPermits;
    private final Map<String, Semaphore> userSemaphores;
    private final ScheduledExecutorService replenisher;

    private MultiUserRateLimiter(int permitsPerUser, Duration period) {
        if (permitsPerUser <= 0) {
            throw new IllegalArgumentException("Number of permits must be positive.");
        }
        if (period.isNegative() || period.isZero()) {
            throw new IllegalArgumentException("Replenishment period must be positive.");
        }

        this.maxPermits = permitsPerUser;
        this.userSemaphores = new ConcurrentHashMap<>();
        this.replenisher = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

        // Schedule the global replenishment task.
        long periodMillis = period.toMillis();
        this.replenisher.scheduleAtFixedRate(this::replenishAll, periodMillis, periodMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Factory method to create a MultiUserRateLimiter.
     *
     * @param permitsPerUser The number of permits allowed per user per time period.
     * @param period         The time period (e.g., Duration.ofSeconds(1)).
     * @return A new instance of MultiUserRateLimiter.
     */
    public static MultiUserRateLimiter create(int permitsPerUser, Duration period) {
        return new MultiUserRateLimiter(permitsPerUser, period);
    }

    /**
     * Acquires a permit for a specific user, blocking until one is available.
     *
     * @param userId The identifier for the user.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public void acquire(String userId) throws InterruptedException {
        getSemaphoreForUser(userId).acquire();
    }

    /**
     * Attempts to acquire a permit for a specific user without blocking.
     *
     * @param userId The identifier for the user.
     * @return true if a permit was acquired, false otherwise.
     */
    public boolean tryAcquire(String userId) {
        return getSemaphoreForUser(userId).tryAcquire();
    }

    /**
     * Retrieves the semaphore for a given user, creating it on-the-fly if it doesn't exist.
     *
     * @param userId The user identifier.
     * @return The Semaphore for that user.
     */
    private Semaphore getSemaphoreForUser(String userId) {
        // computeIfAbsent is an atomic operation, ensuring that we only create
        // one semaphore per user even under concurrent access.
        return userSemaphores.computeIfAbsent(userId, k -> new Semaphore(maxPermits));
    }

    /**
     * The global replenishment task that runs periodically.
     * It iterates over all known users and tops up their permits.
     */
    private void replenishAll() {
        for (Semaphore semaphore : userSemaphores.values()) {
            int permitsToAdd = maxPermits - semaphore.availablePermits();
            if (permitsToAdd > 0) {
                semaphore.release(permitsToAdd);
            }
        }
    }

    /**
     * Shuts down the background replenishment thread.
     */
    @Override
    public void close() {
        replenisher.shutdown();
    }

    // --- Example Usage ---
    public static void main(String[] args) {
        System.out.println("Starting per-user rate limiter demo: 3 permits per user, per second.");

        try (var rateLimiter = MultiUserRateLimiter.create(3, Duration.ofSeconds(1))) {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                // Simulate 10 requests from 'user-A'
                for (int i = 0; i < 10; i++) {
                    executor.submit(() -> {
                        try {
                            rateLimiter.acquire("user-A");
                            System.out.printf("[user-A] Request successful at %dms%n", System.currentTimeMillis() % 10000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }

                // Simulate 10 requests from 'user-B'
                for (int i = 0; i < 10; i++) {
                    executor.submit(() -> {
                        try {
                            rateLimiter.acquire("user-B");
                            System.out.printf("[user-B] Request successful at %dms%n", System.currentTimeMillis() % 10000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            }
        }
        System.out.println("Demo finished.");
    }
}