package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for geometries.Cylinder class.
 *
 * @author eli and david
 */
class CylinderTest {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     * This test verifies that the normal vector is correctly calculated
     * for points on the round surface, bottom base, and upper base of the cylinder.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        double DELTA = 0.000001; // Allowed margin of error for floating-point comparisons

        // TC01: Create a cylinder with radius 4 and height 10
        Cylinder c1 = new Cylinder(4, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 10);

        // TC02: Point on the curved surface of the cylinder
        Point p1 = new Point(4, 0, 2);
        Vector normal = new Vector(1, 0, 0);
        assertEquals(normal, c1.getNormal(p1), "point on the round surface");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the round surface");

        // TC03: Point on the bottom base of the cylinder
        p1 = new Point(2, 2, 0);
        normal = new Vector(0, 0, -1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base");

        // TC04: Point on the upper base of the cylinder
        p1 = new Point(2, 2, 10);
        normal = new Vector(0, 0, 1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base");

        // =============== Boundary Values Tests ==================

        // TC05: Point in the center of the bottom base
        p1 = new Point(0, 0, 0);
        normal = new Vector(0, 0, -1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base in the middle");

        // TC06: Point in the center of the upper base
        p1 = new Point(0, 0, 10);
        normal = new Vector(0, 0, 1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base in the middle");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base in the middle");

        // TC07: Point on the edge of the bottom base
        p1 = new Point(4, 0, 0);
        normal = new Vector(0, 0, -1);
        assertEquals(normal, c1.getNormal(p1), "point on the bottom base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the bottom base on the edge");

        // TC08: Point on the edge of the upper base
        p1 = new Point(4, 0, 10);
        normal = new Vector(0, 0, 1);
        assertEquals(normal, c1.getNormal(p1), "point on the upper base on the edge");
        assertEquals(1, c1.getNormal(p1).normalize().length(), DELTA, "point on the upper base on the edge");
    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {

        // ============ Equivalence Partitions Tests ==============
        Cylinder cylinder = new Cylinder(1.0, new Ray(new Point(1, 1, 0), new Vector(0, 0, 1)), 2.0);

        // TC01: test when the ray crosses the cylinder surface (2 points)
        Ray ray01 = new Ray(new Point(-0.5, 1, 1), new Vector(1, 0, 0));
        var result01 = cylinder.findIntersections(ray01);
        assertNotNull(result01, "Expected intersection points");
        assertEquals(2, result01.size(), "Wrong number of intersection points");

        // TC02: test when the ray is cross the cylinder top cap (2 point)
        Ray ray02 = new Ray(new Point(1, 1, 3), new Vector(0, 0, -1));
        var result02 = cylinder.findIntersections(ray02);
        assertNotNull(result02, "Expected intersection points");
        assertEquals(2, result02.size(), "Wrong number of intersection points");
        assertEquals(new Point(1, 1, 2), result02.get(0), "Wrong intersection point");
        assertEquals(new Point(1, 1, 0), result02.get(1), "Wrong intersection point");

        // TC03: test when the ray is cross the cylinder top cap and surface (2 points)
        Ray ray03 = new Ray(new Point(0, 1, -1), new Vector(1, 0, 1));
        var result03 = cylinder.findIntersections(ray03);
        assertNotNull(result03, "Expected intersection points");
        assertEquals(2, result03.size(), "Wrong number of intersection points");

        // TC04: test when the ray is cross the axis of the cylinder (2 points)
        Ray ray04 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
        var result04 = cylinder.findIntersections(ray04);
        assertNotNull(result04, "Expected intersection points");
        assertEquals(2, result04.size(), "Wrong number of intersection points");

        // TC05: test when the ray is outside the cylinder (0 points)
        Ray ray05 = new Ray(new Point(3, 3, 1), new Vector(1, 0, 0));
        var result05 = cylinder.findIntersections(ray05);
        assertNull(result05, "Expected no intersection points");

        // TC06: test when the ray is parallel to the cylinder axis inside (2 points)
        Ray ray06 = new Ray(new Point(1.5, 1, -1), new Vector(0, 0, 1));
        var result06 = cylinder.findIntersections(ray06);
        assertNotNull(result06, "Expected intersection points");
        assertEquals(2, result06.size(), "Wrong number of intersection points");

        // TC07: test when the ray is parallel to the cylinder axis outside (0 points)
        Ray ray07 = new Ray(new Point(3, 1, -1), new Vector(0, 0, 1));
        var result07 = cylinder.findIntersections(ray07);
        assertNull(result07, "Expected no intersection points");

        // TC08: ray starts on the surface and goes inward (1 point)
        Ray ray08 = new Ray(new Point(2, 1, 1), new Vector(-1, 0, 0));
        var result08 = cylinder.findIntersections(ray08);
        assertNotNull(result08, "Expected intersection points");
        assertEquals(1, result08.size(), "Wrong number of intersection points");

        // TC09: test when the ray from bottom cap facing upward  (1 point)
        Ray ray09 = new Ray(new Point(1.5, 1, 0), new Vector(0, 0, 1));
        var result09 = cylinder.findIntersections(ray09);
        assertNotNull(result09, "Expected intersection points");
        assertEquals(1, result09.size(), "Wrong number of intersection points");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's touch the surface
        // TC11: test when the ray is tangent to the cylinder surface (0 point)
        Ray ray11 = new Ray(new Point(2, 1, 1), new Vector(0, 1, 0));
        var result11 = cylinder.findIntersections(ray11);
        assertNull(result11, "Expected no intersection points");

        // TC12: test when the ray is start from the cylinder surface and face inside (1 point)
        Ray ray12 = new Ray(new Point(2, 1, 1), new Vector(-1, 0, 0));
        var result12 = cylinder.findIntersections(ray12);
        assertNotNull(result12, "Expected intersection points");
        assertEquals(1, result12.size(), "Wrong number of intersection points");

        // TC13: test when the ray is start from the cylinder surface and face outside (0 point)
        Ray ray13 = new Ray(new Point(2, 1, 1), new Vector(1, 0, 0));
        var result13 = cylinder.findIntersections(ray13);
        assertNull(result13, "Expected no intersection points");

        // **** Group 2: Ray's touch the up and bottom surfaces
        // TC21: test when the ray is start from the cylinder top surface and face inside (1 point)
        Ray ray21 = new Ray(new Point(1, 1, 2), new Vector(0, 0, -1));
        var result21 = cylinder.findIntersections(ray21);
        assertNotNull(result21, "Expected intersection points");
        assertEquals(1, result21.size(), "Wrong number of intersection points");

        // TC22: test when the ray is start from the cylinder bottom surface and face inside (1 point)
        Ray ray22 = new Ray(new Point(1, 0.5, 0), new Vector(0, 0, 1));
        var result22 = cylinder.findIntersections(ray22);
        assertNotNull(result22, "Expected intersection points");
        assertEquals(1, result22.size(), "Wrong number of intersection points");

        // **** Group 3: Ray's start from the surface
        // TC31: test when the ray is start from the cylinder top surface and face outside (0 point)
        Ray ray31 = new Ray(new Point(1.5, 1, 2), new Vector(0, 0, 1));
        var result31 = cylinder.findIntersections(ray31);
        assertNull(result31, "Expected no intersection points");

        // TC32: test when the ray is start from the cylinder bottom surface and face inside (1 point)
        Ray ray32 = new Ray(new Point(1.5, 1, 0), new Vector(0, 0, 1));
        var result32 = cylinder.findIntersections(ray32);
        assertNotNull(result32, "Expected intersection points");
        assertEquals(1, result32.size(), "Wrong number of intersection points");

        // **** Group 4: Ray's cross in connects
        // TC41: test when the ray is cross exactly between the surface and the bottom (1 point)
        Ray ray41 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
        var result41 = cylinder.findIntersections(ray41);
        assertNotNull(result41, "Expected intersection points");
        assertEquals(1, result41.size(), "Wrong number of intersection points");

        // TC42: test when the ray is cross exactly between the surface and the top (1 point)
        Ray ra42 = new Ray(new Point(0, 1, 2), new Vector(1, 0, 0));
        var resul42 = cylinder.findIntersections(ra42);
        assertNotNull(resul42, "Expected intersection points");
        assertEquals(1, resul42.size(), "Wrong number of intersection points");

        // TC43: test when the ray is move parallel to the surface (0 point)
        Ray ray43 = new Ray(new Point(2, 1, -1), new Vector(0, 0, 1));
        var result43 = cylinder.findIntersections(ray43);
        assertNull(result43, "Expected no intersection points");

        // TC44: test when the ray starts exactly at the edge between bottom cap and side surface (1 point)
        Ray ray44 = new Ray(new Point(2, 1, 0), new Vector(-1, 0, 1));
        var result44 = cylinder.findIntersections(ray44);
        assertNotNull(result44, "Expected intersection point");
        assertEquals(1, result44.size(), "Wrong number of intersection points");

        // TC45: test when the ray starts exactly at the edge between top cap and side surface (0 point)
        Ray ray45 = new Ray(new Point(2, 1, 2), new Vector(1, 0, 1));
        var result45 = cylinder.findIntersections(ray45);
        assertNull(result45, "Expected no intersection points");
    }

    /**
     * Test method for {@link geometries.Cylinder#calculateIntersections(primitives.Ray, double)}.
     * This test verifies that the intersection points are correctly calculated
     * for various cases, including when the ray is parallel to the cylinder axis,
     * when it intersects the cylinder surface, and when it starts inside or outside the cylinder.
     */
    @Test
    void testCalculateIntersectionsWithDistance() {
        // ============ Equivalence Partitions Tests ==============
        Cylinder cylinder = new Cylinder(1.0, new Ray(new Point(1, 1, 0), new Vector(0, 0, 1)), 2.0);
        double maxDistance = 4;

        // TC01: test when the ray crosses the cylinder surface (2 points)
        Ray ray01 = new Ray(new Point(-0.5, 1, 1), new Vector(1, 0, 0));
        var result01 = cylinder.calculateIntersections(ray01, maxDistance);
        assertNotNull(result01, "Expected intersection points");
        assertEquals(2, result01.size(), "Wrong number of intersection points");

        // TC02: test when the ray stop before the cylinder surface (0 points)
        Ray ray02 = new Ray(new Point(-0.5, 1, 1), new Vector(1, 0, 0));
        var result02 = cylinder.calculateIntersections(ray02, 0.2);
        assertNull(result02, "Expected no intersection points");

        // TC03: test when the ray starts inside the cylinder (1 point)
        Ray ray03 = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
        var result03 = cylinder.calculateIntersections(ray03, maxDistance);
        assertNotNull(result03, "Expected intersection points");
        assertEquals(1, result03.size(), "Wrong number of intersection points");

        // TC04: test when the ray starts and stop inside the cylinder (0 points)
        Ray ray04 = new Ray(new Point(1.5, 1, 1), new Vector(1, 0, 0));
        var result04 = cylinder.calculateIntersections(ray04, 0.4);
        assertNull(result04, "Expected no intersection points");

        // TC05: test when the ray starts after the cylinder (0 points)
        Ray ray05 = new Ray(new Point(1, 1, 3), new Vector(0, 0, 1));
        var result05 = cylinder.calculateIntersections(ray05, maxDistance);
        assertNull(result05, "Expected no intersection points");
    }
}
