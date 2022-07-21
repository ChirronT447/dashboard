package com.gateway.dashboard.hired;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DebuggingTwoTest {

    /**
     * Input: numbers: [9, 5, 12, 6, 5, 4, 1]
     * Expected Output: 15
     */
    @Test
    void testMethod() {
        long res = DebuggingTwo.largestSumOfConsecutiveDecreasingPositiveIntegers(new int[]{9, 5, 12, 6, 5, 4, 1});
        Assertions.assertEquals(15, res);

        // [9, 8, 7, -4, 6, 4] -> -1
        res = DebuggingTwo.largestSumOfConsecutiveDecreasingPositiveIntegers(new int[]{9, 8, 7, -4, 6, 4});
        Assertions.assertEquals(-1, res);

        // [8, 6] -> 8
        res = DebuggingTwo.largestSumOfConsecutiveDecreasingPositiveIntegers(new int[]{8, 6});
        Assertions.assertEquals(8, res);
    }

}