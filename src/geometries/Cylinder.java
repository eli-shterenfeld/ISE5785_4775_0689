package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a cylinder, which extends a Tube and has a specific height.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a Cylinder with a given radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }


    @Override
    public Vector getNormal(Point point) {

        Point head = axis.getHead();
        Vector direction = axis.getDirection();

        if (head.equals(point) || point.subtract(head).dotProduct(direction) == 0.0)
            return direction.scale(-1);


        Point upperBase = head.add(direction.scale(height));

        if (upperBase.equals(point) ||upperBase.subtract(point).dotProduct(direction) == 0)
            return direction;

        return super.getNormal(point);
    }
}

