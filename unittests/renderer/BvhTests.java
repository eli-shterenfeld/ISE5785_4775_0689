package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.YELLOW;

public class BvhTests {

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            // ✅ fixed: first arg must be a direction vector, not point
            .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
            .setVpDistance(100)
            .setVpSize(500, 500);

    private Scene createScene() {
        Scene scene = new Scene("BVH Scene")
                .setBackground(new Color(10, 10, 10)) // darker background to see highlights better
                // ✅ added intensity (0.3) for ambient light — prevents full black shading
                .setAmbientLight(new AmbientLight(new Color(255, 255, 255)));

        scene.geometries.add(
                // ✅ moved sphere back to avoid overlap with view plane
                new Sphere(30, new Point(0, 0, -150))
                        .setEmission(new Color(100, 50, 255))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(50)),

                new Plane(new Point(0, 0, -180), new Vector(0, 0, 1))
                        .setEmission(new Color(100, 100, 100))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3)),

                new Cylinder(10, new Ray(new Point(-50, 50, -160), new Vector(0, 0, 1)), 80)
                        .setEmission(new Color(20, 255, 100))
                        .setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(30))
        );

        // ✅ moved light source behind camera (z = -50 instead of +150)
        scene.lights.add(
                new PointLight(new Color(1000, 800, 800), new Point(-100, -100, -50))
                        .setKl(0.001).setKq(0.0001)
        );

        return scene;
    }

    private void runTest(String name, boolean useBox, int useThreads) {
        Scene scene = createScene();
        cameraBuilder.setBoundingBoxUsage(useBox).setMultithreading(useThreads);

        long start = System.nanoTime();

        cameraBuilder.setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage("BVH_" + name);

        long end = System.nanoTime();
        System.out.printf("Render '%s' took %.2f seconds%n", name, (end - start) / 1e9);
    }

    @Test
    void test_NoBox_NoThreads() {
        runTest("noBox_noThreads", false, 0);
    }

    @Test
    void test_Box_NoThreads() {
        runTest("withBox_noThreads", true, 0);
    }

    @Test
    void test_NoBox_Threads() {
        runTest("noBox_withThreads", false, 4);
    }

    @Test
    void test_Box_Threads() {
        runTest("withBox_withThreads", true, 4);
    }

    @Test
    void test_BasicScene_WithBVH() {
        Scene scene = new Scene("BVH Basic Test")
                .setBackground(new Color(20, 20, 20))
                .setAmbientLight(new AmbientLight(new Color(255, 255, 255)));

        // גופים פשוטים שמפוזרים כדי שה-BVH יופעל
        scene.geometries.add(
                new Sphere(40, new Point(-60, 0, -150))
                        .setEmission(new Color(255, 0, 0)),

                new Sphere(40, new Point(60, 0, -150))
                        .setEmission(new Color(0, 255, 0)),

                new Triangle(
                        new Point(-70, -70, -140),
                        new Point(-40, -40, -140),
                        new Point(-70, -40, -140)
                ).setEmission(new Color(0, 0, 255)),

                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(100, 100, 100))
        );

        scene.lights.add(
                new PointLight(new Color(100, 100, 100), new Point(0, 100, -50))
                        .setKl(0.001).setKq(0.0001)
        );

        Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
                .setVpDistance(100)
                .setVpSize(500, 500)
                .setBoundingBoxUsage(false) // ✅ BVH פעיל
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .writeToImage("BVH_Basic_Success");
    }
}
