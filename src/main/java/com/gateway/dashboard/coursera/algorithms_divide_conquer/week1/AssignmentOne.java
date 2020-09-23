package com.gateway.dashboard.coursera.algorithms_divide_conquer.week1;

import com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.lang.Math.min;

/**
 * In this programming assignment you will implement one or more of the integer multiplication algorithms described in lecture.
 *
 * To get the most out of this assignment, your program should restrict itself to multiplying only pairs of single-digit numbers.
 * You can implement the grade-school algorithm if you want, but to get the most out of the assignment
 *  you'll want to implement recursive integer multiplication and/or Karatsuba's algorithm.
 *
 * So: what's the product of the following two 64-digit numbers?
 *
 * 3141592653589793238462643383279502884197169399375105820974944592
 *
 * 2718281828459045235360287471352662497757247093699959574966967627
 *
 * [TIP: before submitting, first test the correctness of your program on some small test cases of your own devising.
 * Then post your best test cases to the discussion forums to help your fellow students!]
 *
 * [Food for thought: the number of digits in each input number is a power of 2. Does this make your life easier?
 * Does it depend on which algorithm you're implementing?]
 *
 * The numeric answer should be typed in the space below. So if your answer is 1198233847, then just type 1198233847
 *  in the space provided without any space / commas / any other punctuation marks.
 *
 * (We do not require you to submit your code, so feel free to use any programming language you want ---
 *  just type the final numeric answer in the following space.)
 */
public class AssignmentOne {

    public static <T> List<T> math(List<T> list, UnaryOperator<T> ... operators ) {
        return list.stream().filter(Objects::nonNull)
                .map( element -> {
                    T result = element;
                    for(UnaryOperator<T> operator: operators) {
                        result = operator.apply(result);
                    }
                    return result;
                }).filter(Objects::nonNull)
                .filter(element -> element instanceof String && element != "")
                .collect(Collectors.toList());
    }

    public static BigInteger karatsuba(BigInteger num1, BigInteger num2) {
        // cutoff to brute force
        int N = Math.max(num1.bitLength(), num2.bitLength());
        if (N <= 2000){
            return num1.multiply(num2);                // optimize this parameter
        }

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b,   y = c + 2^N d
        BigInteger b = num1.shiftRight(N);
        BigInteger a = num1.subtract(b.shiftLeft(N));
        BigInteger d = num2.shiftRight(N);
        BigInteger c = num2.subtract(d.shiftLeft(N));

        // compute sub-expressions
        BigInteger ac    = karatsuba(a, c);
        BigInteger bd    = karatsuba(b, d);
        BigInteger abcd  = karatsuba(a.add(b), c.add(d));

        return ac.add(
                abcd.subtract(ac).subtract(bd).shiftLeft(N)
        ).add(bd.shiftLeft(2*N));
    }

//    public static <T, R> List<T> math(List<T> list, Function<T, R>... operators ) {
//        return list.stream().map( element -> {
//            T result = element;
//            for(Function<T, R> operator: operators) {
//                result = operator.apply(result);
//            }
//            return result;
//        }).collect(Collectors.toList());
//    }

}
