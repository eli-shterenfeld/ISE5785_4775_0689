package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;

        Point p1 = new Point(2.0, 0.0, 0.0);
        Point p2 = new Point(0.0, 1.0, 0.0);
        Point p3 = new Point(0.0, 0.0, 1.0);

        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);

        Plane pl = new Plane(p1, p2, p3);

        assertEquals(1, pl.normal.length(), DELTA);

        assertEquals(0, pl.normal.dotProduct(v1));

        assertEquals(0, pl.normal.dotProduct(v2));

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2.0, 0.0, 0.0), new Point(2.0, 0.0, 0.0), new Point(0.0, 0.0, 1.0)),
                "Constructed a plane with two identical points, 1 and 2");

        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(2.0, 0.0, 0.0)),
                "Constructed a plane with two identical points, 1 and 3");


        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(2.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(0.0, 1.0, 0.0)),
                "Constructed a plane with two identical points, 2 and 3");

        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(1.0, 0.0, 0.0), new Point(1.0, 0.0, 0.0), new Point(1.0, 0.0, 0.0)),
                "Constructed a plane with three identical points");


        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(1.0, 0.0, 0.0), new Point(2.0, 0.0, 0.0), new Point(3.0, 0.0, 0.0)),
                "Constructed a plane with all points on one line");

    }

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;

        Point p1 = new Point(1.0, 0.0, 0.0);
        Point p2 = new Point(0.0, 1.0, 0.0);
        Point p3 = new Point(-1.0, 0.0, 0.0);

        Plane pl = new Plane(p1, p2, p3);

        Vector normal = new Vector(0,0,1);
        Vector reversedNormal = new Vector(0,0,-1);

        assertTrue(normal.equals(pl.normal) || reversedNormal.equals(pl.normal),"incorrect normal");
        assertEquals(1, pl.normal.length(),DELTA, "the normal is not normalized");
    }
}