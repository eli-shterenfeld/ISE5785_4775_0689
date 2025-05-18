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
        return !preprocessIntersection(intersection, ray.getDirection()) ? Color.BLACK // No local effects → return black
                // Start with ambient light + emission
                : scene.ambientLight.getIntensity().scale(intersection.material.kA).add(calcColorLocalEffects(intersection));
    }

    /**
     * Preprocesses intersection data before local effects calculation.
     *
     * @param intersection    the intersection object
     * @param RayIntersection the ray direction vector
     * @return true if local effects can be calculated, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector RayIntersection) {
        intersection.rayDirection = RayIntersection.normalize();    // Save ray direction
        intersection.normal = intersection.geometry.getNormal(intersection.point);  // Calculate normal at intersection point
        intersection.dotProductRayNormal = intersection.rayDirection.dotProduct(intersection.normal); // Compute dot product between ray direction and normal
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
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point);
        // Calculate dot product of normal and light direction
        intersection.nl = intersection.normal.dotProduct(intersection.lightDirection);
        // If dot(n, l) * dot(n, v) <= 0 → light or viewer are on the wrong side → no contribution
        return alignZero(intersection.dotProductRayNormal) * intersection.nl > 0;
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
            if (!setLightSource(intersection, light))
                continue; // No contribution from this light source
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
        double vr = alignZero(intersection.rayDirection.dotProduct(r));
        return vr >= 0 ? Double3.ZERO
                // Calculate specular component: kS * max(0,-R·V)^nShininess
                : intersection.material.kS.scale(Math.pow(-vr, intersection.material.nSh));
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