package primitives;

/**
 * Represents a point in 3D space, defined by three coordinates (x, y, z).
 */
public class Point {

    /**
     * The coordinates of the point.
     */
    protected Double3 xyz;

    /**
     * A constant representing the point (0, 0, 0).
     */
    public static Point ZERO = new Point(0.0, 0.0, 0.0);

    /**
     * Constructs a Point with given coordinates.
     *
     * @param d1 the x-coordinate of the point
     * @param d2 the y-coordinate of the point
     * @param d3 the z-coordinate of the point
     */
    public Point(double d1, double d2, double d3) {
        xyz = new Double3(d1, d2, d3);
    }

    /**
     * Constructs a Point using a Double3 object.
     *
     * @param d a Double3 object representing the coordinates
     */
    public Point(Double3 d) {
        xyz = d;
    }

    /**
     * Subtracts a given point from this point to get the resulting vector.
     *
     * @param p the point to subtract
     * @return the resulting vector from the subtraction
     */
    public Vector subtract(Point p) {
        return new Vector(xyz.subtract(p.xyz));
    }

    /**
     * Adds a given vector to this point, returning a new point.
     *
     * @param v the vector to add
     * @return the resulting point after addition
     */
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }

    /**
     * Computes the squared distance from this point to another point.
     *
     * @param p the point to measure the distance to
     * @return the squared distance between this point and the given point
     */
    public double distanceSquared(Point p) {
        return (this.xyz.d1() - p.xyz.d1()) * (this.xyz.d1() - p.xyz.d1()) +
                (this.xyz.d2() - p.xyz.d2()) * (this.xyz.d2() - p.xyz.d2()) +
                (this.xyz.d3() - p.xyz.d3()) * (this.xyz.d3() - p.xyz.d3());
    }

    /**
     * Computes the distance from this point to another point.
     *
     * @param p the point to measure the distance to
     * @return the distance between this point and the given point
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }


    @Override
    public String toString() {
        return xyz.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && xyz.equals(other.xyz);
    }
}

