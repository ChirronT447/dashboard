package com.gateway.dashboard.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * LeetCode 588: Design In-Memory File System (Java 21 Version)
 * This solution leverages modern Java features for improved type safety and clarity.
 * - A `sealed interface` models the distinct roles of Files and Directories.
 * - Pattern Matching for `instanceof` simplifies logic that differs by node type.
 * - `var` is used for concise local variable declarations.
 */
public class FileSystem {

    /**
     * A sealed interface to represent a node in the file system.
     * It explicitly permits only File and Directory as subtypes, enhancing type safety.
     */
    private sealed interface FileSystemNode {
        String getName();
    }

    /**
     * A final class representing a directory, containing child nodes.
     */
    private static final class Directory implements FileSystemNode {
        private final String name;
        // TreeMap automatically keeps children sorted lexicographically for `ls`.
        private final TreeMap<String, FileSystemNode> children = new TreeMap<>();

        Directory(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    /**
     * A final class representing a file, containing content.
     */
    private static final class File implements FileSystemNode {
        private final String name;
        private final StringBuilder content = new StringBuilder();

        File(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    private final Directory root;

    public FileSystem() {
        // The root is a directory named "" representing "/".
        root = new Directory("");
    }

    public List<String> ls(final String path) {
        var node = findNode(path);
        return switch (node) {
            case File file -> List.of(file.getName());
            case Directory directory -> new ArrayList<>(directory.children.keySet());
        };
    }

    public void mkdir(final String directoryPath) {
        findOrCreatePath(directoryPath, Directory::new);
    }

    public void addContentToFile(String filePath, String content) {
        var file = (File) findOrCreatePath(filePath, File::new);
        file.content.append(content);
    }

    public String readContentFromFile(String filePath) {
        var file = (File) findNode(filePath);
        return file.content.toString();
    }

    /**
     * Helper to find a node at a given path. Assumes path exists for read/ls.
     */
    private FileSystemNode findNode(String path) {
        var components = path.split("/");
        FileSystemNode current = root;
        // Start from 1 to skip the empty string before the first '/'.
        for (int i = 1; i < components.length; i++) {
            current = ((Directory) current).children.get(components[i]);
        }
        return current;
    }

    /**
     * Helper to find or create nodes along a path.
     * @param path The full path to create.
     * @param finalNodeFactory A function (e.g., a constructor reference like File::new)
     * that creates the final node in the path.
     * @return The node at the end of the path.
     */
    private FileSystemNode findOrCreatePath(String path, Function<String, FileSystemNode> finalNodeFactory) {
        var components = path.split("/");
        Directory current = root;

        // Traverse/create parent directories.
        for (int i = 1; i < components.length - 1; i++) {
            current = (Directory) current.children.computeIfAbsent(components[i], Directory::new);
        }

        // Handle the final component using the provided factory.
        if (components.length > 1) {
            var finalComponent = components[components.length - 1];
            return current.children.computeIfAbsent(finalComponent, finalNodeFactory);
        }

        return root; // Path was just "/"
    }
}
