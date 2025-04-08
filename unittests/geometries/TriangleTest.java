package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        double DELTA = 0.000001;

        // TC01: Define three points of the triangle
        Point p1 = new Point(4, 0, 0);
        Point p2 = new Point(0, 4, 0);
        Point p3 = new Point(0, 0, 0);

        // TC02: Create a triangle with these points
        Triangle tr = new Triangle(p1, p2, p3);

        // TC03: Expected normal vectors
        Vector normal = new Vector(0, 0, 1);
        Vector reversedNormal = new Vector(0, 0, -1);

        // TC04: A point inside the triangle plane
        Point in = new Point(1, 1, 0);

        // TC05: Verify that the calculated normal is correct (can be either normal or its reverse)
        assertTrue(normal.equals(tr.getNormal(in)) || reversedNormal.equals(tr.getNormal(in)), "Incorrect normal vector");

        // TC06: Verify that the normal is a unit vector
        assertEquals(1, tr.getNormal(in).length(), DELTA, "Normal vector is not normalized");
    }



    @Test
    public void testFindIntersections() {

        Triangle t1 = new Triangle(new Point(1,0,0), new Point(1,2,0), new Point(5,0,0));
        Ray ray = null;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the triangle
        ray = new Ray(new Point(2,1,1),new Vector(0,0,-1));
        assertEquals(new Point(2,1,0), t1.findIntersections(ray).getFirst(), "incorrect intersection");

        // TC02: Ray passes between one of the triangle's points
        ray = new Ray(new Point(6,-1,1),new Vector(0,0,-1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC03: Ray passes near one of the triangle's lines
        ray = new Ray(new Point(2,-1,1),new Vector(0,0,-1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // =============== Boundary Values Tests ==================
        // TC04: Ray passes on one of the triangle's lines
        ray = new Ray(new Point(2,0,1),new Vector(0,0,-1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC05: Ray passes on one of the triangle's points
        ray = new Ray(new Point(1,0,1),new Vector(0,0,-1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

        // TC06: Ray passes on one of the triangle's imaginary continued lines
        ray = new Ray(new Point(1,-1,1),new Vector(0,0,-1));
        assertNull(t1.findIntersections(ray), "there should not be any intersections");

    }

}
