package com.gateway.dashboard.interviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetectSquareTest {

    private DetectSquare detectSquare = new DetectSquare();
    
    @BeforeEach
    void setup() {
        this.detectSquare = new DetectSquare();
    }
    
    @Test
    void add() {
        Assertions.assertFalse(detectSquare.add(1, 2)); // ['-','-','-','-']
        Assertions.assertFalse(detectSquare.add(1, 3)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 3)); // ['-','-','-','X']
        Assertions.assertFalse(detectSquare.add(3, 1)); // ['-','X','-','-']

        Assertions.assertFalse(detectSquare.add(0, 0)); // ['X','-','X','-']
        Assertions.assertFalse(detectSquare.add(0, 2)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 0)); // ['X','-','X','X']
        Assertions.assertTrue( detectSquare.add(2, 2)); // ['-','X','-','-']
    }

    @Test
    void add2() {
        Assertions.assertFalse(detectSquare.add(1, 2)); // ['-','-','-','-']
        Assertions.assertFalse(detectSquare.add(1, 3)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 3)); // ['-','-','-','X']
        Assertions.assertFalse(detectSquare.add(3, 1)); // ['-','X','-','-']

        Assertions.assertFalse(detectSquare.add(0, 0)); // ['X','-','X','-']
        Assertions.assertFalse(detectSquare.add(0, 2)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 0)); // ['X','-','-','X']
        Assertions.assertFalse(detectSquare.add(3,2)); // ['-','X','X','-']
    }

    @Test
    void add3() {
        Assertions.assertFalse(detectSquare.add(0,0)); // ['X','X','-','-']
        Assertions.assertFalse(detectSquare.add(0,1)); // ['X','X','-','-']
        Assertions.assertFalse(detectSquare.add(1,0)); // ['-','-','-','-']
        Assertions.assertTrue( detectSquare.add(1,1)); // ['-','-','-','-']
    }

    @Test
    void add4() {
        Assertions.assertFalse(detectSquare.add(0,0)); // ['X','X','-','-']
        Assertions.assertFalse(detectSquare.add(0,1)); // ['X','','-','-']
        Assertions.assertFalse(detectSquare.add(1,0)); // ['-','X','-','-']
        Assertions.assertFalse(detectSquare.add(2,1)); // ['-','-','-','-']
    }

    @Test
    void add5() {
        Assertions.assertFalse(detectSquare.add(1, 2)); // ['-','-','-','-']
        Assertions.assertFalse(detectSquare.add(1, 3)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 3)); // ['-','-','-','X']
        Assertions.assertFalse(detectSquare.add(3, 1)); // ['-','X','-','-']

        Assertions.assertFalse(detectSquare.add(0, 0)); // ['X','-','X','X']
        Assertions.assertFalse(detectSquare.add(0, 2)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 0)); // ['X','-','-','X']
        Assertions.assertFalse(detectSquare.add(0, 3)); // ['-','X','-','-']
    }

    @Test
    void add6() {
        Assertions.assertFalse(detectSquare.add(0,0)); // ['X','-','X','-']
        Assertions.assertFalse(detectSquare.add(0,1)); // ['X','-','X','-']
        Assertions.assertFalse(detectSquare.add(1,0)); // ['-','-','-','-']
        Assertions.assertFalse(detectSquare.add(1,2)); // ['-','-','-','-']
    }

    @Test
    void add7() {
        Assertions.assertFalse(detectSquare.add(1, 2)); // ['-','-','-','-']
        Assertions.assertFalse(detectSquare.add(1, 3)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 3)); // ['-','X','X','X']
        Assertions.assertFalse(detectSquare.add(3, 1)); // ['-','X','-','-']
        Assertions.assertFalse(detectSquare.add(2, 1));
        Assertions.assertFalse(detectSquare.add(2, 2));

        Assertions.assertFalse(detectSquare.add(0, 0)); // ['X','-','X','X']
        Assertions.assertFalse(detectSquare.add(0, 2)); // ['-','X','X','-']
        Assertions.assertFalse(detectSquare.add(2, 0)); // ['X','X','-','X']
        Assertions.assertFalse(detectSquare.add(0, 3)); // ['-','X','-','-']
    }
}