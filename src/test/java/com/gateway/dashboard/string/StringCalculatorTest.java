package com.gateway.dashboard.string;

import com.gateway.dashboard.hackerrank.medium.Anagram;
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

    // ----------------------------------------------------------------------------------

    @Test
    void testFindLargestPalindrome() {
        // Input: s = "313551"
        // Output: 531135
        Assertions.assertEquals("531135", StringCalculator.findLargestPalindrome("313551"));

        // No palindrome:
        Assertions.assertEquals("", StringCalculator.findLargestPalindrome("123456789"));

        // No palindrome: single 6 - 6531135
        Assertions.assertEquals("", StringCalculator.findLargestPalindrome("3135516"));

        // No palindrome: single 4 - 5431135
        Assertions.assertEquals("", StringCalculator.findLargestPalindrome("3135514"));

        // No palindrome: single 4 and single 2 - 54321135
        Assertions.assertEquals("", StringCalculator.findLargestPalindrome("31355142"));

        // Input: s = "33551"
        // Output: 53135
        Assertions.assertEquals("53135", StringCalculator.findLargestPalindrome("33551"));

        // Blank string:
        Assertions.assertEquals("", StringCalculator.findLargestPalindrome(""));
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testAreRotations() {
        // "ABACD" + "ABACD" = "ABACDABACD"
        // Since "CDABA" is a substring of this, "ABACD" and "CDABA" are rotations of each other.
        Assertions.assertTrue(StringCalculator.areRotations("ABACD", "CDABA"));

        // False due to different length
        Assertions.assertFalse(StringCalculator.areRotations("ABACD", "CDABAC"));
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testIsNumber() {
        Assertions.assertTrue(StringCalculator.isNumber("768"));

        Assertions.assertFalse(StringCalculator.isNumber("768.99"));

        Assertions.assertFalse(StringCalculator.isNumber("117B"));
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testAllCharactersSame() {
        Assertions.assertTrue(StringCalculator.allCharactersSame("dddd"));

        Assertions.assertFalse(StringCalculator.allCharactersSame("fffhfff"));

        Assertions.assertFalse(StringCalculator.allCharactersSame(""));
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testCheckBraces() {
        Assertions.assertIterableEquals(
                List.of(true, false, false, true, true),
                StringCalculator.checkBraces(
                        List.of("()", ")[]{{}})", "([{])", "([{}])", "").toArray(new String[0])
                )
        );
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testAngleMatching() {
        StringCalculator.angleMatching("><");
        StringCalculator.angleMatching("<<>>>>><<<>>");
    }

}