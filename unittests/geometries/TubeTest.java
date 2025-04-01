package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001;

        double radius = 1;
        Ray ray = new Ray(new Point(0.0, 0.0, 0.0), new Vector(0, 0, 1));
        Tube tube = new Tube(radius, ray);

        Point p = new Point(1.0, 0.0, 3.0);
        Vector normal = new Vector(1, 0, 0);

        assertEquals(1, tube.getNormal(p).length(), DELTA, "incorrect normal length");
        assertEquals(normal, tube.getNormal(p), "incorrect normal");


        // =============== Boundary Values Tests ==================
         p = new Point(1.0, 0.0, 0.0);

        assertEquals(1, tube.getNormal(p).length(), DELTA, "incorrect normal length");
        assertEquals(normal, tube.getNormal(p), "incorrect normal");
    }
}