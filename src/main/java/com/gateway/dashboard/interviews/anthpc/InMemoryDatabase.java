package com.gateway.dashboard.interviews.anthpc;

import java.util.*;

// Time-to-live in milliseconds. -1 for infinite.
record FieldValue(String value, long ttl){}

public final class InMemoryDatabase {

    // Core data store for key-field-value data with versioning
    private final Map<String, Map<String, TreeMap<Long, FieldValue>>> store = new HashMap<>();

    public String set(final String key, final String field, final String value) {
        store.computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(field, f -> new TreeMap<>())
                .put(0L, new FieldValue(value, -1));
        return "";
    }

    public String get(final String key, final String field) {
        if (!store.containsKey(key) || !store.get(key).containsKey(field)) {
            return "";
        }
        final TreeMap<Long, FieldValue> history = store.get(key).get(field);
        if (history.isEmpty()) {
            return "";
        }
        // Get the latest value (highest timestamp)
        final FieldValue fv = history.lastEntry().getValue();
        return fv.value() != null ? fv.value() : "";
    }

    public String delete(final String key, final String field) {
        if (!store.containsKey(key) ||
            !store.get(key).containsKey(field) ||
            store.get(key).get(field).isEmpty()
        ) { return "false"; }
        final FieldValue latestValue = store.get(key).get(field).lastEntry().getValue();
        if (latestValue.value() == null) { // Already "deleted"
            return "false";
        } // Add tombstone:
        store.get(key).get(field).put(0L, new FieldValue(null, -1));
        return "true";
    }
}