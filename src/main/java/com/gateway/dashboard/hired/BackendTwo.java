package com.gateway.dashboard.hired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BackendTwo {

    // Given an array of integers, return an array containing the integer that occurs the least number of times.
    // If there are multiple answers, return all possibilities within the resulting array sorted in ascending order.
    // When no solution can be deduced, return an empty array.
    // Example Input: [10, 941, 13, 13, 13, 941]
    // Example Output: [10]
    public static long[] solution(long[] numbers) {
        if(numbers.length == 0) {
            return numbers;
        }

        final Map<Long, Long> countMap = Arrays.stream(numbers).boxed().collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting())
        );

        long smallest = countMap.get(
                Collections.min(countMap.entrySet(), (entry1, entry2) -> (int) (entry1.getValue() - entry2.getValue())).getKey()
        );
        Set<Long> result = new HashSet<>();
        for(Long key : countMap.keySet()) {
            if(countMap.get(key) == smallest) {
                result.add(key);
            }
        }
        return Arrays.stream(numbers).filter(result::contains).sorted().toArray();
    }

    // A substitution cipher is a simple way to obfuscate a string by replacing the letters given a mapping. For example:
    //  cipher:   'mpgzkeyrsxfwlvjbcnuidhoqat'
    //  alphabet: 'abcdefghijklmnopqrstuvwxyz'
    // Each letter in the cipher replaces the corresponding letter in the alphabet (i.e. m replaces occurrences of a, p
    //  replaces occurrences of b, etc.). So a word like abs becomes mpu.
    //
    // Example Input: plain_text: 'helloworld' cipher_alphabet: 'mpgzkeyrsxfwlvjbcnuidhoqat'
    // Example Output: rkwwjojnwz
    // Explanation: In the cipher_alphabet 'helloworld' is encoded to 'rkwwjojnwz`
    //
    public final static String EMPTY = "";
    public final static char[] ALPHABET = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static String solution(String word, String cipher) {

        if(!isAlpha(word)) // contains number, space, special char
            return EMPTY;
        if(cipher.length() != cipher.chars().distinct().count())
            return EMPTY;
        if(cipher.length() != ALPHABET.length)
            return EMPTY;

        final Map<Character, Character> convertMap = buildConvertMap(cipher);
        return word.chars()
                .mapToObj(a -> (char) a)
                .map(convertMap::get)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public static Map<Character, Character> buildConvertMap(String cipher) {
        char[] ciphr = cipher.toCharArray();
        return IntStream.range(0, ALPHABET.length).boxed()
                .collect(Collectors.toMap(i -> ALPHABET[i], i -> ciphr[i]));
        }

    public static boolean isAlpha(String word) {
        return word.chars().allMatch(Character::isLetter);
    }

}