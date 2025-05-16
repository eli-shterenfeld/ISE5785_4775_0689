package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A simple ray tracer implementation that extends RayTracerBase.
 * Implements basic color computation for rays intersecting a scene.
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
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null
                ? scene.background
                : calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection object
     * @param ray          the ray that hit the object
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // Preprocess data: set normal, ray direction, dot product
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK; // No local effects → return black
        // Start with ambient light + emission
        return scene.ambientLight.getIntensity().scale(intersection.material.kA).add(calcColorLocalEffects(intersection));
    }

    /**
     * Preprocesses intersection data before local effects calculation.
     *
     * @param intersection    the intersection object
     * @param RayIntersection the ray direction vector
     * @return true if local effects can be calculated, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector RayIntersection) {
        // Save ray direction
        intersection.rayDirection = RayIntersection.normalize();
        // Calculate normal at intersection point
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        // Compute dot product between ray direction and normal
        intersection.dotProductRayNormal = intersection.rayDirection.dotProduct(intersection.normal);

        // If dot is 0, the ray is parallel to the surface → no local effects
        return !isZero(intersection.dotProductRayNormal);
    }

    /**
     * Sets the light source data in the intersection object.
     *
     * @param intersection the intersection object (for current hit)
     * @param lightSource  the light source being considered
     * @return false if both dot products are zero (no contribution), true otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource lightSource) {
        // Set the light source
        intersection.lightSource = lightSource;
        // Calculate light direction from light source to intersection point
        intersection.lightDirection = lightSource.getL(intersection.point).normalize();
        // Calculate dot product of normal and light direction
        intersection.nl = intersection.normal.dotProduct(intersection.lightDirection);
        // If dot product is 0, the light source is parallel to the surface → no contribution
        return alignZero(intersection.dotProductRayNormal * intersection.nl) > 0;
    }

    /**
     * Calculates the local lighting effects (diffusive + specular) at the intersection point.
     *
     * @param intersection the intersection object
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color result = intersection.geometry.getEmission();

        for (var light : scene.lights) {
            if (setLightSource(intersection, light))
                result = result.add(light.getIntensity(intersection.point).scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
        }

        return result;
    }

    /**
     * Calculates the specular component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the specular component as Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        // Calculate reflection vector R = L - 2 * (N·L) * N
        Vector r = intersection.lightDirection.subtract(intersection.normal.scale(2 * intersection.nl)).normalize();
        // Calculate specular component: kS * max(0,-R·V)^nShininess
        return intersection.material.kS.scale(Math.pow(Math.max(0, -r.dotProduct(intersection.rayDirection.normalize())), intersection.material.nSh));
    }

    /**
     * Calculates the diffusive component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the diffusive component as Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.nl));
    }
}