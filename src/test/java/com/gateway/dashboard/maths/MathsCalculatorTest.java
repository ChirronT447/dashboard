package com.gateway.dashboard.maths;

import com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils.Pair;
import org.assertj.core.api.IntegerAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}