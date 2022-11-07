package com.gateway.dashboard.leetcode;

import java.util.PriorityQueue;

public class FurthestBuilding {

    /**
     * You are given an integer array representing the heights of buildings, some bricks and some ladders.
     * Starting from building 0 you move to the next building using bricks or ladders.
     * While moving from building i to building i+1 (0-indexed):
     *  - If the next building's height is <= to the current building's height you do not need a ladder / bricks.
     *  - Otherwise: you can either use one ladder or (height[i+1] - height[i]) bricks.
     * Return the index of the furthest building (0-indexed) you can reach using the ladders and bricks optimally.
     * @param buildingHeights the heights of buildings
     * @param numberOfBricks number of bricks
     * @param numberOfLadders number of ladders
     * @return the index of the furthest building you can reach, using ladders and bricks optimally.
     */
    public static int furthestBuilding(int[] buildingHeights, int numberOfBricks, int numberOfLadders) {
        final PriorityQueue<Integer> pq = new PriorityQueue<>();
        final int numberOfBuildings = buildingHeights.length - 1;
        for (int i = 0; i < numberOfBuildings; i++) {
            int height = buildingHeights[i + 1] - buildingHeights[i];
            if (height > 0)
                pq.add(height);
            if (pq.size() > numberOfLadders)
                numberOfBricks = numberOfBricks - pq.poll();
            if (numberOfBricks < 0)
                return i;
        }
        return numberOfBuildings;
    }

}
