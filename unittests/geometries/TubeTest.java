package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Tube} class.
 * @author eli
 */
class TubeTest {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     * This test verifies the correct calculation of the normal vector at a given point on the tube.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;

        // Define the radius and ray for the tube
        double radius = 1;
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(radius, ray);

        // Test normal for a point on the tube surface
        Point p = new Point(1, 0, 3);
        Vector normal = new Vector(1, 0, 0);

        // Verify the normal vector is correct and has unit length
        assertEquals(1, tube.getNormal(p).length(), DELTA, "Incorrect normal length");
        assertEquals(normal, tube.getNormal(p), "Incorrect normal vector");

        // =============== Boundary Values Tests ==================
        // Test normal for a point at the base of the tube
        p = new Point(1, 0, 0);

        // Verify the normal vector is correct and has unit length
        assertEquals(1, tube.getNormal(p).length(), DELTA, "Incorrect normal length");
        assertEquals(normal, tube.getNormal(p), "Incorrect normal vector");
    }
}