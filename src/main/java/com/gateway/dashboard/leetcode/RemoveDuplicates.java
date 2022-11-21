package com.gateway.dashboard.leetcode;

import java.util.Stack;

/**
 * 1209. Remove All Adjacent Duplicates in String II
 * https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/description/
 * You are given a string s and an integer k, a k duplicate removal consists of choosing k adjacent and equal letters from s and removing them, causing the left and the right side of the deleted substring to concatenate together.
 * We repeatedly make k duplicate removals on s until we no longer can.
 * Return the final string after all such duplicate removals have been made. It is guaranteed that the answer is unique.
 */
public class RemoveDuplicates {

    public static String removeDuplicates(final String s, final int k) {
        final Stack<Pair> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (stack.isEmpty() || stack.peek().character != c) {
                stack.push(new Pair(c, 1));
            } else if (stack.peek().character == c) {
                if (stack.peek().count < k - 1) {
                    stack.peek().count++;
                }
                else if (stack.peek().count == k - 1) {
                    stack.pop();
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        for (Pair p : stack) {
            while (p.count-- > 0) {
                sb.append(p.character);
            }
        }
        return sb.toString();
    }

    static class Pair {
        char character;
        int count;

        public Pair(char character, int count) {
            this.character = character;
            this.count = count;
        }
    }

}
