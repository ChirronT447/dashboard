package com.gateway.dashboard.interviews.cs;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BusFactor {

    final Set<String> vowels = Set.of("a", "e", "i", "o", "u");
    public String removeVowels(String word) {
//        return Arrays.stream(word.split(""))
//                .filter(character -> !vowels.contains(character))
//                .collect(Collectors.joining());

        return word.replaceAll("[aeiou]", "");
    }

}

