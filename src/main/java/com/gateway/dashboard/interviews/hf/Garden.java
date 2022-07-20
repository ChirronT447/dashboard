package com.gateway.dashboard.interviews.hf;


import java.util.*;

/**
 * This class represents a Garden
 *
 * The garden input file will be a text file which visually represents the empty garden layout and possible planting locations.
 * Walls are represented as 'X' and possible planting locations as ' '. All gardens will be rectangular.
 * You may plant a flower only on a ' ', and in a position which meets that flowers' constraints.
 *
 * An example garden input file is shown below:
 * XXXXXXXXXX
 * X      XXX
 * X X   XXXX
 * X     XXXX
 * X     XX X
 * XXXXXXXXXX
 *
 * In this garden there are 21 possible positions in which a flower can be planted.
 */
public class Garden {

    // I've put the garden struct in here and made it private/final and then provide a method to add flowers.
    private final String[][] garden;

    // Origianly tracking minimum distances in here to avoid neverending looping, the map contained i, j co-ordinates
    // private final Set<Pair> minimumDistanceLocations;

    // Compute width/height
    private final Pair widthHeight;

    // Constructor
    Garden(String[][] garden) {
        this.garden = garden;
        widthHeight = new Pair(garden.length, garden[0].length);
        //this.minimumDistanceLocations = new HashSet<>();
        prettyPrint();
    }

    /**
     * Split each garden into rows, take each row and fill each plot into a 2D Garden array.
     * Makes assumptions about the format of the file
     * @param gardenPath
     * @return
     */
    public static Garden build(String gardenPath) {
        if(gardenPath != null && !gardenPath.equals("")) {
            String gardenStr = Helper.readFile(gardenPath);
            String[] gardenRows = gardenStr.split("\r\n");
            String[][] garden = new String[gardenRows.length][gardenRows[0].length()];
            int i = 0, j = 0;
            for (String rowStr : gardenRows) {
                String[] row = rowStr.split("");
                for(String plot : row) {
                    garden[i][j] = plot;
                    j++;
                }
                i++; j = 0;
            }
            return new Garden(garden);
        } else {
            throw new RuntimeException("Garden cannot be empty");
        }
    }

    /**
     * Easier to visualise
     */
    public void prettyPrint() {
        System.out.println("Garden:");
        System.out.println("----------------------------");
        for(int i = 0; i < this.garden.length; i++) {
            for(int j = 0; j < this.garden[0].length; j++) {
                System.out.print(String.format("%s", this.garden[i][j]));
            }
            System.out.println("");
        }
        System.out.println("----------------------------");
    }

    /**
     * Quick and dirty approach here, we could definitely improve this by
     * 1) reducing the number of checks we're performing (we could quickly rule out spaces up front)
     * 2) Using a different data structure, it would be better to use something like a binary tree in here.
     * 3) Being a bit smarter about ....everything... there are too many loops
     * @param flower
     * @return
     */
    public boolean plant(Flower flower) {
        // move through the garden and find an empty space, ignore the outer walls
        for(int i = 1; i < this.garden.length - 1; i++) {
            for(int j = 1; j < this.garden[0].length - 1; j++) {
                // Check if that space can be used for this flower, if so check the size constraint and consider a better
                //  location; plant it and return.
                if(" ".equals(this.garden[i][j])) {
                    if(flower.getMinimumDistance() > 2) {
                        Pair location = suggestLocation(flower, i, j);
                        this.garden[location.getI()][location.getJ()] = flower.getType();
                        markLocations(flower, location.getI(), location.getJ());
                        i = 1; j = 1; // reset
                    } else {
                        this.garden[i][j] = flower.getType();
                        markLocations(flower, i, j);
                    }
                    return true;
                } // otherwise move on the next space
            }
        }
        // If we can't find anywhere to plant this flower return false
        return false;
    }

    // Method to suggest a location that will use up minimum free space
    // brute forcing and this slows things down a lot! Also dealing directly with indices like this = very prone to bugs
    private Pair suggestLocation(Flower flower, int i, int j) {
        int minimumDistance = flower.getMinimumDistance();
        int idx, jdx;

        for(int iLoc = 1; iLoc < this.garden.length - 1; iLoc++) {
            for(int jLoc = 1; jLoc < this.garden[0].length - 1; jLoc++) {
                if (this.garden[iLoc][jLoc].equals(" ")) {
                    int verticalCounter = 0, horizontalCounter = 0;
                    for (idx = iLoc + 1, jdx = jLoc + 1;
                         (idx < minimumDistance + iLoc + 1 && idx < widthHeight.getI()) &&
                                 (jdx < minimumDistance + jLoc + 1 && jdx < widthHeight.getJ());
                         idx++, jdx++) {
                        if (this.garden[iLoc][jdx].equals("X")) {
                            verticalCounter++;
                        }
                        if (this.garden[idx][jLoc].equals("X")) {
                            horizontalCounter++;
                        }
                    }

                    // If we can use all the space in one direction lets do it.
                    if (horizontalCounter == minimumDistance || verticalCounter == minimumDistance) {
                        System.out.println(String.format("Suggesting location (%s, %s)", iLoc, jLoc));
                        return new Pair(iLoc, jLoc);
                    }
                }
            }
        }
        // Can't find anywhere
        return new Pair(i, j);
    }

