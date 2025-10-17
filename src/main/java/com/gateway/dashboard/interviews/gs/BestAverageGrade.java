package com.gateway.dashboard.interviews.gs;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/*
 **  Instructions:
 **
 **  Given a list of student test scores, find the best average grade.
 **  Each student may have more than one test score in the list.
 **
 **  Complete the bestAverageGrade function in the editor below.
 **  It has one parameter, scores, which is an array of student test scores.
 **  Each element in the array is a two-element array of the form [student name, test score]
 **  e.g. [ "Bobby", "87" ].
 **  Test scores may be positive or negative integers.
 **
 **  If you end up with an average grade that is not an integer, you should
 **  use a floor function to return the largest integer less than or equal to the average.
 **  Return 0 for an empty input.
 **
 **  Example:
 **
 **  Input:
 **  [["Bobby", "87"],
 **   ["Charles", "100"],
 **   ["Eric", "64"],
 **   ["Charles", "22"]].
 **
 **  Expected output: 87
 **  Explanation: The average scores are 87, 61, and 64 for Bobby, Charles, and Eric,
 **  respectively. 87 is the highest.
 */

class BestAverageGrade {
    /*
     **  Find the best average grade.
     */
    public static int bestAverageGrade(String[][] scores) {
        if(scores == null || scores.length == 0) {
            return 0;
        }

        // Step 1: Aggregate scores for each student.
        // We use a HashMap where the value is now an int array: [sum, count].
        Map<String, int[]> studentScores = new HashMap<>();

        for (String[] scoreData : scores) {
            if (scoreData == null || scoreData.length < 2) {
                continue;
            }
            String name = scoreData[0];
            int score = Integer.parseInt(scoreData[1]);

            // Get the student's current data [sum, count], or create it if it's their first score.
            studentScores.computeIfAbsent(name, k -> new int[2]);

            int[] currentData = studentScores.get(name);
            currentData[0] += score; // Add to sum
            currentData[1]++;      // Increment count
        }

        // If the input array had entries but none were valid, the map could be empty.
        if (studentScores.isEmpty()) {
            return 0;
        }

        // Step 2: Find the maximum average.
        // Initialize with the smallest possible integer value to handle all cases, including all-negative scores.
        int maxAverage = Integer.MIN_VALUE;

        for (int[] data : studentScores.values()) {
            int sum = data[0];
            int count = data[1];

            // Use Math.floorDiv for correct integer-based floor division for both positive and negative numbers.
            int average = Math.floorDiv(sum, count);

            if (average > maxAverage) {
                maxAverage = average;
            }
        }

        return maxAverage;
    }

    /*
     **  Returns true if the tests pass. Otherwise, returns false;
     */
    public static boolean doTestsPass() {
        // TODO: implement more test cases
        String[][] tc1 = {
                {"Bobby", "87"},
                {"Charles", "100"},
                {"Eric", "64"},
                {"Charles", "22"}};

        return bestAverageGrade(tc1) == 87;
    }

    /*
     **  Execution entry point.
     */
    public static void main(String[] args) {
        // Run the tests
        if (doTestsPass()) {
            System.out.println("All tests pass");
        }
        else {
            System.out.println("Tests fail.");
        }
    }
}
