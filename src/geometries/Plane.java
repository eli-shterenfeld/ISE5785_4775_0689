package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Represents a plane in 3D space, defined by a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    final private Point p;

    /**
     * The normal vector of the plane.
     */
    final private Vector normal;

    /**
     * Constructs a plane using three points in space.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        p = p1;

        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);
        normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructs a plane using a point and a normal vector.
     *
     * @param p1     a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point p1, Vector normal) {
        p = p1;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        // Check if the ray origin coincides with the plane's reference point
        if (p.equals(ray.getHead()))
            return null;

        // Check if the ray is parallel to the plane
        double VdotN = ray.getDirection().dotProduct(normal);
        if (isZero(VdotN))
            return null;

        // Calculate the scalar parameter t for the intersection point
        double t = (normal.dotProduct(p.subtract(ray.getHead()))) / VdotN;

        // If t is zero or negative (or very close to zero), consider no intersection
        if (alignZero(t) <= 0)
            return null;

        // Return the intersection point as a list
        return List.of(ray.getPoint(t));
    }
}
