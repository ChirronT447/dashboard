package com.gateway.dashboard.hired;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BackendTwoTest {

    @Test
    void testBackend() {
        Assertions.assertThat(
                BackendTwo.solution(new long[]{10, 941, 13, 13, 13, 941})
        ).contains(10);
    }

    @Test
    void testBackend2() {
        // plain_text: 'helloworld'
        // cipher_alphabet: 'mpgzkeyrsxfwlvjbcnuidhoqat'
        Assertions.assertThat(
                BackendTwo.solution("helloworld", "mpgzkeyrsxfwlvjbcnuidhoqat")
        ).isEqualTo("rkwwjojnwz");
    }

}