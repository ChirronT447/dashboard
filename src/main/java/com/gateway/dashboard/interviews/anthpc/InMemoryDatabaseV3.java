package com.gateway.dashboard.interviews.anthpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryDatabaseV3 {

    // Core data store for key-field-value data with versioning
    private final Map<String, Map<String, TreeMap<Long, FieldValue>>> store = new HashMap<>();

    private FieldValue getValueAt(String key, String field, long timestamp) {
        if (!store.containsKey(key) ||!store.get(key).containsKey(field)) {
            return null;
        }
        TreeMap<Long, FieldValue> history = store.get(key).get(field);
        Map.Entry<Long, FieldValue> entry = history.floorEntry(timestamp);

        if (entry == null) return null;

        FieldValue fv = entry.getValue();
        long entryTimestamp = entry.getKey();

        if (fv.ttl()!= -1 && (entryTimestamp + fv.ttl()) <= timestamp) return null;
        if (fv.value() == null) return null;

        return fv;
    }

    public String setAt(String key, String field, String value, long timestamp) {
        store.computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(field, f -> new TreeMap<>())
                .put(timestamp, new FieldValue(value, -1));
        return "";
    }

    public String setAtWithTtl(String key, String field, String value, long timestamp, long ttl) {
        store.computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(field, f -> new TreeMap<>())
                .put(timestamp, new FieldValue(value, ttl));
        return "";
    }

    public String getAt(String key, String field, long timestamp) {
        FieldValue fv = getValueAt(key, field, timestamp);
        return (fv!= null)? fv.value() : "";
    }

    public String deleteAt(String key, String field, long timestamp) {
        FieldValue currentFv = getValueAt(key, field, timestamp);
        if (currentFv == null) {
            return "false";
        }
        store.get(key).get(field).put(timestamp, new FieldValue(null, -1));
        return "true";
    }

    public String scanAt(String key, long timestamp) {
        return performScan(key, field -> true, timestamp, false);
    }

    public String scanByPrefixAt(String key, String prefix, long timestamp) {
        return performScan(key, field -> field.startsWith(prefix), timestamp, false);
    }

    // --- Private Helper for All Scan Operations ---

    /**
     * A single, modular method to handle all scanning logic.
     * @param key The key to scan.
     * @param fieldFilter A predicate to filter field names (e.g., by prefix).
     * @param timestamp The timestamp for time-aware scans.
     * @param useLatest If true, ignores timestamp and gets the latest value (for L2).
     * @return A formatted string of matching field-value pairs.
     */
    private String performScan(String key, Predicate<String> fieldFilter, long timestamp, boolean useLatest) {
        if (!store.containsKey(key)) {
            return "";
        }
        Map<String, TreeMap<Long, FieldValue>> fields = store.get(key);
        return fields.keySet().stream()
                .filter(fieldFilter)
                .sorted()
                .map(field -> {
                    FieldValue fv;
                    if (useLatest) {
                        TreeMap<Long, FieldValue> history = fields.get(field);
                        fv = (history == null || history.isEmpty())? null : history.lastEntry().getValue();
                        // Check for tombstone on latest value
                        if (fv!= null && fv.value() == null) fv = null;
                    } else {
                        fv = getValueAt(key, field, timestamp);
                    }
                    return fv!= null? String.format("%s(%s)", field, fv.value()) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

}
