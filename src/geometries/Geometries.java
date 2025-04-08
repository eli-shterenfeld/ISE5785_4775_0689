package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() {

    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {

        this.geometries.addAll(List.of(geometries));
    }

    public List<Point> findIntersections(Ray ray) {

        /*List<Point> intersections = new ArrayList<>(); // A mutable list to store intersections

        for (Intersectable geometry : geometries) {
            List<Point> intersectionPoints = geometry.findIntersections(ray);
            if (intersectionPoints != null) {
                intersections.addAll(intersectionPoints); // Use addAll to combine points
            }
        }

        return intersections; // Return the mutable list of intersections*/












        List<Point> L = null;
        List<Point> found = null;

        if(geometries.isEmpty())
            return null;

        for(Intersectable item : geometries) {

            found = item.findIntersections(ray);
            if(found != null)
            {
                if (L == null)
                    L = new ArrayList<>(found);
                else
                    L.addAll(new ArrayList<>(found));
            }
            found = null;
        }
        return L;
    }

}
