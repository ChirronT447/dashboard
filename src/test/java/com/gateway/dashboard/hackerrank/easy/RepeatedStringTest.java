package com.gateway.dashboard.hackerrank.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepeatedStringTest {

    @Test
    void repeatedString() {
        /**
         * aba
         * 10
         *
         * The first n=10 letters of the infinite string are abaabaabaa
         *  because there are 7 a's, we print 7 on a new line.
         */
        assertEquals(7, RepeatedString.repeatedString("aba", 10L));

        /**
         * a
         * 1000000000000
         *
         * Because all of the first 1000000000000 letters of the infinite string are a,
         *  we print 1000000000000 on a new line.
         */
        assertEquals(1000000000000L, RepeatedString.repeatedString("a", 1000000000000L));
    }
}