package com.gateway.dashboard.leetcode;

/**
 * 200. Number of Islands - Medium
 * https://leetcode.com/problems/number-of-islands/
 * Given an m x n 2D binary grid which represents a map of '1's (land) and '0's (water),
 * return the number of islands.
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 * You may assume all four edges of the grid are all surrounded by water.
 */
public class NumberOfIslands {

    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    count++; // Found an island and:
                    clearRestOfLand(grid, i, j);
                }
            }
        }
        return count;
    }

    private static void clearRestOfLand(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length || grid[i][j] == '0') return;

        grid[i][j] = '0';
        clearRestOfLand(grid, i+1, j);
//        clearRestOfLand(grid, i-1, j);
        clearRestOfLand(grid, i, j+1);
//        clearRestOfLand(grid, i, j-1);
    }

}
