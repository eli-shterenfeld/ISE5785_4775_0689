package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents an infinite tube in 3D space, defined by a radius and an axis.
 */
public class Tube extends RadialGeometry {

    /**
     * The axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a Tube with a given radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis   the axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Point head = axis.getHead();
        Vector direction = axis.getDirection();

        Vector v = point.subtract(head);
        double t = v.dotProduct(direction);
        return point.subtract(axis.getPoint(t)).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Get ray origin and direction
        final Point rayOrigin = ray.getPoint(0);
        final Point axisPoint = axis.getPoint(0); // Cylinder axis starting point
        final Vector axisDir = axis.getDirection(); // Cylinder axis direction
        final Vector rayDir = ray.getDirection();

        Vector deltaP;
        boolean isDeltaPZero;
        try {
            // Vector from cylinder axis point to ray origin
            deltaP = rayOrigin.subtract(axisPoint);
            isDeltaPZero = false;
        } catch (IllegalArgumentException e) {
            // Special case: ray origin lies exactly on the axis line
            deltaP = null;
            isDeltaPZero = true;
        }

        // Compute quadratic coefficients for intersection equation
        double a = rayDir.dotProduct(rayDir) - Math.pow(rayDir.dotProduct(axisDir), 2);

        double b, c;
        if (isDeltaPZero) {
            // Special case handling if deltaP is zero
            b = 0;
            c = -radiusSquared;
        } else {
            // General case
            double rayDirDotAxis = rayDir.dotProduct(axisDir);
            double deltaPDotAxis = deltaP.dotProduct(axisDir);

            // Coefficient b of quadratic equation
            b = 2 * (rayDir.dotProduct(deltaP) - rayDirDotAxis * deltaPDotAxis);

            // Coefficient c of quadratic equation
            c = deltaP.dotProduct(deltaP) - Math.pow(deltaPDotAxis, 2) - radiusSquared;
        }

        // Calculate discriminant to determine intersection existence
        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0) return null; // No real solutions → no intersection

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double denom = 2 * a;

        // Calculate the intersection parameters (t values)
        double t1 = alignZero((-b - sqrtDiscriminant) / denom);
        double t2 = alignZero((-b + sqrtDiscriminant) / denom);

        List<Point> intersections = new LinkedList<>();
        // Check valid positive t values and compute intersection points
        if (alignZero(t1) > 0) intersections.add(ray.getPoint(t1));
        if (alignZero(t2) > 0) intersections.add(ray.getPoint(t2));

        // Return result or null if no intersections
        return intersections.isEmpty() ? null : intersections;
    }
}

