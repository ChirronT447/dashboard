package com.gateway.dashboard.hired;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DebuggingOneTest {

    @Test
    void solution() {
        // {column, rows, price, quantity}
        int[][] inventory = new int[][]{ {0, 1, 100, 2}, {0, 2, 50, 1}};
        int[][] purchaseAttempts = new int[][]{ {0, 1}, {0, 1}, {0, 0}};
        Assertions.assertThat(
                VendingMachine.solution(inventory, purchaseAttempts)
        ).isEqualTo(50);
    }

    @Test
    void testInvalidInventory() {
        // {column, rows, price, quantity}
        int[][] inventory = new int[][]{ {0}, {0, 5, 20, 3}};
        int[][] purchaseAttempts = new int[][]{ {0, 1}, {0, 5}};
        Assertions.assertThat(
                VendingMachine.solution(inventory, purchaseAttempts)
        ).isEqualTo(40);
    }

    @Test
    void testValueCheck() {
        //                           /  3  1  4   2  /  5
        int[] inventory = new int[]{ 4, 9, 3, 12, 6, 4, 15};
        Assertions.assertThat(
                DebuggingOne.findLargestMultiple(inventory, 4, 3)
        ).isEqualTo(12);
    }
}