package com.gateway.dashboard.coursera.algorithms_divide_conquer.week1.utils;

public class Pair<T, T1> {

    private T low;
    private T1 high;

    public Pair(T low, T1 high) {
        this.low = low;
        this.high = high;
    }

    public T1 getHigh() {
        return high;
    }

    public void setHigh(T1 high) {
        this.high = high;
    }

    public T getLow() {
        return low;
    }

    public void setLow(T low) {
        this.low = low;
    }
}
