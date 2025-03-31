package geometries;

import primitives.*;

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
        //ליבדוק אם להכניס את ה t
        //double t = point.subtract(head).dotProduct(direction);
        return point.subtract(head.add(direction.scale(point.subtract(head).dotProduct(direction)))).normalize();
    }
}

