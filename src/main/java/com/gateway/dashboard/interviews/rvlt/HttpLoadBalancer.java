package com.gateway.dashboard.interviews.rvlt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A conceptual HTTP Load Balancer using modern Java features.
 * <p>
 * This is a simplified version that handles plain HTTP traffic by acting
 * as a TCP proxy. It does not handle SSL/TLS.
 * <p>
 * Key Features Showcased:
 * <ul>
 * <li><b>Virtual Threads (Project Loom):</b> Uses a virtual thread per task executor
 * to handle thousands of concurrent connections efficiently.</li>
 * <li><b>Core Networking Sockets:</b> Uses `ServerSocket` to accept client connections.</li>
 * <li><b>Round-Robin Strategy:</b> A thread-safe round-robin algorithm to distribute load.</li>
 * <li><b>Modern I/O:</b> Uses `InputStream.transferTo` for efficient data proxying.</li>
 * </ul>
 */
public class HttpLoadBalancer {

    // Configuration - In a real app, this would be externalized.
    private static final int LISTENING_PORT = 8080;
    private static final List<BackendServer> BACKEND_SERVERS = List.of(
            new BackendServer("localhost", 9001),
            new BackendServer("localhost", 9002),
            new BackendServer("localhost", 9003)
    );

    // AtomicInteger for thread-safe round-robin counter.
    private final AtomicInteger serverIndex = new AtomicInteger(0);

    public void start() throws IOException {
        // --- 1. Use a Virtual Thread Executor ---
        // This executor creates a new virtual thread for each incoming connection.
        // It's ideal for I/O-bound tasks like proxying network data.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
             ServerSocket serverSocket = new ServerSocket(LISTENING_PORT)) {

            System.out.printf("✅ HTTP Load Balancer started on port %d...%n", LISTENING_PORT);
            System.out.println("Registered backend servers:");
            BACKEND_SERVERS.forEach(s -> System.out.println(" -> " + s));

            // --- 2. Main Server Loop ---
            while (true) {
                // Accept an incoming client connection. This blocks until a client connects.
                Socket clientSocket = serverSocket.accept();
                // Submit the connection handling to a new virtual thread.
                executor.submit(() -> handleClient(clientSocket));
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        // --- 3. Select a Backend Server (Round-Robin) ---
        BackendServer targetServer = selectBackendServer();
        System.out.printf("[INFO] Client %s -> Routing to %s%n", clientSocket.getRemoteSocketAddress(), targetServer);

        // --- 4. Proxy the Connection ---
        // We establish a connection to the backend and then ferry data in both directions.
        try (clientSocket; // This socket will be closed automatically by try-with-resources
             Socket backendSocket = new Socket(targetServer.host(), targetServer.port())) {

            // Use two virtual threads to handle bidirectional streaming.
            try (ExecutorService proxyExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                proxyExecutor.submit(() -> {
                    try {
                        transferData(clientSocket.getInputStream(), backendSocket.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                proxyExecutor.submit(() -> {
                    try {
                        transferData(backendSocket.getInputStream(), clientSocket.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (IOException e) {
            System.err.printf("[ERROR] Proxying error for client %s: %s%n", clientSocket.getRemoteSocketAddress(), e.getMessage());
        }
    }

    private void transferData(InputStream in, OutputStream out) {
        try {
            // transferTo is a highly efficient way to move data from an input stream
            // to an output stream, often using zero-copy techniques at the OS level.
            in.transferTo(out);
        } catch (IOException e) {
            // This can happen if one side closes the connection prematurely. It's often not a critical error.
            System.err.println("Stream transfer error: " + e.getMessage());
        }
    }

    private BackendServer selectBackendServer() {
        // getAndIncrement() is an atomic operation, making this thread-safe.
        int index = serverIndex.getAndIncrement() % BACKEND_SERVERS.size();
        // Math.abs is needed because getAndIncrement() can wrap around to Integer.MIN_VALUE
        return BACKEND_SERVERS.get(Math.abs(index));
    }

    // Using a record for an immutable backend server representation.
    private record BackendServer(String host, int port) {
        @Override
        public String toString() {
            return host + ":" + port;
        }
    }

    public static void main(String[] args) {
        try {
            new HttpLoadBalancer().start();
        } catch (Exception e) {
            System.err.println("❌ Failed to start the load balancer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}