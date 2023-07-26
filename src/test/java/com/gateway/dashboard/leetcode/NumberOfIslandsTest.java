package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**

 * Given an m x n 2D binary grid which represents a map of '1's (land) and '0's (water),
 * return the number of islands.
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 * You may assume all four edges of the grid are all surrounded by water.
 */
class NumberOfIslandsTest {

    @Test
    void numIslands() {
        // Input: grid = [
        //  ["1","1","1","1","0"],
        //  ["1","1","0","1","0"],
        //  ["1","1","0","0","0"],
        //  ["0","0","0","0","0"]
        //]
        //Output: 1
        Assertions.assertThat(NumberOfIslands.numIslands(new char[][]
                {
                    {'1', '1', '1', '1', '0'},
                    {'1', '1', '0', '1', '0'},
                    {'1', '1', '0', '0', '0'},
                    {'0', '0', '0', '0', '0'}
                }
        )).isEqualTo(1);

        // Input: grid = [
        //  ["1","1","0","0","0"],
        //  ["1","1","0","0","0"],
        //  ["0","0","1","0","0"],
        //  ["0","0","0","1","1"]
        //]
        //Output: 3
        Assertions.assertThat(NumberOfIslands.numIslands(new char[][]
                {
                        {'1', '1', '0', '0', '0'},
                        {'1', '1', '0', '0', '0'},
                        {'0', '0', '1', '0', '0'},
                        {'0', '0', '0', '1', '1'}
                }
        )).isEqualTo(3);
    }
}