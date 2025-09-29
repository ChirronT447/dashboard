package com.gateway.dashboard.interviews.cs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowRateLimiterTest {

    @Test
    @DisplayName("Should deny request when burst exceeds limit within the time window")
    void shouldDenyRequestWhenOverLimit() {
        // Limit of 5 requests per 10 seconds.
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(5, 10);
        String userId = "user1";

        // The first 5 requests should be allowed. These will execute very quickly.
        for (int i = 0; i < 5; i++) {
            assertThat(rateLimiter.shouldAllow(userId)).isTrue();
        }

        // The 6th request should be denied as it's within the same time window.
        assertThat(rateLimiter.shouldAllow(userId)).isFalse();
    }

    @Test
    @DisplayName("Should allow request again after the window expires")
    void shouldAllowRequestAgainAfterWindowExpires() throws InterruptedException {
        // A tight window for a faster test: 2 requests per 1 second.
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(2, 1);
        String userId = "user2";

        // First 2 requests are allowed.
        assertThat(rateLimiter.shouldAllow(userId)).isTrue();
        assertThat(rateLimiter.shouldAllow(userId)).isTrue();

        // 3rd request should be denied.
        assertThat(rateLimiter.shouldAllow(userId)).isFalse();

        // Wait for the window to expire. We wait slightly longer than 1s (1100ms)
        // to account for test execution overhead and ensure the window has passed.
        Thread.sleep(1100);

        // Now, the first two requests have expired, so a new one should be allowed.
        assertThat(rateLimiter.shouldAllow(userId)).isTrue();
    }

    @Test
    @DisplayName("Should handle multiple users independently")
    void shouldHandleMultipleUsersIndependently() throws InterruptedException {
        // Limit of 1 request per 2 seconds.
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(1, 2);

        // userA is allowed.
        assertThat(rateLimiter.shouldAllow("userA")).isTrue();

        // userB is also allowed, as their limit is separate.
        assertThat(rateLimiter.shouldAllow("userB")).isTrue();

        // userA is now denied.
        assertThat(rateLimiter.shouldAllow("userA")).isFalse();

        // userB is also denied.
        assertThat(rateLimiter.shouldAllow("userB")).isFalse();

        // Wait for 2.1 seconds
        Thread.sleep(2100);

        // Both users should be allowed again.
        assertThat(rateLimiter.shouldAllow("userA")).isTrue();
        assertThat(rateLimiter.shouldAllow("userB")).isTrue();
    }

    @Test
    @DisplayName("Should be thread-safe and not exceed the rate limit under heavy contention")
    void shouldBeThreadSafeUnderContention() throws InterruptedException {
        // A generous limit to test concurrency: 50 requests per 2 seconds.
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(50, 2);
        String userId = "concurrentUser";
        int totalThreads = 200;
        int requestsPerThread = 10; // Total attempts = 2000

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        AtomicLong allowedCount = new AtomicLong(0);

        // All threads will be created and started, bombarding the limiter.
        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < requestsPerThread; j++) {
                    if (rateLimiter.shouldAllow(userId)) {
                        allowedCount.incrementAndGet();
                    }
                }
                latch.countDown();
            });
        }

        // Wait for all threads to finish their work.
        boolean finished = latch.await(5, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(finished).isTrue().withFailMessage("Test timed out, not all threads completed.");

        // Despite 2000 total attempts happening in a burst, only the max number of requests should be allowed.
        assertThat(allowedCount.get()).isEqualTo(50);
    }
}