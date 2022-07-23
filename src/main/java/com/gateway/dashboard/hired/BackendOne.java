package com.gateway.dashboard.hired;

public class BackendOne {

// Given a maze, write a function that takes the maze, the size of the maze (n > 2),
//   and returns true if the maze has a path from the top left corner top the bottom right corner.
//
//   The maze tiles are represented as an matrix of 0 and 1, where passable tiles are represented
//    as 0 and walls are noted as 1. The possible movements are vertical or horizontal.
//
//    No diagonal movements are allowed.
//
//For example, the following maze has a path from the top left to the bottom right

    public static boolean solveMaze(long[][] maze) {
        int sizeX = maze.length, sizeY = maze[0].length;
        long[][] solvedMaze = new long[sizeX][sizeY];
        solveMaze(maze, solvedMaze, 0, 0);
        return solvedMaze[sizeX-1][sizeY-1] == 1;
    }

    static boolean solveMaze(long[][] maze, long[][] solvedMaze, int startX, int startY) {
        if(isSafe(maze, startX, startY)) {
            solvedMaze[startX][startY] = 1; // Visiting here
            if (solveMaze(maze, solvedMaze, startX + 1, startY))
                return true; // Check x axis
            if (solveMaze(maze, solvedMaze, startX, startY + 1))
                return true; // check Y axis
            // if neither of these work we need to backtrack
            solvedMaze[startX][startY] = 0;
        }
        // Check if solved
        return solvedMaze[maze.length - 1][maze[0].length - 1] == 1;
    }

    static boolean isSafe(long[][] maze, int x, int y) {
        // if (x, y outside maze) return false
        return (x >= 0 && x < maze.length &&
                y >= 0 && y < maze.length && maze[x][y] == 0);
    }

}

class Tree {

    public static long calcHeight(long[] tree) {
        return maxDepth(tree, 0, 0);
    }

    static long maxDepth(long[] tree, int index, long height) {
        if(isSafe(tree, height)) {
            long currLevel = tree[index];
            if(currLevel != -1) {
                return Math.max( // TODO: fix this
                        1 + maxDepth(tree, index + 1, height + 1),
                        1 + maxDepth(tree, index + 2, height + 1)
                );
            }
        }
        return 0;
    }

    static boolean isSafe(long[] tree, long i) {
        return i >= 0 && i < tree.length;
    }
}