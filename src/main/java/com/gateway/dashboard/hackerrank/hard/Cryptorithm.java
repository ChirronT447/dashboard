package com.gateway.dashboard.hackerrank.hard;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A cryptarithm is a puzzle where the goal is to find the correspondence between letters and digits
 *  such that a given arithmetic equation consisting of letters holds true.
 *
 * Task: Given a cryptarithm as an array of strings, count the number of valid solutions.
 *
 * A solution is valid if each letter represents a different digit & the leading digit (of a multi digit #) is not zero.
 *
 * Input: [word1, word2, word3] -> which stands for the word1 + word2 = word3 solution.
 *
 * Example: ["SEND", "MORE", "MONEY"], output = 1, because there is only one solution to this:
 *  M = 1, O = 0, N = 6, E = 5, Y = 2, D = 7, R = 8, and S = 9 (9567 + 1085 = 10652).
 *
 * Example: ["GREEN", "BLUE", "BLACK"], output = 12, because there are 12 possible valid solutions:
 *  54889 + 6138 = 61027
 *  18559 + 2075 = 20634
 *  72449 + 8064 = 80513
 *  48229 + 5372 = 53601
 *  47119 + 5261 = 52380
 *  36887 + 4028 = 40915
 *  83447 + 9204 = 92651
 *  74665 + 8236 = 82901
 *  65884 + 7308 = 73192
 *  57883 + 6408 = 64291
 *  57881 + 6428 = 64309
 *  83441 + 9264 = 92705
 *
 * Example: ["ONE", "TWO", "THREE"], output = 0, because there are no valid solutions.
 *
 * [execution time limit] is 3 seconds; there are only 10! possible assignments of digits to letters, and these can be
 *  checked against the puzzle in linear time. The input array will be 3 non-empty strings containing uppercase English letters.
 * 1 ≤ crypt[i].length ≤ 35.
 *
 * 1) Brute force -> permutations
 * 2) backtracking
 *
 * Use of mod-10 arithmetic allows columns to be treated as simultaneous equations (assuming addition)
 * Use of mod-2 arithmetic allows inference based on parity of the numbers ( even vs odd ).
 *
 */
public class Cryptorithm {

    public static int solve(String[] input) {
        return 0;
    }

    // [
    //  m: (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    //  o: (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    //  v: (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    //  e: (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    // ]
    public static class PreComputeCryptorithm {

//        final Letter[] word1;
//        final Letter[] word2;
//        final Letter[] solution;
        final Map<Character, Letter> word1;
        final Map<Character, Letter> word2;
        final Map<Character, Letter> solution;
        final Map<Character, Letter> allLetters = new HashMap<>();

        public PreComputeCryptorithm(String word1, String word2, String solution) {
            this.word1 = Objects.requireNonNull(buildConstruct(word1));
            this.word2 = Objects.requireNonNull(buildConstruct(word2));
            this.solution = Objects.requireNonNull(buildConstruct(solution));
        }

        public Map<Character, Letter> buildConstruct(String word) {
            return word.chars()
                    .mapToObj(character -> (char) character)
                    .peek(character -> this.allLetters.putIfAbsent(character, new Letter(character)))
                    .collect(Collectors.toMap(
                            character -> character,
                            this.allLetters::get,
                            (u, v) -> u,
                            LinkedHashMap::new
                    ));
        }

//        public static Letter[] buildConstruct(String word) {
//            return word.chars()
//                    .mapToObj(letter -> new Letter((char) letter))
//                    .toArray(Letter[]::new);
//        }

//        public void reduce() {
//            for(int i = ) {
//
//            }
//        }
//
//        public boolean checkValid() {
//            return
//        }

        public static int sum(Letter[] word) {
            return Integer.parseInt(
                    Arrays.stream(word)
                            .map(Letter::currentAsStr)
                            .collect(Collectors.joining(""))
            );
        }

        public Letter increment(Letter letter) {
            letter.next();
            return letter;
        }

        public void sumWords() {
//            return sum(word1) + sum(word2);
//            int sum = 0;
//            for(int i = word1.length - 1; i >= 0; i--) {
//                sum += (word1[i].currentNumber + word2[i].currentNumber);
//                if( i - 1 >= 0) {
//                    sum *= 10;
//                }
//            }
//            return sum;
        }

    }

    public static class Letter {

        // Each letter can have a range of possible values; we'll move through these with an iterator
        private final Set<Integer> range = new LinkedHashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        private final Iterator<Integer> iterator = range.iterator();
        private final Character letter;
        public int currentNumber;

        public Letter(final Character letter) {
            this.letter = letter;
        }

        public Integer next() {
            this.currentNumber = iterator.next();
            return currentNumber;
        }

        public void reduceToZero() {
            this.range.removeIf(num -> num > 0);
        }

        public void removeZero() {
            this.range.remove(0);
        }

        public int current() {
            return currentNumber;
        }

        public String currentAsStr() {
            return String.valueOf(currentNumber);
        }

    }

    // -------------------------------------------------

    final String word1;
    final String word2;
    final String solution;

    public Cryptorithm(String... words) {
        this.word1 = words[0];
        this.word2 = words[1];
        this.solution = words[2];
    }

    public int solve() {
        return IntStream.generate(() -> 9)
                .limit(word1.length())
                .reduce(0, (subtotal, nine) -> subtotal = subtotal * 10 + nine);
    }

}
