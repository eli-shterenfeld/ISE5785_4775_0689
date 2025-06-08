package geometries;

import primitives.*;

/**
 * Abstract class representing a geometric shape.
 */
abstract public class Geometry extends Intersectable {

    /**
     * Emission color of the geometry (glow/light source).
     */
    protected Color emission = Color.BLACK;

    /**
     * Material properties of the geometry.
     */
    private Material material = new Material();

    /**
     * Computes the normal vector at a given point on the geometry.
     *
     * @param p the point on the geometry
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);

    /**
     * Getter for emission color.
     *
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Setter for emission color (Builder pattern).
     *
     * @param emission the new emission color
     * @return the current geometry object (for chaining)
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter for material properties.
     *
     * @return the material properties of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter for material properties (Builder pattern).
     *
     * @param material the new material properties
     * @return the current geometry object (for chaining)
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}