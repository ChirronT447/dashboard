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
        Collection<Set<String>> results = Arrays.stream(arr).collect(Collectors.groupingBy(
                word -> { // Key:
                    char[] letters = word.toCharArray();
                    Arrays.sort(letters);
                    return new String(letters);
                }, Collectors.toSet()
            )).values();

        return results;
    }

    // ----------------------------------------------------------------------------------

    public Function<String, Integer> exampleFun(String s) {
        return x -> s.length();
    }
    Function<String, Integer> tst = exampleFun("abc");
    Integer result = tst.andThen(x -> x + 4).apply("abcd");

    // ----------------------------------------------------------------------------------

    public static String cd(String path) {
        final Stack<String> stack = new Stack<>();
        final String[] dir = path.split("/");
        stack.push(dir[0]);
        for(int i = 1; i < dir.length; i++) {
            switch (dir[i]) {
                case "":
                case ".": continue;
                case "..": stack.pop(); break;
                default: stack.push(dir[i]);
            }
        }
        return stack.stream().map(elem -> "/" + elem).collect(Collectors.joining());
    }
}
