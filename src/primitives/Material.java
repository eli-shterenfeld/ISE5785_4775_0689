package primitives;

/**
 * Class representing material properties of a geometry object.
 * Used for controlling light behavior in the rendering process.
 */
public class Material {

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
}