package com.gateway.dashboard.interviews.anthpc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

public class DuplicateFileDetector {

    /**
     *  Find duplicate files under some filesystem path. For each set of duplicates output a list of filepaths
     *  Example:
     *           [
     *          ["a/file1.mp4", "a/file1_copy.mp4"],
     *          ["a/file3.mp4", "b/c/file3b.mp4", "d/foo.mp4"]
     *         ]
     * @param fileSystemPath
     * @return
     * @throws IOException
     */
    public Collection<Set<Path>> findDuplicateFiles(final String fileSystemPath) throws IOException {

        // [
        // ["a/file1.mp4", "a/file1_copy.mp4"],
        // ["a/file3.mp4", "b/c/file3b.mp4", "d/foo.mp4"]
        //]
        List<List<String>> result = null;
        Path path = Path.of(fileSystemPath);

        Map<Long, List<Path>> fileSizeToLocation = new HashMap<>();
        List<Path> filePaths = Files.walk(path, 10, FileVisitOption.FOLLOW_LINKS).toList();
        for(Path pathToCheck : filePaths) {
            if(Files.isRegularFile(pathToCheck)) {
                Long fileSize = (Long) Files.readAttributes(pathToCheck, "size", LinkOption.NOFOLLOW_LINKS).get("size");
                fileSizeToLocation.computeIfAbsent(fileSize, fs -> new ArrayList<>()).add(pathToCheck);
            }
        }

        // Check files that are the same size:
        Map<Integer, Set<Path>> fileMatches = new HashMap<>();
        for(List<Path> files : fileSizeToLocation.values()) {
            if(files.size() <= 1) {
                continue;
            } else {
                for(Path file : files) {
                    int hash = Files.readString(file).hashCode();
                    fileMatches.computeIfAbsent(hash, f -> new HashSet<>()).add(file);
                }
            }
        }
        return fileMatches.values();
    }


    /**
     *  Find duplicate files under some filesystem path. For each set of duplicates output a list of filepaths
     *  Example:
     *           [
     *          ["a/file1.mp4", "a/file1_copy.mp4"],
     *          ["a/file3.mp4", "b/c/file3b.mp4", "d/foo.mp4"]
     *         ]
     * @param fileSystemPath
     * @return
     * @throws IOException
     */
    public List<List<Path>> findDuplicateFilesV2(final String fileSystemPath) throws IOException, NoSuchAlgorithmException {
        final Path basePath = Path.of(fileSystemPath);

        // 1. Group all files by size:
        final Map<Long, List<Path>> filesBySize = new HashMap<>();
        try (Stream<Path> paths = Files.walk(basePath)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    long size = Files.size(path);
                    filesBySize.computeIfAbsent(size, k -> new ArrayList<>()).add(path);
                } catch (IOException e) {
                    System.err.printf("Could not access file at " + path);
                }
            });

        }

        // The final collection of duplicate file groups
        List<List<Path>> duplicates = new ArrayList<>();

        // 2. Iterate through files of the same size:
        final Map<Integer, Set<Path>> fileMatches = new HashMap<>();
        for(List<Path> sameSizeFiles : filesBySize.values()) {
            if(sameSizeFiles.size() > 1) {
                // 3. Group same-sized files by a strong hash (SHA-256):
                Map<String, List<Path>> filesByHash = new HashMap<>();
                for (Path path : sameSizeFiles) {
                    String hash = calculateFileHash(path);
                    filesByHash.computeIfAbsent(hash, k -> new ArrayList<>()).add(path);
                }

                // 4. For files with the same size and hash we *should* do a final verification.
                //    (While SHA-256 collisions are nearly impossible, this is for 100% correctness)
                for (List<Path> potentialDuplicates : filesByHash.values()) {
                    if (potentialDuplicates.size() > 1) {
                        // In a real-world scenario, you might do a byte-by-byte comparison here.
                        duplicates.add(potentialDuplicates);
                    }
                }
            }
        }
        return duplicates;
    }

    /**
     * Calculates the SHA-256 hash of a file by reading it in chunks.
     * This avoids loading large files into memory.
     * @param path The path to the file.
     * @return The hex representation of the file's hash.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
     */
    private String calculateFileHash(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192]; // 8KB buffer for reading files
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }
        byte[] digest = md.digest();
        // Convert byte array to a hex string
        return new BigInteger(1, digest).toString(16);
    }
}