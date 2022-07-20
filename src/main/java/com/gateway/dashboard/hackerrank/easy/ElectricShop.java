package com.gateway.dashboard.hackerrank.easy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/electronics-shop/problem
 *
 * Determine the most expensive computer keyboard and mouse that can be purchased with a given budget.
 * Given price lists for keyboards and mice and a budget, find the cost to buy them.
 * If it is not possible to buy both items, return -1
 *
 * Example:
 * - max = 60
 * - keyboards = [40, 50, 60]
 * - mice = [5, 8, 12]
 *
 * The person can buy 40 + 12 = 52 or 50 + 8 = 58.
 * Choose the latter as the more expensive option and return 58
 */
public class ElectricShop {

    public static int calculateMoneySpent(int target, Integer[] larger, int[] smaller) {
        int max = -1;
        if(larger.length == 0 || smaller.length == 0) {
            return max;
        }

        Arrays.sort(smaller);
        Arrays.sort(larger, Comparator.reverseOrder());

        for(int lIdx = 0, j  = 0; lIdx < larger.length && j < smaller.length;) {
            int sum = larger[lIdx] + smaller[j];
            System.out.println("Checking: " + larger[lIdx] + " and " +  smaller[j]);
            if(sum < target && sum > max) {     // Found a new max
                max = sum;
                j++;        // Check the next largest j
                System.out.println("New max: " + max);
            } else {
                lIdx++;     // otherwise move through the larger value items
                j = 0;      // no point in looking at the smaller array
            }
        }

        System.out.println("Final result: " +max);
        return max;
    }

}
