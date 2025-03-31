package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1,0, 0);
        Vector v2 = new Vector(1,1, 0);

        assertEquals(new Vector(1,2,0), v1.add(v2), "adding two vector of acute angle");

         v1 = new Vector(1,0, 0);
         v2 = new Vector(-1,1, 0);

        assertEquals(new Vector(1,2,0), v1.add(v2), "adding two vector of obtuse angle");



        // =============== Boundary Values Tests ==================
        v1 = new Vector(1,0, 0);
        v2 = new Vector(-1,0, 0);
        Vector v0 = new Vector(0, 0, 0);

        assertEquals(v0, v1.add(v2), "adding two vectors equal in length in opposite direction");
    }

    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 7, 0);
        double scalar = 5;

        assertEquals(new Vector(5, 35, 0), v1.scale(scalar), "scaling a vector");
    }

    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1,0, 0);
        Vector v2 = new Vector(1,1, 0);
        assertEquals(1, v1.dotProduct(v2), "dot Product two vector of acute angle");

        v1 = new Vector(1,0, 0);
        v2 = new Vector(-1,1, 0);
        assertEquals(-1, v1.dotProduct(v2), "dot Product two vector of obtuse angle");

        // =============== Boundary Values Tests ==================
        v1 = new Vector(2,0, 0);
        v2 = new Vector(0,4, 0);
        Vector v0 = new Vector(0, 0, 0);
        assertEquals(0, v1.dotProduct(v2), "dot product of orthagonals");

        v1 = new Vector(1,0, 0);
        v2 = new Vector(1,4, 0);
        assertEquals(0, v1.dotProduct(v2), "dot product of a unit vector");
    }

    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1,0, 0);
        Vector v2 = new Vector(1,1, 0);

        assertEquals(new Vector(0,0,1), v1.crossProduct(v2), "cross product two vector of acute angle");

       /* v1 = new Vector(1,0, 0);
        v2 = new Vector(-1,1, 0);

        assertEquals(new Vector(1,2,0), v1.crossProduct(v2), "cross product two vector of obtuse angle");
*/


        // =============== Boundary Values Tests ==================
        v1 = new Vector(5,0, 0);
        v2 = new Vector(5,0, 0);
        assertEquals(new Vector(0,0,0), v1.crossProduct(v2), "cross product of two vectors with the same direction and length");

        v1 = new Vector(1,0, 0);
        v2 = new Vector(5,0, 0);
        assertEquals(new Vector(0,0,0), v1.crossProduct(v2), "cross product of two vectors with the same direction and different length");

        v1 = new Vector(2,0, 0);
        v2 = new Vector(-2,0, 0);
        assertEquals(new Vector(0,0,0), v1.crossProduct(v2), "cross product of two vectors with the opposite direction and the same lengths");

        v1 = new Vector(2,0, 0);
        v2 = new Vector(-3,0, 0);
        assertEquals(new Vector(0,0,0), v1.crossProduct(v2), "cross product of two vectors with the opposite direction and different lengths");
    }

    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(16, v1.lengthSquared(), "length squared");

    }

    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(4, v1.lengthSquared(), "length");

    }

    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(4,0, 0);
        assertEquals(new Vector(1,0,0), v1.normalize(), "normalized vector");
    }
}