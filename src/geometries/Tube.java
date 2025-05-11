package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        final Point rayOrigin = ray.getHead();
        final Point axisPoint = axis.getHead(); // Cylinder axis starting point
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

        double rayDirDotAxis = rayDir.dotProduct(axisDir);
        // Compute quadratic coefficients for intersection equation
        double a = rayDir.dotProduct(rayDir) - rayDirDotAxis * rayDirDotAxis;
        double b, c;

        if (isDeltaPZero) {
            // Special case handling if deltaP is zero
            b = 0;
            c = -radiusSquared;
        } else {
            // General case
            double deltaPDotAxis = deltaP.dotProduct(axisDir);

            // Coefficient b of quadratic equation
            b = 2 * (rayDir.dotProduct(deltaP) - rayDirDotAxis * deltaPDotAxis);

            // Coefficient c of quadratic equation
            c = deltaP.dotProduct(deltaP) - deltaPDotAxis * deltaPDotAxis - radiusSquared;
        }

        // Calculate discriminant to determine intersection existence
        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0) return null; // No real solutions → no intersection

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double denominator = 2 * a;

        // quadratic parameter 'a' is always positive in our equation, therefore t2 is always greater than t1
        // Calculate the intersection parameters (t values)
        double t2 = alignZero((-b + sqrtDiscriminant) / denominator);
        if (t2 <= 0) return null; // No valid intersection

        double t1 = alignZero((-b - sqrtDiscriminant) / denominator);
        return t1 <= 0 ? List.of(ray.getPoint(t2)) : List.of(ray.getPoint(t1), ray.getPoint(t2));
    }
}

