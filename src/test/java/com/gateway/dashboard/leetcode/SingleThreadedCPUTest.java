package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.*;

class SingleThreadedCPUTest {

    @Test
    void getOrder() {
        // [1,2],[2,4],[3,2],[4,1]
        Assertions.assertThat(
                SingleThreadedCPU.getOrder(new int[][]{{1,2}, {2,4}, {3,2}, {4,1}})
        ).isEqualTo(new int[]{0,2,3,1});
    }
}