package com.gateway.dashboard.leetcode;

import java.util.Random;

/**
 * 384. Shuffle an Array - Medium
 * https://leetcode.com/problems/shuffle-an-array/
 * Given an integer array nums, design an algorithm to randomly shuffle the array. All permutations of the array should be equally likely as a result of the shuffling.
 * Implement the Solution class:
 * Solution(int[] nums) Initializes the object with the integer array nums.
 * int[] reset() Resets the array to its original configuration and returns it.
 * int[] shuffle() Returns a random shuffling of the array.
 */
public class ArrayShuffle {

    private final int[] inputArray;
    private final int arrayLength;
    private final Random random;

    public ArrayShuffle(int[] inputArray) {
        this.inputArray = inputArray != null ? inputArray : new int[0];
        this.arrayLength = this.inputArray.length;
        this.random = new Random();
    }

    /** Return original array */
    public int[] reset() {
        return inputArray;
    }

    /** Return a random shuffling */
    public int[] shuffle() {
        int[] result = inputArray.clone();
        for(int i = arrayLength; i > 0; i--) {
            swap(result, i - 1, random.nextInt(i));
        }
        return result;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
