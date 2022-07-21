package com.gateway.dashboard.hired;

import java.util.Arrays;
import java.util.stream.IntStream;

class DebuggingTwo {

//
//            int largestSum = 0;
//            for (int i = 0; i < numbers.length; i++) {
//                int previousNumber = numbers[i];
//                if (previousNumber == -1) {
//                    return -1;
//                }
//
//                int sum = previousNumber;
//                for (int j = i + 1; j < numbers.length; j++) {
//                    int number = numbers[j];
//                    sum += number;
//                    previousNumber = number;
//                }
//
//                largestSum = Math.max(largestSum, sum);
//            }
//
//            return largestSum;

    public static long largestSumOfConsecutiveDecreasingPositiveIntegers(int[] numbers) {
        int[] nums = Arrays.stream(numbers).distinct().sorted().toArray();
        if (nums.length == 0) {
            return 0;
        } else if (Arrays.stream(nums).anyMatch(x -> x < 0)) {
            return -1;
        }

        final int result = IntStream.range(1, nums.length - 1)
                .filter(i -> nums[i + 1] - nums[i] == 1 || nums[i - 1] - nums[1] == 1)
                .map(i -> nums[i])
                .sum();

        if (result != 0) {
            return result;
        } else {
            return Arrays.stream(numbers).max().getAsInt();
        }

    }
}

    // ----------------------------------------------------------------------------------

class Job {
    int start;
    int end;
    int load;

    Job(long[] arr) {
        this.start = (int)arr[0];
        this.end = (int)arr[1];
        this.load = (int)arr[2];
    }
}

    /**
     * Input:
     * jobValues: [[6, 7, 10], [2, 4, 11], [8, 12, 15]]
     * Expected Output:
     * 15
     * Output:
     * null
     *
     * Input:
     * jobValues: [[1, 5, 3], [2, 5, 4], [7, 9, 6]]
     * Expected Output:
     * 7
     * Output:
     * null
     */
//    class Solution {
//        public static long solution(long[][] jobValues) {
//            Job[] jobs = new Job[jobValues.length];
//            for (int i = 0; i < jobValues.length; i++) {
//                long[] jobValue = jobValues[i];
//                jobs[i] = new Job(jobValue);
//            }
//
//            int timelineSize = jobs.length;
//
//            int[] cpuTimeline = new int[timelineSize];
//            Arrays.fill(cpuTimeline, 0);
//            for (Job job: jobs) {
//                for (int i = job.start; i < job.end; i++) {
//                    cpuTimeline[i] = job.load;
//                }
//            }
//
//            return Arrays.stream(cpuTimeline).reduce(0, (currentMax, next) -> Math.max(currentMax, next));
//        }
//    }
//
