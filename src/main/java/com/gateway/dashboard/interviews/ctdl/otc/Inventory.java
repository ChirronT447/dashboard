package com.gateway.dashboard.interviews.ctdl.otc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private final ConcurrentHashMap<String, Integer> store = new ConcurrentHashMap<>();

    // Attempt to grab some required quantity of inventory. Use compute for atomicity
    private int tryAcquire(final String isin, final int quantity) {
        final AtomicInteger acquiredQuantity = new AtomicInteger(0);
        store.compute(isin, (key, available) -> {
            if(available != null) {
                // The amount we can actually give is the smaller of what's available or what we want.
                int returning = Math.min(quantity, available);
                int remaining = available - returning;
                acquiredQuantity.set(returning);
                // Returning null removes the isin from the inventory
                return remaining > 0 ? remaining : null;
            } else {
                // Asset not currently in inventory
                return null;
            }
        });
        return acquiredQuantity.get();
    }

}
