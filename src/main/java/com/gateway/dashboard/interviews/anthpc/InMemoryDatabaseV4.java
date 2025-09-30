package com.gateway.dashboard.interviews.anthpc;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabaseV4 {

    // Data structures for Level 4 file management
    private static class User {
        long storageCapacity;
        Map<String, Integer> files = new HashMap<>(); // fileName -> fileSize
        User(long capacity) {
            this.storageCapacity = capacity;
        }
    }
    private final Map<String, User> users = new HashMap<>();

    public String compressFile(String userId, String fileName) {
        users.putIfAbsent(userId, new User(1000)); // Example capacity
        final User user = users.get(userId);

        if (fileName.endsWith(".compressed") ||!user.files.containsKey(fileName)) {
            return "";
        }

        final int originalSize = user.files.get(fileName);
        user.files.remove(fileName);
        user.files.put(fileName + ".compressed", originalSize / 2);

        final long usedSpace = user.files.values().stream().mapToLong(Integer::intValue).sum();
        return String.valueOf(user.storageCapacity - usedSpace);
    }

    public String decompressFile(String userId, String fileName) {
        users.putIfAbsent(userId, new User(1000));
        final User user = users.get(userId);
        final String originalName = fileName.replace(".compressed", "");

        if (!fileName.endsWith(".compressed") ||
            !user.files.containsKey(fileName) ||
            user.files.containsKey(originalName)
        ) {
            return "";
        }

        final int compressedSize = user.files.get(fileName);
        final int originalSize = compressedSize * 2;
        final long currentUsedSpace = user.files.values().stream().mapToLong(Integer::intValue).sum();
        final long spaceAfterDecompression = currentUsedSpace - compressedSize + originalSize;

        if (spaceAfterDecompression > user.storageCapacity) {
            return ""; // Not enough capacity
        }

        user.files.remove(fileName);
        user.files.put(originalName, originalSize);
        return String.valueOf(user.storageCapacity - spaceAfterDecompression);
    }

}
