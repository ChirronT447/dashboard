package com.gateway.dashboard.interviews.anthpc;

// SimpleFileCache.java
// Java 25 example/demo of a local file cache daemon with chunking, peer fetch, and a demo in-memory GMS.
//
// Usage (demo):
// 1) Run two instances on different ports in separate terminals:
//    java SimpleFileCache 9001 /tmp/cache1
//    java SimpleFileCache 9002 /tmp/cache2
// 2) Register the second as a peer of the first by calling the register endpoint (or let demo register on start).
// 3) Use HTTP to request a chunk from daemon1; if it's not local but peer has it, daemon1 will fetch from daemon2;
//    otherwise it will fetch from the origin URL you provide (we use file URLs or http URLs).
//
// Note: This is a demo — production concerns (security, persistence, HA GMS, eviction, checksums, retries, chunk
// verification, TLS, access control) are intentionally omitted to keep the example focused on core mechanics.

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SimpleFileCache {
    // chunk size 16 MiB for demo (adjustable)
    private static final int CHUNK_SIZE = 16 * 1024 * 1024;

    // Simple in-memory Global Metadata Service (GMS) for demo only.
    // Maps objectId (filePath + chunkIndex) -> set of node addresses (http://host:port)
    public static class GMS {
        private static final ConcurrentMap<String, Set<String>> registry = new ConcurrentHashMap<>();

        public static void registerReplica(String filePath, int chunkIndex, String nodeAddr) {
            String key = key(filePath, chunkIndex);
            registry.compute(key, (k, v) -> {
                if (v == null) v = ConcurrentHashMap.newKeySet();
                v.add(nodeAddr);
                return v;
            });
        }

        public static void unregisterReplica(String filePath, int chunkIndex, String nodeAddr) {
            String key = key(filePath, chunkIndex);
            registry.computeIfPresent(key, (k, v) -> {
                v.remove(nodeAddr);
                return v.isEmpty() ? null : v;
            });
        }

        public static Set<String> findReplicas(String filePath, int chunkIndex) {
            var v = registry.get(key(filePath, chunkIndex));
            return v == null ? Set.of() : Set.copyOf(v);
        }

        private static String key(String filePath, int chunkIndex) {
            return filePath + "::" + chunkIndex;
        }
    }

    // Local chunk store backed by a directory on disk.
    public static class LocalStore {
        private final Path baseDir;

        public LocalStore(Path baseDir) throws IOException {
            this.baseDir = baseDir;
            Files.createDirectories(baseDir);
        }

        private Path chunkPath(String filePath, int chunkIndex) {
            // sanitize filePath into a safe filename
            String safe = Base64.getUrlEncoder().withoutPadding().encodeToString(filePath.getBytes());
            return baseDir.resolve(safe + "." + chunkIndex + ".chunk");
        }

        public boolean hasChunk(String filePath, int chunkIndex) {
            return Files.exists(chunkPath(filePath, chunkIndex));
        }

        public void writeChunk(String filePath, int chunkIndex, byte[] data) throws IOException {
            Path p = chunkPath(filePath, chunkIndex);
            try (FileOutputStream fos = new FileOutputStream(p.toFile())) {
                fos.write(data);
            }
        }

        public byte[] readChunk(String filePath, int chunkIndex) throws IOException {
            Path p = chunkPath(filePath, chunkIndex);
            if (!Files.exists(p)) return null;
            return Files.readAllBytes(p);
        }

        public long getChunkSize(String filePath, int chunkIndex) throws IOException {
            Path p = chunkPath(filePath, chunkIndex);
            return Files.exists(p) ? Files.size(p) : 0;
        }
    }

    // Cache daemon exposing small HTTP API:
    // GET /chunk?file=<url-encoded-filePath>&index=<chunkIndex>
    // POST /register  body: nodeAddr=<http://host:port>  (used by peers to announce they exist)
    public static class CacheDaemon {
        private final String nodeAddr; // e.g., http://localhost:9001
        private final int port;
        private final LocalStore store;
        private final HttpClient httpClient;
        private final ExecutorService serverExecutor;
        private final CopyOnWriteArrayList<String> peers = new CopyOnWriteArrayList<>(); // initial known peers
        private final boolean autoRegisterWithPeers;

        public CacheDaemon(int port, Path storageDir, List<String> initialPeers, boolean autoRegisterWithPeers) throws IOException {
            this.port = port;
            this.nodeAddr = "http://localhost:" + port;
            this.store = new LocalStore(storageDir);
            // virtual threads per task executor (Java 25)
            this.serverExecutor = Executors.newVirtualThreadPerTaskExecutor();
            this.httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .executor(serverExecutor)
                    .build();
            if (initialPeers != null) this.peers.addAll(initialPeers);
            this.autoRegisterWithPeers = autoRegisterWithPeers;
        }

        // Start HTTP server
        public void start() throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(serverExecutor);

            server.createContext("/chunk", this::handleGetChunk);
            server.createContext("/register", this::handleRegister); // simple peer register
            server.createContext("/status", this::handleStatus);
            server.start();

            System.out.println("CacheDaemon listening on " + nodeAddr);
            if (autoRegisterWithPeers) {
                // announce ourselves to peers so GMS entries can be populated (simplified)
                for (String peer : peers) {
                    try {
                        announceToPeer(peer);
                    } catch (Exception e) {
                        System.err.println("Failed to announce to peer " + peer + ": " + e.getMessage());
                    }
                }
            }
        }

        private void handleStatus(HttpExchange exchange) throws IOException {
            String body = "node=" + nodeAddr + ", peers=" + String.join(",", peers) + "\n";
            byte[] bytes = body.getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void handleRegister(HttpExchange exchange) throws IOException {
            // A peer calls POST /register with form "nodeAddr=http://host:port"
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes());
            var params = parseForm(body);
            String other = params.get("nodeAddr");
            if (other != null && !other.isBlank()) {
                peers.addIfAbsent(other);
                // respond with our address so peer can know us (not strictly necessary)
                byte[] resp = ("registered " + nodeAddr).getBytes();
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } else {
                exchange.sendResponseHeaders(400, -1);
            }
        }

        private void handleGetChunk(HttpExchange exchange) throws IOException {
            // GET /chunk?file=<url-encoded-filePath>&index=0
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            String q = exchange.getRequestURI().getQuery();
            var params = parseQuery(q);
            String file = params.get("file");
            String indexStr = params.get("index");
            if (file == null || indexStr == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            int index = Integer.parseInt(indexStr);

            try {
                // 1) Check local store
                if (store.hasChunk(file, index)) {
                    byte[] data = store.readChunk(file, index);
                    exchange.getResponseHeaders().add("X-Cache", "HIT-LOCAL");
                    exchange.sendResponseHeaders(200, data.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(data);
                    }
                    return;
                }

                // 2) Check GMS for replicas on other nodes
                var replicaNodes = GMS.findReplicas(file, index)
                        .stream()
                        .filter(addr -> !addr.equals(nodeAddr)) // exclude self
                        .collect(Collectors.toList());

                // Also include known peers list as fallback (not necessary if GMS is used)
                for (String p : peers) if (!replicaNodes.contains(p) && !p.equals(nodeAddr)) replicaNodes.add(p);

                for (String peer : replicaNodes) {
                    try {
                        System.out.println("Attempting peer fetch from " + peer + " for " + file + ":" + index);
                        byte[] data = fetchChunkFromPeer(peer, file, index).join();
                        if (data != null && data.length > 0) {
                            // write local copy and register
                            store.writeChunk(file, index, data);
                            GMS.registerReplica(file, index, nodeAddr);
                            exchange.getResponseHeaders().add("X-Cache", "HIT-PEER");
                            exchange.sendResponseHeaders(200, data.length);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(data);
                            }
                            return;
                        }
                    } catch (CompletionException | IOException e) {
                        System.err.println("Peer fetch failed from " + peer + ": " + e.getMessage());
                    }
                }

                // 3) Fetch from origin (filePath is treated as origin URL here)
                try {
                    System.out.println("Fetching from origin for " + file + ":" + index);
                    byte[] data = fetchChunkFromOrigin(file, index).join();
                    if (data != null && data.length > 0) {
                        store.writeChunk(file, index, data);
                        GMS.registerReplica(file, index, nodeAddr);
                        exchange.getResponseHeaders().add("X-Cache", "HIT-ORIGIN");
                        exchange.sendResponseHeaders(200, data.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(data);
                        }
                        return;
                    } else {
                        exchange.sendResponseHeaders(502, -1);
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Origin fetch failed for " + file + ":" + index + " -> " + e.getMessage());
                    exchange.sendResponseHeaders(502, -1);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        }

        // Announce this node to peer (simple register)
        private void announceToPeer(String peerAddr) throws Exception {
            var req = HttpRequest.newBuilder(URI.create(peerAddr + "/register"))
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString("nodeAddr=" + URLEncoder.encode(nodeAddr, "UTF-8")))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        }

        // Fetch chunk via HTTP from a peer node at /chunk
        private CompletableFuture<byte[]> fetchChunkFromPeer(String peerAddr, String filePath, int index) {
            String encoded = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String uri = peerAddr + "/chunk?file=" + encoded + "&index=" + index;
            var req = HttpRequest.newBuilder(URI.create(uri)).GET().timeout(Duration.ofSeconds(20)).build();
            return httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(resp -> {
                        if (resp.statusCode() == 200) return resp.body();
                        return null;
                    });
        }

        // Fetch from origin: we treat filePath as URL; supports file:// and http(s)://
        // This method fetches the specific chunk index via range semantics for remote HTTP,
        // or via reading a local file for file:// scheme.
        private CompletableFuture<byte[]> fetchChunkFromOrigin(String filePath, int chunkIndex) {
            try {
                URI uri = URI.create(filePath);
                if ("file".equalsIgnoreCase(uri.getScheme()) || uri.getScheme() == null) {
                    // local file
                    Path p = uri.getScheme() == null ? Paths.get(filePath) : Paths.get(uri);
                    return CompletableFuture.supplyAsync(() -> {
                        try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r")) {
                            long offset = (long) chunkIndex * CHUNK_SIZE;
                            if (offset >= raf.length()) return new byte[0];
                            raf.seek(offset);
                            int toRead = (int) Math.min(CHUNK_SIZE, raf.length() - offset);
                            byte[] buf = new byte[toRead];
                            raf.readFully(buf);
                            return buf;
                        } catch (IOException e) {
                            throw new CompletionException(e);
                        }
                    }, serverExecutor);
                } else {
                    // remote http(s) origin - use Range header
                    long start = (long) chunkIndex * CHUNK_SIZE;
                    long end = start + CHUNK_SIZE - 1;
                    HttpRequest req = HttpRequest.newBuilder(uri)
                            .timeout(Duration.ofSeconds(20))
                            .header("Range", "bytes=" + start + "-" + end)
                            .GET()
                            .build();
                    return httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray())
                            .thenApply(resp -> {
                                int status = resp.statusCode();
                                if (status == 200 || status == 206) {
                                    return resp.body();
                                } else {
                                    return null;
                                }
                            });
                }
            } catch (Exception e) {
                CompletableFuture<byte[]> failed = new CompletableFuture<>();
                failed.completeExceptionally(e);
                return failed;
            }
        }

        private static Map<String, String> parseQuery(String q) {
            if (q == null || q.isBlank()) return Map.of();
            return Arrays.stream(q.split("&"))
                    .map(s -> s.split("=", 2))
                    .filter(parts -> parts.length == 2)
                    .collect(Collectors.toMap(
                            p -> URLDecoder.decode(p[0], StandardCharsets.UTF_8),
                            p -> URLDecoder.decode(p[1], StandardCharsets.UTF_8)
                    ));
        }

        private static Map<String, String> parseForm(String body) {
            return parseQuery(body);
        }
    }

    // Demo main: run daemon with args: <port> <storageDir> [peer1,peer2,...]
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java SimpleFileCache <port> <storageDir> [peer1,peer2,...]");
            System.out.println("Example: java SimpleFileCache 9001 /tmp/cache1 http://localhost:9002");
            return;
        }
        int port = Integer.parseInt(args[0]);
        Path storage = Paths.get(args[1]);
        List<String> peers = args.length >= 3 ? Arrays.asList(Arrays.copyOfRange(args, 2, args.length)) : List.of();

        // Instantiate daemon, auto-register with peers for demo
        var daemon = new CacheDaemon(port, storage, peers, true);
        daemon.start();

        // For demo: print a small help and block
        System.out.println("Started SimpleFileCache on port " + port + ", storage=" + storage);
        System.out.println("Available endpoints:");
        System.out.println("  GET /chunk?file=<url>&index=<i>    -> fetch chunk i of file (file is origin URL)");
        System.out.println("  POST /register (form nodeAddr=...) -> ask this node to add peer");
        System.out.println("Use ctrl-C to stop.");
        Thread.currentThread().join();
    }
}