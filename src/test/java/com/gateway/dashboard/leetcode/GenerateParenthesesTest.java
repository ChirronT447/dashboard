package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateParenthesesTest {

    @Test
    void generateParenthesis() {

        // Input: n = 1
        // Output: ["()"]
        Assertions.assertThat(
                GenerateParentheses.generateParenthesis(1)
        ).isEqualTo(List.of("()"));

        // Input: n = 3
        // Output: ["((()))","(()())","(())()","()(())","()()()"]
        Assertions.assertThat(
                GenerateParentheses.generateParenthesis(3)
        ).isEqualTo(List.of("()()()", "()(())", "(())()", "(()())", "((()))"));
    }

}