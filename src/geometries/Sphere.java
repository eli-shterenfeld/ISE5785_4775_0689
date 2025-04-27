package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
    public List<Point> findIntersections(Ray ray) {
        Vector u;
        try {
            // vector from ray head to the center of the sphere
            u = center.subtract(ray.getHead());
        } catch (IllegalArgumentException e) {
            // the ray origin is the center of the sphere
            return List.of(ray.getPoint(radius));
        }

        // projection of u on the ray direction
        double tm = u.dotProduct(ray.getDirection());
        // squared distance from the center of the sphere to the ray
        double dSquared = u.lengthSquared() - tm * tm;
        double thSquared = radiusSquared - dSquared;
        // if the distance is greater than or equal to the radius
        if (alignZero(thSquared) <= 0) return null;
        double th = Math.sqrt(thSquared);

        double t2 = alignZero(tm + th);
        if (t2 <= 0) return null;

        double t1 = alignZero(tm - th);
        return t1 <= 0 ? List.of(ray.getPoint(t2)) : List.of(ray.getPoint(t1), ray.getPoint(t2));
    }
}
