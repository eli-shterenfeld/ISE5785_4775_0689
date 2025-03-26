package geometries;
import primitives.*;

/**
 * Represents a plane in 3D space, defined by a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    Point p;

    /**
     * The normal vector of the plane.
     */
    Vector normal;

    /**
     * Default constructor for a plane.
     */
    public Plane() {
        super();
    }

    /**
     * Constructs a plane using three points in space.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        super();
        normal = null; // Normal calculation should be implemented
        p = p1;
    }

    /**
     * Constructs a plane using a point and a normal vector.
     *
     * @param p1    a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point p1, Vector normal) {
        super();
        p = p1;
        this.normal = normal.normalize();
    }


    @Override
    Vector getNormal(Point p) {
        return normal;
    }
}
