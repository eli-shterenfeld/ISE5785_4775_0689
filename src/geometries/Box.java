package geometries;

import primitives.*;

public class Box {

    /**
     * The minimum corner point of the bounding box.
     */
    private final Point min;

    /**
     * The maximum corner point of the bounding box.
     */
    private final Point max;

    public Box(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Checks if a ray intersects the bounding box using an optimized slab method.
     *
     * @param ray the ray to test for intersection
     * @return true if the ray intersects the bounding box, false otherwise
     */
    public boolean intersect(Ray ray, double maxDistance) {
        Point head = ray.getHead();
        Vector dir = ray.getDirection();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        for (int axis = 0; axis < 3; axis++) {
            double axisDir = dir.get(axis);
            double axisOrigin = head.get(axis);
            double axisMin = min.get(axis);
            double axisMax = max.get(axis);

            // If ray is (almost) parallel to the axis
            if (Math.abs(axisDir) < 1e-15) {
                if (axisOrigin < axisMin || axisOrigin > axisMax) {
                    return false;
                }
                continue;
            }

            double t1 = (axisMin - axisOrigin) / axisDir;
            double t2 = (axisMax - axisOrigin) / axisDir;

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax + 1e-10) {
                return false;
            }
        }

        // We intersected the box; now check if it's within the max distance
        return tMin <= maxDistance && tMax >= 0 && tMax <= maxDistance;
    }

    /**
     * Checks if a point is inside the bounding box.
     *
     * @return true if the point is inside the bounding box, false otherwise
     */
    public Point getMin() {
        return min;
    }

    /**
     * Returns the maximum corner point of the bounding box.
     *
     * @return the maximum corner point
     */
    public Point getMax() {
        return max;
    }
}

