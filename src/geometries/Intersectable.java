package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

//להוסיף java doc
public interface Intersectable {

    //להוסיף java doc
     List<Point> findIntersections(Ray ray);


}
