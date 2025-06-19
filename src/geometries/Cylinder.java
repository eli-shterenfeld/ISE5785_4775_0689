package geometries;

import primitives.*;
import primitives.Vector;

import java.util.*;

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        final List<Intersection> intersections = new LinkedList<>();
        final Vector axisDir = axis.getDirection();
        final Point baseCenter = axis.getHead();
        final Point topCenter = axis.getPoint(height);
        final Point rayOrigin = ray.getHead();

        // 1. Tube intersections
        var tubeIntersections = super.calculateIntersectionsHelper(ray, maxDistance);
        if (tubeIntersections != null) {
            for (Intersection p : tubeIntersections) {
                double axisProjection = axisDir.dotProduct(p.point.subtract(baseCenter));
                if (alignZero(axisProjection) >= 0 && alignZero(axisProjection - height) <= 0) {
                    intersections.add(new Intersection(this, p.point));
                }
            }
        }

        // 2. Bottom cap
        var bottom = bottomPlane.calculateIntersections(ray, maxDistance);
        if (bottom != null) {
            Point p = bottom.getFirst().point;
            if (alignZero(p.distanceSquared(baseCenter) - radiusSquared) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }

        // 3. Top cap
        var top = topPlane.calculateIntersections(ray, maxDistance);
        if (top != null) {
            Point p = top.getFirst().point;
            if (alignZero(p.distanceSquared(topCenter) - radiusSquared) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }

        // 4. Sort by distance
        intersections.sort(Comparator.comparingDouble(p ->
                p.point.subtract(rayOrigin).dotProduct(ray.getDirection())));

        return intersections.isEmpty() ? null : intersections;
    }

    @Override
    public void setBoundingBox() {
        Point base = axis.getPoint(0);
        Vector dir = axis.getDirection().normalize();
        Point top = base.add(dir.scale(height));

        Vector u = dir.findAnyOrthogonal().normalize();
        Vector v = dir.crossProduct(u).normalize();

        Point[] points = new Point[]{
                base.add(u.scale(radius)).add(v.scale(radius)),
                base.add(u.scale(radius)).add(v.scale(-radius)),
                base.add(u.scale(-radius)).add(v.scale(radius)),
                base.add(u.scale(-radius)).add(v.scale(-radius)),
                top.add(u.scale(radius)).add(v.scale(radius)),
                top.add(u.scale(radius)).add(v.scale(-radius)),
                top.add(u.scale(-radius)).add(v.scale(radius)),
                top.add(u.scale(-radius)).add(v.scale(-radius))
        };

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Point p : points) {
            double x = p.getX(), y = p.getY(), z = p.getZ();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            if (z < minZ) minZ = z;
            if (z > maxZ) maxZ = z;
        }

        this.box = new Box(
                new Point(minX, minY, minZ),
                new Point(maxX, maxY, maxZ)
        );
    }
}