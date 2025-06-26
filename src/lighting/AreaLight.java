package lighting;

/**
 * Area light source emitting from a finite surface; produces soft shadows.
 * Implementations must return intensity and sample a point on the emitter.
 */
public interface AreaLight {

    /**
     * Returns the radius of the area light.
     *
     * @return the radius of the area light
     */
    double getRadius();

    /**
     * Sets the radius of the area light.
     *
     * @return the area light instance
     */
    int getShadowRayCount();
}
