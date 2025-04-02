package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class.
 * @author eli
 */
class CylinderTest {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     * This test verifies that the normal vector is correctly calculated
     * for points on the round surface, bottom base, and upper base of the cylinder.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001; // Allowed margin of error for floating-point comparisons

        // Create a cylinder with radius 4 and height 10
        Cylinder c1 = new Cylinder(4, new Ray(new Point(0,0,0), new Vector(0,0,1)),10);

        // Test case: point on the curved surface of the cylinder
        Point p1 = new Point(4,0,2);
        Vector normal = new Vector(1,0,0);
        assertEquals(normal, c1.getNormal(p1), "point on the round surface");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the round surface");

        // Test case: point on the bottom base of the cylinder
        p1 = new Point(2,2,0);
        normal = new Vector(0,0,-1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base");

        // Test case: point on the upper base of the cylinder
        p1 = new Point(2,2,10);
        normal = new Vector(0,0,1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base");

        // =============== Boundary Values Tests ==================

        // Test case: point in the center of the bottom base
        p1 = new Point(0,0,0);
        normal = new Vector(0,0,-1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base in the middle");

        // Test case: point in the center of the upper base
        p1 = new Point(0,0,10);
        normal = new Vector(0,0,1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base in the middle");

        // Test case: point on the edge of the bottom base
        p1 = new Point(4,0,0);
        normal = new Vector(0,0,-1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base on the edge");

        // Test case: point on the edge of the upper base
        p1 = new Point(4,0,10);
        normal = new Vector(0,0,1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base on the edge");
    }
}