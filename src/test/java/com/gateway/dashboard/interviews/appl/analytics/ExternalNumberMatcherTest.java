package com.gateway.dashboard.interviews.appl.analytics;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Jimfs is an in-memory file system that implements the Java NIO API and supports almost every feature of it.
 * This is particularly useful, as it means we can emulate a virtual in-memory filesystem and interact with it
 *  using our existing java.nio layer.
 */
class ExternalNumberMatcherTest {

    @Test
    void testCompareWithInMemoryFileSystem() throws IOException {
        // Create in-memory filesystem and write numbers to files:
        try(final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform())) {
            final Path firstPath = Files.write(
                    fileSystem.getPath("/numbers1.txt"),
                    List.of("0",
                            "99",
                            "13243525364",
                            "223372036854775801",
                            "223372036854775802",
                            "223372036854775808",
                            "223372036854775811",
                            "223372036854775814",
                            "223372036854775815"
                    )
            );
            final Path secondPath = Files.write(
                    fileSystem.getPath("/numbers2.txt"),
                    List.of(
                            "223372036854775806",
                            "223372036854775807",
                            "223372036854775808",
                            "223372036854775809",
                            "223372036854775810",
                            "223372036854775811",
                            "223372036854775812",
                            "223372036854775813",
                            "223372036854775814",
                            "223372036854775815",
                            "223372036854775816",
                            "223372036854775817"
                    )
            );

            // Perform comparison:
            final Path result = ExternalNumberMatcher.findCommonNumbers(firstPath, secondPath);
            assertTrue(Files.exists(result));

            final List<String> results = Files.readAllLines(result);
            assertFalse(results.isEmpty());
            assertIterableEquals(
                    List.of("223372036854775808",
                            "223372036854775811",
                            "223372036854775814",
                            "223372036854775815"
                    ), results
            );
        }
    }

}