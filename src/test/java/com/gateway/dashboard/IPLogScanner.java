package com.gateway.dashboard;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IPLogScanner {

    // 1) For each entry in the array
    // 2) Take the IP by splitting on ' ' (first whitespace) [0]
    // 3) Have an IP counter in a map and increment
    public static String findTopIPAddress(String[] lines) {
        Map<String, Integer> counter = new HashMap<>();

        for(String line : lines) {
            String ip = line.split(" ")[0];
            counter.merge(ip, 1, Integer::sum);
        }

        String result = counter.entrySet().stream().max(
                (entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : 0
        ).get().getKey();

        return result;
    }

    @Test
    public void testParsing() {
        String lines[] = new String[] {
                "100.0.1.0 Log testing",
                "10.1.1.0 now another line",
                "10.1.1.0 something something"
        };
        assertEquals("10.1.1.0", IPLogScanner.findTopIPAddress(lines));
    }


}
