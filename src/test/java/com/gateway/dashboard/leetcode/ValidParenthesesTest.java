package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidParenthesesTest {

    @Test
    void isValid() {
        Assertions.assertThat(ValidParentheses.isValid("()")).isTrue();
        Assertions.assertThat(ValidParentheses.isValid("()[]{}")).isTrue();
        Assertions.assertThat(ValidParentheses.isValid("(}")).isFalse();
        Assertions.assertThat(ValidParentheses.isValid("((hjhj))")).isFalse();
    }
}