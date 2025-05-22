package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 *
 * @author Dan
 */
class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0.0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     * This test verifies the intersection points of a ray with a convex polygon.
     */
    @Test
    public void testFindIntersections() {

        // Create a convex polygon (square shifted by +1 on X and Y)
        Polygon polygon = new Polygon(
                new Point(1, 1, 0),
                new Point(1, 3, 0),
                new Point(3, 3, 0),
                new Point(3, 1, 0)
        );

        Ray ray;

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects inside the polygon
        ray = new Ray(new Point(2, 2, 1), new Vector(0, 0, -1));
        assertEquals(new Point(2, 2, 0), polygon.findIntersections(ray).getFirst(), "Incorrect intersection inside polygon");

        // TC02: Ray misses the polygon (crosses plane outside polygon)
        ray = new Ray(new Point(4, 4, 1), new Vector(0, 0, -1));
        assertNull(polygon.findIntersections(ray), "There should be no intersection outside polygon");

        // TC03: Ray crosses near an edge but outside
        ray = new Ray(new Point(3.5, 2, 1), new Vector(0, 0, -1));
        assertNull(polygon.findIntersections(ray), "There should be no intersection near edge but outside");

        // =============== Boundary Values Tests ==================

        // TC04: Ray crosses exactly on an edge of the polygon
        ray = new Ray(new Point(2, 1, 1), new Vector(0, 0, -1));
        assertNull(polygon.findIntersections(ray), "Ray crossing on edge should not count as intersection");

        // TC05: Ray crosses exactly on a vertex of the polygon
        ray = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
        assertNull(polygon.findIntersections(ray), "Ray crossing on vertex should not count as intersection");

        // TC06: Ray crosses on an extended line outside polygon
        ray = new Ray(new Point(0, 1, 1), new Vector(0, 0, -1));
        assertNull(polygon.findIntersections(ray), "Ray crossing on extended line should not count as intersection");

        // TC07: Ray parallel to the polygon plane and above
        ray = new Ray(new Point(2, 2, 1), new Vector(1, 0, 0));
        assertNull(polygon.findIntersections(ray), "Ray parallel to plane should not intersect");

        // TC08: Ray starts from the polygon plane but pointing away
        ray = new Ray(new Point(2, 2, 0), new Vector(0, 0, 1));
        assertNull(polygon.findIntersections(ray), "Ray starting from the plane should not count as intersection");
    }

    /**
     * Test method for {@link geometries.Polygon#calculateIntersections(primitives.Ray, double)}.
     * This test verifies the intersection points of a ray with a polygon within a specified distance.
     */
    @Test
    void testCalculateIntersectionsWithDistance() {
        // ============ Equivalence Partitions Tests ==============
        Polygon polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        double maxDistance = 3.5;

        // TC01: Ray's line stops after the polygon (1 point)
        Ray ray01 = new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, 1));
        var result01 = polygon.calculateIntersections(ray01, maxDistance);
        assertNotNull(result01, "Expected intersection points");
        assertEquals(1, result01.size(), "Wrong number of intersection points");

        // TC02: Ray's line stop before the polygon (0 points)
        Ray ray02 = new Ray(new Point(0.5, -0.5, 0), new Vector(0, 0, 1));
        var result02 = polygon.calculateIntersections(ray02, maxDistance);
        assertNull(result02, "Expected no intersection points");

        // TC03: Ray's line start and stop before the polygon (0 points)
        Ray ray03 = new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1));
        var result03 = polygon.calculateIntersections(ray03, maxDistance);
        assertNull(result03, "Expected no intersection points");
    }
}
