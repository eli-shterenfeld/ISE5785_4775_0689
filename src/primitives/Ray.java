package primitives;

/**
 * Represents a ray in 3D space, defined by a starting point (head) and a direction.
 */
public class Ray {

    /**
     * The starting point (head) of the ray.
     */
    Point head;

    /**
     * The direction vector of the ray.
     */
    Vector direction;

    /**
     * Constructs a Ray with a given starting point and direction vector.
     *
     * @param p the starting point of the ray
     * @param v the direction vector of the ray
     */
    public Ray(Point p, Vector v) {
        head = p;
        direction = v.normalize();
    }


    @Override
    public String toString() {
        return "Ray:" + head + direction;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && head.equals(other.head) && direction.equals(other.direction);
    }
}

