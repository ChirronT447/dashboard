package com.gateway.dashboard.maths;

import com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils.Pair;
import com.gateway.dashboard.string.StringCalculator;
import org.assertj.core.api.IntegerAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MathsCalculatorTest {

    @Test
    void testFractionCalc() {
        // (2 / 3) + (4 / 12) = (2 / 3) + (1 / 3) = 1 / 1
        int[] actual = MathsCalculator.addFractions(new int[]{2, 3}, new int[]{4, 12});
        Assertions.assertArrayEquals(new int[]{1, 1}, actual);

        // (1/500) + (2/1500) = 5 / 1500 = 1 / 300
        actual = MathsCalculator.addFractions(new int[]{1, 500}, new int[]{2, 1500});
        Assertions.assertArrayEquals(new int[]{1, 300}, actual);

        // (1/3) + (1/4) = 7 / 12
        actual = MathsCalculator.addFractions(new int[]{1, 3}, new int[]{1, 4});
        Assertions.assertArrayEquals(new int[]{7, 12}, actual);
    }

    @Test
    void testGreatestCommonDivisor() {
        // gcd(42, 56) = 14
        Assertions.assertEquals(14, MathsCalculator.greatestCommonDivisor(42, 56));
    }

    @Test
    void testLowestCommonMultiple() {
        // lcm(12, 18) is 36.
        Assertions.assertEquals(36, MathsCalculator.lowestCommonMultiple(12, 18));
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testDotProduct() {
        int[] vectA = { 3, -5, 4 };
        int[] vectB = { 2, 6, 5  };

        int result = MathsCalculator.dotProduct(vectA, vectB);
        Assertions.assertEquals(-4, result);
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testCountCycles() {
        int[] cycle = { 1, 2, 0 };
        int result = MathsCalculator.countCycles(cycle, 0);
        Assertions.assertEquals(result, 3);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testPrimeFactors() {
        Set<Integer> results = MathsCalculator.primeFactors(315);  // Factors: [3, 5, 7]
        Set<Integer> check = Stream.of(3, 5, 7).collect(Collectors.toSet());
        Assertions.assertEquals(results, check);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testIsPowerOfTen() {
        Assertions.assertTrue(MathsCalculator.isPowerOfTen(10));
        Assertions.assertTrue(MathsCalculator.isPowerOfTen(1000));
        Assertions.assertFalse(MathsCalculator.isPowerOfTen(20));
        Assertions.assertFalse(MathsCalculator.isPowerOfTen(11));
    }

    @Test
    public void testPower() {
        double res = MathsCalculator.power(5, 2);
        Assertions.assertEquals(25, res);
    }

    @Test
    public void testPowerWithMath() {
        double res = MathsCalculator.powerWithMath(5, 2);
        Assertions.assertEquals(25, res);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testMergeOverlappingIntervals() {
        Pair arr[] = new Pair[4];
        arr[0] = new Pair(6, 8);
        arr[1] = new Pair(1, 9);
        arr[2] = new Pair(2, 4);
        arr[3] = new Pair(4, 7);
        Stack res = MathsCalculator.mergeOverlappingIntervals(arr);
        Assertions.assertNotNull(res);
    }

    @Test
    public void testMergeOverlappingIntervalsMultiple() {
        Pair arr[] = new Pair[3];
        arr[0] = new Pair(6, 8);
        arr[1] = new Pair(1, 3);
        arr[2] = new Pair(2, 4);
        Stack res = MathsCalculator.mergeOverlappingIntervals(arr);
        Assertions.assertNotNull(res);
    }

    // ----------------------------------------------------------------------------------

    // Calculate what index in the input array can "see" sea level
    // Input: [4, 3, 2, 3, 1] => Sea level is 0 here <=
    // Result: [0, 3, 4]
    @Test
    public void testCalculateSeaView() {
        int[] arr = new int[]{4, 3, 2, 3, 1};
        List<Integer> res = MathsCalculator.calculateSeaView(arr);
        Assertions.assertEquals(res, List.of(0, 3, 4));
    }

    // ----------------------------------------------------------------------------------

    // Find contiguous sequence summing to target. eg:
    // ([1, 3, 1, 4, 23], 8) => True [3, 1, 4]
    // ([1, 3, 1, 4, 23], 7) => False
    @Test
    public void testFindTargetInSequence() {
        Assertions.assertTrue(
                MathsCalculator.findTargetInSequence(new int[]{1, 3, 1, 4, 23}, 8)
        );
        Assertions.assertFalse(
                MathsCalculator.findTargetInSequence(new int[]{1, 3, 1, 4, 23}, 7)
        );
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testTwoSum() {
        // Input: nums = [2,7,11,15], target = 9
        // Output: [0,1]
        // Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
        Assertions.assertArrayEquals(
                new int[]{0, 1},
                MathsCalculator.twoSum(new int[]{2,7,11,15}, 9)
        );

        // Input: nums = [3,2,4], target = 6
        // Output: [1,2]
        Assertions.assertArrayEquals(
                new int[]{1, 2},
                MathsCalculator.twoSum(new int[]{3, 2, 4}, 6)
        );
    }

    // ----------------------------------------------------------------------------------

    @Test
    void testAddTwoNumbersZero() {
        // Input: l1 = [0], l2 = [0]
        // Output: [0]
        Assertions.assertEquals(
                MathsCalculator.buildLinkedList(0, 0),
                MathsCalculator.addTwoNumbers(
                        MathsCalculator.buildLinkedList(0, 0),
                        MathsCalculator.buildLinkedList(0, 0)
                )
        );
    }

    @Test
    public void testAddTwoNumbers() {
        // Input: l1 = [2,4,3], l2 = [5,6,4]
        // Output: [7,0,8]
        // Explanation: 342 + 465 = 807.
        Assertions.assertEquals(
            MathsCalculator.buildLinkedList(0, 7,0,8),
            MathsCalculator.addTwoNumbers(
                    MathsCalculator.buildLinkedList(0, 2,4,3),
                    MathsCalculator.buildLinkedList(0, 5,6,4)
            )
        );
    }

    @Test
    void testAddTwoNumbersOverflow() {
        //        l2 = [9,9,9,9]
        // Input: l1 = [9,9,9,9,9,9,9],
        // Output:     [8,9,9,9,0,0,0,1]
        Assertions.assertEquals(
                MathsCalculator.buildLinkedList(0,  8,9,9,9,0,0,0,1),
                MathsCalculator.addTwoNumbers(
                        MathsCalculator.buildLinkedList(0, 9,9,9,9,9,9,9),
                        MathsCalculator.buildLinkedList(0, 9,9,9,9)
                )
        );
    }

    // -------------------------------------------------------------------

    @Test
    void testCountIslands() {
        int[][] M = {
                {1, 1, 0, 0, 0},
                {0, 1, 0, 0, 1},
                {1, 0, 0, 1, 1},
                {0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1}
        };

        System.out.println("Number of islands is: " + MathsCalculator.countIslands(M));
    }
}