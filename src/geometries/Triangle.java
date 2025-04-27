package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

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
    public List<Point> findIntersections(Ray ray) {

        // Find intersection of the ray with the triangle's plane
        List<Point> intersection = plane.findIntersections(ray);
        if (intersection == null) {
            return null;
        }

        // Take the intersection point
        Point q = intersection.getFirst();
        Point a = vertices.get(0);
        Point b = vertices.get(1);
        Point c = vertices.get(2);

        // Check if the point is exactly on a vertex
        if (q.equals(a) || q.equals(b) || q.equals(c))
            return null;

        // Create vectors for barycentric coordinates calculation
        Vector v0 = c.subtract(a); // vector AC
        Vector v1 = b.subtract(a); // vector AB
        Vector v2 = q.subtract(a); // vector AQ

        // Compute dot products
        double dot00 = v0.dotProduct(v0);
        double dot01 = v0.dotProduct(v1);
        double dot02 = v0.dotProduct(v2);
        double dot11 = v1.dotProduct(v1);
        double dot12 = v1.dotProduct(v2);

        // Compute the denominator of the barycentric coordinates
        double denom = dot00 * dot11 - dot01 * dot01;

        // Check for a degenerate triangle (zero area)
        if (isZero(denom)) {
            return null;
        }

        // Calculate barycentric coordinates (u, v, w)
        double u = (dot11 * dot02 - dot01 * dot12) / denom;
        double v = (dot00 * dot12 - dot01 * dot02) / denom;
        double w = 1 - u - v;

        // Strict inside check: point must be strictly inside the triangle (no edges or vertices)
        if (alignZero(u) > 0 && alignZero(v) > 0 && alignZero(w) > 0) {
            return intersection;
        }

        // If not strictly inside, return null
        return null;
    }
}