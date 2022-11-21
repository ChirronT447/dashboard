package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrappingRainWaterTest {

    @Test
    void trap() {
        // Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
        //Output: 6
        //Explanation: The above elevation map (black section) is represented by array
        //  [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
        Assertions.assertThat(
                TrappingRainWater.trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1})
        ).isEqualTo(6);

        // Input: height = [4,2,0,3,2,5]
        //Output: 9
        Assertions.assertThat(
                TrappingRainWater.trap(new int[]{4,2,0,3,2,5})
        ).isEqualTo(9);
    }
}