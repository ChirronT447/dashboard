package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LetterCombinationTest {

    @Test
    void letterCombinations() {
        //Input: digits = "2"
        //Output: ["a","b","c"]
        Assertions.assertThat(LetterCombination.letterCombinations("2")).isEqualTo(List.of("a", "b", "c"));
        //Input: digits = ""
        //Output: []
        Assertions.assertThat(LetterCombination.letterCombinations("")).isEqualTo(List.of());
        //Input: digits = "23"
        //Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
        Assertions.assertThat(LetterCombination.letterCombinations("23"))
                .isEqualTo(List.of("ad","ae","af","bd","be","bf","cd","ce","cf"));
    }
}