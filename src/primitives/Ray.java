package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;

/**
 * Represents a ray in 3D space, defined by a starting point (head) and a direction.
 */
public class Ray {

    /**
     * A small value used to avoid numerical issues when calculating intersections.
     */
    private static final double DELTA = 0.1;

    /**
     * The starting point (head) of the ray.
     */
    private final Point head;

    /**
     * The direction vector of the ray.
     */
    private final Vector direction;

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

    /**
     * Constructs a Ray with a given starting point, direction vector, and normal vector.
     * The starting point is adjusted slightly in the direction of the normal vector to avoid numerical issues.
     *
     * @param point     the starting point of the ray
     * @param direction the direction vector of the ray
     * @param normal    the normal vector to adjust the starting point
     */
    public Ray(Point point, Vector direction, Vector normal) {
        this(point.add(normal.scale(direction.dotProduct(normal) > 0 ? DELTA : -DELTA)), direction);
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

    /**
     * Returns the starting point (head) of the ray.
     *
     * @return the starting point
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return the direction vector
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a point on the ray at a distance t from the starting point (head).
     *
     * @param t the distance from the head
     * @return the point on the ray at distance t
     */
    public Point getPoint(double t) {
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException e) {
            return head;
        }
    }

    /**
     * Finds the closest point from a list of points to the ray's head.
     *
     * @param points the list of points to search
     * @return the closest point to the ray's head, or null if the list is empty
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Finds the closest intersection point from a list of intersections to the ray's head.
     *
     * @param intersections the list of intersections to search
     * @return the closest intersection point to the ray's head, or null if the list is empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null) return null;

        Intersection closestIntersection = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Intersection intersection : intersections) {
            double distance = head.distanceSquared(intersection.point);
            if (distance < minDistance) {
                minDistance = distance;
                closestIntersection = intersection;
            }
        }

        return closestIntersection;
    }
}

