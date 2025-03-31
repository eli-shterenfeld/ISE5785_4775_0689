package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sph = new Sphere(1, new Point(0.0, 0.0, 0.0));
        Vector v1 = new Vector(0, 0, 1);

        assertEquals(v1, sph.getNormal(new Point(0.0, 0.0, 1.0)), "incorrect normal");
        assertEquals(1, sph.getNormal(new Point(0.0, 0.0, 1.0)).length(), "incorrect normal");
    }
}