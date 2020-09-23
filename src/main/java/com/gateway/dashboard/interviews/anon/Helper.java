package com.gateway.dashboard.interviews.anon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Helper {

    /**
     * Given a path return a list of files in that location that satisfy the filters.
     * @param path
     * @return
     */
    public static List<String> fetchFilePaths(String path) {
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            return walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".txt"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(String.format("Unable to fetch file paths from %s due to %s", path, e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Load a list of files located at the supplied paths.
     * @param paths
     * @return
     */
    public static List<String> loadFiles(List<String> paths) {
        List<String> result = new ArrayList<>();
        for(String path: paths) {
            result.add(readFile(path));
        }
        return result;
    }

    /**
     * Reads all content from a file into a string, decoding from bytes to characters using the default (StandardCharsets.UTF_8) charset.
     * It ensures that the file is closed when all content have been read.
     * @param path The location of the file to be read
     * @return String - The file contents
     */
    public static String readFile(String path) {
        Path filePath = Paths.get(path);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            System.err.println(String.format("Unable to read file located at: %s due to: %s", path, e));
            throw new RuntimeException(e);
        }
    }

}
