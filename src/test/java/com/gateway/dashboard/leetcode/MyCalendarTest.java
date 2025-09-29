package com.gateway.dashboard.leetcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCalendarTest {

    @Test
    void book() {
        MyCalendar myCalendar = new MyCalendar();
        assertTrue(myCalendar.book(10, 20));

        // It cannot be booked because time 15 is already booked by another event.
        assertFalse(myCalendar.book(15, 25));

        // The event can be booked, as the first event takes every time less than 20, but not including 20.
        assertTrue(myCalendar.book(20, 30));
    }
}