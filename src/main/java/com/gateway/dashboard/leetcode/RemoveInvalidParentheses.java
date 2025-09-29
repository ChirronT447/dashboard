package com.gateway.dashboard.leetcode;

import java.util.*;

/**
 * 301. Remove Invalid Parentheses
 * https://leetcode.com/problems/remove-invalid-parentheses/description/
 * Given a string s that contains parentheses and letters, remove the minimum number of invalid parentheses to make the input string valid.
 * Return a list of unique strings that are valid with the minimum number of removals. You may return the answer in any order.
 *
 * Example 1:
 *
 * Input: s = "()())()"
 * Output: ["(())()","()()()"]
 * Example 2:
 *
 * Input: s = "(a)())()"
 * Output: ["(a())()","(a)()()"]
 * Example 3:
 *
 * Input: s = ")("
 * Output: [""]
 */
public class RemoveInvalidParentheses {

    public List<String> removeInvalidParentheses(String s) {
        List<String> result = new ArrayList<>();
        if (s == null) {
            return result;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        boolean found = false;

        // Initial state
        queue.add(s);
        visited.add(s);

        while (!queue.isEmpty()) {
            String currentString = queue.poll();

            // Check if the current string is valid
            if (isValid(currentString)) {
                result.add(currentString);
                found = true; // We have found a solution at the current level
            }

            // If a solution is found, we don't need to explore further (shorter) strings.
            // We only need to finish checking other strings at the CURRENT level.
            if (found) {
                continue;
            }

            // Generate all possible next states by removing one parenthesis
            for (int i = 0; i < currentString.length(); i++) {
                char currentChar = currentString.charAt(i);

                // We only care about removing parentheses
                if (currentChar != '(' && currentChar != ')') {
                    continue;
                }

                // Create the next string by removing the character at index i
                String nextString = currentString.substring(0, i) + currentString.substring(i + 1);

                if (!visited.contains(nextString)) {
                    visited.add(nextString);
                    queue.add(nextString);
                }
            }
        }

        return result;
    }

    /**
     * Helper function to check if a string has valid parentheses.
     * A string is valid if:
     * 1. The total number of '(' equals the total number of ')'.
     * 2. At any point while scanning from left to right, the count of ')'
     * does not exceed the count of '('.
     */
    private boolean isValid(String s) {
        int count = 0; // Acts as a balance counter
        for (char c : s.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }
            // If count is negative, it means we have a closing parenthesis
            // without a matching opening one before it.
            if (count < 0) {
                return false;
            }
        }
        // A valid string must have a final balance of 0.
        return count == 0;
    }

}
