package com.gateway.dashboard.interviews.anthpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryDatabaseV2 {

    // Core data store for key-field-value data with versioning
    private final Map<String, Map<String, TreeMap<Long, FieldValue>>> store = new HashMap<>();

    public String scan(String key) {
        return performScan(key, field -> true);
    }

    public String scanByPrefix(String key, String prefix) {
        return performScan(key, field -> field.startsWith(prefix));
    }

    private String performScan(String key, Predicate<String> fieldFilter) {
        if (!store.containsKey(key)) {
            return "";
        }
        final Map<String, TreeMap<Long, FieldValue>> fields = store.get(key);
        return fields.keySet().stream()
                .filter(fieldFilter)
                .sorted()
                .map(field -> {
                    FieldValue fv = fields.get(field).lastEntry().getValue();
                    return fv.value() != null ? String.format("%s(%s)", field, fv.value()) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}