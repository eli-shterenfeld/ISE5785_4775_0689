package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Geometries} class.
 *
 * @author eli and david
 */
class GeometriesTest {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     * This test verifies the intersection points of a ray with multiple geometries.
     */
    @Test
    void findIntersections() {

        Geometries geo = null;
        Plane plane = null;
        Triangle triangle = null;
        Sphere sphere = null;
        Ray ray = null;

        // ============ Equivalence Partitions Tests ==============
        // TC01: some shapes are intersected but not all
        geo = new Geometries();
        plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        triangle = new Triangle(new Point(10, 0, 2), new Point(0, 10, 2), new Point(0, 0, -1));
        sphere = new Sphere(1, new Point(100, 0, 0));
        ray = new Ray(new Point(1, 1, -5), new Vector(0, 0, 1));
        geo.add(plane, triangle, sphere);
        assertEquals(2, geo.findIntersections(ray).size(), "there should be 2 points of intersection");

        // =============== Boundary Values Tests ==================
        // TC01: no shapes in the list
        geo = new Geometries();
        assertNull(geo.findIntersections(ray), "the list of points should be null");

        // TC02: no shape is intersected
        geo = new Geometries();
        plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        triangle = new Triangle(new Point(10, 0, 2), new Point(0, 10, 2), new Point(0, 0, -1));
        sphere = new Sphere(1, new Point(100, 0, 0));
        ray = new Ray(new Point(1, 1, -5), new Vector(0, 0, -1));
        geo.add(plane, triangle, sphere);
        assertNull(geo.findIntersections(ray), "there should be 0 points of intersection");

        // TC03: only one shape is intersected
        geo = new Geometries();
        plane = new Plane(new Point(0, 0, -20), new Vector(0, 0, 1));
        triangle = new Triangle(new Point(10, 0, 2), new Point(0, 10, 2), new Point(0, 0, -1));
        sphere = new Sphere(1, new Point(100, 0, 0));
        ray = new Ray(new Point(1, 1, -5), new Vector(0, 0, -1));
        geo.add(plane, triangle, sphere);
        assertEquals(1, geo.findIntersections(ray).size(), "there should be 1 point of intersection");

        // TC04: Ray intersects the triangle
        geo = new Geometries();
        plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        triangle = new Triangle(new Point(10, 0, 2), new Point(0, 10, 2), new Point(0, 0, -1));
        sphere = new Sphere(10, new Point(1, 1, 0));
        ray = new Ray(new Point(1, 1, -50), new Vector(0, 0, 1));
        geo.add(plane, triangle, sphere);
        assertEquals(4, geo.findIntersections(ray).size(), "all the shapes should be intersected");
    }
}