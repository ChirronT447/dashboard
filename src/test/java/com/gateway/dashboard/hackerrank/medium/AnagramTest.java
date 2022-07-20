package com.gateway.dashboard.hackerrank.medium;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnagramTest {

    @Test
    void anagramCheck() {
        Assertions.assertIterableEquals(
                List.of(1, 1, 0, 0),
                Anagram.anagramCheck(
                    List.of("thunder", "host", "", "rain").toArray(new String[0]),
                    List.of("thudner", "shot", "", "train").toArray(new String[0])
                )
        );
    }

}