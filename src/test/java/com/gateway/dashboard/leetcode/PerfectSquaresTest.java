package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfectSquaresTest {

    @Test
    void numSquares() {
        // Input: n = 12
        //Output: 3
        //Explanation: 12 = 4 + 4 + 4.
        Assertions.assertEquals(3, PerfectSquares.numSquares(12));

        // Input: n = 13
        //Output: 2
        //Explanation: 13 = 4 + 9.
        Assertions.assertEquals(2, PerfectSquares.numSquares(13));
    }
}