package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 *
 * @author eli and david
 */
class PointTest {

    /**
     * Reference point for testing.
     */
    private final Point p1 = new Point(1, 2, 3);

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * This test verifies correct subtraction of two points.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: subtracting two points
        Point p2 = new Point(4, 5, 6);
        Vector expected = new Vector(-3, -3, -3);
        assertEquals(expected, p1.subtract(p2), "incorrect vector");

        // =============== Boundary Values Tests ==================
        // TC02: subtracting point from itself (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "subtracting point from itself threw an exception");

        // TC03: subtracting zero point
        assertEquals(new Vector(1, 2, 3), p1.subtract(Point.ZERO), "incorrect vector");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     * This test verifies correct addition of a point and a vector.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: adding a point and a vector
        Vector v1 = new Vector(4, 5, 6);
        Point expected = new Point(5, 7, 9);
        assertEquals(expected, p1.add(v1), "incorrect point");

        // =============== Boundary Values Tests ==================
        // TC02: adding opposite vector to reach zero
        assertEquals(Point.ZERO, p1.add(new Vector(-1, -2, -3)), "incorrect point");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     * This test verifies correct calculation of the squared distance between two points.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: calculating squared distance between two points
        Point p2 = new Point(4, 5, 6);
        double expected = 27;
        assertEquals(expected, p1.distanceSquared(p2), "incorrect distance");

        // =============== Boundary Values Tests ==================
        // TC02: distance squared of a point to itself
        assertEquals(0, p1.distanceSquared(p1), "incorrect distance");

        // TC03: distance squared of a point to zero
        assertEquals(new Vector(1, 2, 3).lengthSquared(), p1.distanceSquared(Point.ZERO), "incorrect vector");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     * This test verifies correct calculation of the Euclidean distance between two points.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: calculating distance between two points
        Point p2 = new Point(4, 5, 6);
        double expected = Math.sqrt(27);
        assertEquals(expected, p1.distance(p2), "incorrect distance");

        // =============== Boundary Values Tests ==================
        // TC02: distance of a point to itself
        assertEquals(0, p1.distance(p1), "incorrect distance");

        // TC03: distance of a point to zero
        assertEquals(new Vector(1, 2, 3).length(), p1.distance(Point.ZERO), "incorrect length");
    }
}