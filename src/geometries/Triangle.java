package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a triangle, which is a specific type of polygon.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle from three points.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        final Point origin = ray.getHead();
        final Vector direction = ray.getDirection();

        final Point v0 = vertices.get(0);
        final Point v1 = vertices.get(1);
        final Point v2 = vertices.get(2);

        final Vector edge1, edge2, h, s, q;

        try {
            edge1 = v1.subtract(v0);
            edge2 = v2.subtract(v0);
            h = direction.crossProduct(edge2);
        } catch (IllegalArgumentException e) {
            return null;
        }

        final double a = edge1.dotProduct(h);
        if (isZero(a)) return null;

        final double f = 1.0 / a;
        try {
            s = origin.subtract(v0);
            q = s.crossProduct(edge1);
        } catch (IllegalArgumentException e) {
            return null;
        }

        final double u = f * s.dotProduct(h);
        final double v = f * direction.dotProduct(q);

        // Strict inside only: u, v strictly > 0 and < 1
        if (alignZero(u) <= 0 || alignZero(u - 1) >= 0) return null;
        if (alignZero(v) <= 0 || alignZero(v - 1) >= 0) return null;
        if (alignZero(u + v - 1) >= 0) return null;

        final double t = alignZero(f * edge2.dotProduct(q));
        if (t <= 0 || alignZero(t - maxDistance) >= 0) return null;

        final Point intersectionPoint = origin.add(direction.scale(t));

        return List.of(new Intersection(this, intersectionPoint));
    }
}