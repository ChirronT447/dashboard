package com.gateway.dashboard.hackerrank.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RansomNoteTest {

    @Test
    void checkMagazine() {
        assertFalse(RansomNote.checkMagazine(
                new String[]{"ive", "got", "a", "lovely", "bunch", "of", "coconuts"},
                new String[]{"ive", "got", "some", "coconuts"}
                )
        );
        assertTrue(RansomNote.checkMagazine(
                new String[]{"give", "me", "one", "grand", "today", "night"},
                new String[]{"give", "one", "grand", "today"}
                )
        );
    }
}