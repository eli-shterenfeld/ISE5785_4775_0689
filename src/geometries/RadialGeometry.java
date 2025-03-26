package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a geometric object with a radial dimension, extending Geometry.
 */
abstract public class RadialGeometry extends Geometry {

    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructs a RadialGeometry with a specified radius.
     *
     * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

}
