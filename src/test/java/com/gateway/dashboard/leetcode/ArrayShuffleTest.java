package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArrayShuffleTest {

    @Test
    void shuffle() {
        ArrayShuffle arrayShuffle = new ArrayShuffle(new int[]{1, 2, 3});
        int i = 10;
        while(i-- > 0) {
            print(arrayShuffle.shuffle());
        }
    }

    private void print(int[] shuffle) {
        Arrays.stream(shuffle).forEach(e -> System.out.print(e + ", "));
        System.out.println("---");
    }

}