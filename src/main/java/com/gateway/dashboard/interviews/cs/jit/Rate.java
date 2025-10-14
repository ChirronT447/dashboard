package com.gateway.dashboard.interviews.cs.jit;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * A simple value object to represent a rate.
 * e.g., 100 permits per MINUTE.
 */
public record Rate(long permits, ChronoUnit perUnit) {

    public static Rate of(long permits, ChronoUnit perUnit) {
        if (permits <= 0 || perUnit == null) {
            throw new IllegalArgumentException("Permits must be positive and unit must not be null.");
        }
        return new Rate(permits, perUnit);
    }

    public long getPeriodNanos() {
        return perUnit.getDuration().toNanos();
    }
}

