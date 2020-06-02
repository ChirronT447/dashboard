package com.gateway.dashboard.interviews.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchingBracketsTest {

    @Test
    void isValidExpression() {
        assertTrue(MatchingBrackets.isValidExpression("(()){}()"));
        assertFalse(MatchingBrackets.isValidExpression("({}("));
    }
}