package com.gateway.dashboard.interviews.appl.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    final Game game = new Game();

    @Test
    void startGame() {
        System.out.println(game.startGame("test"));
        assertEquals(game.state().state(), State.IN_PROGRESS);
        assertEquals(game.state().lives(), 8);
    }

    @Test
    void makeCorrectGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('s'));
        assertEquals(game.state().state(), State.IN_PROGRESS);
        assertEquals(game.state().lives(), 8);
    }

    @Test
    void makeTheSameGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('s'));
        System.out.println(game.makeAGuess('s'));
        System.out.println(game.makeAGuess('s'));
        assertEquals(game.state().state(), State.IN_PROGRESS);
        assertEquals(game.state().lives(), 8);
    }

    @Test
    void makeAnIncorrectGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('x'));
        assertEquals(game.state().state(), State.IN_PROGRESS);
        assertEquals(game.state().lives(), 7);
    }

    @Test
    void makeAQuestionableGuess() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess(' '));
        assertEquals(game.state().state(), State.IN_PROGRESS);
        assertEquals(game.state().lives(), 7);
    }

    @Test
    void winGame() {
        System.out.println(game.startGame("tests"));
        System.out.println(game.makeAGuess('t'));
        System.out.println(game.makeAGuess('e'));
        System.out.println(game.makeAGuess('s'));
        assertEquals(game.state().state(), State.WON);
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
        assertEquals(game.state().state(), State.LOSS);
    }
}