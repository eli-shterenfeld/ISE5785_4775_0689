package renderer;

import geometries.Intersectable.Intersection;
import lighting.AreaLight;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.*;

/**
 * A simple ray tracer implementation that extends RayTracerBase.
 * Implements basic color computation for rays intersecting a scene.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * RaySampler instance used for generating rays with jittered disk sampling.
     * This sampler is used for glossy reflections and refractions.
     */
    private static final RaySampler raySampler = new JitterDiskSampler();

    /**
     * Maximum recursion level for color calculation.
     * Limits the depth of recursive calls to prevent infinite recursion.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum value for color components to be considered significant.
     * If the attenuation factor is lower than this value, the color is considered negligible.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial attenuation factor for color components.
     * Used as a starting point for recursive color calculations.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        // Find the closest intersections with the geometries in the scene
        var intersection = findClosestIntersection(ray);
        return intersection == null ? scene.background : calcColor(intersection, ray);
    }

    /**
     * Finds the closest intersection between a ray and the scene geometries.
     *
     * @param ray the ray to find intersections with
     * @return the closest intersection or null if none found
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return ray.findClosestIntersection(intersections);
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
        return preprocessIntersection(intersection, ray.getDirection()) ?
                (calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K))
                        .add(scene.ambientLight.getIntensity().scale(intersection.material.kA)) : // Start with ambient light + emission
                Color.BLACK; // No local effects → return black
    }

    /**
     * Calculates the color at the intersection point with recursive depth limiting.
     *
     * @param intersection the intersection object
     * @param level        the recursive level (decreasing with each call)
     * @param k            attenuation factor for each color component
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection);
        return level == 1 || k.lowerThan(MIN_CALC_COLOR_K) ? color : color.add(calcGlobalEffects(intersection, level - 1, k));
    }

    /**
     * Calculates the global effects (reflection and refraction) at the intersection point.
     * This method is used when glossiness is not applied.
     *
     * @param intersection the intersection object
     * @param level        the recursive level (decreasing with each call)
     * @param k            attenuation factor for each color component
     * @return the color contribution from global effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Material m = intersection.material;
        return traceBeamAverage(constructReflectedRay(intersection), intersection.normal, m, m.kR, level, k)
                .add(traceBeamAverage(constructRefractedRay(intersection), intersection.normal.scale(-1), m, m.kT, level, k));
    }

    /**
     * Traces a beam of rays for glossy reflection or refraction.
     * This method averages the color contributions from multiple rays.
     *
     * @param baseRay  the base ray (reflection or refraction)
     * @param normal   the surface normal at the intersection point
     * @param material the material of the intersected geometry
     * @param kX       attenuation factor for the current color component
     * @param level    the recursive level (decreasing with each call)
     * @param k        attenuation factor for each color component
     * @return the average color contribution from glossy effects
     */
    private Color traceBeamAverage(Ray baseRay, Vector normal, Material material, Double3 kX, int level, Double3 k) {
        if (material.glossinessRadius <= 0 || material.glossinessRays <= 1)
            return calcGlobalEffect(baseRay, level, k, kX);

        var rayList = raySampler.sample(
                baseRay, normal, material.glossinessRadius,
                material.glossinessDistance, material.glossinessRays
        );

        Color sum = Color.BLACK;
        for (Ray r : rayList)
            sum = sum.add(calcGlobalEffect(r, level, k, kX));
        return rayList.isEmpty() ? calcGlobalEffect(baseRay, level, k, kX) : sum.reduce(rayList.size());
    }

    /**
     * Constructs a reflected ray based on the intersection data.
     *
     * @param intersection the intersection object
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.rayDirection.subtract(intersection.normal.scale(2 * intersection.dotProductRayNormal));
        return new Ray(intersection.point, r, intersection.normal);
    }

    /**
     * Constructs a refracted ray based on the intersection data.
     *
     * @param intersection the intersection object
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.rayDirection, intersection.normal);
    }

    /**
     * Calculates the global effect of light on the intersection point.
     *
     * @param ray   the ray to trace
     * @param level the recursive level (decreasing with each call)
     * @param k     attenuation factor for each color component
     * @param kx    attenuation factor for the current color component
     * @return the color contribution from global effects
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        var kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        var intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);

        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
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
        intersection.nl = intersection.normal.dotProduct(intersection.lightDirection);  // Calculate dot product of normal and light direction
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
            if (!setLightSource(intersection, light)) continue; // No contribution from this light source

            result = result.add(light.getIntensity(intersection.point).scale(calcDiffusive(intersection)
                    .add(calcSpecular(intersection))).scale(transparency(intersection)));
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

    /**
     * Checks if the intersection point is unshaded by other objects.
     *
     * @param intersection the intersection data
     * @return true if unshaded, false otherwise
     */
    @SuppressWarnings("unused")
    private boolean unshaded(Intersection intersection) {
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.lightSource.getL(intersection.point).scale(-1),
                intersection.normal
        );
        var intersections = scene.geometries.calculateIntersections(shadowRay, intersection.lightSource.getDistance(intersection.point));
        if (intersections == null) return true;

        for (var i : intersections) {
            if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                return false;
        }
        return true;
    }

    /**
     * Calculates the transparency of the intersection point.
     *
     * @param intersection the intersection data
     * @return the transparency factor as Double3
     */
