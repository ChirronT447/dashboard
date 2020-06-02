package com.gateway.dashboard.maths;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {

    public static int lenLongestFibSubseq(int[] sequence) {
        Map<Integer, Integer> index = new HashMap();
        int sequenceLength = sequence.length;
        for (int i = 0; i < sequenceLength; ++i) {
            index.put(sequence[i], i);
        }

        int result = 0;
        Map<Integer, Integer> longest = new HashMap();
        for (int k = 0; k < sequenceLength; ++k) {
            for (int j = 0; j < k; ++j) {
                int i = index.getOrDefault(sequence[k] - sequence[j], -1);
                if (i >= 0 && i < j) {
                    // Encoding tuple (i, j) as integer (i * sequenceLength + j)
                    int cand = longest.getOrDefault(i * sequenceLength + j, 2) + 1;
                    longest.put(j * sequenceLength + k, cand);
                    result = Math.max(result, cand);
                }
            }
        }

        return result >= 3 ? result : 0;
    }

    public int lenLongestFibSubseq_2(int[] sequence) {
        int result = 0, sequenceLength = sequence.length;
        int [][] lookup = new int [sequenceLength][sequenceLength];

        for (int index = 2; index < sequenceLength; index ++) {
            int a = sequence[index], s = 0, e = index - 1;
            while (s < e) {
                int v = sequence[s] + sequence[e];
                if (v > a) e--;
                else if (v < a) s++;
                else {
                    result = Math.max(
                            result,
                            lookup[e][index] = (lookup[s][e] == 0 ? 2 : lookup[s][e]) + 1
                    );
                    s ++;
                }
            }
        }
        return result;
    }

}
