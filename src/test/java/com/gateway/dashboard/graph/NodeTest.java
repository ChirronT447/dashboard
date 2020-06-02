package com.gateway.dashboard.graph;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    public void test() {
        boolean result =
        isArmstrongNumber(153) &&
        isArmstrongNumber(407);

        System.out.println(result);
    }

    public boolean isArmstrongNumber(int someInt) {
        String number = String.valueOf(someInt);
        int length = number.length();
        int result = number.chars()
                .map(Character::getNumericValue)
                .map(num -> (int) Math.pow(num, length))
                .sum();

        return result == someInt;
    }

}