//    private Double3 transparency(Intersection intersection) {
//        if (intersection.lightSource.getShadowRayCount() > 1) return transparencyWithManyRays(intersection);
//
//        Ray shadowRay = new Ray(
//                intersection.point,
//                intersection.lightSource.getL(intersection.point).scale(-1),
//                intersection.normal
//        );
//
//        var shadowIntersections = scene.geometries.calculateIntersections(
//                shadowRay,
//                intersection.lightSource.getDistance(intersection.point)
//        );
//
//        var ktr = Double3.ONE;
//        if (shadowIntersections == null) return ktr;
//
//        for (var shadowI : shadowIntersections) {
//            ktr = ktr.product(shadowI.material.kT);
//            if (ktr.lowerThan(MIN_CALC_COLOR_K))
//                return Double3.ZERO;
//        }
//        return ktr;
//    }

// Fixed transparency methods

    /**
     * Calculates the transparency of the intersection point.
     * Automatically chooses between hard and soft shadow transparency
     * based on the light source type and properties.
     *
     * @param intersection the intersection data
     * @return the transparency factor as Double3
     */
    private Double3 transparency(Intersection intersection) {
        return supportsAreaLighting(intersection.lightSource)
                ? transparencySoft(intersection)
                : transparencyHard(intersection);
    }

    /**
     * Determines if a light source supports area lighting (soft shadows).
     *
     * @param light the light source to check
     * @return true if the light supports soft shadows with multiple rays
     */
    private boolean supportsAreaLighting(LightSource light) {
        return light instanceof AreaLight areaLight
                && areaLight.getRadius() > 0
                && areaLight.getShadowRayCount() > 1;
    }

    /**
     * Hard shadow transparency calculation (original logic).
     * Uses a single shadow ray toward the light center.
     *
     * @param intersection the intersection data
     * @return the transparency factor as Double3
     */
    private Double3 transparencyHard(Intersection intersection) {
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.lightSource.getL(intersection.point).scale(-1),
                intersection.normal
        );
        return calculateRayTransparency(shadowRay, intersection.lightSource.getDistance(intersection.point));
    }

    /**
     * Soft shadow transparency calculation using beam sampling.
     * Samples multiple rays toward the light's disk area and averages the results.
     *
     * @param intersection the intersection data
     * @return the transparency factor as Double3
     */
    private Double3 transparencySoft(Intersection intersection) {
        LightSource light = intersection.lightSource;

        // Assumes caller verified that light is an AreaLight (see supportsAreaLighting)
        AreaLight areaLight = (AreaLight) light;

        // Create base shadow ray toward light center
        Ray baseShadowRay = new Ray(
                intersection.point,
                light.getL(intersection.point).scale(-1),
                intersection.normal);

        // Sample rays toward the light's disk area
        var shadowRays = raySampler.sample(
                baseShadowRay,
                intersection.normal,
                areaLight.getRadius(),
                light.getDistance(intersection.point),
                areaLight.getShadowRayCount()
        );

        // Average transparency across all shadow rays
        Double3 totalTransparency = Double3.ZERO;
        for (Ray shadowRay : shadowRays) {
            totalTransparency = totalTransparency.add(
                    calculateRayTransparency(shadowRay, light.getDistance(intersection.point))
            );
        }
        return shadowRays.isEmpty() ? Double3.ONE : totalTransparency.reduce(shadowRays.size());
    }

    /**
     * Calculates transparency for a single shadow ray.
     * Extracted helper method to eliminate code duplication between hard and soft methods.
     *
     * @param shadowRay   the shadow ray to test
     * @param maxDistance the maximum distance to test for intersections
     * @return the transparency factor as Double3
     */
    private Double3 calculateRayTransparency(Ray shadowRay, double maxDistance) {
        var shadowIntersections = scene.geometries.calculateIntersections(
                shadowRay,
                maxDistance
        );
        return calculateTransparencyFromIntersections(shadowIntersections);
    }

    /**
     * Calculates the final transparency value from a list of shadow intersections.
     * Common logic extracted from both hard and soft transparency calculations.
     *
     * @param shadowIntersections the list of intersections along the shadow ray
     * @return the transparency factor as Double3
     */
    private Double3 calculateTransparencyFromIntersections(List<Intersection> shadowIntersections) {
        var ktr = Double3.ONE;
        if (shadowIntersections == null) return ktr;

        for (var shadowI : shadowIntersections) {
            ktr = ktr.product(shadowI.material.kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }

        return ktr;
    }
}