package com.gateway.dashboard.hired;

import java.util.HashMap;

public class DebuggingThree {

    static class Solution {
        public static double tradeCalculator(double[] prices, int[][] trades) {
            double profit = 0;
            for (int[] trade : trades) {
                long sharesTraded = trade[1];
                double price = prices[trade[0]];
                profit += -(price * sharesTraded);
            }
            return profit;
        }
    }
}

class Factorial {

    private static final HashMap<Integer, Integer> cache = new HashMap<>();
    private static Integer calculationsPerformed = 0;

    public static long[][] calculate(long[] numbersToCalculate) {
        long[][] results = new long[numbersToCalculate.length][2];
        for (int i = 0; i < numbersToCalculate.length; i++) {
            Integer result = factorial((int)numbersToCalculate[i]);
            results[i][0] = result.longValue();
            results[i][1] = calculationsPerformed.longValue();
            calculationsPerformed = 0;
        }
        cache.clear();
        return results;
    }

    private static Integer factorial(Integer value) {
        if (value <= 1) {
            return value;
        }

        Integer cached = cache.get(value);
        if (cached != null) {
            return cached;
        }

        // n * factorialUsingRecursion(n - 1);
        Integer result = value * factorial(value - 1);
        cache.put(value, result);
        calculationsPerformed++;
        return result;
    }
}
