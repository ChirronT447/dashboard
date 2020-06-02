package com.gateway.dashboard.exchange;

import java.util.*;

// Tracks the current state of all orders in the system
public class OrderBook {

    SortedMap<Double, Integer> buySide = new TreeMap<>();

    SortedMap<Double, Integer> sellSide = new TreeMap<>();

    public void insertOrder(Order order) {
        Direction direction = order.getDirection();
        if(Direction.BUY.equals(direction)) {
            buySide.put(order.getPrice(), order.getUnits());
        } else if(Direction.SELL.equals(direction)) {
            sellSide.put(order.getPrice(), order.getUnits());
        } else {
            // AHhhhhhhh
        }
    }

    public SortedMap<Double, Integer> getBuySide() {
        return buySide;
    }

    public SortedMap<Double, Integer> getSellSide() {
        return sellSide;
    }

}
