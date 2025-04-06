package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a triangle, which is a specific type of polygon.
 */
public class Triangle extends Polygon {

    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }
}