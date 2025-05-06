package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(java.util.List)}.
     * This test verifies the correct calculation of the closest point on the ray to a given list of points.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));

        // ============ Equivalence Partition Test ==============

        // TC01: EP – Closest point is in the middle of the list
        Point p1 = new Point(0, 0, 5);
        Point p2 = new Point(0, 0, 6);
        Point p3 = new Point(0, 0, 10);
        assertEquals(p1, ray.findClosestPoint(List.of(p2, p1, p3)), "TC01: wrong closest point (middle)");

        // ============ Boundary Value Tests ====================

        // TC02: BVA – List is null
        assertNull(ray.findClosestPoint(null), "TC02: expected null for null list");

        // TC03: BVA – Closest point is the first in the list
        assertEquals(p1, ray.findClosestPoint(List.of(p1, p2, p3)), "TC03: wrong closest point (first)");

        // TC04: BVA – Closest point is the last in the list
        assertEquals(p1, ray.findClosestPoint(List.of(p2, p3, p1)), "TC04: wrong closest point (last)");
    }

}