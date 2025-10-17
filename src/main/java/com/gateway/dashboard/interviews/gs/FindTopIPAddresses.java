package com.gateway.dashboard.interviews.gs;

import java.util.*;

public class FindTopIPAddresses {


    /**
     * Given an Apache log file, return the IP address(es) that accessed the site most often.
     * If there is a tie for the most frequent IP, return all tied IPs in a comma-separated string.
     *
     * @param lines An array of log file entries.
     * @return A comma-separated string of the most frequent IP(s), or null if the input is empty.
     */
    public static String findTopIpAddresses(String[] lines) {
        // Edge case: Handle null or empty input.
        if (lines == null || lines.length == 0) {
            return null;
        }

        // Step 1: Count the frequency of each IP address (same as before).
        Map<String, Integer> ipCounts = new HashMap<>();
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String ip = line.split(" ")[0];
            ipCounts.put(ip, ipCounts.getOrDefault(ip, 0) + 1);
        }

        // Handle case where input contained only empty lines.
        if (ipCounts.isEmpty()) {
            return null;
        }

        // Step 2: First pass - find the maximum frequency.
        int maxCount = Collections.max(ipCounts.values());

        // Step 3: Second pass - collect all IPs that have the max frequency.
        List<String> topIps = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ipCounts.entrySet()) {
            if (entry.getValue() == maxCount) {
                topIps.add(entry.getKey());
            }
        }

        // Step 4: Format the output as a comma-separated string.
        return String.join(",", topIps);
    }

}
