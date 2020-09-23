package com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils;

import java.math.BigDecimal;

public class Pair<T, T1> {

    private BigDecimal high;
    private BigDecimal low;

    public Pair(BigDecimal high, BigDecimal low) {
        this.high = high;
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }
}
