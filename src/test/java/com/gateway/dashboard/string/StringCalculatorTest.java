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

    @Test
    public void testFindTopIPAddress() {
        String[] logFile = new String[]{
                "10.185.248.71 - [09/Jan/2024:19:12:06 +0000] 808840 GET pathHere",
                "10.185.552.51 - [09/Jan/2024:19:12:07 +0000] 808840 GET pathHere",
                "10.111.422.51 - [09/Jan/2024:19:12:07 +0000] 808840 GET pathHere",
                "10.412.222.51 - [09/Jan/2024:19:12:07 +0000] 808840 GET pathHere",
                "10.185.552.51 - [09/Jan/2024:19:12:07 +0000] 808840 GET pathHere",
                "10.185.248.71 - [09/Jan/2024:19:12:06 +0000] 808840 GET pathHere",
                "10.185.552.51 - [09/Jan/2024:19:12:07 +0000] 808840 GET pathHere"
        };

        String topAddress = StringCalculator.findTopIPAddress(logFile);
        Assertions.assertEquals("10.185.552.51", topAddress);
    }

}