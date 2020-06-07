package com.gateway.dashboard.hackerrank.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonSubstringTest {

    @Test
    void twoStrings() {
        assertTrue(CommonSubstring.twoStrings("hello", "world"));
        assertFalse(CommonSubstring.twoStrings("happy", "world"));
        assertFalse(CommonSubstring.twoStrings("", ""));
        assertFalse(CommonSubstring.twoStrings(null, null));
    }
}