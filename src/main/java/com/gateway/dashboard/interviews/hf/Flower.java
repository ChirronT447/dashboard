package com.gateway.dashboard.interviews.hf;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents a flower.
 *
 * The flower input file will be a text file with each line containing a tuple of flower type, the number of flowers to plant
 * of that type, and a constraint on how close flowers of that type can be planted. There will only by a single line for each
 * flower type.
 *
 * An example flower input file is show below:
 * a,3,1
 * b,2,2
 * c,2,2
 * d,2,5
 *
 * This should be interpreted as:
 * 3 flowers of type 'a' should be planted - the minimum distance is 1 (so these can be placed next to each other)
 * 2 flowers of type 'b' should be planted - the minimum distance is 2 (so all 'b' flowers must have at least distance 2 to all other 'b' flowers)
 * 2 flowers of type 'c' should be planted - the minimum distance is 2
 * 2 flowers of type 'd' should be planted - the minimum distance is 5
 *
 * When considering the distance between two plants, use the manhattan distance (vertical distance + horizontal distance).
 */
public class Flower implements Comparable<Flower> {

    // Each Flower has a name/type
    private final String type;

    // This is not actually an attribute of the flower but I'm putting it in here for ease
    private int numberAvailableForPlanting;

    // The closest each flower can be placed to another flower.
    // We use the manhattan distance (the vertical distance + the horizontal distance).
    // I.e. The distance between two points is the sum of the difference of the abs value of their Cartesian (x,y) coordinates.
    // e.g. (0,0) -> (2,3) = distance 5
    private int minimumDistance;

    // Location in the garden to be filled in when calculated, should equal to at least closestPlantingDistance
    private Integer xCoordinate;
    private Integer yCoordinate;

    public Flower(String type, int numberAvailableForPlanting, int minimumDistance) {
        this.type = type;
        this.numberAvailableForPlanting = numberAvailableForPlanting;
        this.minimumDistance = minimumDistance;
    }

    /**
     * An example flower input file is show below:
     * a,3,1
     * b,2,2
     * c,2,2
     * d,2,5
     * @param path
     * @return
     */
    public static List<Flower> build(String path) {
        String flowerFile = Helper.readFile(path);
        return List.of(flowerFile.split("\\s+"))
                .stream()
                .map(str -> {
                    String[] args = str.split(",");
                    return new Flower(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                }).sorted()
                .collect(Collectors.toList());
    }

    public void prettyPrint() {
        System.out.print("Flower [" + type + "]"
                + "* [" + this.numberAvailableForPlanting + "] available"
                + "@ distance [" + this.minimumDistance + "]");
    }

    public String getType() {
        return type;
    }

    public int getNumberAvailableForPlanting() {
        return numberAvailableForPlanting;
    }

    public void setNumberAvailableForPlanting(int numberAvailableForPlanting) {
        this.numberAvailableForPlanting = numberAvailableForPlanting;
    }

    public int getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(int minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return Objects.equals(xCoordinate, flower.xCoordinate) &&
                Objects.equals(yCoordinate, flower.yCoordinate) &&
                type.equals(flower.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, xCoordinate, yCoordinate);
    }

    @Override
    public String toString() {
        return "Flower{" +
                "type='" + type + '\'' +
                ", numberAvailableForPlanting=" + numberAvailableForPlanting +
                ", closestPlantingDistance=" + minimumDistance +
                '}';
    }

    /**
     * Compare planting distance then number available (we want to try planting the biggest ones first).
     * @param o
     * @return
     */
    @Override
    public int compareTo(Flower o) {
        return Comparator.comparing(Flower::getMinimumDistance)
                .thenComparing(Flower::getNumberAvailableForPlanting)
                .reversed() // if we want to plant the biggest first
                .compare(this, o);
    }
}
