package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * This class contains tests for rendering scenes with complex geometries
 */
public class tryTestsForBonus {

    /**
     * Default constructor to satisfy JavaDoc generator
     */
    tryTestsForBonus() { /* to satisfy JavaDoc generator */ }

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Builds a scene with various geometries and lights for testing.
     * This scene includes spheres, triangles, cylinders, tubes, polygons, and planes,
     * with different materials and light sources.
     *
     * @return the constructed Scene object
     */
    private Scene buildScene() {
        Scene scene = new Scene("Test scene2");

        Point leftBase1 = new Point(-450, -700, -600);
        Point leftBase2 = new Point(-100, -700, -700);
        Point rightBase1 = new Point(-100, -700, -700);
        Point rightBase2 = new Point(250, -700, -450);
        Point topLeft = new Point(-275, -300, -600);
        Point topRight = new Point(75, -300, -600);

        scene.geometries.add(

                new Sphere(100, new Point(-200, -600, -350))
                        .setEmission(new Color(30, 80, 150))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.9).setShininess(300)
                                .setKT(0.6).setKR(0.25)),

                new Sphere(100, new Point(-750, -600, -950))
                        .setEmission(new Color(150, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.8).setShininess(250)),

                new Triangle(
                        new Point(-1000, -700, -1300),
                        new Point(-500, -700, -1300),
                        new Point(-750, -300, -1300))
                        .setEmission(new Color(80, 30, 120))
                        .setMaterial(new Material()
                                .setKD(0.15).setKS(0.9).setShininess(250).setKR(0.75)),

                new Triangle(leftBase1, leftBase2, topLeft)
                        .setEmission(new Color(80, 30, 120))
                        .setMaterial(new Material()
                                .setKD(0.15).setKS(0.9).setShininess(250).setKR(0.75)),

                new Triangle(rightBase1, rightBase2, topRight)
                        .setEmission(new Color(80, 30, 120))
                        .setMaterial(new Material()
                                .setKD(0.15).setKS(0.9).setShininess(250).setKR(0.75)),

                new Triangle(topLeft, topRight, rightBase1)
                        .setEmission(new Color(80, 30, 120))
                        .setMaterial(new Material()
                                .setKD(0.15).setKS(0.9).setShininess(250).setKR(0.75)),

