package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * A simple ray tracer implementation that extends RayTracerBase.
 * This class will eventually implement basic color computation for rays intersecting a scene.
 * For now, the traceRay method is not implemented and throws an exception.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Not implemented yet. This method will compute the color for a given ray in the future.
     *
     * @param ray the ray to trace
     * @return not applicable yet
     * @throws UnsupportedOperationException always, since not implemented yet
     */
    @Override
    public Color traceRay(Ray ray) {
        // Find intersections with the geometries in the scene
        List<Point> intersections = scene.geometries.findIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            // No intersections, return background color
            return scene.background;
        }

        // Find closest intersection point to the ray origin
        Point closestPoint = ray.findClosestPoint(intersections);

        // Return the color at the closest point (currently ambient only)
        return calcColor(closestPoint);
    }

    /**
     * Calculate the color at a given point (currently only ambient light).
     *
     * @param point the intersection point (not used at this stage)
     * @return the ambient light color from the scene
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}