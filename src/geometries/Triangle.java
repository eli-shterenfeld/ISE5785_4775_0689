package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
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
        if (u <= 0 || u >= 1) return null;
        if (v <= 0 || v >= 1) return null;
        if (u + v >= 1) return null;

        final double t = f * edge2.dotProduct(q);
        if (t <= 0) return null;

        final Point intersectionPoint = origin.add(direction.scale(t));

        // === Check if intersection point is exactly on any vertex ===
        if (intersectionPoint.equals(v0) || intersectionPoint.equals(v1) || intersectionPoint.equals(v2))
            return null;

        // === Check if intersection point lies on any edge ===
        try {
            // Edge v0-v1
            final Vector ap1 = intersectionPoint.subtract(v0);
            final Vector ab1 = v1.subtract(v0);
            if (ap1.crossProduct(ab1).lengthSquared() == 0) {
                final double dot = ap1.dotProduct(ab1);
                if (dot >= 0 && dot <= ab1.lengthSquared())
                    return null;
            }

            // Edge v1-v2
            final Vector ap2 = intersectionPoint.subtract(v1);
            final Vector ab2 = v2.subtract(v1);
            if (ap2.crossProduct(ab2).lengthSquared() == 0) {
                final double dot = ap2.dotProduct(ab2);
                if (dot >= 0 && dot <= ab2.lengthSquared())
                    return null;
            }

            // Edge v2-v0
            final Vector ap3 = intersectionPoint.subtract(v2);
            final Vector ab3 = v0.subtract(v2);
            if (ap3.crossProduct(ab3).lengthSquared() == 0) {
                final double dot = ap3.dotProduct(ab3);
                if (dot >= 0 && dot <= ab3.lengthSquared())
                    return null;
            }

        } catch (IllegalArgumentException e) {
            // Do nothing, just avoid crash on zero vector
        }

        return List.of(new Intersection(this, intersectionPoint));
    }

}