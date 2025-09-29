package com.gateway.dashboard.interviews.ctdl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe producer that caches messages and sends them in batches.
 * Batches are sent when either the maximum message count is reached or a
 * message has been held for the maximum allowed time.
 * <p>
 * This class is AutoCloseable to ensure graceful shutdown of its background thread.
 * It should be used within a try-with-resources block.
 */
public final class MessageProducer implements AutoCloseable {

    private final int maxCount;
    private final long maxTimeMillis;

    // The buffer for caching messages. Guarded by the lock.
    private final List<String> messageBuffer = new ArrayList<>();
    // A timestamp for when the first message in the current batch was added.
    private long batchStartTimeMillis = 0;
    // ReentrantLock provides more flexibility than synchronized blocks and is preferred.
    private final ReentrantLock lock = new ReentrantLock();
    // A dedicated background thread to handle time-based flushing.
    private final ScheduledExecutorService scheduler;

    /**
     * Constructs a new MessageProducer.
     *
     * @param maxCount The maximum number of messages to hold in the buffer before sending.
     * @param maxTimeAllowed The maximum time a message is allowed to be held in the buffer.
     */
    public MessageProducer(final int maxCount, final Duration maxTimeAllowed) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException("Max count must be positive.");
        }
        if (maxTimeAllowed == null || maxTimeAllowed.isNegative() || maxTimeAllowed.isZero()) {
            throw new IllegalArgumentException("Max time allowed must be a positive duration.");
        }

        this.maxCount = maxCount;
        this.maxTimeMillis = maxTimeAllowed.toMillis();

        // The scheduler will periodically check if the current batch has expired.
        // The check interval is set to a fraction of the max time for responsiveness.
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        long checkInterval = Math.max(10, this.maxTimeMillis / 5); // Check reasonably often.
        this.scheduler.scheduleAtFixedRate(this::flushIfTimeExpired, checkInterval, checkInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Accepts a message and adds it to the internal buffer for batch sending.
     * This method is thread-safe and can be called by multiple producer threads.
     *
     * @param message The message to send.
     */
    public void sendMessage(final String message) {
        if (message == null) {
            return; // Or throw IllegalArgumentException
        }

        List<String> batchToSend = null;
        lock.lock();
        try {
            if (messageBuffer.isEmpty()) {
                // This is the first message of a new batch, record the start time.
                batchStartTimeMillis = System.currentTimeMillis();
            }
            messageBuffer.add(message);

            // If the buffer is full, trigger a flush.
            if (messageBuffer.size() >= maxCount) {
                batchToSend = flushBuffer();
            }
        } finally {
            lock.unlock();
        }

        // Perform the slow network operation outside of the lock.
        if (batchToSend != null) {
            sendBatchToEndpoint(batchToSend);
        }
    }

    /**
     * Periodically called by the scheduler to flush messages if they've been held too long.
     */
    private void flushIfTimeExpired() {
        List<String> batchToSend = null;
        lock.lock();
        try {
            if (messageBuffer.isEmpty()) {
                return; // Nothing to do.
            }

            long elapsedTime = System.currentTimeMillis() - batchStartTimeMillis;
            if (elapsedTime >= maxTimeMillis) {
                System.out.printf("[%s] Flushing due to time expiry (%dms). Batch size: %d.%n",
                        Thread.currentThread().getName(), elapsedTime, messageBuffer.size());
                batchToSend = flushBuffer();
            }
        } finally {
            lock.unlock();
        }

        // Perform the slow network operation outside of the lock.
        if (batchToSend != null) {
            sendBatchToEndpoint(batchToSend);
        }
    }

    /**
     * Gracefully shuts down the producer.
     * It flushes any remaining messages in the buffer and stops the background thread.
     */
    @Override
    public void close() throws InterruptedException {
        System.out.println("Shutting down MessageProducer...");

        // Stop the scheduler from accepting new tasks.
        scheduler.shutdown();

        // Flush any remaining messages.
        List<String> finalBatch;
        lock.lock();
        try {
            finalBatch = flushBuffer();
        } finally {
            lock.unlock();
        }

        if (finalBatch != null) {
            System.out.println("Sending final batch during shutdown...");
            sendBatchToEndpoint(finalBatch);
        }

        // Wait for the scheduler to terminate.
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.err.println("Scheduler did not terminate gracefully.");
            scheduler.shutdownNow();
        }
        System.out.println("MessageProducer shut down.");
    }

    /**
     * Internal helper to extract the buffer content for sending.
     * This method MUST be called within a locked block.
     * @return A list of messages to send, or null if buffer was empty.
     */
    private List<String> flushBuffer() {
        if (messageBuffer.isEmpty()) {
            return null;
        }

        // Create a copy to be sent. The network call will happen outside the lock.
        List<String> batchToSend = new ArrayList<>(messageBuffer);

        // Clear the buffer and reset the timer for the next batch.
        messageBuffer.clear();
        batchStartTimeMillis = 0;

        return batchToSend;
    }

    /**
     * Simulates sending a batch of messages to a network endpoint.
     * This is a placeholder for the actual network client logic.
     */
    private void sendBatchToEndpoint(final List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        System.out.printf("[%s] >> Sending batch of %d messages to network endpoint...%n",
                Thread.currentThread().getName(), messages.size());

        // Simulate network latency
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] >> Batch sent successfully.%n", Thread.currentThread().getName());
    }
}
