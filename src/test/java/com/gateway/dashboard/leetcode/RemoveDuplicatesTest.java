package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveDuplicatesTest {

    @Test
    void removeDuplicates() {
        Assertions.assertThat(RemoveDuplicates.removeDuplicates("aaabbcccbca", 3))
                .isEqualTo("ca");
    }
}