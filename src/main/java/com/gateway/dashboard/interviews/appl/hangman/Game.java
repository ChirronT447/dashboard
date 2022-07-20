package com.gateway.dashboard.interviews.appl.hangman;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private GameState state;
    private String answerStr;
    private char[] answer;

    public GameState startGame(String word) {
        int length = word.length();
        this.answerStr = word;
        this.answer = word.toCharArray();
        this.state = new GameState(
                8,
                generateResultPlaceholder(length),
                new HashSet<>(),
                new HashSet<>(),
                State.IN_PROGRESS
        );
        return this.state;
    }

    // Take a guess
    public GameState makeAGuess(char letter) {
        if(answerStr.contains(String.valueOf(letter))) {
            this.state = gameContinues(letter);
        } else {
            final Set<Character> incorrectGuesses = this.state.incorrectGuesses();
            incorrectGuesses.add(letter);
            if(this.state.lives() > 1) {
                this.state = gameContinues(incorrectGuesses);
            } else {
                this.state = gameOver(incorrectGuesses);
            }
        }
        return this.state;
    }

    private GameState gameOver(Set<Character> incorrectGuesses) {
        return new GameState(
                0,                        // No lives remaining
                answerStr,                      // Able to return answer now
                this.state.correctGuesses(),    // Correct guesses stays the same
                incorrectGuesses,               // Added to incorrect guesses
                State.LOSS                      // State == Loss now
        );
    }

    private GameState gameContinues(Set<Character> incorrectGuesses) {
        return new GameState(
                this.state.lives() - 1,       // Minus 1 life
                this.state.discoveredWordSoFar(),   // DiscoveredWOrdSoFar stays the same
                this.state.correctGuesses(),        // Correct guesses stays the same
                incorrectGuesses,                   // Added to incorrect guesses
                State.IN_PROGRESS                   // State == Game still going
        );
    }

    private GameState gameContinues(char letter) {
        final Set<Character> correctGuesses = this.state.correctGuesses();
        correctGuesses.add(letter);
        final String discoveredWordSoFar = updateDiscoveredWordSoFar(letter);
        final State state = discoveredWordSoFar.equals(this.answerStr) ? State.WON : State.IN_PROGRESS;
        return new GameState(
                this.state.lives(),                 // Life stays the same
                discoveredWordSoFar,                // DiscoveredWordSoFar updates
                correctGuesses,                     // Added to correct guesses
                this.state.incorrectGuesses(),      // Incorrect guesses stays the same
                state                               // Game may continue or finish
        );
    }

    public GameState state() {
        return this.state;
    }

    private String updateDiscoveredWordSoFar(char letter) {
        char[] discoveredWord = this.state.discoveredWordSoFar().toCharArray();
        char[] solutionWord = this.answer;
        for(int i = 0; i < solutionWord.length; i++) {
            if(letter == solutionWord[i]) {
                discoveredWord[i] = letter;
            }
        }
        return new String(discoveredWord);
    }

    private String generateResultPlaceholder(int length) {
        return "_".repeat(length);
    }

}
