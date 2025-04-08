package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
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



    @Test
    public void testFindIntersections() {


    }

}
