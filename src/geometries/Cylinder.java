package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;


/**
 * Represents a cylinder, which extends a Tube and has a specific height.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final double height;  // Cylinder's height – unique to Cylinder, not present in Tube

    /**
     * To represent bottomPlane of the cylinder.
     */
    private final Plane bottomPlane;

    /**
     * To represent topPlane of the cylinder.
     */
    private final Plane topPlane;

    /**
     * Constructs a Cylinder with a given radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);     // Call the Tube constructor to set radius and axis
        this.height = height;    // Set the height specific to Cylinder
        this.bottomPlane = new Plane(axis.getHead(), axis.getDirection());
        this.topPlane = new Plane(axis.getPoint(height), axis.getDirection());
    }

    @Override
    public Vector getNormal(Point point) {
        // Returns the normal vector to the surface at the given point

        Point head = axis.getHead();               // Bottom base center of the cylinder
        Vector direction = axis.getDirection();    // Direction vector of the axis ray

        // Check if the point lies on the bottom base
        if (head.equals(point) || point.subtract(head).dotProduct(direction) == 0)
            return direction.scale(-1);            // Return normal pointing outward (opposite to the axis)

        // Compute the top base center by moving 'height' units along the axis
        Point upperBase = axis.getPoint(height);

        // Check if the point lies on the top base
        if (upperBase.equals(point) || upperBase.subtract(point).dotProduct(direction) == 0)
            return direction;                      // Normal is same as axis direction

        // Otherwise, point lies on the curved side – defer to Tube’s implementation
        return super.getNormal(point);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        final List<Intersection> intersections = new LinkedList<>();
        final Vector axisDir = axis.getDirection();
        final Point baseCenter = axis.getHead();
        final Point topCenter = axis.getPoint(height);
        final Point rayOrigin = ray.getHead();

        // 1. Tube intersections
        var tubeIntersections = super.findIntersections(ray);
        if (tubeIntersections != null) {
            for (var p : tubeIntersections) {
                double axisProjection = axisDir.dotProduct(p.subtract(baseCenter));
                if (alignZero(axisProjection) >= 0 && alignZero(axisProjection - height) <= 0) {
                    intersections.add(new Intersection(this, p));
                }
            }
        }

        // 2. Bottom cap
        List<Point> bottom = bottomPlane.findIntersections(ray);
        if (bottom != null) {
            Point p = bottom.getFirst();
            if (alignZero(p.distanceSquared(baseCenter) - radiusSquared) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }

        // 3. Top cap
        List<Point> top = topPlane.findIntersections(ray);
        if (top != null) {
            Point p = top.getFirst();
            if (alignZero(p.distanceSquared(topCenter) - radiusSquared) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }

        // 4. Sort by distance
        intersections.sort(Comparator.comparingDouble(p ->
                p.point.subtract(rayOrigin).dotProduct(ray.getDirection())));

        return intersections.isEmpty() ? null : intersections;
    }
}


