package com.gateway.dashboard.interviews.appl.analytics;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Task:
 * Let's assume we have two files, each containing a list of numbers, one number per line.
 * The assignment is to write a function which takes these two files and finds all the numbers that are only present in both files.
 * For this exercise, make the following assumptions:
 * - Numbers are 64 bits integers
 * - The files are very large, so that neither of them fits entirely in memory
 * - The numbers are not stored in any particular order, and they may contain duplicates within the same file
 * - The output should be written to another file, in the same format. The primary goal is to implement a computationally efficient solution.
 * <p>
 * Please send a short description to explain the asymptotical complexity of your solution, using O notation, specifying the type of operations the complexity is expressed in.
 * For example, you should say "O(n) comparisons" (Note: this is not a hint, just an example)
 * <p>
 * Also consider the following:
 * - There's no requirement for a particular coding style but please write code as you would like others to write
 *    code you'd have to read and understand yourself.
 * - Please treat this as any other work assignment: the goal is to implement a good solution that could be part of a
 *    library used by other engineers. This said, we would like you to implement the whole logic - please refrain from
 *    using external libraries which might solve the problem for you.
 * - We're not necessarily looking for originality here, just good engineering. Please feel free to research the
 *    solution to this, but please do not just copy/paste from elsewhere.
 * - Please do not upload the solution to any public repository (e.g. public GitHub).
 *
 * Assumptions:
 * - The file is correctly formed and there is no need to store unparsable (invalid) lines
 * - Ex. The numbers are integer values (64 bit) without punctuation (eg. 9223372036 instead of 9,223,372,036) or whitespace
 * - The .txt file extension is acceptable for the output file
 * - The output file is ok to be placed in the same directory as the first input file
 */
public class Solution {

    /**
     * A function which takes two files and finds all the numbers that are only present in both files.
     * It does this by sorting both files (removing duplicates) and comparing them;
     * The result is output to a new file.
     * @param firstPath The path to the first file
     * @param secondPath The path to the second file
     * @return The path to a new file with the shared numbers:
     * @throws IOException Any issues accessing a file may cause this to be thrown
     */
    public Path findCommonNumbers(final Path firstPath, final Path secondPath) throws IOException {
        validatePaths(firstPath, secondPath);
        final Path firstSortedPath = ExternalSorter.sortOnDisk(firstPath);
        final Path secondSortedPath = ExternalSorter.sortOnDisk(secondPath);
        return ExternalNumberMatcher.findCommonNumbers(firstSortedPath, secondSortedPath);
    }

    // Basic validation on input: files must be regular, accessible and not the same
    private void validatePaths(final Path firstPath, final Path secondPath) throws IOException {
        final String fileOneId = Objects.requireNonNull(firstPath, "First path must not be null").toString();
        final String fileTwoId = Objects.requireNonNull(secondPath, "Second path must not be null").toString();

        if(!Files.isRegularFile(firstPath) || !Files.isReadable(firstPath)) {
            throw new FileSystemException(fileOneId, fileTwoId, "Path must be regular & readable: %s".formatted(fileOneId));
        }
        if(!Files.isRegularFile(secondPath) || !Files.isReadable(secondPath)) {
            throw new FileSystemException(fileOneId, fileTwoId, "Path must be regular & readable: %s".formatted(fileTwoId));
        }
        if(Files.isSameFile(firstPath, secondPath)) {
            throw new FileSystemException(fileOneId, fileTwoId, "Paths must not point to the same file");
        }
    }
}