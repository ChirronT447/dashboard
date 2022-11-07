package com.gateway.dashboard.leetcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 17. Letter Combinations of a Phone Number - Medium
 * <a href="https://leetcode.com/problems/letter-combinations-of-a-phone-number/">...</a>
 * <p>
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations
 *  that the number could represent. Return the answer in any order.
 * A mapping of digits to letters (just like on the telephone buttons) is given below.
 * Note that 1 does not map to any letters.
 */
public class LetterCombination {

    // Constraints:
    // - 0 <= digits.length <= 4
    // - digits[i] is a digit in the range ['2', '9'].
    private final static Map<Character, List<Character>> MAPPING = Map.of(
            '2', List.of('a', 'b', 'c'),
            '3', List.of('d', 'e', 'f'),
            '4', List.of('g', 'h', 'i'),
            '5', List.of('j', 'k', 'l'),
            '6', List.of('m', 'n', 'o'),
            '7', List.of('p', 'q', 'r', 's'),
            '8', List.of('t', 'u', 'v'),
            '9', List.of('w', 'x', 'y', 'z')
    );

    /**
     * For each digit added, remove and copy every element in the queue and add the possible letter to
     *  each element, then add the updated elements back into queue again.
     *  Repeat this procedure until all the digits are iterated.
     * @param digits
     * @return
     */
    public static List<String> letterCombinations(String digits) {
        if(digits.isEmpty()) return List.of();
        final LinkedList<String> result = new LinkedList<>(List.of(""));
        while(digits.length() != result.peek().length()){
            final String priorLetters = result.remove();
            final List<Character> phoneLetters = MAPPING.get(digits.charAt(priorLetters.length()));
            phoneLetters.forEach(nextLetter -> result.addLast(priorLetters + nextLetter));
        }
        return result;
    }

}