                new Cylinder(80, new Ray(new Point(800, -700, -900), new Vector(0, 1, 0)), 400)
                        .setEmission(new Color(140, 110, 40))
                        .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(200)),

                new Cylinder(
                        80,
                        new Ray(new Point(0, -300, -1800), new Vector(0, 1, 0)),
                        400)
                        .setEmission(new Color(20, 200, 180))
                        .setMaterial(new Material()
                                .setKD(0.3)
                                .setKS(0.2)
                                .setShininess(100)
                        ),


                new Tube(
                        60,
                        new Ray(new Point(-1000, -1000, -4050), new Vector(1, 1, 0))
                )
                        .setEmission(new Color(120, 20, 30))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.6).setShininess(200)),


                new Tube(
                        60,
                        new Ray(new Point(1000, -1000, -4050), new Vector(-1, 1, 0))
                )
                        .setEmission(new Color(120, 20, 30))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.6).setShininess(200)),

                new Polygon(
                        new Point(-200, -700, -1600),
                        new Point(200, -700, -1600),
                        new Point(200, -700, -2000),
                        new Point(-200, -700, -2000))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.9)
                                .setShininess(400)
                                .setKR(0.3)),

                new Polygon(
                        new Point(-200, -700, -2000),
                        new Point(200, -700, -2000),
                        new Point(200, -300, -2000),
                        new Point(-200, -300, -2000))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.8)
                                .setShininess(300)
                                .setKT(0.3)),

                new Polygon(
                        new Point(-200, -300, -1600),
                        new Point(200, -300, -1600),
                        new Point(200, -300, -2000),
                        new Point(-200, -300, -2000))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.9)
                                .setShininess(400)
                                .setKR(0.3)),

                new Polygon(
                        new Point(-200, -700, -1600),
                        new Point(-200, -700, -2000),
                        new Point(-200, -300, -2000),
                        new Point(-200, -300, -1600))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.9)
                                .setShininess(400)
                                .setKR(0.3)),

                new Polygon(
                        new Point(200, -700, -1600),
                        new Point(200, -700, -2000),
                        new Point(200, -300, -2000),
                        new Point(200, -300, -1600))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.9)
                                .setShininess(400)
                                .setKR(0.3)),

                new Polygon(
                        new Point(-200, -700, -1600),
                        new Point(200, -700, -1600),
                        new Point(200, -300, -1600),
                        new Point(-200, -300, -1600))
                        .setEmission(new Color(80, 40, 120))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.8)
                                .setShininess(300)
                                .setKT(0.3)),

                new Sphere(100, new Point(0, -500, -1800))
                        .setEmission(new Color(30, 80, 150))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.9).setShininess(300)
                                .setKT(0.6).setKR(0.25)),

                new Plane(new Point(0, -700, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(100).setKR(0.25))
        );

        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        scene.lights.add(
                new SpotLight(new Color(800, 600, 600),
                        new Point(0, 1000, 800),
                        new Vector(0, -1.3, -1.3))
                        .setKl(0.0001).setKq(0.00005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(120, 120, 150), new Vector(-1, -1, -0.5))
        );
        return scene;

    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void beautifulGlassAndFireball() {
        scene.geometries.add(

                new Sphere(400d, new Point(-500, -400, -1000))
                        .setEmission(new Color(30, 80, 150))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.9).setShininess(300)
                                .setKT(0.6).setKR(0.25)),


                new Sphere(400d, new Point(300, -400, -1000))
                        .setEmission(new Color(150, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.8).setShininess(250)),


                new Triangle(
                        new Point(-1500, -1000, -1500),
                        new Point(1500, -1000, -1500),
                        new Point(0, 1200, -1700))
                        .setEmission(new Color(80, 30, 120))
                        .setMaterial(new Material()
                                .setKD(0.15)
                                .setKS(0.9)
                                .setShininess(250)
                                .setKR(0.75)),


                new Plane(new Point(0, -800, 0), new Vector(0, 1, 0))
                        .setMaterial(new Material()
                                .setKR(0.2).setKD(0.4).setKS(0.6).setShininess(100))
                        .setEmission(new Color(20, 20, 20))
        );


        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        scene.lights.add(
                new SpotLight(new Color(1000, 600, 800),
                        new Point(0, 800, 1000),
                        new Vector(0, -1, -2).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );


        scene.lights.add(
                new DirectionalLight(new Color(200, 200, 250),
                        new Vector(1, -0.5, -1))
        );


        cameraBuilder
                .setLocation(new Point(600, 650, 2000))
                .setDirection(new Point(100, -400, -1000), new Vector(0, 1, 0))
                .setVpDistance(2000)
                .setVpSize(2500, 2500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("beautifulGlassAndFireball");
    }

    /**
     * Produce a picture of a composed scene with separated geometries
     * and a camera looking at the scene.
     */
    @Test
    void composedSeparatedSceneForBonus() {
        Scene scene = buildScene();
        cameraBuilder
                .setLocation(new Point(-1600, -200, 2165))
                .setDirection(new Point(0, -500, -1500), new Vector(0, 1, 0))
                .setVpDistance(1600)
                .setVpSize(2560, 1440)
                .setResolution(2560, 1440)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("composedSeparatedScene");
    }

    /**
     * Produce a picture of a composed scene with separated geometries
     * and a looped camera animation.
     */
    @Test
    void composedSeparatedSceneForBonusLoop() { // no very mach Resolution!!!!!!!!!!!!!!!!
        new File("images/bonus").mkdirs();

        Scene scene = buildScene();
        Point focus = new Point(-200, -600, -350);
        double radiusX = 4000;
        double radiusZ = 2500;
        double baseHeight = 400;
        double heightAmplitude = 500;

        int totalFrames = 10;
        int angleStepDegrees = 72;

        for (int frameIndex = 0; frameIndex < totalFrames; frameIndex++) {
            double angleRad = Math.toRadians(frameIndex * angleStepDegrees);
            double x = radiusX * Math.cos(angleRad);
            double z = radiusZ * Math.sin(angleRad);
            double y = baseHeight + heightAmplitude * Math.sin(angleRad);

            Point camLocation = focus.add(new Vector(x, y, z));

            Camera cam = Camera.getBuilder()
                    .setLocation(camLocation)
                    .setDirection(focus, new Vector(0, 1, 0))
                    .setVpDistance(1300)
                    .setResolution(800, 450)
                    .setVpSize(800, 450)
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .build();

            cam.renderImage().writeToImage(String.format("bonus/composedSceneLooped_%04d", frameIndex));
        }
    }


//    virson with looped  camera animation involved threads
//
    //
//    /**
//     * Produce a picture of a composed scene with separated geometries
//     * and a looped camera animation.
//     */
//    @Test
//    void composedSeparatedSceneForBonusLoopWithThreads() {
//        new File("images/bonus").mkdirs();
//
//        Scene scene = buildScene();
//        Point focus = new Point(-200, -600, -350);
//
//        double radiusX = 4000;
//        double radiusZ = 2500;
//        double baseHeight = 400;
//        double heightAmplitude = 500;
//
//        int totalFrames = 60;
//        int totalRenderFrames = 2 * totalFrames - 2;
//        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        for (int frameIndex = 0; frameIndex < totalRenderFrames; frameIndex++) {
//            final int i = (frameIndex < totalFrames)
//                    ? frameIndex
//                    : totalFrames * 2 - 2 - frameIndex;
//
//            final int currentFrame = frameIndex;
//
//            executor.submit(() -> {
//                double angleRad = Math.toRadians(i * 6);
//                double x = radiusX * Math.cos(angleRad);
//                double z = radiusZ * Math.sin(angleRad);
//                double y = baseHeight + heightAmplitude * Math.sin(angleRad);
//
//                Point camLocation = focus.add(new Vector(x, y, z));
//
//                Camera cam = Camera.getBuilder()
//                        .setLocation(camLocation)
//                        .setDirection(focus, new Vector(0, 1, 0))
//                        .setVpDistance(1300)
//                        .setVpSize(2560, 1440)
//                        .setResolution(2560, 1440)
//                        .setRayTracer(scene, RayTracerType.SIMPLE)
//                        .build();
//
//                String filename = String.format("bonus/composedSceneLooped_%04d", currentFrame);
//                cam.renderImage().writeToImage(filename);
//            });
//
//        }
//        executor.shutdown();
//        try {
//            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
//                System.err.println("Timeout! Some frames may not have finished rendering.");
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Test method for rotating the camera around the "to" point.
     * The camera should maintain its direction while rotating around the "to" point.
     */
    @Test
    void testCameraRotationAroundTo() {
        Point location = new Point(0, 0, 0);
        Point lookAt = new Point(0, 0, -1);
        Vector up = new Vector(0, 1, 0);

        Camera original = Camera.getBuilder()
                .setLocation(location)
                .setDirection(lookAt, up)
                .setVpDistance(100)
                .setVpSize(200, 200)
                .setResolution(10, 10)
                .build();

        Camera rotated = Camera.getBuilder(original)
                .rotateAroundTo(90)
                .build();

        assertEquals(original.getTo(), rotated.getTo(), "Camera direction (to) should not change after rotation around to");

        Vector expectedUp = new Vector(1, 0, 0);

        double dot = rotated.getUp().normalize().dotProduct(expectedUp);
        assertTrue(Math.abs(dot) > 0.999, "Camera up vector is not aligned with expected direction after rotation");
    }

    /**
     * Test method for
     * moving the camera while keeping the focus on the original "to" point.
     */
    @Test
    void testCameraMoveKeepsFocus() {
        Point originalLocation = new Point(0, 0, 0);
        Point lookAt = new Point(0, 0, -1);
        Vector up = new Vector(0, 1, 0);

        Camera camera = Camera.getBuilder()
                .setLocation(originalLocation)
                .setDirection(lookAt, up)
                .setVpDistance(100)
                .setVpSize(200, 200)
                .setResolution(10, 10)
                .move(new Vector(0, 0, 10))
                .build();

        assertEquals(new Point(0, 0, 10), camera.getLocation(), "Camera didn't move correctly");

        Vector expectedTo = new Vector(0, 0, -1).subtract(new Vector(0, 0, 10)).normalize();  // כלומר כיוון אל lookAt המקורי
        assertEquals(expectedTo, camera.getTo(), "Camera direction after move is wrong");
    }
}
