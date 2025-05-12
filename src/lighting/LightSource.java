package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface for light sources in the scene.
 */
public interface LightSource {
    /**
     * Returns the intensity (color) of the light at a given point.
     *
     * @param p the point where intensity is requested
     * @return the intensity color
     */
    public Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the point.
     *
     * @param p the point
     * @return the normalized direction vector to the point
     */
    public Vector getL(Point p);
}