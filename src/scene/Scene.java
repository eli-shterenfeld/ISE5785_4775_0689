package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Represents a 3D scene containing geometries, ambient light and background color.
 * All fields are public (PDS – Plain Data Structure).
 */
public class Scene {

    /**
     * The name of the scene
     */
    public final String name;

    /**
     * The background color of the scene – default is black
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene – default is AmbientLight.NONE
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The geometries contained in the scene – default is an empty set
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructor accepting only the name of the scene
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Set the background color
     *
     * @param background the background color
     * @return the scene itself (for method chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Set the ambient light
     *
     * @param ambientLight the ambient light
     * @return the scene itself (for method chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Set the geometries
     *
     * @param geometries the geometries to assign
     * @return the scene itself (for method chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}