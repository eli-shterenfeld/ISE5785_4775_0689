package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class cylinder extends Tube{
    private final double height;

    public cylinder(double radius, Ray axis, double height) {
        super(radius,axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
