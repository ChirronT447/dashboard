package com.gateway.dashboard.string;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringCalculator {

    /**
     * Reverse string using character index
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        return reverseString(str, str.length() - 1);
    }

    private static String reverseString(String str, int index) {
        if(index == 0) {
            return str.charAt(0) + "";
        }
        char letter = str.charAt(index);
        return letter + reverseString(str, index - 1);
    }

    // ----------------------------------------------------------------------------------

    // Find first non repeating calendar
    public static char findFirst(String input) {
        // Build a map with a count:
        Map<Character, Integer> counts = new LinkedHashMap<>(input.length());
        for(char c : input.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        // Return the max value seen:
        return Collections.max(
                counts.entrySet(),
                Comparator.comparingInt(Map.Entry::getValue)
        ).getKey();
    }

    // ----------------------------------------------------------------------------------

    public static Collection<Set<String>> groupAnagrams(String[] arr) {
        return Arrays.stream(arr).collect(Collectors.groupingBy(
                word -> { // Key:
                    char[] letters = word.toCharArray();
                    Arrays.sort(letters);
                    return new String(letters);
                }, Collectors.toSet()
            )).values();
    }

    // ----------------------------------------------------------------------------------


}
