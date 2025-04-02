package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    Vector v1 = new Vector(1,0, 0);
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(2,1,0), v1.add(new Vector(1,1, 0)), "adding two vector of acute angle");

        assertEquals(new Vector(0,1,0), v1.add(new Vector(-1,1, 0)), "adding two vector of obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1,0, 0)),
                "Expected adding opposite vectors to throw an exception");
    }

    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(0,-1,0), v1.subtract(new Vector(1,1, 0)), "subtracting two vector of acute angle");

        assertEquals(new Vector(2,-1,0), v1.subtract(new Vector(-1,1, 0)), "subtracting two vector of obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(new Vector(1,0, 0)),
                "Expected subtracting opposite vectors to throw an exception");
    }

    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(1, 7, 0);
        double scalar = 5;
        assertEquals(new Vector(5, 35, 0), v2.scale(scalar), "scaling a vector");
    }

    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(1, v1.dotProduct(new Vector(1,1, 0)), "dot Product two vector of acute angle");

        assertEquals(-1, v1.dotProduct(new Vector(-1,1, 0)), "dot Product two vector of obtuse angle");

        // =============== Boundary Values Tests ==================
        assertEquals(0, v1.dotProduct(new Vector(0,4, 0)), " dot product of orthagonals Vectors");

        assertEquals(1, v1.dotProduct(new Vector(1,4, 0)), "dot product of a unit vector");
    }

    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(0,0,1), v1.crossProduct(new Vector(1,1, 0)), "cross product two vector of acute angle");

        assertEquals(new Vector(0,0,1), v1.crossProduct(new Vector(-1,1, 0)), "cross product two vector of obtuse angle");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () ->  new Vector(5,0, 0).crossProduct(new Vector(5,0, 0)),
                "cross product of two vectors with the same direction and length");

        assertThrows(IllegalArgumentException.class, () ->  new Vector(1,0, 0).crossProduct(new Vector(5,0, 0)),
                "cross product of two vectors with the same direction and different length");

        assertThrows(IllegalArgumentException.class, () ->  new Vector(2,0, 0).crossProduct(new Vector(-2,0, 0)),
                "cross product of two vectors with the opposite direction and the same lengths");

        assertThrows(IllegalArgumentException.class, () ->  new Vector(2,0, 0).crossProduct(new Vector(-3,0, 0)),
                "cross product of two vectors with the opposite direction and different lengths");
    }

    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(16, v1.lengthSquared(), "length squared");
    }

    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(4, v1.length(), "length");
    }

    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(new Vector(1,0,0), v1.normalize(), "normalized vector");
    }
}