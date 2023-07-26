package com.gateway.dashboard.interviews.appl.analytics;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

class SolutionTest {

    @Test
    void findCommonNumbers() throws IOException {
        final Solution solution = new Solution();
        solution.findCommonNumbers(
                Path.of("src/test/resources/numbers/numbers1.txt"),
                Path.of("src/test/resources/numbers/numbers2.txt")
        );
    }

}