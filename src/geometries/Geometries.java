package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

/**
 * The {@code Geometries} class represents a collection of multiple {@link Intersectable} objects.
 * <p>
 * It allows grouping several geometries into a single composite structure
 * and provides functionality to find intersection points of a given {@link Ray}
 * with all the contained geometries.
 * <p>
 * The class supports adding geometries dynamically and returns all intersection points,
 * or {@code null} if no intersections are found.
 *
 * @author eli and david
 */
public class Geometries extends Intersectable {

    /**
     * The infinite geometries witch has not had bounding box.
     */
    private final List<Intersectable> infinite = new ArrayList<>();
    /**
     * The bounding box of the geometries.
     */
    private Intersectable accelerationStructure = null;
    /**
     * List of intersectable geometries.
     */
    private List<Intersectable> geometries = new ArrayList<Intersectable>();

    /**
     * Default constructor for the Geometries class.
     */
    public Geometries() {
    }

    /**
     * Constructs a Geometries object with a variable number of intersectable geometries.
     *
     * @param geometries the intersectable geometries to be added
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds a list of intersectable geometries to the current Geometries object.
     *
     * @param geometries the intersectable geometries to be added
     */
    public void add(Intersectable... geometries) {

        this.geometries.addAll(List.of(geometries));
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> list = null;
        if (accelerationStructure != null) {
            for (Intersectable item : infinite) {
                List<Intersection> tmp = item.calculateIntersections(ray, maxDistance);
                if (tmp != null) {
                    if (list == null)
                        list = new ArrayList<>(tmp);
                    else
                        list.addAll(tmp);
                }
            }
            List<Intersection> listBVH = accelerationStructure.calculateIntersections(ray, maxDistance);

            if (listBVH == null)
                return list;

            if (list == null)
                return listBVH;

            list.addAll(listBVH);
            return list;
        }

        // if (accelerationStructure != null) return accelerationStructure.calculateIntersections(ray, maxDistance);

        //List<Intersection> list = null;
        for (Intersectable item : geometries) {
            List<Intersection> found = item.calculateIntersections(ray, maxDistance);
            if (found != null) {
                if (list == null)
                    list = new LinkedList<>(found);
                else
                    list.addAll(found);
            }
        }
        return list;
    }

    @Override
    public void setBoundingBox() {
        if (geometries.isEmpty()) {
            this.box = null;
            return;
        }

        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : geometries) {
            geo.setBoundingBox();
            AABB b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin(), max = b.getMax();
            if (min.getX() < minX) minX = min.getX();
            if (min.getY() < minY) minY = min.getY();
            if (min.getZ() < minZ) minZ = min.getZ();

            if (max.getX() > maxX) maxX = max.getX();
            if (max.getY() > maxY) maxY = max.getY();
            if (max.getZ() > maxZ) maxZ = max.getZ();
        }

        this.box = new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Builds a BVH acceleration structure over the current geometries.
     */
    public void buildBVH() {
        setBoundingBox();
        for (Intersectable g : geometries) {
            if (g.getBoundingBox() == null)
                infinite.add(g);
        }
        this.accelerationStructure = BVHNode.buildFrom(geometries);
        this.box = AABB.createInfiniteBoundingBox();
        geometries.clear(); // optional, to free memory or mark as transferred
    }
}