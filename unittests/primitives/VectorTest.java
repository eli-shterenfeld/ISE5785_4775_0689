package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class.
 * @author eli
 */
class VectorTest {

    /**
     * A sample vector used for testing.
     */
    Vector v1 = new Vector(1,0, 0);

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     * This test verifies the correct addition of vectors, including edge cases.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(2,1,0), v1.add(new Vector(1,1, 0)), "adding two vectors with an acute angle");
        assertEquals(new Vector(0,1,0), v1.add(new Vector(-1,1, 0)), "adding two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1,0, 0)),
                "Expected adding opposite vectors to throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#subtract(Vector)}.
     * This test verifies the correct subtraction of vectors, including edge cases.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(0,-1,0), v1.subtract(new Vector(1,1, 0)), "subtracting two vectors with an acute angle");
        assertEquals(new Vector(2,-1,0), v1.subtract(new Vector(-1,1, 0)), "subtracting two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(new Vector(1,0, 0)),
                "Expected subtracting opposite vectors to throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * This test verifies the correct scaling of a vector.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(1, 7, 0);
        double scalar = 5;
        assertEquals(new Vector(5, 35, 0), v2.scale(scalar), "scaling a vector");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * This test verifies the correct calculation of the dot product between vectors.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(1, v1.dotProduct(new Vector(1,1, 0)), "dot product of two vectors with an acute angle");
        assertEquals(-1, v1.dotProduct(new Vector(-1,1, 0)), "dot product of two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        assertEquals(0, v1.dotProduct(new Vector(0,4, 0)), "dot product of orthogonal vectors");
        assertEquals(1, v1.dotProduct(new Vector(1,4, 0)), "dot product with a unit vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * This test verifies the correct calculation of the cross product between vectors.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(0,0,1), v1.crossProduct(new Vector(1,1, 0)), "cross product of two vectors with an acute angle");
        assertEquals(new Vector(0,0,1), v1.crossProduct(new Vector(-1,1, 0)), "cross product of two vectors with an obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> new Vector(5,0, 0).crossProduct(new Vector(5,0, 0)),
                "cross product of two vectors with the same direction and length");
        assertThrows(IllegalArgumentException.class, () -> new Vector(1,0, 0).crossProduct(new Vector(5,0, 0)),
                "cross product of two vectors with the same direction and different length");
        assertThrows(IllegalArgumentException.class, () -> new Vector(2,0, 0).crossProduct(new Vector(-2,0, 0)),
                "cross product of two vectors with the opposite direction and the same length");
        assertThrows(IllegalArgumentException.class, () -> new Vector(2,0, 0).crossProduct(new Vector(-3,0, 0)),
                "cross product of two vectors with the opposite direction and different lengths");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     * This test verifies the correct calculation of the squared length of a vector.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(16, v1.lengthSquared(), "length squared calculation");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     * This test verifies the correct calculation of the length of a vector.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(4, v1.length(), "length calculation");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     * This test verifies the correct normalization of a vector.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(new Vector(1,0,0), v1.normalize(), "vector normalization");
    }
}