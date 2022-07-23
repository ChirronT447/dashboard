package com.gateway.dashboard.hired;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackendOneTest {

    // maze:
    //    [
    //      [0, 0, 1],
    //      [1, 0, 0],
    //      [1, 1, 0]
    //    ]
    // https://www.geeksforgeeks.org/rat-in-a-maze-backtracking-2/
    @Test
    void testSolveMaze() {
        Assertions.assertTrue(BackendOne.solveMaze(new long[][]{{0, 0}, {0, 0}}));
        Assertions.assertTrue(BackendOne.solveMaze(new long[][]{{0, 0, 0}, {1, 0, 0}, {1, 1, 0}}));
    }

    // https://www.geeksforgeeks.org/write-a-c-program-to-find-the-maximum-depth-or-height-of-a-tree/
    @Test
    void testCalcTreeHeight() {
        Tree.calcHeight(new long[]{1, 2, 3, 4, 3, 1, -1, 2, -1, -1});
    }

}
