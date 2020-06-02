package com.gateway.dashboard.hackerrank.easy;

public class RepeatedString {

    /**
     * Lilah has a string, s, of lowercase English letters that she repeated infinitely many times.
     * Given an integer, n, find and print the number of letter a's in the first n letters of Lilah's
     *  infinite string.
     * For example, if the string  and , the substring we consider is ,
     * the first  characters of her infinite string.
     * There are  occurrences of a in the substring.
     *
     * Complete the repeatedString function. It should return an integer representing the number of
     *  occurrences of a in the prefix of length  in the infinitely repeating string.
     *
     * repeatedString has the following parameter(s):
     * s: a string to repeat
     * n: the number of characters to consider
     */
    // Complete the repeatedString function below.
    static long repeatedString(String str, long numberOfCharacters) {
        long countOfLetterA = str.chars().filter(ch -> ch == 'a').count();

        int length = str.length();
        long quotient = numberOfCharacters / length;
        long remainder = numberOfCharacters % length;

        // eg. aba, 10 => a
        String substring = str.substring(0, Math.toIntExact(remainder));
        // eg. 7 = 2 * (10 / 3) + 1
        long result = (countOfLetterA * quotient)
                + substring.chars().filter(ch -> ch == 'a').count();

        return result;
    }

}
