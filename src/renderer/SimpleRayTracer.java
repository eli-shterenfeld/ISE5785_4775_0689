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

    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray and computes the color for the ray in the scene.
     *
     * @param ray the ray to trace
     * @return the color for the given ray
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
     * Finds the closest intersection between a ray and the scene geometries.
     *
     * @param ray the ray to find intersections with
     * @return the closest intersection or null if none found
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
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
                : scene.ambientLight.getIntensity().add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
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
        Color color = calcColorLocalEffects(intersection).scale(intersection.material.kA);
        return level == 1 || k.lowerThan(MIN_CALC_COLOR_K) ? color : color.add(calcGlobalEffects(intersection, level - 1, k));
    }

    /**
     * Calculates the global effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection object
     * @param level        the recursive level (decreasing with each call)
     * @param k            attenuation factor for each color component
     * @return the color contribution from global effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Ray reflectedRay = constructReflectedRay(intersection);
        Ray refractedRay = constructRefractedRay(intersection);

        return calcGlobalEffect(reflectedRay, level, k, intersection.material.kR)
                .add(calcGlobalEffect(refractedRay, level, k, intersection.material.kT));
    }

    /**
     * Constructs a reflected ray based on the intersection data.
     *
     * @param intersection the intersection object
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDirection;
        Vector n = intersection.normal;
        Vector r = v.subtract(n.scale(2 * intersection.dotProductRayNormal));
        return new Ray(intersection.point, r, n);
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
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background.scale(kx);

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
            if (!setLightSource(intersection, light)) continue; // No contribution from this light source
            //if (!unshaded(intersection)) continue; // Shadowed by another object
            Double3 ktr = transparency(intersection);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                continue;
            result = result.add(light.getIntensity(intersection.point).scale(calcDiffusive(intersection).add(calcSpecular(intersection))).scale(ktr));
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
    private boolean unshaded(Intersection intersection) {
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.lightSource.getL(intersection.point).scale(-1),
                intersection.normal
        );
        var intersections = scene.geometries.calculateIntersections(shadowRay, intersection.lightSource.getDistance(intersection.point));

        if (intersections == null) return true;

        for (var i : intersections) {
            if (i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K))
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
    private Double3 transparency(Intersection intersection) {
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.lightSource.getL(intersection.point).scale(-1),
                intersection.normal
        );

        var shadowIntersections = scene.geometries.calculateIntersections(
                shadowRay,
                intersection.lightSource.getDistance(intersection.point)
        );

        Double3 ktr = Double3.ONE;
        if (shadowIntersections == null) return ktr;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        for (var shadowI : shadowIntersections) {
            double t = shadowI.point.distance(intersection.point);
            if (alignZero(t - lightDistance) < 0) {
                ktr = ktr.product(shadowI.geometry.getMaterial().kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
            }
        }

        return ktr;
    }

}

//    private boolean unshaded(Intersection intersection) {
//        Vector delta = intersection.normal.scale(intersection.dotProductRayNormal > 0 ? -DELTA : DELTA);
//        Ray shadowRay = new Ray(intersection.point.add(delta), intersection.lightSource.getL(intersection.point).scale(-1));
//        return scene.geometries.calculateIntersections(shadowRay, intersection.lightSource.getDistance(intersection.point)) == null;
//    }
