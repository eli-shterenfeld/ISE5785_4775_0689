package lighting;

import primitives.*;

/**
 * Directional light source - light comes from a specific direction only.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The direction of the light.
     */
    private final Vector direction;

    /**
     * Constructor for DirectionalLight.
     *
     * @param intensity the light intensity
     * @param direction the light direction
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }
}