package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;


/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a Sphere with a given radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points between a ray and the sphere.
     *
     * @param ray the ray to intersect with the sphere
     * @return a list of intersection points or {@code null} if there are no intersections
     */
    public List<Point> findIntersections(Ray ray) {

        // if the ray origin is the center of the sphere
        if (center.equals(ray.getHead()))
            return List.of(ray.getPoint(radius));

        // vector from ray head to the center of the sphere
        Vector u = center.subtract(ray.getHead());

        // projection of u on the ray direction
        double tm = u.dotProduct(ray.getDirection());

        // additional check: if the ray is outside the sphere and the angle between u and the ray direction is greater than 90, then no intersection
        if (alignZero(tm) <= 0 && alignZero(u.length()) >= radius)
            return null;

        // squared distance from the center of the sphere to the ray
        double dSquared = u.lengthSquared() - tm * tm;

        // if the distance is greater than or equal to the radius
        if (alignZero(dSquared - radius * radius) >= 0)
            return null;

        double th = Math.sqrt(radius * radius - dSquared);

        // check each case
        if (alignZero(tm - th) > 0 && alignZero(tm + th) > 0)
            return List.of(ray.getPoint(tm - th), ray.getPoint(tm + th));

        if (alignZero(tm - th) <= 0 && alignZero(tm + th) > 0)
            return List.of(ray.getPoint(tm + th));

        if (alignZero(tm - th) <= 0 && alignZero(tm + th) <= 0)
            return null;

        return null;
    }
}
