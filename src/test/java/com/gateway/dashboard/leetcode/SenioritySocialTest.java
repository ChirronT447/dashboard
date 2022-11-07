package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SenioritySocialTest {

    @Test
    void maxSeniorityLevel() {
        SenioritySocial.Employee e = new SenioritySocial.Employee(0, List.of(), 0);
        SenioritySocial.Employee e2 = new SenioritySocial.Employee(1, List.of(e), 9);
        SenioritySocial.Employee e3 = new SenioritySocial.Employee(2, List.of(), 5);
        SenioritySocial.Employee e4 = new SenioritySocial.Employee(3, List.of(), 5);
        SenioritySocial.Employee e5 = new SenioritySocial.Employee(4, List.of(e3, e4), 4);
        SenioritySocial.Employee e6 = new SenioritySocial.Employee(5, List.of(), 2);
        SenioritySocial.Employee e7 = new SenioritySocial.Employee(6, List.of(e6), 5);
        SenioritySocial.Employee e8 = new SenioritySocial.Employee(7, List.of(e5), 7);
        SenioritySocial.Employee e9 = new SenioritySocial.Employee(8, List.of(e7, e8), 6);
        SenioritySocial.Employee e10 = new SenioritySocial.Employee(10, List.of(e9), 4);
        SenioritySocial.Employee e11 = new SenioritySocial.Employee(11, List.of(e2, e10), 12);

        SenioritySocial.maxSeniorityLevel(e11);
    }
}