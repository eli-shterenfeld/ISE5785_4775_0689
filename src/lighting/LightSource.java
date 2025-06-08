package lighting;

import primitives.*;

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
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the point.
     *
     * @param p the point
     * @return the normalized direction vector to the point
     */
    Vector getL(Point p);

    /**
     * Returns the distance from the light source to the point.
     *
     * @param p the point
     * @return the distance to the point
     */
    double getDistance(Point p);
}