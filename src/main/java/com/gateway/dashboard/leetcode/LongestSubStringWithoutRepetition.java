package com.gateway.dashboard.leetcode;

import java.util.HashMap;

/**
 * 3. Longest Substring Without Repeating Characters - Medium
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
 * Given a string s, find the length of the longest substring without repeating characters.
 */
public class LongestSubStringWithoutRepetition {

    // keep a hashmap which stores the characters in string as keys and their positions as values,
    // and keep two pointers which define the max substring. move the right pointer to scan through
    // the string , and meanwhile update the hashmap. If the character is already in the hashmap,
    // then move the left pointer to the right of the same character last found.
    // Note that the two pointers can only move forward.
    public static int lengthOfLongestSubstring(String s) {
        if (s.length()==0) return 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int max=0;
        for (int i=0, j=0; i<s.length(); ++i){
            if (map.containsKey(s.charAt(i))){
                j = Math.max(j,map.get(s.charAt(i))+1);
            }
            map.put(s.charAt(i),i);
            max = Math.max(max,i-j+1);
        }
        return max;
    }

}
