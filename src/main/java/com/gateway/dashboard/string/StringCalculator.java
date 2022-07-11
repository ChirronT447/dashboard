package com.gateway.dashboard.string;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    // ----------------------------------------------------------------------------------

    // Leetcode 3) https://leetcode.com/problems/longest-substring-without-repeating-characters/
    // Given a string s, find the length of the longest substring without repeating characters.
    public static int lengthOfLongestSubstring(String s) {
        int lengthOfLongestSubstring = 0;
        int substring = 0;
        Set<String> letterSet = new HashSet<>();
        for(String letter : s.split("")) {
            if (!letterSet.contains(letter)) {
                System.out.print(letter + ", ");
                letterSet.add(letter);
                substring++;
                if (substring > lengthOfLongestSubstring) {
                    lengthOfLongestSubstring = substring;
                }
            } else {
                System.out.println("");
                letterSet.clear();
                substring = 0;
            }
        }
        return lengthOfLongestSubstring;
    }

    // ----------------------------------------------------------------------------------

    /**
     * Input : 313551
     * -> 113355
     * Output : 531135
     * Explanations : 531135 is the largest number which is a palindrome
     * 135531, 315513 and other numbers can also be formed, but we need the largest of all palindromes.
     * @param number
     * @return string
     */
    public static String findLargestPalindrome(String number) {

        // Ignore null / blank strings:
        if (number == null || number.length() == 0) {
            System.out.println("findLargestPalindrome provided: " + number);
            return "";
        }

        // Split the number up into a list of Characters: 313551 -> ['1','1','3','3','5','5']
        // Count the number of each character: ['1','1','3','3','5','5'] -> {'1':2, '3':2, '5':2}
        final Map<Character, Integer> characterCount = number.chars().sorted()
                .mapToObj(integer -> (char) integer)
                .collect(Collectors.groupingBy(character -> character, Collectors.summingInt(num -> 1)));

        // Take the set of Characters, sort into a list: {'3','1','5'} -> ['1','3','5']
        final List<Character> numbers = new ArrayList<>(characterCount.keySet().stream().sorted().toList());

        // Working with characters for readability:
        final Deque<Character> result = new LinkedList<>();

        // There can be at most 1 unpaired number which must be the lowest value, as this will sit at the centre.
        // Otherwise, no palindrome is possible. So take the first number and add as many as we have to result:
        final Character firstNumber = numbers.get(0);
        IntStream.range(0, characterCount.get(numbers.get(0))).forEach(i -> result.add(firstNumber));
        characterCount.remove(firstNumber);
        numbers.remove(firstNumber);

        // For each number, in order of smallest -> biggest:
        for (Character num : numbers) {
            // Working with pairs now, adding to front and back:
            int numberOfCharToAdd = characterCount.get(num) / 2;
            System.out.println("We have " + numberOfCharToAdd * 2 + " '" + num + "' to add.");
            if(numberOfCharToAdd != 0) {
                // Keeping note of where we only have 1 character by only removing when we have +2:
                characterCount.remove(num);
            }
            while (numberOfCharToAdd-- != 0) {
                result.push(num);
                result.add(num);
            }
        }

        // We should have removed everything from the count but don't forget about the first character added:
        if (characterCount.isEmpty() && result.size() != 1) {
            System.out.println("Result: " + result);
            return result.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        } else {
            return "";
        }
    }

    // ----------------------------------------------------------------------------------

    /**
     * Given two strings, say whether one is a rotation of the other eg.
     * Given "ABCD" and "CDAB", return true
     * Given "ABCD", and "ACBD" , return false
     * Explanation: Check lengths are the same and then add the first string to itself and check for the second within.
     * @param str1
     * @param str2
     * @return
     */
    public static boolean areRotations(String str1, String str2) {
        return str1.length() == str2.length() && (str1 + str1).contains(str2);
    }

    // ----------------------------------------------------------------------------------

    /**
     * Check if input is an integer or a string
     * @return
     */
    public static boolean isNumber(String maybeNumber) {
        return maybeNumber.chars().allMatch(Character::isDigit);
    }

    // ----------------------------------------------------------------------------------

    public static boolean allCharactersSame(String str) {
        return str != null && str.chars().distinct().count() == 1;
    }

}
