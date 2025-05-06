package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between Camera and Geometries.
 * Each test constructs rays through a 3x3 view plane and intersects them with a geometry,
 * counting the number of total intersection points.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Helper method to count intersections between rays from a camera and a given geometry.
     *
     * @param camera        the camera generating rays
     * @param geometry      the geometry to intersect with
     * @param expectedCount the expected number of intersection points
     * @param message       a descriptive message for assertion
     */
    private void assertCountIntersections(Camera camera, Intersectable geometry, int expectedCount, String message) {
        int count = 0;
        int nX = 3, nY = 3;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                var intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        assertEquals(expectedCount, count, message);
    }

    /**
     * Integration test: camera rays intersecting with spheres of different sizes and positions.
     */
    @Test
    void testCameraSphereIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(10)
                .build();

        // Case 1: Small sphere behind center
        Sphere sphere1 = new Sphere(1, new Point(0, 0, -14));
        assertCountIntersections(camera, sphere1, 2, "Sphere Case 1: Expected 2 intersections");

        // Case 2: Large sphere that encompasses the entire view
        Sphere sphere2 = new Sphere(10, new Point(0, 0, -20));
        assertCountIntersections(camera, sphere2, 18, "Sphere Case 2: Expected 18 intersections");

        // Case 3: Medium sphere intersecting central rays
        Sphere sphere3 = new Sphere(2, new Point(0, 0, -2.5));
        assertCountIntersections(camera, sphere3, 10, "Sphere Case 3: Expected 10 intersections");

        // Case 4: Sphere in front of camera — should miss
        Sphere sphere4 = new Sphere(0.5, new Point(0, 0, 1));
        assertCountIntersections(camera, sphere4, 0, "Sphere Case 4: Expected 0 intersections");
    }

    /**
     * Integration test: camera rays intersecting with planes of different orientations.
     */
    @Test
    void testCameraPlaneIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(3)
                .build();

        // Case 1: Plane perpendicular to view plane
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        assertCountIntersections(camera, plane1, 9, "Plane Case 1: All rays intersect");

        // Case 2: Slightly tilted plane
        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 2));
        assertCountIntersections(camera, plane2, 9, "Plane Case 2: All rays intersect (tilted)");

        // Case 3: Steep angled plane
        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 1));
        assertCountIntersections(camera, plane3, 6, "Plane Case 3: Only some rays intersect (steep)");
    }

    /**
     * Integration test: camera rays intersecting with triangles in various positions.
     */
    @Test
    void testCameraTriangleIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        // Case 1: Small triangle in the center
        Triangle triangle1 = new Triangle(
                new Point(0, 1, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        assertCountIntersections(camera, triangle1, 1, "Triangle Case 1: One central intersection");

        // Case 2: Larger triangle covering more area
        Triangle triangle2 = new Triangle(
                new Point(0, 20, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        assertCountIntersections(camera, triangle2, 2, "Triangle Case 2: Two intersections (larger area)");
    }
}