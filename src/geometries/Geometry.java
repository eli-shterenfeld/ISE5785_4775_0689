package geometries;

import primitives.*;

/**
 * Abstract class representing a geometric shape.
 */
abstract public class Geometry implements Intersectable {

    /**
     * Computes the normal vector at a given point on the geometry.
     *
     * @param p the point on the geometry
     * @return the normal vector at the given point
     */
    abstract Vector getNormal(Point p);

}
