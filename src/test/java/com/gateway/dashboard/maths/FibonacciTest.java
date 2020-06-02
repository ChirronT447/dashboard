package com.gateway.dashboard.maths;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciTest {

    @Test
    public void testFibSeq() {
        int[] intArray = {1,3,7,11,12,14,18};
        int result = Fibonacci.lenLongestFibSubseq(intArray);
        Assert.isTrue(result==3, "Result should be 3 but is: " + result);
    }

    @Test
    public void testStream() {
        List<Integer> lst = new ArrayList<>();
        lst.add(4);
        lst.add(5);
        lst.add(9);
        //lst.stream().skip(lst.size())
    }


}