package com.gateway.dashboard.hackerrank.easy;

import com.gateway.dashboard.hackerrank.easy.JumpingOnTheClouds;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JumpingOnTheCloudsTest {

    @Test
    void jumpingOnClouds() {
        assertEquals(4,
                JumpingOnTheClouds.jumpingOnClouds(
                        Stream.of(0, 0, 1, 0, 0, 1, 0).mapToInt( i -> i ).toArray()
                )
        );
        assertEquals(3,
                JumpingOnTheClouds.jumpingOnClouds(
                        Stream.of(0, 0, 0, 1, 0, 0).mapToInt( i -> i ).toArray()
                )
        );
    }
}