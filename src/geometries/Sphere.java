package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

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

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Vector u;
        try {
            // vector from ray head to the center of the sphere
            u = center.subtract(ray.getHead());
        } catch (IllegalArgumentException e) {
            // the ray origin is the center of the sphere
            return alignZero(radius - maxDistance) < 0
                    ? List.of(new Intersection(this, ray.getPoint(radius)))
                    : null;
        }

        // projection of u on the ray direction
        double tm = u.dotProduct(ray.getDirection());
        // squared distance from the center of the sphere to the ray
        double dSquared = u.lengthSquared() - tm * tm;
        double thSquared = radiusSquared - dSquared;
        if (alignZero(thSquared) <= 0) return null;

        double th = Math.sqrt(thSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t2 <= 0 || alignZero(t1 - maxDistance) >= 0) return null;

        if (alignZero(t2 - maxDistance) >= 0)
            return t1 <= 0 ? null : List.of(new Intersection(this, ray.getPoint(t1)));
        else
            return t1 <= 0 ? List.of(new Intersection(this, ray.getPoint(t2)))
                    : List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
    }
}
