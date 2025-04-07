package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============

        Ray ray = new Ray(new Point(0,0,1), new Vector(0,0,1));

        assertEquals(new Point(0,0, 5), ray.getPoint(4),"positive scale");

        assertEquals(new Point(0,0,-1), ray.getPoint(-2),"negative scale");


        // =============== Boundary Values Tests ==================

        assertEquals(new Point(0,0, 1), ray.getPoint(0),"zero scale");
    }
}