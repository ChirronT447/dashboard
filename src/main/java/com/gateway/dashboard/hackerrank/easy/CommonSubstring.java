package com.gateway.dashboard.hackerrank.easy;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Given two strings, determine if they share a common substring.
 *  A substring may be as small as one character.
 *
 * For example, the words "a", "and", "art" share the common substring .
 * The words "be" and "cat" do not share a substring.
 */
public class CommonSubstring {

    /**
     * Complete the function twoStrings in the editor below.
     * It should return a string, either YES or NO based on whether the strings share a common substring.
     *
     * twoStrings has the following parameter(s):
     * s1, s2: two strings to analyze .
     */
    static boolean twoStrings(String str1, String str2) {
        if(str1 == null || str2 == null) {
            return false;
        }

        Map<String, Long> str1CharCount = str1.codePoints()
                .mapToObj(Character::toString)
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting())
                );

        return str2.chars()
                .mapToObj(Character::toString)
                .distinct()
                .anyMatch(str1CharCount::containsKey);

        // return result ? "YES" : "NO";
    }

}
