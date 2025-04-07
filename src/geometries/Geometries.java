package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() {

    }

    public Geometries(Intersectable... geometries) {


    }


    public void add(Intersectable... geometries) {




    }

    public List<Point> findIntersections(Ray ray) {

        List<Point> L = null;

        if(geometries.isEmpty())
            return null;

        for(Intersectable item : geometries) {

            List<Point> found = item.findIntersections(ray);
            if(found != null)
            {

                if (L == null)
                    L = found;
                else
                    L.addAll(found);
            }
            found = null;
        }
        return L;
    }

}
