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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        final Point rayOrigin = ray.getHead();
        final Point axisPoint = axis.getHead(); // Cylinder axis starting point
        final Vector axisDir = axis.getDirection(); // Cylinder axis direction
        final Vector rayDir = ray.getDirection();

        Vector deltaP;
        boolean isDeltaPZero;
        try {
            deltaP = rayOrigin.subtract(axisPoint);
            isDeltaPZero = false;
        } catch (IllegalArgumentException e) {
            deltaP = null;
            isDeltaPZero = true;
        }

        double rayDirDotAxis = rayDir.dotProduct(axisDir);
        double a = rayDir.dotProduct(rayDir) - rayDirDotAxis * rayDirDotAxis;
        double b, c;

        if (isDeltaPZero) {
            b = 0;
            c = -radiusSquared;
        } else {
            double deltaPDotAxis = deltaP.dotProduct(axisDir);
            b = 2 * (rayDir.dotProduct(deltaP) - rayDirDotAxis * deltaPDotAxis);
            c = deltaP.dotProduct(deltaP) - deltaPDotAxis * deltaPDotAxis - radiusSquared;
        }

        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0) return null;

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double denominator = 2 * a;

        double t2 = alignZero((-b + sqrtDiscriminant) / denominator);
        double t1 = alignZero((-b - sqrtDiscriminant) / denominator);

        if (t1 > 0 && alignZero(t1 - maxDistance) < 0) {
            return (t2 > 0 && alignZero(t2 - maxDistance) < 0)
                    ? List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)))
                    : List.of(new Intersection(this, ray.getPoint(t1)));
        }

        return (t2 > 0 && alignZero(t2 - maxDistance) < 0)
                ? List.of(new Intersection(this, ray.getPoint(t2)))
                : null;
    }
}

