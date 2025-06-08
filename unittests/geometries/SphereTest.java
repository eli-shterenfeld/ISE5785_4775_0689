package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Sphere} class.
 *
 * @author eli and david
 */
class SphereTest {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     * This test verifies that the normal vector calculation is correct and normalized.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // Create a sphere with radius 1 centered at (0,0,0)
        Sphere sph = new Sphere(1, new Point(0, 0, 0));

        //Expected normal at point (0,0,1)
        Vector expectedNormal = new Vector(0, 0, 1);

        // TC01: Verify that the calculated normal is correct
        assertEquals(expectedNormal, sph.getNormal(new Point(0, 0, 1)), "Incorrect normal vector");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     * This test verifies the intersection points of a ray with the sphere.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere;
        Point p1;
        Point p2;
        List<Point> intersections;

        // ============ Equivalence Partitions Tests ==============
        sphere = new Sphere(5, new Point(2, 3, 4));

        // TC1: Ray starts outside the sphere (2 point)
        intersections = sphere.findIntersections(new Ray(new Point(-4, 3, 4), new Vector(1, 0, 0)));
        p1 = new Point(-3, 3, 4);
        p2 = new Point(7, 3, 4);
        assertEquals(List.of(p1, p2), intersections, "incorrect intersections");

        // TC2: Ray starts inside the sphere (1 point)
        intersections = sphere.findIntersections(new Ray(new Point(-2, 3, 4), new Vector(1, 0, 0)));
        p1 = new Point(7, 3, 4);
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // TC3: Ray starts outside and not aimed at the sphere (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(-10, 3, 4), new Vector(0, 1, 0)));
        assertNull(intersections, "there should be 0 intersections");

        // TC4: Ray starts outside and after the sphere and would intersect twice if reversed  (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(10, 3, 4), new Vector(1, 0, 0)));
        assertNull(intersections, "there should be 0 intersections");

        // =============== Boundary Values Tests ==================

        // **** Group 2: The ray’s line crosses the sphere twice
        sphere = new Sphere(5, new Point(2, 3, 4));

        // TC5: Ray starts on the sphere to the inside (1 point)
        intersections = sphere.findIntersections(new Ray(new Point(-3, 3, 4), new Vector(1, 0, 0)));
        p1 = new Point(7, 3, 4);
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // TC6: Ray starts on the sphere to the outside (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(7, 3, 4), new Vector(1, 0, 0)));
        assertNull(intersections, "there should be 0 intersections");

        // **** Group 3: The ray’s line is tangent to the Sphere
        sphere = new Sphere(1.0, new Point(0, 0, 1));

        // TC7: Ray starts before the tangent (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, 1)));
        assertNull(intersections, "there should be 0 intersections");

        // TC8: Ray starts at the tangent (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(1, 0, 1), new Vector(0, 0, 1)));
        assertNull(intersections, "there should be 0 intersections");

        // TC9: Ray starts after the tangent (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(2, 0, 1), new Vector(0, 0, 1)));
        assertNull(intersections, "there should be 0 intersections");

        // **** Group 4: The ray’s line crosses the Sphere’s center
        sphere = new Sphere(1.0, new Point(0, 0, 1));

        // TC10: Ray starts in the sphere's center (1 point)
        p1 = new Point(1, 0, 1);
        intersections = sphere.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 0, 0)));
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // TC11: Ray starts on the sphere (1 point)
        p1 = new Point(1, 0, 1);
        intersections = sphere.findIntersections(new Ray(new Point(-1, 0, 1), new Vector(1, 0, 0)));
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // TC12: Ray starts outside before the sphere (2 point)
        p1 = new Point(-1, 0, 1);
        p2 = new Point(1, 0, 1);
        intersections = sphere.findIntersections(new Ray(new Point(-2, 0, 1), new Vector(1, 0, 0)));
        assertEquals(List.of(p1, p2), intersections, "incorrect intersections");

        // TC13: Ray starts outside after the sphere (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(2, 0, 1), new Vector(1, 0, 0)));
        assertNull(intersections, "there should be 0 intersections");

        // TC14: Ray starts on the sphere to outside (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(1, 0, 1), new Vector(1, 0, 0)));
        assertNull(intersections, "there should be 0 intersections");

        // TC15: Ray starts inside the sphere (1 point)
        p1 = new Point(1, 0, 1);
        intersections = sphere.findIntersections(new Ray(new Point(0.5, 0, 1), new Vector(1, 0, 0)));
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // **** Group 5: The ray is orthogonal to the segment [p0, o]
        sphere = new Sphere(5, new Point(2, 3, 4));

        // TC16: Ray starts inside the sphere (1 point)
        p1 = new Point(6.898979485566356, 2, 4);
        intersections = sphere.findIntersections(new Ray(new Point(2, 2, 4), new Vector(1, 0, 0)));
        assertEquals(List.of(p1), intersections, "incorrect intersections");

        // TC17: Ray starts outside (0 point)
        intersections = sphere.findIntersections(new Ray(new Point(20, 0, 4), new Vector(0, 0, 1)));
        assertNull(intersections, "there should be 0 intersections");
    }

    /**
     * Test method for {@link geometries.Sphere#calculateIntersections(primitives.Ray, double)}.
     * This test verifies the intersection points of a ray with the sphere within a specified distance.
     */
    @Test
    public void testFindIntersectionsWithDistance() {

        // ============ Equivalence Partitions Tests ==============
        Sphere sphere = new Sphere(1d, new Point(100, 0, 0));
        double maxDistance = 3.5;

        // TC01: the ray is stop before the sphere (0 points)
        assertNull(sphere.calculateIntersections(new Ray(new Point(95, 2, 0), new Vector(1, 0, 0)), maxDistance)
                , "Ray's line out of sphere");

        // TC02: the ray is stop inside the sphere (1 point)
        var result2 = sphere.calculateIntersections(new Ray(new Point(97.4, 0, 0), new Vector(1, 0, 0)), maxDistance);
        assertEquals(1, result2.size(), "Wrong number of points");

        // Tc03: the ray start and stop at the sphere (0 point)
        assertNull(sphere.calculateIntersections(new Ray(new Point(99, 0, 0), new Vector(1, 0, 0)), 0.3),
                "Ray's line out of sphere");

        // TC04: the ray start at the sphere and stop outside (1 point)
        var result4 = sphere.calculateIntersections(new Ray(new Point(100, 0, 0), new Vector(1, 0, 0)), maxDistance);
        assertEquals(1, result4.size(), "Wrong number of points");

        // TC05: the ray start inside the sphere close to the end and stop outside (1 point)
        var result5 = sphere.calculateIntersections(new Ray(new Point(100.8, -0.5, 0), new Vector(1, 0, 0)), maxDistance);
        assertEquals(1, result5.size(), "Wrong number of points");

        // TC06: the ray start after the sphere and stop outside (0 point)
        assertNull(sphere.calculateIntersections(new Ray(new Point(98.5, -3.5, 0), new Vector(1, 0, 0)), maxDistance),
                "Ray's line out of sphere");
    }
}
