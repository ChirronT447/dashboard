package com.gateway.dashboard.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class StringCalculatorTest {

    @Test
    public void testReverseString() {
        String res = StringCalculator.reverseString("Test");
        Assertions.assertEquals(res, "tseT");
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testFindFirst() {
        Character res = StringCalculator.findFirst("Striing");
        Assertions.assertEquals('i', res);
    }

    // ----------------------------------------------------------------------------------

    @Test
    public void testGroupAnagrams() {
        Collection<Set<String>>  res = StringCalculator.groupAnagrams(
                new String[] {"dog", "god", "art", "tar", "rub"}
        );
        Assertions.assertEquals(res.size(), 3);
    }

    // ----------------------------------------------------------------------------------

}