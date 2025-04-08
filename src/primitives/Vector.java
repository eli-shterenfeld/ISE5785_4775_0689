package primitives;

/**
 * Represents a vector in 3D space, extending the Point class.
 * A vector has both a direction and a magnitude but no fixed position.
 */
public class Vector extends Point {

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
}

