package com.gateway.dashboard.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 22. Generate Parentheses - https://leetcode.com/problems/generate-parentheses/ - Medium
 * Write a function to generate all combinations of "n" paired parentheses.
 * ie. n == 1 == "()"
 */
public class GenerateParentheses {

    public static List<String> generateParenthesis(final int n) {
        final List<String> result = new ArrayList<>();
        if (n == 0) {
            result.add("");
        } else {
            for (int idx = 0; idx < n; ++idx)
                for (String left: generateParenthesis(idx))
                    for (String right: generateParenthesis(n - 1 - idx))
                        result.add("(" + left + ")" + right);
        }
        return result;
    }
}
