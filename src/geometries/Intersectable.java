package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
            this.material = (geometry != null) ? geometry.getMaterial() : null;
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    ", material=" + material +
                    ", normal=" + normal +
                    ", distance=" + distance +
                    ", lightSource=" + lightSource +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Intersection that = (Intersection) o;
            return geometry == that.geometry && point.equals(that.point);
        }
    }

    /**
     * Template method (NVI pattern).
     * This is the main public method to find intersections.
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
     * Secondary NVI method.
     * Calls the helper which each subclass must implement.
     *
     * @param ray the ray
     * @return list of Intersection objects or null
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Subclasses must implement this helper to find intersections.
     *
     * @param ray the ray
     * @return list of Intersection objects or null
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
}
