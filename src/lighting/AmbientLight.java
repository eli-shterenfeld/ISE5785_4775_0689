package lighting;

import primitives.Color;

/**
 * Represents ambient light in a scene, defined by intensity color.
 * This light is uniform and affects all objects equally.
 */
public class AmbientLight {

    /**
     * The intensity of the ambient light, represented as a Color object.
     * This color is used to determine the brightness of the ambient light.
     */
    private final Color intensity;

    /**
     * Constant representing "no ambient light" (i.e., black)
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor that initializes the ambient light with the given intensity
     *
     * @param intensity the intensity color (I_a)
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the ambient light intensity
     *
     * @return the intensity color
     */
    public Color getIntensity() {
        return intensity;
    }
}