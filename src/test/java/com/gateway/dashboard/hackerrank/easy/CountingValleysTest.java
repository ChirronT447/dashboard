package com.gateway.dashboard.hackerrank.easy;

import com.gateway.dashboard.hackerrank.easy.CountingValleys;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountingValleysTest {

    @Test
    void countingValleys() {
        /**
         * _/\      _
         *    \    /
         *     \/\/
         */
        assertEquals(1, CountingValleys.countingValleys(8, "UDDDUDUU"));

        /**
         * _  _       _
         *  \/\      /
         *     \  /\/
         *      \/
         */
        assertEquals(2, CountingValleys.countingValleys(10, "DUDDDUUDUU"));
    }
}