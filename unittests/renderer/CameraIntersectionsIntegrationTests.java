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
     * @param camera   the camera generating rays
     * @param geometry the geometry to intersect with
     * @return total number of intersection points
     */
    private int countIntersections(Camera camera, Intersectable geometry) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = camera.constructRay(3, 3, j, i);
                var intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        return count;
    }

    /**
     * Integration test: camera rays intersecting with a sphere
     */
    @Test
    void testCameraSphereIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        Sphere sphere = new Sphere(1, new Point(0, 0, -3));
        int count = countIntersections(camera, sphere);

        assertEquals(2, count, "Wrong number of sphere intersections");
    }

    /**
     * Integration test: camera rays intersecting with a plane
     */
    @Test
    void testCameraPlaneIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        int count = countIntersections(camera, plane);

        assertEquals(9, count, "Wrong number of plane intersections");
    }

    /**
     * Integration test: camera rays intersecting with a triangle
     */
    @Test
    void testCameraTriangleIntegration() {
        Camera camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        Triangle triangle = new Triangle(
                new Point(0, 1, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        int count = countIntersections(camera, triangle);

        assertEquals(1, count, "Wrong number of triangle intersections");
    }
}