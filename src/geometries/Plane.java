package geometries;
import primitives.*;

public class Plane extends Geometry {

    Point p;
    Vector normal;
    public Plane() {
        super();
    }

    public Plane(Point p1, Point p2, Point p3) {
        super();
        normal = null;
        p = p1;
    }

    public Plane(Point p1, Vector normal) {
        super();
        p = p1;
        this.normal = normal.normalize();
    }

    @Override
    Vector getNormal(Point p) {
        return normal;
    }
}
