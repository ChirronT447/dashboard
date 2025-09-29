package com.gateway.dashboard.leetcode;

import java.util.Map;
import java.util.TreeMap;

/**
 * 729. My Calendar I
 * https://leetcode.com/problems/my-calendar-i/description/
 * You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a double booking.
 * A double booking happens when two events have some non-empty intersection (i.e., some moment is common to both events.).
 * The event can be represented as a pair of integers startTime and endTime that represents a booking on the
 *  half-open interval [startTime, endTime), the range of real numbers x such that startTime <= x < endTime.
 * Implement the MyCalendar class:
 * MyCalendar() Initializes the calendar object.
 * boolean book(int startTime, int endTime) Returns true if the event can be added to the calendar successfully without
 *  causing a double booking. Otherwise, return false and do not add the event to the calendar.
 */
class MyCalendar {

    // Key: start time, Value: end time
    private final TreeMap<Integer, Integer> bookings = new TreeMap<>();

    public boolean book(int start, int end) {
        // Find the event that starts just before or at the same time as the new event
        Map.Entry<Integer, Integer> floor = bookings.floorEntry(start);
        if (floor != null && floor.getValue() > start) {
            // Overlap: The previous event ends after the new one starts
            return false;
        }

        // Find the event that starts just after or at the same time as the new event
        Map.Entry<Integer, Integer> ceiling = bookings.ceilingEntry(start);
        if (ceiling != null && ceiling.getKey() < end) {
            // Overlap: The new event ends after the next one starts
            return false;
        }

        // No overlaps found, so book it
        bookings.put(start, end);
        return true;
    }
}