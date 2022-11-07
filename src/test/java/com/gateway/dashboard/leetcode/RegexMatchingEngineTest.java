package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexMatchingEngineTest {

    @Test
    void isMatch() {
        // Input: s = "aa", p = "a"
        // Output: false
        // Explanation: "a" does not match the entire string "aa".
        Assertions.assertFalse(RegexMatchingEngine.isMatch("aa", "a"));

        // Input: s = "aa", p = "a*"
        // Output: true
        // Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
        Assertions.assertTrue(RegexMatchingEngine.isMatch("aa", "a*"));

        // Input: s = "ab", p = ".*"
        // Output: true
        // Explanation: ".*" means "zero or more (*) of any character (.)".
        Assertions.assertTrue(RegexMatchingEngine.isMatch("ab", ".*"));
    }

}