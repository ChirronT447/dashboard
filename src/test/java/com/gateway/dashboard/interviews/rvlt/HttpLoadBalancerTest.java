package com.gateway.dashboard.interviews.rvlt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpLoadBalancerTest {

    private static final int LOAD_BALANCER_PORT = 8888;
    private final List<MockBackendServer> backendServers = new ArrayList<>();
    private Thread loadBalancerThread;
    private HttpLoadBalancer loadBalancer;

    @BeforeEach
    void setUp() throws InterruptedException {
        // Start mock backend servers
        startMockBackendServer(9001, "Server1");
        startMockBackendServer(9002, "Server2");
        startMockBackendServer(9003, "Server3");

        // Start the HttpLoadBalancer in a background thread so the test doesn't block
        loadBalancer = new HttpLoadBalancer(); // Assuming HttpLoadBalancer is in the same package
        loadBalancerThread = new Thread(() -> {
            try {
                loadBalancer.start(); // This blocks, so it needs its own thread
            } catch (IOException e) {
                // Ignore exceptions during shutdown
                if (!"Socket closed".equals(e.getMessage())) {
                    e.printStackTrace();
                }
            }
        });
        loadBalancerThread.start();

        // Give the server a moment to start up
        TimeUnit.MILLISECONDS.sleep(100);
    }

    @AfterEach
    void tearDown() throws Exception {
        // A bit of a forceful shutdown, but necessary for this design
        // A real app would have a graceful shutdown method.
        loadBalancerThread.interrupt(); // This will cause serverSocket.accept() to throw an exception

        for (MockBackendServer server : backendServers) {
            server.stop();
        }
    }

    @Test
    @DisplayName("Load Balancer should distribute requests in a round-robin fashion")
    void testRoundRobinDistribution() throws IOException {
        // Make 4 requests to ensure the loop wraps around
        String response1 = makeRequest("Request 1");
        String response2 = makeRequest("Request 2");
        String response3 = makeRequest("Request 3");
        String response4 = makeRequest("Request 4");

        // Assert that the responses came from the correct servers in order
        assertEquals("Response from Server1", response1);
        assertEquals("Response from Server2", response2);
        assertEquals("Response from Server3", response3);
        assertEquals("Response from Server1", response4); // Wraps around
    }

    @Test
    @DisplayName("Load Balancer should correctly proxy data back and forth")
    void testDataProxying() throws IOException {
        String message = "Hello, this is a test of the data proxying!";
        String response = makeRequest(message);

        // The mock backend server's name is "Server1" for the first request.
        // It prepends its name to the echoed message.
        assertEquals("Server1 echoes: " + message, response);
    }

    // --- Helper Methods & Classes ---

    private String makeRequest(String message) throws IOException {
        try (Socket clientSocket = new Socket("localhost", LOAD_BALANCER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println(message);
            return in.readLine();
        }
    }

    private void startMockBackendServer(int port, String name) {
        MockBackendServer server = new MockBackendServer(port, name);
        new Thread(server).start();
        backendServers.add(server);
    }

    /**
     * A simple mock server that runs in a thread, accepts one connection,
     * echoes back whatever it receives with a prefix, and then shuts down.
     * For the round-robin test, it responds with its name only.
     */
    private static class MockBackendServer implements Runnable {
        private final int port;
        private final String name;
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private ServerSocket serverSocket;

        public MockBackendServer(int port, String name) {
            this.port = port;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                // Keep accepting connections to handle multiple test requests
                while (!Thread.currentThread().isInterrupted()) {
                    Socket client = serverSocket.accept();
                    executor.submit(() -> handleClient(client));
                }
            } catch (IOException e) {
                // Expected when stopping
            }
        }

        private void handleClient(Socket client) {
            try (client;
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                String line = in.readLine();
                if ("Request 1".equals(line) || "Request 2".equals(line) || "Request 3".equals(line) || "Request 4".equals(line)) {
                    out.println("Response from " + name); // For the round-robin test
                } else {
                    out.println(name + " echoes: " + line); // For the proxy test
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() throws IOException {
            executor.shutdownNow();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}