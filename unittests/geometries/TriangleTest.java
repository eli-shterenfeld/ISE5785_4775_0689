package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Triangle} class.
 *
 * @author eli and david
 */
class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     * This test verifies that the normal vector calculation is correct and normalized.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Define three points of the triangle
        Point p1 = new Point(4, 0, 0);
        Point p2 = new Point(0, 4, 0);
        Point p3 = new Point(0, 0, 0);

        // Create a triangle with these points
        Triangle tr = new Triangle(p1, p2, p3);

        //Expected normal vectors
        Vector normal = new Vector(0, 0, 1);
        Vector reversedNormal = new Vector(0, 0, -1);

        //A point inside the triangle plane
        Point in = new Point(1, 1, 0);

        // TC01: Verify that the calculated normal is correct (can be either normal or its reverse)
        assertTrue(normal.equals(tr.getNormal(in)) || reversedNormal.equals(tr.getNormal(in)), "Incorrect normal vector");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     * This test verifies the intersection points of a ray with the triangle.
     */
    @Test
    public void testFindIntersections() {

        Triangle t1 = new Triangle(new Point(1, 0, 0), new Point(1, 2, 0), new Point(5, 0, 0));
        Ray ray;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the triangle
        ray = new Ray(new Point(2, 1, 1), new Vector(0, 0, -1));
        assertEquals(List.of(new Point(2, 1, 0)), t1.findIntersections(ray), "incorrect intersection");

        // TC02: Ray passes between one of the triangle's points
        ray = new Ray(new Point(6, -1, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC03: Ray passes near one of the triangle's lines
        ray = new Ray(new Point(2, -1, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC04: Ray parallel to the triangle's plane
        ray = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // =============== Boundary Values Tests ==================
        // TC05: Ray passes on one of the triangle's lines
        ray = new Ray(new Point(2, 0, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC06: Ray passes on one of the triangle's points
        ray = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC07: Ray passes on one of the triangle's imaginary continued lines
        ray = new Ray(new Point(1, -1, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC08: Ray starts inside the triangle
        ray = new Ray(new Point(2, 1, 0), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");
    }

    /**
     * Test method for {@link geometries.Polygon#calculateIntersections(primitives.Ray, double)}.
     * This test verifies the intersection points of a ray with a polygon within a specified distance.
     */
    @Test
    void testCalculateIntersectionsWithDistance() {
        // ============ Equivalence Partitions Tests ==============
        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        double maxDistance = 3.5;

        // TC01: Ray's line stop after the triangle (1 point)
        Ray ray01 = new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, 1));
        var result01 = triangle.calculateIntersections(ray01, maxDistance);
        assertNotNull(result01, "Expected intersection points");
        assertEquals(1, result01.size(), "Wrong number of intersection points");

        // TC02: Ray's line stop before the triangle (0 point)
        Ray ray02 = new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, -1));
        var result02 = triangle.calculateIntersections(ray02, maxDistance);
        assertNull(result02, "Expected no intersection points");

        // TC03: Ray's line starts and stop after the triangle (0 point)
        Ray ray03 = new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, 1));
        var result03 = triangle.calculateIntersections(ray03, 0.5);
        assertNull(result03, "Expected no intersection points");
    }
}
