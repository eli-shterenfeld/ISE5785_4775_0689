package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Ray} class.
 *
 * @author eli and david
 */
class RayTest {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     * This test verifies the correct calculation of a point on the ray at a given distance.
     */
    @Test
    void getPoint() {

        // ============ Equivalence Partitions Tests ==============
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));

        // TC01: positive scaling
        assertEquals(new Point(0, 0, 5), ray.getPoint(4), "incorrect positive scale");

        // TC02: negative scaling
        assertEquals(new Point(0, 0, -1), ray.getPoint(-2), "incorrect negative scale");

        // =============== Boundary Values Tests ==================
        // TC03: scaling by zero
        assertEquals(new Point(0, 0, 1), ray.getPoint(0), "incorrect zero scale");
    }
}