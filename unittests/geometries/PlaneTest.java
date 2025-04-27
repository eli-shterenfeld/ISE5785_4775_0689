package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Plane} class.
 *
 * @author eli and david
 */
class PlaneTest {

    /**
     * A sample plane used for testing.
     */
    final Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)} constructor.
     * This test verifies that the constructor correctly calculates the normal vector
     * and throws exceptions for invalid input cases.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001; // Allowed margin of error for floating-point comparisons

        // TC01: Define three non-collinear points
        Point p1 = new Point(2, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);

        // TC02: Compute vectors on the plane
        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);

        // TC03: Create the plane
        Plane pl = new Plane(p1, p2, p3);

        // TC04: Check that the normal is a unit vector
        assertEquals(1, pl.getNormal(p1).length(), DELTA);

        // TC05: Check that the normal is perpendicular to vector v1
        assertEquals(0, pl.getNormal(p1).dotProduct(v1));

        // TC06: Check that the normal is perpendicular to vector v2
        assertEquals(0, pl.getNormal(p1).dotProduct(v2));

        // =============== Boundary Values Tests ==================

        // TC07: Two identical points (points 1 and 2 identical)
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(2, 0, 0), new Point(2, 0, 0), new Point(0, 0, 1)),
                "Constructed a plane with two identical points, 1 and 2");

        // TC08: Two identical points (points 1 and 3 identical)
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(2, 0, 0), new Point(0, 1, 0), new Point(2, 0, 0)),
                "Constructed a plane with two identical points, 1 and 3");

        // TC09: Two identical points (points 2 and 3 identical)
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(2, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a plane with two identical points, 2 and 3");

        // TC10: Three identical points
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 0, 0), new Point(1, 0, 0), new Point(1, 0, 0)),
                "Constructed a plane with three identical points");

        // TC11: All points on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0)),
                "Constructed a plane with all points on one line");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     * This test checks that the normal vector of the plane is correct and normalized.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001; // Allowed margin of error for floating-point comparisons

        //Define three non-collinear points to construct the plane
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(-1, 0, 0);

        // Create the plane
        Plane pl = new Plane(p1, p2, p3);

        // Define possible normal directions
        Vector normal = new Vector(0, 0, 1);
        Vector reversedNormal = new Vector(0, 0, -1);

        // TC01: Check that the calculated normal matches one of the expected directions
        assertTrue(normal.equals(pl.getNormal(p1)) || reversedNormal.equals(pl.getNormal(p1)), "incorrect normal");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     * This test verifies the intersection points of a ray with the plane.
     */
    @Test
    public void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is not parallel nor orthogonal and intersects
        Ray ray = new Ray(new Point(0, 1, 0), new Vector(0, 1, 1));
        assertEquals(new Point(0, 2, 1), plane.findIntersections(ray).getFirst(), "Ray should intersect the plane");

        // TC02: Ray is not parallel nor orthogonal and does not intersect
        ray = new Ray(new Point(0, 1, 0), new Vector(0, -1, -1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line is parallel or orthogonal to the plane
        // TC03: "Ray is parallel to the plane
        ray = new Ray(new Point(0, 1, 0), new Vector(0, 1, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC04: Ray contained in the plane
        ray = new Ray(new Point(0, 1, 1), new Vector(0, 1, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // **** Group 2: Ray's line is orthogonal to the plane
        // TC05: Ray is orthogonal and starts after the plane
        ray = new Ray(new Point(0, 1, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC06: Ray is orthogonal and starts in the plane
        ray = new Ray(new Point(0, 1, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC07: Ray is orthogonal and starts before the plane
        ray = new Ray(new Point(0, 1, 0), new Vector(0, 0, 1));
        assertEquals(new Point(0, 1, 1), plane.findIntersections(ray).getFirst(), "incorrect intersections");

        // **** Group 3: Ray is not parallel nor orthogonal but starts in the plane
        // TC05: Ray contained in the plane
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 1, 1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC08: Ray contained in the plane
        ray = new Ray(new Point(0, 1, 1), new Vector(0, 1, 1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");
    }
}