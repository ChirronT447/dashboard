package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FurthestBuildingTest {

    /**
     * Input: heights = [4,2,7,6,9,14,12], bricks = 5, ladders = 1
     * Output: 4
     * -
     * Input: heights = [4,12,2,7,3,18,20,3,19], bricks = 10, ladders = 2
     * Output: 7
     * -
     * Input: heights = [14,3,19,3], bricks = 17, ladders = 0
     * Output: 3
     */
    @Test
    void furthestBuilding() {
        Assertions.assertThat(
                FurthestBuilding.furthestBuilding(new int[]{4,2,7,6,9,14,12}, 5, 1)
        ).isEqualTo(4);

        Assertions.assertThat(
                FurthestBuilding.furthestBuilding(new int[]{4,12,2,7,3,18,20,3,19}, 10, 2)
        ).isEqualTo(7);

        Assertions.assertThat(
                FurthestBuilding.furthestBuilding(new int[]{14,3,19,3}, 17, 0)
        ).isEqualTo(3);
    }
}