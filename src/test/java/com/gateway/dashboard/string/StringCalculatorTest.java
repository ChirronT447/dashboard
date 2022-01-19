package com.gateway.dashboard.string;

import com.gateway.dashboard.maths.MathsCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

class StringCalculatorTest {

    @Test
    public void testReverseString() {
        String res = StringCalculator.reverseString("Test");
        Assertions.assertEquals(res, "tseT");
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testFindFirst() {
        Character res = StringCalculator.findFirst("Striing");
        Assertions.assertEquals('i', res);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testGroupAnagrams() {
        Collection<Set<String>>  res = StringCalculator.groupAnagrams(
                new String[] {"dog", "god", "art", "tar", "rub"}
        );
        Assertions.assertEquals(res.size(), 3);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testCD() {
        System.out.println(
                StringCalculator.cd("/home/dir/test/directory/../second/./../directory")
        );
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testlengthOfLongestSubstring() {
        // Input: s = "abcabcbb"
        // Output: 3
        // Explanation: The answer is "abc", with the length of 3.
        Assertions.assertEquals(3, StringCalculator.lengthOfLongestSubstring("abcabcbb"));

        //
        // Input: s = "bbbbb"
        // Output: 1
        // Explanation: The answer is "b", with the length of 1.
        Assertions.assertEquals(1, StringCalculator.lengthOfLongestSubstring("bbbbb"));

        //
        // Input: s = "pwwkew"
        // Output: 3
        // Explanation: The answer is "wke", with the length of 3.
        // Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
        Assertions.assertEquals(3, StringCalculator.lengthOfLongestSubstring("pwwkew"));
    }

}