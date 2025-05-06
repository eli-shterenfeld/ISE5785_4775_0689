package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracers.
 * A ray tracer is responsible for calculating the color of a ray
 * as it intersects with the scene.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered using this ray tracer.
     * This field is protected and final, meaning it can be accessed by subclasses,
     * but cannot be reassigned after initialization.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer with the given scene.
     *
     * @param scene the scene to associate with this ray tracer
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Calculates the color resulting from a ray intersecting the scene.
     *
     * @param ray the ray for which to compute the color
     * @return the color computed for the given ray
     */
    public abstract Color traceRay(Ray ray);
}