package com.gateway.dashboard.interviews;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DetectSquare {

    private record Point(int x, int y){}
    private final Set<Point> coordinates;

    public DetectSquare() {
        this.coordinates = new TreeSet<>(
                Comparator.comparing(Point::x).thenComparing(Point::y)
        );
    }

    public boolean add(int x, int y) {
        this.coordinates.add(new Point(x, y));
        return detectSquare(x, y);
    }

    /**
     * ['x','-','-','-'] (3,0)
     * ['x','-','-','x'] (2,0) & (2,3)
     * ['x','-','-','-'] (1,0)
     * ['-','x','-','x'] (0,1) & (0,3)
     * 1) (0,0) -> yes [
     * 2) (0,1) -> no
     * @return
     */
    private boolean detectSquare(int x, int y){
        final List<Point> pointsOfInterest = coordinates.stream()
                .filter(point -> point.x == x && !(point.y == y))
                .toList();

        // If we don't have at least 2 candidate points:
        if (pointsOfInterest.size() < 1) return false;

        final List<Integer> distancesToCheck = pointsOfInterest.stream()
                .map(point -> Math.abs(point.y - y))
                .toList();
        if (distancesToCheck.size() < 1) return false;

        for(int dist : distancesToCheck) {
            if(( coordinates.contains(new Point(x+dist, y)) ||
                 coordinates.contains(new Point(x-dist, y)) ) &&
                coordinates.contains(new Point(x+dist, y+dist)) ||
                coordinates.contains(new Point(x-dist, y-dist))
            ) {
                return true;
            }
        }
        return false;
    }

}
