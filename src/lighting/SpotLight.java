package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Spot light source - emits light in a specific direction with cone shape.
 */
public class SpotLight extends PointLight {

    /**
     * Direction of the light beam.
     */
    private final Vector direction;

    /**
     * Narrowing factor for the light beam.
     */
    private double narrowBeam = 1;

    /**
     * Constructor for SpotLight.
     *
     * @param intensity the light intensity
     * @param position  the position of the light
     * @param direction the direction of the light
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }


    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }


    public SpotLight setNarrowBeam(double narrowBeam) {
        if (narrowBeam <= 0)
            throw new IllegalArgumentException("Narrow beam must be positive");
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p);
        double factor = Math.max(0, direction.dotProduct(l));
        if (narrowBeam != 1)
            factor = Math.pow(factor, narrowBeam);
        return super.getIntensity(p).scale(factor);
    }

}