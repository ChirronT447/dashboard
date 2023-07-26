package com.gateway.dashboard.interviews;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SieveOfEratosthenes {

    // Given two numbers (an interval range), find the prime numbers between this interval.
    public static Set<Integer> sieveOfEratosthenes(final int start, final int n) {
        final boolean[] prime = new boolean[n + 2];
        Arrays.fill(prime, true);
        prime[0] = false;
        prime[1] = false;

        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                // Update all multiples of p >= the square of it. Numbers which are multiple of p and
                // are less than p^2 are already been marked.
                for (int i = p * p; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }

        // Retrieve all prime numbers in the range
        return IntStream.range(start, n)
                .filter(p -> prime[p])
                .peek(p -> System.out.print(p + " "))
                .boxed()
                .collect(Collectors.toSet());
    }
}