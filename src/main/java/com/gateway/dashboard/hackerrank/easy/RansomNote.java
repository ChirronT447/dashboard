package com.gateway.dashboard.hackerrank.easy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given the words in the magazine and the words in the ransom note,
 *  print Yes if he can replicate his ransom note
 *  exactly using whole words from the magazine; otherwise, print No.
 *
 * For example, the note is "Attack at dawn". The magazine contains only "attack at dawn".
 *  The magazine has all the right words, but there's a case mismatch. The answer is no.
 */
public class RansomNote {

    // Complete the checkMagazine function below.

    /**
     * Complete the checkMagazine function in the editor below.
     * It must print yes if the note can be formed using the magazine, or no.
     *
     * checkMagazine has the following parameters:
     * @param magazine an array of strings, each a word in the magazine
     * @param note an array of strings, each a word in the ransom note
     */
    static boolean checkMagazine(String[] magazine, String[] note) {
        // To avoid timeout but 41ms vs 35ms when running locally (quicker when disabled) but that's with smaller tests
        Arrays.sort(magazine);
        Arrays.sort(note);
        List<String> magazineWords = new ArrayList<>(Arrays.asList(magazine));
        //Arrays.asList(magazine); is immutable
        for(String noteWord: note) {
            if(!magazineWords.contains(noteWord)) {
                return false;
            }
            magazineWords.remove(noteWord);
        }
        return true;
    }

}
