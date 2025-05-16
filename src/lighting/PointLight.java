package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Point light source - emits light from a point in space.
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the point light in 3D space.
     */
    protected final Point position;

    /**
     * The constant attenuation factor.
     */
    private double kC = 1;

    /**
     * The linear attenuation factor.
     */
    private double kL = 0;

    /**
     * The quadratic attenuation factor.
     */
    private double kQ = 0;

    /**
     * Constructor for PointLight.
     *
     * @param intensity the light intensity
     * @param position  the light position
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Builder pattern - set constant attenuation.
     *
     * @param kC constant factor
     * @return the point light
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Builder pattern - set linear attenuation.
     *
     * @param kL linear factor
     * @return the point light
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Builder pattern - set quadratic attenuation.
     *
     * @param kQ quadratic factor
     * @return the point light
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double attenuation = 1.0 / (kC + kL * d + kQ * d * d);
        return intensity.scale(attenuation);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize(); // FROM light TO point
    }
}