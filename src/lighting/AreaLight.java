package lighting;

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
     * @param radius the radius to set
     * @return the area light instance
     */
    int getShadowRayCount();
}
