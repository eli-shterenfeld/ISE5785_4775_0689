package primitives;

/**
 * Class representing material properties of a geometry object.
 * Used for controlling light behavior in the rendering process.
 */
public class Material {

    /**
     * Glossiness radius for specular reflection.
     * If set to 0, the material is perfectly shiny.
     * If greater than 0, it simulates a glossy surface with a certain radius.
     */
    public double glossinessRadius = 0;

    /**
     * Distance for glossy reflection.
     * Determines how far the glossy effect extends.
     * A value of 1 means the glossy effect is applied at the intersection point.
     */
    public double glossinessDistance = 1;

    /**
     * Number of rays to be used for glossy reflection.
     * Determines the quality of the glossy effect.
     */
    public int glossinessRays = 1;

    /**
     * Reflection coefficient.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Transparency coefficient.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Ambient reflection coefficient.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Shininess coefficient for specular highlight.
     */
    public int nSh = 0;

    /**
     * Empty default constructor.
     */
    public Material() {
    }

    /**
     * Setter for ambient reflection coefficient.
     *
     * @param kA the coefficient
     * @return the current material object
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for ambient reflection coefficient.
     *
     * @param kA the coefficient
     * @return the current material object
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Setter for diffuse reflection coefficient.
     *
     * @param kD the coefficient
     * @return the current material object
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for diffuse reflection coefficient.
     *
     * @param kD the coefficient
     * @return the current material object
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for specular reflection coefficient.
     *
     * @param kS the coefficient
     * @return the current material object
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for specular reflection coefficient.
     *
     * @param kS the coefficient
     * @return the current material object
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for shininess.
     *
     * @param nSh shininess value
     * @return the current material object
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

    /**
     * Setter for reflection coefficient.
     *
     * @param kT the coefficient
     * @return the current material object
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for reflection coefficient.
     *
     * @param kT the coefficient
     * @return the current material object
     */
    public Material setKT(Double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Setter for reflection coefficient.
     *
     * @param kR the coefficient
     * @return the current material object
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Setter for reflection coefficient.
     *
     * @param kR the coefficient
     * @return the current material object
     */
    public Material setKR(Double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the glossiness properties of the material.
     *
     * @param radius the radius for glossiness
     * @param rays   the number of rays for glossiness
     * @return the current material object
     */
    public Material setGlossiness(double radius, double distance, int rays) {
        this.glossinessRadius = radius <= 0 ? 0 : radius;
        this.glossinessRays = rays <= 0 ? 1 : rays;
        this.glossinessDistance = distance <= 0 ? 1 : distance;
        return this;
    }
}