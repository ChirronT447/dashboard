package com.gateway.dashboard.datastructures.graph;

import java.util.HashMap;
import java.util.Map;

public class Heuristic<T> {

    private final Map<T, Double> heuristic;

    public Heuristic() {
        this.heuristic = new HashMap<>();
    }

    public <T> double get(T destination) {
        return heuristic.get(destination);
    }

    public Heuristic<T> put(T a, double v) {
        heuristic.put(a, v);
        return this;
    }
}
