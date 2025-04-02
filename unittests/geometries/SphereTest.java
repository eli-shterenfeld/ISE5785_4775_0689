package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Sphere} class.
 * @author eli
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

        // Expected normal at point (0,0,1)
        Vector expectedNormal = new Vector(0, 0, 1);

        // Verify that the calculated normal is correct
        assertEquals(expectedNormal, sph.getNormal(new Point(0, 0, 1)), "Incorrect normal vector");

        // Verify that the normal is a unit vector
        assertEquals(1, sph.getNormal(new Point(0, 0, 1)).length(), "Normal vector is not normalized");
    }
}