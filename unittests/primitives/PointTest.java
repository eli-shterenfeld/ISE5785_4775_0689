package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 * @author eli and david
 */
class PointTest {

    /**
     * Reference point for testing.
     */
    private final Point p1 = new Point(1, 2, 3);

    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: subtracting two points
        Point p2 = new Point(4, 5, 6);
        Vector expected = new Vector(-3, -3, -3);
        assertEquals(expected, p1.subtract(p2), "subtracting two points");

        // =============== Boundary Values Tests ==================
        // TC02: subtracting point from itself (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "subtracting point from itself should throw exception");

        // TC03: subtracting zero point
        assertEquals(new Vector(1, 2, 3), p1.subtract(Point.ZERO), "subtracting Point zero");
    }

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: adding a point and a vector
        Vector v1 = new Vector(4, 5, 6);
        Point expected = new Point(5, 7, 9);
        assertEquals(expected, p1.add(v1), "adding a point and a vector");

        // =============== Boundary Values Tests ==================
        // TC02: adding opposite vector to reach zero
        assertEquals(Point.ZERO, p1.add(new Vector(-1, -2, -3)), "adding upset vector to a point");
    }

    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: calculating squared distance between two points
        Point p2 = new Point(4, 5, 6);
        double expected = 27;
        assertEquals(expected, p1.distanceSquared(p2), "calculating squared distance between two points");

        // =============== Boundary Values Tests ==================
        // TC02: distance squared of a point to itself
        assertEquals(0, p1.distanceSquared(p1), "distance squared of a point to itself");

        // TC03: distance squared of a point to zero
        assertEquals(new Vector(1, 2, 3).lengthSquared(), p1.distanceSquared(Point.ZERO), "distance squared of a point to zero");
    }

    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: calculating distance between two points
        Point p2 = new Point(4, 5, 6);
        double expected = Math.sqrt(27);
        assertEquals(expected, p1.distance(p2), "calculating distance between two points");

        // =============== Boundary Values Tests ==================
        // TC02: distance of a point to itself
        assertEquals(0, p1.distance(p1), "distance of a point to itself");

        // TC03: distance of a point to zero
        assertEquals(new Vector(1, 2, 3).length(), p1.distance(Point.ZERO), "distance of a point to zero");
    }
}