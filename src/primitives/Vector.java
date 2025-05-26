package primitives;

import static primitives.Util.alignZero;

/**
 * Represents a vector in 3D space, extending the Point class.
 * A vector has both a direction and a magnitude but no fixed position.
 */
public class Vector extends Point {

    /**
     * The unit vector in the direction of the X-axis (1, 0, 0).
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * The unit vector in the direction of the Y-axis (0, 1, 0).
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * The unit vector in the direction of the Z-axis (0, 0, 1).
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Constructs a Vector with given coordinates.
     * Throws an IllegalArgumentException if the vector is a zero vector.
     *
     * @param d1 the x-coordinate of the vector
     * @param d2 the y-coordinate of the vector
     * @param d3 the z-coordinate of the vector
     */
    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero");
    }

    /**
     * Constructs a Vector using a Double3 object.
     * Throws an IllegalArgumentException if the vector is a zero vector.
     *
     * @param double3 a Double3 object representing the vector's coordinates
     */
    public Vector(Double3 double3) {
        super(double3);
        if (double3.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero");
    }

    /**
     * Adds the current vector to another vector, returning the resulting vector.
     *
     * @param v the vector to add
     * @return the resulting vector after addition
     */
    public Vector add(Vector v) {
        return new Vector(v.xyz.add(this.xyz));
    }

    /**
     * Scales the current vector by a scalar value, returning the resulting vector.
     *
     * @param d the scalar to multiply the vector by
     * @return the resulting scaled vector
     */
    public Vector scale(double d) {
        return new Vector(this.xyz.scale(d));
    }

    /**
     * Computes the dot product of the current vector with another vector.
     *
     * @param v the vector to compute the dot product with
     * @return the dot product of the two vectors
     */
    public double dotProduct(Vector v) {
        return v.xyz.d1() * this.xyz.d1() + v.xyz.d2() * this.xyz.d2() + v.xyz.d3() * this.xyz.d3();
    }

    /**
     * Computes the cross product of the current vector with another vector.
     *
     * @param v the vector to compute the cross product with
     * @return the resulting vector from the cross product
     */
    public Vector crossProduct(Vector v) {
        return new Vector(this.xyz.d2() * v.xyz.d3() - v.xyz.d2() * this.xyz.d3(),
                -this.xyz.d1() * v.xyz.d3() + v.xyz.d1() * this.xyz.d3(),
                this.xyz.d1() * v.xyz.d2() - v.xyz.d1() * this.xyz.d2());
    }

    /**
     * Computes the squared length (magnitude squared) of the vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector to have a length of 1, returning the resulting vector.
     * Throws an exception if the length is zero.
     *
     * @return the normalized vector
     */
    public Vector normalize() {// check if this is ok
        return new Vector(this.xyz.reduce(length()));
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.xyz.equals(other.xyz);
    }

    /**
     * Rotates the vector around a given axis by a specified angle (in radians).
     *
     * @param axis  the axis to rotate around
     * @param angle the angle in radians to rotate
     * @return the rotated vector
     */
    /**
     * Rotates this vector around a given axis by a specified angle (in radians),
     * using Rodrigues' rotation formula.
     *
     * @param axis  the axis to rotate around (must not be zero vector)
     * @param angle the angle to rotate, in radians
     * @return the rotated vector
     */
    public Vector rotateAroundAxis(Vector axis, double angle) {
        if (this == null || alignZero(this.lengthSquared()) == 0)
            throw new IllegalStateException("Cannot rotate the zero vector (this is null or zero)");

        if (axis == null || alignZero(axis.lengthSquared()) == 0)
            throw new IllegalArgumentException("Cannot rotate around a zero axis");

        axis = axis.normalize();

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double dot = this.dotProduct(axis);

        Vector part1 = (alignZero(cos) == 0) ? null : this.scale(cos);
        Vector part2 = (alignZero(sin) == 0) ? null : axis.crossProduct(this).scale(sin);
        Vector part3 = (alignZero(dot * (1 - cos)) == 0) ? null : axis.scale(dot * (1 - cos));

        Vector rotated = null;

        if (part1 != null) rotated = part1;
        if (part2 != null) rotated = (rotated == null) ? part2 : rotated.add(part2);
        if (part3 != null) rotated = (rotated == null) ? part3 : rotated.add(part3);
        return rotated;
    }
}

