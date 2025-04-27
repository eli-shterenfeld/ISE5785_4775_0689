package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Composite class for managing a collection of {@link Intersectable} geometries.
 * <p>
 * Supports adding geometries and finding intersections of a ray with all contained geometries.
 *
 * @author eli and david
 */
public interface Intersectable {

    /**
     * Finds the intersection points between the given ray and the geometry.
     * Returns a list of intersection points. If there are no intersections, returns {@code null}.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or {@code null} if no intersections occur
     */
    List<Point> findIntersections(Ray ray);
}
