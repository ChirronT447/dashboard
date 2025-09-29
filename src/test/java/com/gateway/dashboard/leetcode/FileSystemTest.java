package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    private FileSystem fileSystem;

    @BeforeEach
    void setUp() {
        // A new FileSystem instance is created before each test to ensure isolation.
        fileSystem = new FileSystem();
    }

    @Nested
    @DisplayName("ls method tests")
    class LsTests {
        @Test
        @DisplayName("ls on empty root should return an empty list")
        void ls_onEmptyRoot_returnsEmptyList() {
            assertTrue(fileSystem.ls("/").isEmpty(), "ls on an empty root should be an empty list.");
        }

        @Test
        @DisplayName("ls on a file path should return a list with only the file's name")
        void ls_onFilePath_returnsFileName() {
            fileSystem.addContentToFile("/a/b/file.txt", "content");
            assertEquals(List.of("file.txt"), fileSystem.ls("/a/b/file.txt"));
        }

        @Test
        @DisplayName("ls on a directory should return its contents in lexicographical order")
        void ls_onDirectory_returnsSortedContents() {
            fileSystem.mkdir("/a/z");
            fileSystem.addContentToFile("/a/y.txt", "");
            fileSystem.mkdir("/a/x");

            var expected = List.of("x", "y.txt", "z");
            var actual = fileSystem.ls("/a");

            assertEquals(expected, actual, "Directory contents should be sorted lexicographically.");
        }
    }

    @Nested
    @DisplayName("mkdir method tests")
    class MkdirTests {
        @Test
        @DisplayName("mkdir should create a single-level directory")
        void mkdir_createsSingleLevelDirectory() {
            fileSystem.mkdir("/dev");
            assertEquals(List.of("dev"), fileSystem.ls("/"));
        }

        @Test
        @DisplayName("mkdir should create nested directories that don't exist")
        void mkdir_createsNestedDirectories() {
            fileSystem.mkdir("/a/b/c");

            assertEquals(List.of("a"), fileSystem.ls("/"), "Top-level directory 'a' should be created.");
            assertEquals(List.of("b"), fileSystem.ls("/a"), "Mid-level directory 'b' should be created.");
            assertEquals(List.of("c"), fileSystem.ls("/a/b"), "Inner directory 'c' should be created.");
            assertTrue(fileSystem.ls("/a/b/c").isEmpty(), "The new directory should be empty.");
        }

        @Test
        @DisplayName("mkdir should be idempotent and not throw errors on existing paths")
        void mkdir_isIdempotent() {
            fileSystem.mkdir("/a/b");
            assertDoesNotThrow(() -> fileSystem.mkdir("/a/b"), "mkdir on an existing path should not throw an error.");
            assertEquals(List.of("b"), fileSystem.ls("/a")); // State should be unchanged
        }
    }

    @Nested
    @DisplayName("addContentToFile and readContentFromFile tests")
    class FileContentTests {
        @Test
        @DisplayName("should create a file and write initial content")
        void addAndReadContent_writesInitialContent() {
            var filePath = "/home/user/doc.txt";
            var content = "hello world";
            fileSystem.addContentToFile(filePath, content);

            assertEquals(content, fileSystem.readContentFromFile(filePath));
            assertTrue(fileSystem.ls("/home/user").contains("doc.txt"));
        }

        @Test
        @DisplayName("should append content to an existing file")
        void addContentToFile_appendsToExistingFile() {
            var filePath = "/log.txt";
            fileSystem.addContentToFile(filePath, "Initial log. ");
            fileSystem.addContentToFile(filePath, "Additional log.");

            var expectedContent = "Initial log. Additional log.";
            assertEquals(expectedContent, fileSystem.readContentFromFile(filePath));
        }

        @Test
        @DisplayName("addContentToFile should create necessary intermediate directories")
        void addContentToFile_createsIntermediateDirectories() {
            var filePath = "/var/log/app.log";
            var content = "Application started.";
            fileSystem.addContentToFile(filePath, content);

            assertEquals(content, fileSystem.readContentFromFile(filePath));
            assertEquals(List.of("var"), fileSystem.ls("/"));
            assertEquals(List.of("log"), fileSystem.ls("/var"));
            assertEquals(List.of("app.log"), fileSystem.ls("/var/log"));
        }
    }

    @Nested
    @DisplayName("Complex Scenarios")
    class ComplexScenarios {
        @Test
        @DisplayName("should handle a mixed sequence of operations correctly")
        void handlesMixedOperations() {
            // 1. Create a directory structure
            fileSystem.mkdir("/home/user/documents");
            fileSystem.mkdir("/home/user/downloads");

            // 2. Verify initial structure
            var expectedDirs = List.of("documents", "downloads");
            var actualDirs = fileSystem.ls("/home/user");
            Collections.sort(actualDirs); // Sort for consistent comparison
            assertEquals(expectedDirs, actualDirs);

            // 3. Add a file and content
            var reportPath = "/home/user/documents/report.docx";
            fileSystem.addContentToFile(reportPath, "Q1 Report");
            assertEquals("Q1 Report", fileSystem.readContentFromFile(reportPath));

            // 4. Check if file is listed
            assertEquals(List.of("report.docx"), fileSystem.ls("/home/user/documents"));

            // 5. Create another file
            fileSystem.addContentToFile("/home/user/downloads/archive.zip", "zip_content");

            // 6. Append content to the first file
            fileSystem.addContentToFile(reportPath, " - Draft");
            assertEquals("Q1 Report - Draft", fileSystem.readContentFromFile(reportPath));

            // 7. Final check of directory listings
            assertEquals(List.of("archive.zip"), fileSystem.ls("/home/user/downloads"));
            assertEquals(List.of("report.docx"), fileSystem.ls("/home/user/documents"));
        }

        @Test
        @DisplayName("should distinguish between files and directories with the same name")
        void handlesSameNameForFileAndDirectory() {
            fileSystem.mkdir("/a/item"); // 'item' is a directory
            fileSystem.addContentToFile("/b/item", "this is a file"); // 'item' is a file

            // ls on directory '/a/item'
            assertTrue(fileSystem.ls("/a/item").isEmpty());

            // ls on file '/b/item'
            assertEquals(List.of("item"), fileSystem.ls("/b/item"));

            // Check contents
            assertEquals("this is a file", fileSystem.readContentFromFile("/b/item"));
            assertEquals(List.of("item"), fileSystem.ls("/a"));

        }

    }

}