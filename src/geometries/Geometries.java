package geometries;

import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

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
     * List of intersectable geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> list = null;
        for (Intersectable item : geometries) {
            List<Intersection> found = item.calculateIntersections(ray);
            if (found != null) {
                if (list == null)
                    list = new LinkedList<>(found);
                else
                    list.addAll(found);
            }
        }
        return list;
    }
}
