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
     * The square of the radius, used for distance calculations.
     */
    protected final double radiusSquared;
    /**
     * Constructs a RadialGeometry with a specified radius.
     *
     * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

}
