package com.gateway.dashboard.leetcode;

import java.util.Stack;

/**
 * 20. Valid Parentheses: - https://leetcode.com/problems/valid-parentheses/ - Easy
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']',
 *  determine if the input string is valid.
 * An input string is valid if:
 * - Open brackets must be closed by the same type of brackets.
 * - Open brackets must be closed in the correct order.
 * - Every close bracket has a corresponding open bracket of the same type.
 */
public class ValidParentheses {

    public static boolean isValid(final String str) {
        if(str == null || str.length() % 2 != 0) {
            return false;
        }
        final Stack<Character> stack = new Stack<>();
        for (char c : str.toCharArray()) {
            if (c == '(')
                stack.push(')');
            else if (c == '{')
                stack.push('}');
            else if (c == '[')
                stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c)
                return false;
        }
        return stack.isEmpty();
    }

}
