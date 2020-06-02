package com.gateway.dashboard.hackerrank.easy;

public class CountingValleys {

//    private enum Direction {
//        UP("U"),
//        DOWN("D");
//        private String direction;
//        private Direction(String direction) {
//            this.direction = direction;
//        }
//    }

    // Complete the countingValleys function below.
    // steps: the number of steps Gary takes
    // path: a string describing his path
    // Returns: Prints a single integer that denotes the number of valleys
    // Gary walked through during his hike where a valley is a sequence of consecutive steps below sea level,
    // starting with a step down from sea level and ending with a step up to sea level.
    // NB. I read "a sequence of consecutive steps below sea level" to mean 2 or more steps are required for it
    //  to count as a valley...in fact it's only 1 step below sea level.
    static int countingValleys(int steps, String path) {
        int numberOfValleys = 0;
        int consecutive = 0;
        int level = 0;
        for(char direction : path.toCharArray()) {
            switch(Character.toString(direction)) {
                case "D":
                    level--;
                    if(level < 0) {consecutive++;}
                    break;
                case "U":
                    level++;
                    if(level == 0) {
                        if(consecutive >= 1) {numberOfValleys++;}
                        consecutive = 0;
                    }
                    break;
                default: System.out.println("Gary can only move UP or DOWN at this time and he tried to move: " + direction);
            }
        }
        System.out.println("Result: " + numberOfValleys);
        return numberOfValleys;
    }

}