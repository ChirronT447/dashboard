package com.gateway.dashboard.interviews.blk;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A basic message delivery service is to be implemented that has a rate-limiting algorithm that drops any message
 * that has already been delivered in the last k seconds.
 *
 * Given the integer k, a list of messages as an array of n strings, messages, and a sorted integer array timestamps
 * representing the time at which the message arrived, for each message report the string "true" if the message is
 * delivered and "false" otherwise.
 *
 * Example :
 * Suppose n = 6, timestamps = [1, 4, 5, 10, 11, 14], messages = ["hello", "bye", "bye", "hello", "bye", "hello"], and k = 5.
 * The answer is ["true", "true", "false", "true", "true", "false"].
 *
 * Function Description
 * Complete the function getMessageStatus
 * getMessageStatus takes the following arguments:
 *     int timestamp[n]: the time at which messages arrive
 *     string messages[n]: the messages to be delivered
 *     int k: the minimum gap between same messages
 *
 * Returns
 *     string[n]: the status of the messages

 * Constraints
 * 1 ≤ n ≤ 105
 * 1 ≤ |messages[i]| ≤ 15
 * 1 ≤ timestamps[i] ≤ 106
 * It is guaranteed that all the messages consist of lowercase English letters only.
*/
 public class RateLimiting {

    /*
     * Complete the 'getMessageStatus' function below.
     * The function is expected to return a STRING_ARRAY.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY timestamps
     *  2. STRING_ARRAY messages
     *  3. INTEGER k
     */
    public static List<String> getMessageStatus(List<Integer> timestamps, List<String> messages, int k) {
        final List<String> result = new ArrayList<>();
        final Map<String, Integer> messageToLastTimeSeen = new HashMap<>();
        for(int i = 0; i < messages.size(); i++) {
            final String message = messages.get(i);
            final Integer timestamp = timestamps.get(i);
            if(!messageToLastTimeSeen.containsKey(message) || timestamp - messageToLastTimeSeen.get(message) > k) {
                result.add("true");
                messageToLastTimeSeen.put(message, timestamp);
            } else {
                result.add("false");
            }
        }
        return result;
    }
    
}
