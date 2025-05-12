package lighting;

import primitives.Color;

/**
 * Abstract base class representing a light source in the scene.
 */
abstract class Light {

    /**
     * The intensity (color) of the light.
     */
    protected final Color intensity;

    /**
     * Constructor for Light.
     *
     * @param intensity the color intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the light.
     *
     * @return the color intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}