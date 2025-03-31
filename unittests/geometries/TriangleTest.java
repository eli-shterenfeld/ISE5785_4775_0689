package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;

        Point p1 = new Point(4.0, 0.0, 0.0);
        Point p2 = new Point(0.0, 4.0, 0.0);
        Point p3 = new Point(0.0, 0.0, 0.0);

        Triangle tr = new Triangle(p1, p2, p3);

        Vector normal = new Vector(0,0,1);
        Vector reversedNormal = new Vector(0,0,-1);
        Point in = new Point(1.0, 1.0, 0.0);

        assertTrue(normal.equals(tr.getNormal(in)) || reversedNormal.equals(tr.getNormal(in)),"incorrect normal");
        assertEquals(1, tr.getNormal(in).length(),DELTA, "the normal is not normalized");


    }
}