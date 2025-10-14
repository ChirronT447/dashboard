package com.gateway.dashboard.interviews.rvlt;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple URL shortener built using only Core Java APIs.
 * <p>
 * This application uses the built-in HttpServer, a ConcurrentHashMap for in-memory storage,
 * and a base-62 encoding scheme for generating short keys. It showcases modern
 * concurrency with Virtual Threads.
 *
 * Endpoints:
 * 1. POST /shorten - Takes a raw URL string in the body and returns the shortened URL.
 * Example: curl -X POST --data "https://www.google.com" http://localhost:8080/shorten
 *
 * 2. GET /{shortKey} - Redirects to the original long URL.
 * Example: curl -vL http://localhost:8080/aZb
 */
public class UrlShortener {

    // --- Configuration ---
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + PORT + "/";
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // --- State ---
    // A thread-safe map to store shortKey -> longUrl mappings.
    private final ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();
    // A thread-safe counter to ensure unique IDs for each URL.
    // Start from a higher number to get longer keys initially.
    private final AtomicLong counter = new AtomicLong(100_000_000L);

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // --- Define HTTP Handlers (Contexts) ---
        server.createContext("/shorten", this::handleShortenRequest);
        server.createContext("/", this::handleRedirectRequest);

        // --- Use Virtual Threads for High Concurrency ---
        // This is a key modern Java feature for I/O-bound applications.
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        server.start();
        System.out.printf("✅ URL Shortener server started on port %d...%n", PORT);
        System.out.println("Usage: curl -X POST --data \"<your_long_url>\" " + BASE_URL + "shorten");
    }

    /**
     * Handles POST requests to create a new shortened URL.
     */
    private void handleShortenRequest(final HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try (InputStream requestBody = exchange.getRequestBody()) {
            final String longUrl = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            if (!longUrl.startsWith("http")) {
                sendResponse(exchange, 400, "Bad Request: Invalid URL provided.");
                return;
            }

            // Generate a unique ID and encode it to a base-62 string.
            final long id = counter.getAndIncrement();
            final String shortKey = encodeToBase62(id);

            // Store the mapping and create the full short URL.
            urlMap.put(shortKey, longUrl);
            final String shortUrl = BASE_URL + shortKey;

            System.out.printf("[INFO] Shortened '%s' -> %s%n", longUrl, shortUrl);
            sendResponse(exchange, 200, shortUrl);
        }
    }

    /**
     * Handles GET requests to redirect to the original URL.
     */
    private void handleRedirectRequest(final HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        final String path = exchange.getRequestURI().getPath();
        // The path will be "/", so we skip the first character.
        final String shortKey = path.substring(1);

        if (shortKey.isEmpty() || "shorten".equals(shortKey)) {
            sendResponse(exchange, 404, "Not Found");
            return;
        }

        final String longUrl = urlMap.get(shortKey);
        if (longUrl != null) {
            // Issue a 301 Moved Permanently redirect.
            exchange.getResponseHeaders().add("Location", longUrl);
            exchange.sendResponseHeaders(301, -1); // -1 means no response body
            System.out.printf("[INFO] Redirecting '%s' -> %s%n", shortKey, longUrl);
        } else {
            sendResponse(exchange, 404, "Short URL not found");
        }
    }

    /**
     * Converts a base-10 number to a base-62 string.
     */
    private String encodeToBase62(long n) {
        final StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.append(BASE62_CHARS.charAt((int) (n % 62)));
            n /= 62;
        }
        // The result is reversed, so we need to reverse it back.
        return sb.reverse().toString();
    }

    /**
     * Helper method to send a simple text response.
     */
    private void sendResponse(final HttpExchange exchange, final int statusCode, final String responseText) throws IOException {
        final byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (final OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void main(String[] args) {
        try {
            new UrlShortener().start();
        } catch (IOException e) {
            System.err.println("❌ Server failed to start: " + e.getMessage());
        }
    }
}