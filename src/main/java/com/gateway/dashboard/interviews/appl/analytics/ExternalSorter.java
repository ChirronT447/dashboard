package com.gateway.dashboard.interviews.appl.analytics;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * A class to sort a file on disk
 */
public class ExternalSorter {

    private static final String TEMP_FILENAME_TEMPLATE = "tmp-%s.txt";
    private static final String MERGED_FILENAME_TEMPLATE = "merge-%s.txt";

    /**
     * Given a path, sort 64-bit integers inside this file externally on disk
     * @param path The path to the file to be sorted
     * @return The path to the output file containing sorted de-duplicated values
     * @throws IOException Any issues accessing a file may cause this to be thrown
     */
    public static Path sortOnDisk(final Path path) throws IOException {

        final Path rootTmpDir = Files.createTempDirectory(Solution.class.getSimpleName() + "-");
        try {
            // Create a series of files to hold a sorted de-duplicated breakdown of the main file:
            final Queue<Path> fileQueue = createPathQueue(path, rootTmpDir);

            final Path result = mergeFiles(fileQueue);

            // Move to final destination, this is O(1) assuming no partition/filesystem move
            return Files.move(result, path, REPLACE_EXISTING);
        } finally {
            // Cleanup: Recursively deleting temp files & finally rootTmpDir
            try (Stream<Path> walk = Files.walk(rootTmpDir)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .peek(pth -> System.out.println("Deleting: " + pth))
                        .forEach(File::delete);
            }
        }
    }

    private static Queue<Path> createPathQueue(final Path path, final Path rootDir) throws IOException {

        final Queue<Path> fileQueue = new LinkedList<>();

        // Used to maintain order while removing duplicates:
        final TreeSet<Long> treeSet = new TreeSet<>();

        final long bufferSize = computeBufferSize();

        try (Stream<String> linesStream = Files.lines(path)) {
            linesStream.map(Long::valueOf).forEach(line -> {
                treeSet.add(line);
                if (treeSet.size() > bufferSize) {
                    fileQueue.add(flushTreeSetToNewPath(rootDir, treeSet));
                    treeSet.clear();
                }
            });

            fileQueue.add(flushTreeSetToNewPath(rootDir, treeSet));
            System.out.println("Files created: " + fileQueue.size());
        }
        return fileQueue;
    }

    private static Path flushTreeSetToNewPath(final Path rootDir, final TreeSet<Long> buffer) {
        final String tempFilename = TEMP_FILENAME_TEMPLATE.formatted(Instant.now().toString());
        try {
            final Path path = Files.createFile(rootDir.resolve(tempFilename));
            try(final BufferedWriter bw = Files.newBufferedWriter(path)) {
                for (Long number : buffer) {
                    bw.write(number + System.lineSeparator());
                }
            }
            return path;
        } catch (IOException e) {
            System.out.println("Exception thrown while creating temp file: " + tempFilename);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Path mergeFiles(final Queue<Path> fileQueue) throws IOException {
        while (fileQueue.size() > 1) {
            fileQueue.add(mergeFiles(fileQueue.poll(), fileQueue.poll()));
        }
        return fileQueue.poll();
    }

    private static Path mergeFiles(final Path fileOneToMerge, final Path fileTwoToMerge) throws IOException {
        final Path output = fileOneToMerge.getParent().resolve(MERGED_FILENAME_TEMPLATE.formatted(Instant.now().toString()));
        try (final BufferedWriter bw = Files.newBufferedWriter(output)) {
            try (final BufferedReader br1 = Files.newBufferedReader(fileOneToMerge, StandardCharsets.UTF_8);
                 final BufferedReader br2 = Files.newBufferedReader(fileTwoToMerge, StandardCharsets.UTF_8);
            ) {
                String fileOneLine = br1.readLine(), fileTwoLine = br2.readLine();
                while (fileOneLine != null && fileTwoLine != null) {
                    if (Long.parseLong(fileOneLine) <= Long.parseLong(fileTwoLine)) {
                        bw.write(fileOneLine + System.lineSeparator());
                        fileOneLine = br1.readLine();
                    } else {
                        bw.write(fileTwoLine + System.lineSeparator());
                        fileTwoLine = br2.readLine();
                    }
                }
                writeRemainingLines(bw, br1, fileOneLine);
                writeRemainingLines(bw, br2, fileTwoLine);
            }
            Files.delete(fileOneToMerge);
            Files.delete(fileTwoToMerge);
            return output;
        }
    }

    private static void writeRemainingLines(final BufferedWriter bw, final BufferedReader br, String fileLine) throws IOException {
        while (fileLine != null) {
            bw.write(fileLine + System.lineSeparator());
            fileLine = br.readLine();
        }
    }

    // -----------------------------------------------------------------

    private static long computeBufferSize()  {
        // 90% of available RAM divided by 8 bytes (64-bit integer):
        return Math.round(availableMemory() * 0.9 / 8);
    }

    private static long availableMemory() {
        final Runtime runtime = Runtime.getRuntime();
        // Already allocated = the total memory allocated minus the free memory within this allocation
        final long alreadyAllocatedMemory = runtime.totalMemory() - runtime.freeMemory();
        // The max amount of memory available to the JVM minus the already allocated memory
        return runtime.maxMemory() - alreadyAllocatedMemory;
    }

}
