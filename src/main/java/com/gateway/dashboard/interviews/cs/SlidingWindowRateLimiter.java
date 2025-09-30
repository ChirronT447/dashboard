package com.gateway.dashboard.interviews.cs;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindowRateLimiter {

    private final int maxRequests;
    private final long windowMillis; // Use long for time in milliseconds
    private final Map<String, Queue<Long>> userRequestTimestamps;

    public SlidingWindowRateLimiter(int maxRequests, int secondsWindow) {
        this.maxRequests = maxRequests;
        this.windowMillis = secondsWindow * 1000L;
        this.userRequestTimestamps = new ConcurrentHashMap<>();
    }

    /**
     * Checks if a request for a given userId should be allowed.
     * @param userId The ID of the user making the request.
     * @return true if the request is allowed, false otherwise.
     */
    public boolean shouldAllow(final String userId) {
        final long currentTimeMillis = System.currentTimeMillis();

        // computeIfAbsent is atomic, ConcurrentLinkedQueue is thread-safe.
        Queue<Long> timestamps = userRequestTimestamps.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());

        long windowStart = currentTimeMillis - windowMillis;

        // Remove old timestamps - peek() is non-blocking and thread-safe. poll() is also thread-safe.
        while (!timestamps.isEmpty() && timestamps.peek() <= windowStart) {
            timestamps.poll();
        }

        // Add the new request timestamp. This must be done within a synchronized block to prevent race conditions.
        synchronized (timestamps) { // Lock on the specific user's queue
            if (timestamps.size() < maxRequests) {
                timestamps.add(currentTimeMillis);
                return true;
            } else {
                return false;
            }
        }
    }
}