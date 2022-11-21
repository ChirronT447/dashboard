package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecodeStringTest {

    @Test
    void decodeString() {
        // Example 1:
        //
        //Input: s = "3[a]2[bc]"
        //Output: "aaabcbc"
        //Example 2:
        //
        //Input: s = "3[a2[c]]"
        //Output: "accaccacc"
        //Example 3:
        //
        //Input: s = "2[abc]3[cd]ef"
        //Output: "abcabccdcdcdef"
        Assertions.assertThat(DecodeString.decodeString("3[a]2[bc]"))
                .isEqualTo("aaabcbc");

        Assertions.assertThat(DecodeString.decodeString("3[a2[c]]"))
                .isEqualTo("accaccacc");

        Assertions.assertThat(DecodeString.decodeString("2[abc]3[cd]ef"))
                .isEqualTo("abcabccdcdcdef");
    }

}