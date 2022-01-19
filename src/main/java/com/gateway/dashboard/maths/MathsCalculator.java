package com.gateway.dashboard.maths;

import com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils.Pair;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MathsCalculator {

    /**
     * The greatest common divisor is useful for reducing fractions to be in lowest terms.
     * For example, gcd(42, 56) = 14
     * @param number1
     * @param number2
     * @return greatestCommonDivisor
     */
    public static int greatestCommonDivisor(final int number1, final int number2) {
        if(number1 == 0 || number2 == 0) {
            return number1 + number2;
        } else {
            int biggerValue = Math.abs(Math.max(number1, number2));
            int smallerValue = Math.abs(Math.min(number1, number2));
            return greatestCommonDivisor(biggerValue % smallerValue, smallerValue);
        }
    }

    /**
     * The least common multiple (LCM) of two numbers is the smallest number (not zero)
     * that is a multiple of both / the smallest positive integer that is divisible by both.
     * @param number1
     * @param number2
     * @return lowestCommonMultiple
     */
    public static int lowestCommonMultiple(final int number1, final int number2) {
        Assert.isTrue(number1 != 0 && number2 != 0, "Unable to calculate LCM");
        return (number1 * number2) / greatestCommonDivisor(number1, number2);
    }

    // Function to convert the obtained fraction into it's simplest form
    public static int[] simplifyFractionForm(int numerator, int denominator) {
        int commonFactor = greatestCommonDivisor(numerator, denominator);

        // Simplifying by dividing by a common factor
        numerator = numerator / commonFactor;
        denominator = denominator / commonFactor;

        System.out.println(numerator+"/"+denominator);
        return new int[]{numerator, denominator};
    }

    /**
     * Add two fractions eg.
     * [2 / 3] + [1 / 2] = [7 / 6]
     * @param fraction1
     * @param fraction2
     * @return
     */
    public static int[] addFractions(int[] fraction1, int[] fraction2) {
        // Check not null, check not empty etc etc.
        final int bottomLeft    = fraction1[1]; // 1
        final int bottomRight   = fraction2[1]; // 3
        final int topLeft       = fraction1[0]; // 1
        final int topRight      = fraction2[0]; // 4

        // Denominator of the final fraction: common denominator = 12
        final int commonDenom = lowestCommonMultiple(bottomLeft, bottomRight);

        // Numerator of the final fraction:                             // eg.
        final int numerator =   topLeft  * (commonDenom / bottomLeft) + // 1 * 12   +   1 * 12
                                topRight * (commonDenom / bottomRight); //     --           --
                                                                        //      3            4

        // Calling function to convert final fraction into it's simplest form: 7 / 12
        return simplifyFractionForm(numerator, commonDenom);
    }

    // ----------------------------------------------------------------------------------

    /**
     * Calculate dot product of two vector array.
     */
    public static int dotProduct(int[] vectA, int[] vectB) {
        // Assert not null && length is equal
        int vectLength = vectA.length;

        // Loop & calculate dot product
        int product = 0;
        for (int i = 0; i < vectLength; i++) {
            product = product + vectA[i] * vectB[i];
        }

        return product;
    }

    // ----------------------------------------------------------------------------------

    /**
     * Starting from data[index] follow each element to the index it points to.
     * Continue until you find a cycle.
     * @param data
     * @param index
     * @return
     */
    public static int countCycles(int[] data, int index) {
        int count = 0;
        while(data[index] >= 0) {
            int tmp = data[index];
            data[index] = -(++count);
            index = tmp;
        }
        return count + data[index] + 1;
    }

    // ----------------------------------------------------------------------------------

    public static Set<Integer> primeFactors(int n) {
        Set<Integer> factors = new HashSet<>();

        // Checking 2 and while n divides by 2
        if(n % 2 == 0) {
            factors.add(2);
            while(n % 2 == 0) {
                n = n / 2;
            }
        }

        // While i divides n, count i and divide n by i
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if(n % i == 0) {
                factors.add(i);
                while (n % i == 0) {
                    n = n / i;
                }
            }
        }

        // This condition is to handle when  n is a prime number greater than 2
        if (n > 2) {
            factors.add(n);
        }

        return factors;
    }

    // ----------------------------------------------------------------------------------

    public static boolean isPowerOfTen(int input) {
        while(input >= 10 && input % 10 == 0) {
            input = input / 10;
        }
        return input == 1;
    }

    public static double power(double base, int exponent) {
        double result = base;
        for(int i = 1; i < exponent; i++) {
            result = result * base;
        }
        return result;
    }

    public static int powerWithMath(double base, int exponent) {
        double val = exponent * Math.log(base);
        double res =  Math.exp(val);
        return (int) Math.round(res);
    }

    // ----------------------------------------------------------------------------------

    // Merge overlapping intervals
    public static Stack mergeOverlappingIntervals(Pair<Integer, Integer>[] intervals) {
        Stack<Pair<Integer, Integer>> stack = new Stack<>();
        if(intervals.length <= 0) {
            return stack;
        }

        // Sort intervals in increasing order
        Arrays.sort(intervals, Comparator.comparingInt(Pair::getLow));

        stack.push(intervals[0]); // Push the first interval
        for(int i = 1; i < intervals.length; i++) {
            Pair<Integer, Integer> top = stack.peek(); // Investigate the top one
            if(top.getHigh() < intervals[i].getLow()) {
                stack.push(intervals[i]);
            } else if (top.getHigh() < intervals[i].getHigh()){
                top.setHigh(intervals[i].getHigh());
                stack.pop();
                stack.push(top);
            }
        }

        System.out.println("Merged intervals:");
        while(!stack.isEmpty()) {
            Pair<Integer, Integer> pop = stack.pop();
            System.out.println("[" + pop.getLow() + "," + pop.getHigh() + "]");
        }

        return stack;
    }

    // ----------------------------------------------------------------------------------

    // Calculate what index in the input array can "see" sea level
    // Input: [4, 3, 2, 3, 1] => Sea level is 0 here <=
    // Result: [0, 3, 4]
    public static List<Integer> calculateSeaView(int[] apartments) {
        final List<Integer> result = new ArrayList<>();
        if(apartments == null || apartments.length <= 0) {
            return result;
        }

        int maxHeight = 0; // Starting at sea level
        for(int i = apartments.length - 1; i >= 0; i--) {
            if(apartments[i] > maxHeight) {
                result.add(i);
                maxHeight = apartments[i];
            }
        }

        Collections.reverse(result);
        return result;
    }

    // ----------------------------------------------------------------------------------

    // Find contiguous sequence summing to target. eg:
    // ([1, 3, 1, 4, 23], 8) => True [3, 1, 4]
    // ([1, 3, 1, 4, 23], 7) => False
    public static boolean findTargetInSequence(int[] sequence, final int target) {

        int startIndex = 0;
        int endIndex = 0;
        int total = 0;
        for (int num : sequence) {
            total += num;
            if (total >= target) {
                break;
            }
            endIndex++;
        }

        if(total == target) {
            return true;
        } else {
            while(endIndex != sequence.length) {
                total -= sequence[startIndex]; // Shorten the window
                startIndex++;
                if(total == target) {
                    return true;
                }
                total += sequence[endIndex]; // Widen the window
                endIndex++;
                if(total == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // ----------------------------------------------------------------------------------

    // Leetcode 1) https://leetcode.com/problems/two-sum/
    // Given an array of integers and an integer target, return indices of the two numbers such
    //  that they add up to target. You may assume that each input would have exactly one solution, and
    //  you may not use the same element twice. You can return the answer in any order.
    public static int[] twoSum(int[] nums, int target) {
        for(int i = 1; i < nums.length; i++) {
            if(nums[i] + nums[i-1] == target) {
                System.out.println("Found: [" + (i-1) + "," + i + "]");
                return new int[]{i-1, i};
            }
        }
        throw new IllegalArgumentException("Array does not contain two elements that sum to target");
    }

    // ----------------------------------------------------------------------------------

    // Leetcode 2) https://leetcode.com/problems/add-two-numbers/
    // You are given two non-empty linked lists representing two non-negative integers.
    // The digits are stored in reverse order, and each of their nodes contains a single digit.
    // Add the two numbers and return the sum as a linked list.
    //
    // You may assume the two numbers do not contain any leading zero, except the number 0 itself.
    final static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }

        @Override
        public String toString() {
          return "Node: " + val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode listNode = (ListNode) o;
            return val == listNode.val && Objects.equals(next, listNode.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(val, next);
        }
    }

    public static ListNode buildLinkedList(int index, int...nums) {
        if(index < nums.length) {
            return new ListNode(nums[index], buildLinkedList(index + 1, nums));
        } else {
            return null;
        }
    }

    // Easier to add as the digits are in reverse order
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        addNumbersRecursively(result, l1, l2);
        return result;
    }

    private static ListNode addNumbersRecursively(ListNode sum, ListNode l1, ListNode l2) {
        ListNode nextNode = new ListNode();
        System.out.println(l1 + " - " + l2);

        if (l1 != null && l2 != null) { // If we have two numbers, sum them (+ any overflow)
            sum.val += l1.val + l2.val;
            while(sum.val >= 10) { // if there's an overflow; take the remainder, add to the next node
                System.out.println("Overflow: " + sum.val);
                sum.val = sum.val % 10;
                nextNode.val++;
            } // Continue if we have at least one node, if we have none we can stop
            if (l1.next != null || l2.next != null) {
                sum.next = nextNode;
                return addNumbersRecursively(nextNode, l1.next, l2.next);
            }
        }

        if (l1 == null && l2 != null) { // If we've run out of L1 but have some L2
            sum.val += l2.val;
            while(sum.val >= 10) {
                System.out.println("Overflow: " + sum.val);
                sum.val = sum.val % 10;
                nextNode.val++;
            }
            sum.next = nextNode;
            return addNumbersRecursively(nextNode, null, l2.next);
        }

        if (l1 != null && l2 == null){ // If we're run out of L2 but have some L1
            sum.val += l1.val;
            while(sum.val >= 10) {
                System.out.println("Overflow: " + sum.val);
                sum.val = sum.val % 10;
                nextNode.val++;
            }
            sum.next = nextNode;
            return addNumbersRecursively(nextNode, l1.next, null);
        }

        System.out.println("Done... ");
        return sum;
    }

    // ----------------------------------------------------------------------------------


}
