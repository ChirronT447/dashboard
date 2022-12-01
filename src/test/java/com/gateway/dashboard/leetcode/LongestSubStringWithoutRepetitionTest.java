package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LongestSubStringWithoutRepetitionTest {

    @Test
    void lengthOfLongestSubstring() {
        // Example 1:
        //
        //Input: s = "abcabcbb"
        //Output: 3
        //Explanation: The answer is "abc", with the length of 3.
        //Example 2:
        //
        //Input: s = "bbbbb"
        //Output: 1
        //Explanation: The answer is "b", with the length of 1.
        //Example 3:
        //
        //Input: s = "pwwkew"
        //Output: 3
        //Explanation: The answer is "wke", with the length of 3.
        //Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
        Assertions.assertThat(
                LongestSubStringWithoutRepetition.lengthOfLongestSubstring("abcabcbb")
        ).isEqualTo(3);

        Assertions.assertThat(
                LongestSubStringWithoutRepetition.lengthOfLongestSubstring("bbbbb")
        ).isEqualTo(1);

        Assertions.assertThat(
                LongestSubStringWithoutRepetition.lengthOfLongestSubstring("pwwkew")
        ).isEqualTo(3);
    }
}