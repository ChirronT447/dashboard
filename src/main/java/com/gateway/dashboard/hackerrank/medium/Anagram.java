package com.gateway.dashboard.hackerrank.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Two strings are anagrams of each other if the letters of one string can be rearranged to form the other string.
 * Given a string, find the number of pairs of substrings of the string that are anagrams of each othe
 */
public class Anagram {

    /**
     * It must return an integer that represents the number of anagrammatic pairs of substrings in s.
     * @param s
     * @return
     */
    static int sherlockAndAnagrams(String s) {
        int result = 0;
        return result;
    }

    public static List<Integer> anagramCheck(String[] arr1, String[] arr2) {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < arr1.length; i++) {
            if(!checkAnagram(arr1[i], arr2[i])) {
                result.add(0);
            } else {
                result.add(1);
            }
        }
        return result;
    }

    public static boolean checkAnagram(final String firstWord, final String secondWord) {
        if(firstWord != null && secondWord != null &&
            firstWord.length() == secondWord.length() &&
            firstWord.length() != 0
        ) {
            final Set<Integer> first = firstWord.chars().boxed().collect(Collectors.toSet());
            return secondWord.chars().allMatch(first::contains);
        } else {
            return false;
        }
    }

}
