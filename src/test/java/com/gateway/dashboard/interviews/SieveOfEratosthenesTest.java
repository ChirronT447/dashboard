package com.gateway.dashboard.interviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SieveOfEratosthenesTest {

    @Test
    void sieveOfEratosthenes() {
        Set<Integer> result = SieveOfEratosthenes.sieveOfEratosthenes(0, 10);
        Assertions.assertTrue(result.containsAll(Set.of(2, 3, 5, 7)));
    }
}