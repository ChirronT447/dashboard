package com.gateway.dashboard.interviews.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    final Game game = new Game();

    @Test
    void startGame() {
        System.out.println(game.startGame("test"));
    }

    @Test
    void makeCorrectGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('s'));
    }

    @Test
    void makeSeveralCorrectGuesses() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('s'));
        System.out.println(game.makeAGuess('s'));
        System.out.println(game.makeAGuess('s'));
    }

    @Test
    void makeAnIncorrectGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('x'));
    }

    @Test
    void makeAnInvalidGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess(' '));
    }

    @Test
    void winGame() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('t'));
        System.out.println(game.makeAGuess('e'));
        System.out.println(game.makeAGuess('s'));
    }

    @Test
    void loseGame() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('x'));
        System.out.println(game.makeAGuess('q'));
        System.out.println(game.makeAGuess('w'));
        System.out.println(game.makeAGuess('c'));
        System.out.println(game.makeAGuess('v'));
        System.out.println(game.makeAGuess('b'));
        System.out.println(game.makeAGuess('m'));
        System.out.println(game.makeAGuess('n'));
    }
}