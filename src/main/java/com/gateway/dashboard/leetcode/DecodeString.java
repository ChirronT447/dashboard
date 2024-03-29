package com.gateway.dashboard.leetcode;

import java.util.Stack;

/**
 * 394. Decode String - Medium
 * https://leetcode.com/problems/decode-string/description/
 * Given an encoded string, return its decoded string.
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times. Note that k is guaranteed to be a positive integer.
 * You may assume that the input string is always valid; there are no extra white spaces, square brackets are well-formed, etc. Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k. For example, there will not be input like 3a or 2[4].
 * The test cases are generated so that the length of the output will never exceed 105.
 */
public class DecodeString {

    public static String decodeString(String s) {
        String result = "";
        final Stack<Integer> countStack = new Stack<>();
        final Stack<String> resStack = new Stack<>();
        int idx = 0;
        while (idx < s.length()) {
            if (Character.isDigit(s.charAt(idx))) {
                int count = 0;
                while (Character.isDigit(s.charAt(idx))) {
                    count = 10 * count + Character.getNumericValue(s.charAt(idx++));
                }
                countStack.push(count);
            }
            else if (s.charAt(idx) == '[') {
                resStack.push(result);
                result = "";
                idx++;
            }
            else if (s.charAt(idx) == ']') {
                result = resStack.pop() + result.repeat(Math.max(0, countStack.pop()));
                idx++;
            }
            else {
                result += s.charAt(idx++);
            }
        }
        return result;
    }

}
