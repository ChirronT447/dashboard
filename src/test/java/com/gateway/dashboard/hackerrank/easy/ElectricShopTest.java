package com.gateway.dashboard.hackerrank.easy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectricShopTest {

    @Test
    void calculateMoneySpent() {
        Assertions.assertEquals(
                58,
                ElectricShop.calculateMoneySpent(60, new Integer[]{40, 50, 60}, new int[]{5, 12, 8})
        );

        Assertions.assertEquals(
                115,
                ElectricShop.calculateMoneySpent(120, new Integer[]{20, 90, 100}, new int[]{5, 15, 10})
        );

        Assertions.assertEquals(
                -1,
                ElectricShop.calculateMoneySpent(120, new Integer[]{20, 90, 100}, new int[]{})
        );
    }
}