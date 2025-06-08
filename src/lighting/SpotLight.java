package lighting;

import primitives.*;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;

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

    /**
     * Builder pattern - set the narrow beam factor.
     *
     * @param narrowBeam the narrow beam factor
     * @return the spot light
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        if (narrowBeam <= 0)
            throw new IllegalArgumentException("Narrow beam must be positive");
        this.narrowBeam = narrowBeam;
        return this;
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

    @Override
    public Color getIntensity(Point p) {
        double factor = alignZero(direction.dotProduct(getL(p)));
        if (factor <= 0) return Color.BLACK;
        return super.getIntensity(p).scale(narrowBeam == 1 ? factor : pow(factor, narrowBeam));
    }
}