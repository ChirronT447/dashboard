package com.gateway.dashboard.hackerrank.easy;

public class JumpingOnTheClouds {

    // Complete the jumpingOnClouds function below.
    // You can jump on any cumulus cloud having a number that is equal to the number of the current cloud plus 1 or 2. You must avoid the thunderheads.
    // Print the minimum number of jumps needed to win the game.
    static int jumpingOnClouds(int[] c) {
        // Always jump 2 unless that cloud value is 1 in which case jump 1
        int result = 0;
        int i = 0;
        int size = c.length;
        while(i!=-1) {
            if(i+2 < size && c[i+2] != 1){
                i+=2; System.out.print("Jumping 2");
            }
            else {
                i++; System.out.print("Jumping 1");
            }
            result++;
            if(i >= size-1) {
                i = -1; System.out.print("Exiting");
            }
        }
        return result;
    }

}
