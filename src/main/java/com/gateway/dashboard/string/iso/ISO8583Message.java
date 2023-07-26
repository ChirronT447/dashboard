package com.gateway.dashboard.string.iso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ISO8583Message(String messageType, Map<String, String> fields) {

    public static ISO8583Message from(final String message) {
        final List<String> fieldsChunks = splitFields(message.substring(4));
        final Map<String, String> fieldsMap = parseFields(fieldsChunks);
        return new ISO8583Message(message.substring(0, 4), fieldsMap);
    }

    private static List<String> splitFields(final String fieldsData) {
        return Stream.iterate(0, i -> i + 1)
                .limit(fieldsData.length() / 8)
                .map(i -> fieldsData.substring(i * 8, (i + 1) * 8))
                .collect(Collectors.toList());
    }

    private static Map<String, String> parseFields(final List<String> fieldsChunks) {
        return fieldsChunks.stream()
                .map(chunk -> Map.entry(chunk.substring(0, 2), chunk.substring(2)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public String getField(String fieldName) {
        return fields.get(fieldName);
    }

}