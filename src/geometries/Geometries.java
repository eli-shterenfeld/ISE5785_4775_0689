package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
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
public class Geometries implements Intersectable {

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

    /**
     * Adds a single intersectable geometry to the current Geometries object.
     *
     * @param ray for all the geometries
     */
    public List<Point> findIntersections(Ray ray) {

        List<Point> L = null;
        List<Point> found = null;

        if (geometries.isEmpty())
            return null;

        for (Intersectable item : geometries) {

            found = item.findIntersections(ray);
            if (found != null) {
                if (L == null)
                    L = new ArrayList<>(found);
                else
                    L.addAll(found);
            }
        }
        return L;
    }
}
