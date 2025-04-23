package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class.
 *
 * @author eli and david
 */
class VectorTest {

    /**
     * A sample vector used for testing.
     */
    private final Vector v1 = new Vector(1, 0, 0);

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     * This test verifies the correct addition of vectors, including edge cases.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: adding two vectors with an acute angle
        assertEquals(new Vector(2, 1, 0), v1.add(new Vector(1, 1, 0)), "incorrect vector");
        // TC02: adding two vectors with an obtuse angle
        assertEquals(new Vector(0, 1, 0), v1.add(new Vector(-1, 1, 0)), "incorrect vector");

        // =============== Boundary Values Tests ==================
        // TC03: adding opposite vectors (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1, 0, 0)),
                "Expected adding opposite vectors to throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * This test verifies the correct subtraction of vectors, including edge cases.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: subtracting two vectors with an acute angle
        assertEquals(new Vector(0, -1, 0), v1.subtract(new Vector(1, 1, 0)), "incorrect vector");
        // TC02: subtracting two vectors with an obtuse angle
        assertEquals(new Vector(2, -1, 0), v1.subtract(new Vector(-1, 1, 0)), "incorrect vector");

        // =============== Boundary Values Tests ==================
        // TC03: subtracting identical vectors (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(new Vector(1, 0, 0)),
                "Expected subtracting opposite vectors to throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * This test verifies the correct scaling of a vector.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: scaling a vector
        Vector v2 = new Vector(1, 7, 0);
        double scalar = 5;
        assertEquals(new Vector(5, 35, 0), v2.scale(scalar), "incorrect vector");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * This test verifies the correct calculation of the dot product between vectors.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: dot product of two vectors with an acute angle
        assertEquals(1, v1.dotProduct(new Vector(1, 1, 0)), "incorrect dot product of two vectors with an acute angle");
        // TC02: dot product of two vectors with an obtuse angle
        assertEquals(-1, v1.dotProduct(new Vector(-1, 1, 0)), "incorrect dot product of two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        // TC03: dot product of orthogonal vectors
        assertEquals(0, v1.dotProduct(new Vector(0, 4, 0)), "incorrect dot product of orthogonal vectors");
        // TC04: dot product with a unit vector
        assertEquals(1, v1.dotProduct(new Vector(1, 4, 0)), "incorrect dot product with a unit vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * This test verifies the correct calculation of the cross product between vectors.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: cross product of two vectors with an acute angle
        assertEquals(new Vector(0, 0, 1), v1.crossProduct(new Vector(1, 1, 0)), "incorrect cross product of two vectors with an acute angle");
        // TC02: cross product of two vectors with an obtuse angle
        assertEquals(new Vector(0, 0, 1), v1.crossProduct(new Vector(-1, 1, 0)), "incorrect cross product of two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        // TC03: cross product of two vectors with the same direction and length
        assertThrows(IllegalArgumentException.class, () -> new Vector(5, 0, 0).crossProduct(new Vector(5, 0, 0)),
                "cross product of two vectors with the same direction and length");
        // TC04: cross product of two vectors with the same direction and different length
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 0, 0).crossProduct(new Vector(5, 0, 0)),
                "cross product of two vectors with the same direction and different length");
        // TC05: cross product of two vectors with the opposite direction and the same length
        assertThrows(IllegalArgumentException.class, () -> new Vector(2, 0, 0).crossProduct(new Vector(-2, 0, 0)),
                "cross product of two vectors with the opposite direction and the same length");
        // TC06: cross product of two vectors with the opposite direction and different lengths
        assertThrows(IllegalArgumentException.class, () -> new Vector(2, 0, 0).crossProduct(new Vector(-3, 0, 0)),
                "cross product of two vectors with the opposite direction and different lengths");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     * This test verifies the correct calculation of the squared length of a vector.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: length squared calculation
        Vector v1 = new Vector(4, 0, 0);
        assertEquals(16, v1.lengthSquared(), "incorrect length squared calculation");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     * This test verifies the correct calculation of the length of a vector.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: length calculation
        Vector v1 = new Vector(4, 0, 0);
        assertEquals(4, v1.length(), "incorrect length calculation");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     * This test verifies the correct normalization of a vector.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: vector normalization
        Vector v1 = new Vector(4, 0, 0);
        assertEquals(new Vector(1, 0, 0), v1.normalize(), "incorrect vector normalization");
    }
}