package geometries;

import lighting.LightSource;
import primitives.*;

import java.util.List;

/**
 * Abstract class for all geometric objects that can be intersected by a ray.
 */
public abstract class Intersectable {

    /**
     * Passive Data Structure (PDS) to contain intersection information.
     */
    public static class Intersection {
        /**
         * The intersected geometry object
         */
        public final Geometry geometry;

        /**
         * The intersection point
         */
        public final Point point;

        /**
         * The material of the geometry
         */
        public final Material material;

        /**
         * The normal at the intersection point
         */
        public Vector normal;

        /**
         * The ray direction
         */
        public Vector rayDirection;

        /**
         * The dot product of the ray direction and the normal
         */
        public double dotProductRayNormal;

        /**
         * The light direction
         */
        public Vector lightDirection;

        /**
         * The dot product of the light direction and the normal
         */
        public double nl;

        /**
         * The distance from the ray origin to the intersection point (not final)
         */
        public double distance;

        /**
         * The light source that generated the ray, if any (not final)
         */
        public LightSource lightSource;

        /**
         * Constructor to initialize all fields.
         *
         * @param geometry the intersected geometry
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = (geometry == null) ? null : geometry.getMaterial();
        }

        @Override
        public String toString() {
            return "Intersection{" + geometry + "," + point + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other) && geometry == other.geometry && point.equals(other.point);
        }
    }

    /**
     * Template method (NVI pattern).
     * This is the main public method to find intersection points only.
     *
     * @param ray the ray
     * @return list of intersection points or null
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null :
                list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Public method to calculate intersections with unlimited distance.
     *
     * @param ray the ray
     * @return list of Intersection objects or null
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Public method to calculate intersections up to a given max distance.
     *
     * @param ray         the ray
     * @param maxDistance maximum distance to consider
     * @return list of Intersection objects or null
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Subclasses must implement this helper to find intersections up to a max distance.
     *
     * @param ray         the ray
     * @param maxDistance maximum distance from ray origin
     * @return list of Intersection objects or null
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);
}
