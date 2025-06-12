package renderer;

import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Interface for sampling rays around a base ray, typically used for anti-aliasing or soft shadows.
 */
public interface RaySampler {

    /**
     * Samples rays around a base ray.
     *
     * @param baseRay  the central ray (e.g., refraction or reflection)
     * @param normal   the surface normal at the point of intersection
     * @param radius   the maximum deviation angle
     * @param distance the distance from the intersection point to the screen
     * @param count    number of rays to generate
     * @return list of rays deviated from the base direction
     */
    List<Ray> sample(Ray baseRay, Vector normal, double radius, double distance, int count);
}
