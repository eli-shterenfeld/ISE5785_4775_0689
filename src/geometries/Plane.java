package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Check if the ray is parallel to the plane
        double vn = ray.getDirection().dotProduct(normal);
        if (isZero(vn))
            return null;

        Vector u;
        try {
            // vector from ray head to the plane's reference point
            u = p.subtract(ray.getHead());
        } catch (IllegalArgumentException e) {
            // the ray origin is the same as the plane's reference point
            return null;
        }

        // Calculate the scalar parameter t for the intersection point
        double t = (normal.dotProduct(u)) / vn;

        // If t is zero or negative (or very close to zero), consider no intersection
        // Return the intersection point as a list otherwise
        return alignZero(t) <= 0 ? null : List.of(new Intersection(this, ray.getPoint(t)));
    }
}
