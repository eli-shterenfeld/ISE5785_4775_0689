package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a cylinder, which extends a Tube and has a specific height.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final double height;  // Cylinder's height – unique to Cylinder, not present in Tube

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
        Point upperBase = head.add(direction.scale(height));

        // Check if the point lies on the top base
        if (upperBase.equals(point) || upperBase.subtract(point).dotProduct(direction) == 0)
            return direction;                      // Normal is same as axis direction

        // Otherwise, point lies on the curved side – defer to Tube’s implementation
        return super.getNormal(point);
    }


    public List<Point> findIntersections(Ray ray) {
        return null;

    }

}


