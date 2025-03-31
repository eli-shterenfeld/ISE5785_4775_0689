package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class cylinderTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;
        cylinder c1 = new cylinder(4, new Ray(new Point(0.0,0.0,0.0), new Vector(0,0,1)),10);

        Point p1 = new Point(4.0,0.0,2.0);
       Vector normal = new Vector(1,0,0);

        assertEquals(normal, c1.getNormal(p1), "point on the round surface");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the round surface");


         p1 = new Point(2.0,2.0,0.0);
         normal = new Vector(0,0,-1);

        assertEquals(normal, c1.getNormal(p1), "point on the bottom base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base");


        p1 = new Point(2.0,2.0,10.0);
        normal = new Vector(0,0,1);

        assertEquals(normal, c1.getNormal(p1), "point on the upper base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base");


        // =============== Boundary Values Tests ==================
        p1 = new Point(0.0,0.0,0.0);
        normal = new Vector(0,0,-1);

        assertEquals(normal, c1.getNormal(p1), "point on the bottom base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base in the middle");


        p1 = new Point(0.0,0.0,10.0);
        normal = new Vector(0,0,1);

        assertEquals(normal, c1.getNormal(p1), "point on the upper base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base in the middle");


        p1 = new Point(4.0,0.0,0.0);
        normal = new Vector(0,0,-1);

        assertEquals(normal, c1.getNormal(p1), "point on the bottom base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base on the edge");

        p1 = new Point(4.0,0.0,10.0);
        normal = new Vector(0,0,1);

        assertEquals(normal, c1.getNormal(p1), "point on the upper base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base on the edge");
    }
}