package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Plane} class.
 *  @author eli
 */
class PlaneTest {
    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)} constructor.
     * This test verifies that the constructor correctly calculates the normal vector
     * and throws exceptions for invalid input cases.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001; // Allowed margin of error for floating-point comparisons

        // Define three non-collinear points
        Point p1 = new Point(2, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);

        // Compute vectors on the plane
        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);

        // Create the plane
        Plane pl = new Plane(p1, p2, p3);

        // Check that the normal is a unit vector
        assertEquals(1, pl.normal.length(), DELTA);

        // Check that the normal is perpendicular to both vectors defining the plane
        assertEquals(0, pl.normal.dotProduct(v1));
        assertEquals(0, pl.normal.dotProduct(v2));

        // =============== Boundary Values Tests ==================
        // Test case: two identical points (should throw an exception)
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2, 0, 0), new Point(2, 0, 0), new Point(0, 0, 1)),
                "Constructed a plane with two identical points, 1 and 2");

        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2, 0, 0), new Point(0, 1, 0), new Point(2, 0, 0)),
                "Constructed a plane with two identical points, 1 and 3");

        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a plane with two identical points, 2 and 3");

        // Test case: three identical points (should throw an exception)
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(1, 0, 0), new Point(1, 0, 0), new Point(1, 0, 0)),
                "Constructed a plane with three identical points");

        // Test case: all points on the same line (should throw an exception)
        assertThrows(IllegalArgumentException.class, //
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

        // Define three non-collinear points to construct the plane
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(-1, 0, 0);

        // Create the plane
        Plane pl = new Plane(p1, p2, p3);

        // Define possible normal directions
        Vector normal = new Vector(0,0,1);
        Vector reversedNormal = new Vector(0,0,-1);

        // Check that the calculated normal matches one of the expected directions
        assertTrue(normal.equals(pl.normal) || reversedNormal.equals(pl.normal),"incorrect normal");

        // Check that the normal is normalized
        assertEquals(1, pl.normal.length(),DELTA, "the normal is not normalized");
    }
}