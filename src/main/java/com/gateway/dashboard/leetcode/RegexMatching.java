package com.gateway.dashboard.leetcode;

/**
 * 10. Regular Expression Matching - Hard
 * https://leetcode.com/problems/regular-expression-matching/
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 * --
 * 44. Wildcard Matching - Hard
 * https://leetcode.com/problems/wildcard-matching/description/
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*' where:
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 */
public class RegexMatching {

    public static boolean isMatch(final String text, final String pattern) {
        if (pattern.isEmpty()) return text.isEmpty();
        final boolean firstMatch = text.length() > 0 && (
                text.charAt(0) == pattern.charAt(0) || pattern.charAt(0) == '.' || pattern.charAt(0) == '?'
        );
        if (pattern.length() > 1 && pattern.charAt(1) == '*') {  // second char is '*'
            if(isMatch(text, pattern.substring(2))) return true;
            return firstMatch && isMatch(text.substring(1), pattern);
        } else { // second char is not '*'
            return firstMatch && isMatch(text.substring(1), pattern.substring(1));
        }
    }

}
