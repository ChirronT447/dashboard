package com.gateway.dashboard.interviews.hangman;

import java.util.Set;

record GameState(
        int lives,
        String discoveredWordSoFar,
        Set<Character> correctGuesses,
        Set<Character> incorrectGuesses,
        State state
) {}

enum State {
    IN_PROGRESS,
    WON,
    LOSS;
}