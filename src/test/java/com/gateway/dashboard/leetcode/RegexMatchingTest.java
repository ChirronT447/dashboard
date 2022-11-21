package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegexMatchingTest {

    @Test
    void isMatch() {
        // Input: s = "aa", p = "a"
        // Output: false
        // Explanation: "a" does not match the entire string "aa".
        Assertions.assertFalse(RegexMatching.isMatch("aa", "a"));

        // Input: s = "aa", p = "a*"
        // Output: true
        // Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
        Assertions.assertTrue(RegexMatching.isMatch("aa", "a*"));

        // Input: s = "aa", p = "*"
        // Output: true
        // Explanation: '*' matches any sequence.
        //Assertions.assertTrue(RegexMatching.isMatch("aa", "*"));

        // Input: s = "ab", p = ".*"
        // Output: true
        // Explanation: ".*" means "zero or more (*) of any character (.)".
        Assertions.assertTrue(RegexMatching.isMatch("ab", ".*"));

        // Input: s = "cb", p = "?a"
        // Output: false
        // Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
        Assertions.assertFalse(RegexMatching.isMatch("cb", "?a"));
    }

}