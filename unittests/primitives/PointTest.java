package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    Point p1 = new Point(1, 2, 3);
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p2 = new Point(4, 5, 6);
        Vector expected = new Vector(-3, -3, -3);
        assertEquals(expected, p1.subtract(p2), "subtracting two points");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "Expected subtracting the same point to throw an exception");
    }

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4, 5, 6);
        Point expected = new Point(5, 7, 9);
        assertEquals(expected, p1.add(v1), "adding a point and a vector");
    }

    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point p2 = new Point(4, 5, 6);
        double expected = 27;
        assertEquals(expected, p1.distanceSquared(p2), "calculating squared distance between two points");

        // =============== Boundary Values Tests ==================
        assertEquals(0, p1.distanceSquared(p1), "distance squared of a point to itself");
    }

    @Test
    void testDistance() {

        // ============ Equivalence Partitions Tests ==============
        Point p2 = new Point(4, 5, 6);
        double expected = Math.sqrt(27);
        assertEquals(expected, p1.distance(p2), "calculating distance between two points");

        // =============== Boundary Values Tests ==================
        assertEquals(0, p1.distance(p1), "distance of a point to itself");
    }
}