package com.gateway.dashboard.hired;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DebuggingThreeTest {

    @Test
    void testDebuggingThree() {

        // Input: prices: [10.25, 11, 10.75, 11.5]
        //        trades: [[0, 10], [1, -5], [2, 10], [3, -15]]
        // Expected Output: 17.5
        Assertions.assertEquals(
                17.5,
                DebuggingThree.Solution.tradeCalculator(
                        new double[]{  10.25,    11,     10.75,   11.5 },
                        new int[][] { {0, 10}, {1, -5}, {2, 10}, {3, -15} }
                                    // -102.5 + 55 - 107.5 + 172.5 = 17.5
                )
        );
    }

    @Test
    void testDebuggingThree_2() {
        Assertions.assertArrayEquals(
                new long[][]{ {120, 4}, {720, 1}, {6, 0} },
                DebuggingThree.factorial(new long[] {5, 6, 3})
        );

        Assertions.assertArrayEquals(
                new long[][]{ {1, 0}, {6, 2}, {120, 2} },
                DebuggingThree.factorial(new long[] {1, 3, 5})
        );

    }

}