    /**
     * We know that i,j is a valid space to plant but we now need to systematically mark all the surrounding
     *  spaces in accordance with the requirements of this flower.
     *  The tricky part is choosing what spaces to mark because Manhatten distance measurement gives us options.
     * Ignore walls (X)
     * @param flower
     * @param i
     * @param j
     */
    private void markLocations(Flower flower, int i, int j) {
        int minimumDistance = flower.getMinimumDistance() - 1;

        // These plants can be planted next to each other so nothing to mark
        if (minimumDistance == 0) {
            // Output the current state of the garden
            prettyPrint();
            return;
        }

        // We just need one space, lets look backwards and forwards.
        if (minimumDistance == 1) {
            if(this.garden[i + 1][j].equals("X") || this.garden[i][j + 1].equals("X") ||
               this.garden[i - 1][j].equals("X") || this.garden[i][j - 1].equals("X")) {
                // It seems that we can legitimately avoid adding a space by using the wall.
                // This seems wrong to me and I think the garden designs are more interesting when we don't factor
                //  in the wall but test 3 is very large and I don't think it's possible to solve without taking in the wall.
            }
            else {
                if (this.garden[i][j + 1].equals(" ") || this.garden[i][j + 1].equals("-")) {
                    this.garden[i][j + 1] = "-";
                } else if (this.garden[i + 1][j].equals(" ") || this.garden[i + 1][j].equals("-")) {
                    this.garden[i + 1][j] = "-";
                }
            }
            // Output the current state of the garden
            prettyPrint();
            return;
        }

        // We need more than one space.
        // Can we focus all in one direction?
        int verticalCounter = 0, horizontalCounter = 0;
        for(int idx = i + 1, jdx = j + 1;
            (idx < minimumDistance + i + 1 && idx < widthHeight.getI()) &&
            (jdx < minimumDistance + j + 1 && jdx < widthHeight.getJ());
            idx++, jdx++) {
            if(this.garden[i][jdx].equals("X")) {
                verticalCounter++;
            }
            if(this.garden[idx][j].equals("X")) {
                horizontalCounter++;
            }
        }

        // If we can use all the space in one direction lets do it.
        if(verticalCounter == minimumDistance || horizontalCounter == minimumDistance) {
            return;
        }

        // Check +/- half the distance on the horizontal axis
        // I think this is somewhere we can easily break depending on the test structure.
        int checkAtDistance = minimumDistance / 2;
        int idx = Math.max(i - checkAtDistance, 0);
        int jdx = Math.max(j - checkAtDistance, 0);
        int count = 0;

        for (;
             (idx < i + checkAtDistance +1 && idx < widthHeight.getI()) ||
             (jdx < j + checkAtDistance +1 && jdx < widthHeight.getJ());
             idx++, jdx++
        ) {
            if(this.garden[idx][j].equals("X")) {
                count++;
            }
            if (this.garden[i][jdx].equals(" ") && count < minimumDistance) {
                this.garden[i][jdx] = "-";
                count++;
            }
        }

        // Minimum distance isn't even, we need to factor in +1 on another axis
        if (minimumDistance % 2 != 0) {
            // Check garden length vs width to determine where to focus.
            // If width > length: we'll add the extra distance to the horizontal axis.
            int tmp = checkAtDistance + 1;
            if (this.widthHeight.getI() > this.widthHeight.getJ()) {
                this.garden[i + tmp][j] = "-";
            } else {
                this.garden[i + tmp - 1][j] = "-";
            }
        }

        // Output the current state of the garden
        prettyPrint();
    }

    /**
     * Move through the garden and remove all the markers
     */
    public void cleanup() {
        for(int i = 1; i < this.garden.length - 1; i++) {
            for(int j = 1; j < this.garden[0].length - 1; j++) {
                if("-".equals(this.garden[i][j])) {
                    this.garden[i][j] = " ";
                } // otherwise move on the next space
            }
        }
    }

    /**
     * Lets see how many spaces we have to work with
     */
    public void analyseGarden() {
        int plantingSpaces = 0;
        for(int i = 1; i < this.garden.length - 1; i++) {
            for(int j = 1; j < this.garden[0].length - 1; j++) {
                if(garden[i][j].equals(" ")) {
                    plantingSpaces++;
                }
            }
        }
        System.out.println("This garden has " + plantingSpaces + " spaces for planting.");
    }

    /**
     * Pair structure to deal with i,j
     */
    static class Pair {

        private final int i;
        private final int j;

        public Pair(final int i, final int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return i == pair.i &&
                    j == pair.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }
}
