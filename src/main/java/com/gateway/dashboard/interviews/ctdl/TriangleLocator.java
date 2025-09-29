package com.gateway.dashboard.interviews.ctdl;

import java.util.List;

// Using records for immutable, concise data carriers.
record Point(double x, double y) {}
record Triangle(Point a, Point b, Point c, int id) {}

public class TriangleLocator {

    /**
     * Finds the triangle that encapsulates a given point by iterating through a list.
     * Complexity: O(n) per query, where n is the number of triangles.
     *
     * @param triangles A list of non-overlapping triangles that cover a space.
     * @param queryPoint The point to locate.
     * @return The ID of the containing triangle, or -1 if no triangle contains the point.
     */
    public int findTriangleForPoint(List<Triangle> triangles, Point queryPoint) {
        for (Triangle triangle : triangles) {
            if (isPointInTriangle(queryPoint, triangle)) {
                return triangle.id();
            }
        }
        return -1; // Point is outside the entire triangulated space.
    }

    /**
     * Checks if a point is inside a triangle using the cross-product method.
     * This method is robust and handles points on the edges correctly.
     */
    private boolean isPointInTriangle(Point p, Triangle t) {
        Point a = t.a();
        Point b = t.b();
        Point c = t.c();

        // Calculate the cross-product for each edge.
        // The sign of the cross product indicates which side of the line the point is on.
        // Vector v1 = (x1, y1), v2 = (x2, y2) -> Cross Product = x1*y2 - y1*x2
        double crossProduct1 = (b.x() - a.x()) * (p.y() - a.y()) - (b.y() - a.y()) * (p.x() - a.x());
        double crossProduct2 = (c.x() - b.x()) * (p.y() - b.y()) - (c.y() - b.y()) * (p.x() - b.x());
        double crossProduct3 = (a.x() - c.x()) * (p.y() - c.y()) - (a.y() - c.y()) * (p.x() - c.x());

        // If the point is inside, all cross products will have the same sign (or be zero).
        boolean hasNegative = (crossProduct1 < 0) || (crossProduct2 < 0) || (crossProduct3 < 0);
        boolean hasPositive = (crossProduct1 > 0) || (crossProduct2 > 0) || (crossProduct3 > 0);

        // If signs are mixed, the point is outside. If all are same sign (or zero), it's inside or on an edge.
        return !(hasNegative && hasPositive);
    }

    // --- Example Usage ---
    public static void main(String[] args) {
        // Create two triangles that share an edge, forming a square.
        // T1: (0,0), (1,1), (0,1)
        // T2: (0,0), (1,0), (1,1)
        Triangle t1 = new Triangle(new Point(0, 0), new Point(1, 1), new Point(0, 1), 1);
        Triangle t2 = new Triangle(new Point(0, 0), new Point(1, 0), new Point(1, 1), 2);
        List<Triangle> triangleList = List.of(t1, t2);

        TriangleLocator locator = new TriangleLocator();

        // Test points
        Point p1 = new Point(0.2, 0.8); // Should be in T1
        Point p2 = new Point(0.8, 0.2); // Should be in T2
        Point p3 = new Point(0.5, 0.5); // On the shared edge, will be found in the first one tested (T1)
        Point p4 = new Point(2.0, 2.0); // Outside all triangles

        System.out.printf("Point %s is in Triangle: %d\n", p1, locator.findTriangleForPoint(triangleList, p1)); // Expected: 1
        System.out.printf("Point %s is in Triangle: %d\n", p2, locator.findTriangleForPoint(triangleList, p2)); // Expected: 2
        System.out.printf("Point %s is in Triangle: %d\n", p3, locator.findTriangleForPoint(triangleList, p3)); // Expected: 1
        System.out.printf("Point %s is in Triangle: %d\n", p4, locator.findTriangleForPoint(triangleList, p4)); // Expected: -1
    }
}
