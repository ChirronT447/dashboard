package com.gateway.dashboard.interviews.appl.analytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/**
 * Class to compare two files, neither of which fit entirely in memory
 */
public class ExternalNumberMatcher {

    private static final String OUTPUT_FILE = "result-%s.txt";

    /**
     * This method accepts two paths and outputs numbers present in both. The files at these paths must be
     *  sorted and contain only integer values.
     * @param firstPath A file on disk containing integers <= 64-bit
     * @param secondPath A file on disk containing integers <= 64-bit
     * @return A file on disk containing integers found in both files
     * @throws IOException Any file IO may trigger this exception
     */
    public static Path findCommonNumbers(final Path firstPath, final Path secondPath) throws IOException {
        final Path output = firstPath.getParent().resolve(OUTPUT_FILE.formatted(Instant.now().toString()));
        try (final BufferedWriter bw = Files.newBufferedWriter(output, Charset.defaultCharset())) {
            try (final BufferedReader br1 = Files.newBufferedReader(firstPath, StandardCharsets.UTF_8);
                 final BufferedReader br2 = Files.newBufferedReader(secondPath, StandardCharsets.UTF_8);
            ) {
                String fileOneLine = br1.readLine(), fileTwoLine = br2.readLine();
                while (fileOneLine != null && fileTwoLine != null) {
                    if (Long.parseLong(fileOneLine) == Long.parseLong(fileTwoLine)) {
                        bw.write(fileTwoLine + System.lineSeparator());
                        fileOneLine = br1.readLine();
                        fileTwoLine = br2.readLine();
                    } else if (Long.parseLong(fileOneLine) < Long.parseLong(fileTwoLine))
                        fileOneLine = br1.readLine();
                    else {
                        fileTwoLine = br2.readLine();
                    }
                }
            }
        }
        return output;
    }
}