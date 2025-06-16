package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * This class contains tests for rendering scenes with complex geometries
 */
class TryForBonusTests {

    /**
     * Default constructor to satisfy JavaDoc generator
     */
    TryForBonusTests() { /* to satisfy JavaDoc generator */ }

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
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void beautifulGlassAndFireballNoSoft() {
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
                .writeToImage("beautifulGlassAndFireballNoSoft");
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
     * Test for rotating the camera around a point and rendering the scene.
     * The camera is rotated 90 degrees around the lookAt point.
     */
    @Test
    void testCameraRotationAroundToProducesFlippedImage() {
        Scene scene = buildScene();

        Point location = new Point(0, 0, 1000);
        Point lookAt = new Point(0, 0, -1000);
        Vector up = new Vector(0, 1, 0);

        Camera normalCam = Camera.getBuilder()
                .setLocation(location)
                .setDirection(lookAt, up)
                .setVpDistance(1000)
                .setVpSize(800, 800)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        normalCam.renderImage().writeToImage("rotationTest_normal");

        Camera rotatedCam = Camera.getBuilder(normalCam)
                .rotateAroundTo(90)
                .build();

        rotatedCam.renderImage().writeToImage("rotationTest_rotated90");
    }

    /**
     * Test for moving the camera around a focus point and rendering the scene.
     * The camera moves in steps along the right direction vector.
     */
    @Test
    void testCameraMoveAroundFocusRenderOnly() {
        Scene scene = buildScene();

        Point focus = new Point(0.1, 0, 0);
        Point startLocation = new Point(0, 0, 1000);
        Vector up = new Vector(0, 1, 0);

        Vector stepRight = new Vector(1, 0, 0).scale(900);

        Camera cam = Camera.getBuilder()
                .setLocation(startLocation)
                .setDirection(focus, up)
                .setVpDistance(1000)
                .setResolution(1920, 1080)
                .setVpSize(1920, 1080)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        cam.renderImage().writeToImage("moveAroundFocus1");

        cam = Camera.getBuilder(cam)
                .move(stepRight, focus)
                .build();
        cam.renderImage().writeToImage("moveAroundFocus2");
    }

    /**
     * Test for rendering a scene with glass and fireball reflections.
     * This test sets up a scene with a glass sphere and a fireball, checking the reflections.
     */
    @Test
    void GlassAndFireballReflected() {
        scene.geometries.add(

                new Sphere(400d, new Point(0, -400, -500))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)
                        ),

                new Triangle(
                        new Point(-1500, -1000, -1500),
                        new Point(1500, -1000, -1500),
                        new Point(0, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                                .setGlossiness(0.2, 4, 100)),

                new Triangle(
                        new Point(-5000, -1000, -1500),
                        new Point(-3000, -1000, -1500),
                        new Point(-4000, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                        ),

                new Sphere(300d, new Point(-4000, -500, -2000))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

                new Triangle(
                        new Point(-3000, -1000, -1500),
                        new Point(-1000, -1000, -1500),
                        new Point(-2000, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                                .setGlossiness(0.6, 4, 100)),

                new Polygon(
                        new Point(1500, -1000, -1200),
                        new Point(3000, -1000, -1200),
                        new Point(3000, 1200, -1200),
                        new Point(1500, 1200, -1200))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                                .setGlossiness(0.5, 4, 100)),

                new Sphere(300d, new Point(-2000, -500, -2000))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

                new Sphere(300d, new Point(3200, -400, -1550))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

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
                new SpotLight(new Color(1000, 600, 800),
                        new Point(1.1, -400.2, -1200.2),
                        new Vector(0.2, 0, 1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(200, 200, 250),
                        new Vector(1, -0.5, -1))
        );

        cameraBuilder
                .setLocation(new Point(-2500, 650, 8000))
                .setDirection(new Point(-500, -400, -1000), new Vector(0, 1, 0))
                .setVpDistance(2000)
                .setVpSize(2560, 1440)
                .setResolution(2560, 1440)
                .setDebugPrint(1);

        // ====== RAW_THREADS ======
        System.out.println("🔧 Running with RAW THREADS (4 threads)...");
        long startRaw = System.currentTimeMillis();

        cameraBuilder.setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("GlassAndFireball_RAW_THREADS");

        long endRaw = System.currentTimeMillis();
        System.out.println("✅ Done RAW THREADS. Time: " + (endRaw - startRaw) + " ms\n");

        // ======  Java Streams ======
        System.out.println("🔧 Running with STREAMS...");
        long startStream = System.currentTimeMillis();

        cameraBuilder.setMultithreading(-1)
                .build()
                .renderImage()
                .writeToImage("GlassAndFireball_STREAMS");

        long endStream = System.currentTimeMillis();
        System.out.println("✅ Done STREAMS. Time: " + (endStream - startStream) + " ms\n");
    }

    /**
     * Test for rendering a scene with glass and fireball reflections without glossiness.
     * This test sets up a scene with a glass sphere and a fireball, checking the reflections without glossiness.
     */
    @Test
    void GlassAndFireballReflectedNoGlossiness() {
        scene.geometries.add(

                new Sphere(400d, new Point(0, -400, -500))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)
                        ),

                new Triangle(
                        new Point(-1500, -1000, -1500),
                        new Point(1500, -1000, -1500),
                        new Point(0, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                        ),

                new Triangle(
                        new Point(-5000, -1000, -1500),
                        new Point(-3000, -1000, -1500),
                        new Point(-4000, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                        ),

                new Sphere(300d, new Point(-4000, -500, -2000))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

                new Triangle(
                        new Point(-3000, -1000, -1500),
                        new Point(-1000, -1000, -1500),
                        new Point(-2000, 1200, -1700))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                        ),

                new Polygon(
                        new Point(1500, -1000, -1200),
                        new Point(3000, -1000, -1200),
                        new Point(3000, 1200, -1200),
                        new Point(1500, 1200, -1200))
                        .setEmission(new Color(50, 90, 115))
                        .setMaterial(new Material()
                                .setKS(0.4)
                                .setKD(0.05)
                                .setShininess(200)
                                .setKR(0.8)
                                .setKT(0.85)
                        ),

                new Sphere(300d, new Point(-2000, -500, -2000))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

                new Sphere(300d, new Point(3200, -400, -1550))
                        .setEmission(new Color(110, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.7)
                                .setShininess(250)
                                .setKR(0.0)),

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
                new SpotLight(new Color(1000, 600, 800),
                        new Point(1.1, -400.2, -1200.2),
                        new Vector(0.2, 0, 1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(200, 200, 250),
                        new Vector(1, -0.5, -1))
        );

        cameraBuilder
                .setLocation(new Point(-2500, 650, 8000))
                .setDirection(new Point(-500, -400, -1000), new Vector(0, 1, 0))
                .setVpDistance(2000)
                //.setVpSize(2500, 2500)
                //.setResolution(800, 800)
                .setVpSize(2560, 1440)
                .setResolution(2560, 1440)
                .setDebugPrint(1)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("GlassAndFireballReflectedNoGlossiness");
    }

    //    /**
//     * Produce a picture of a composed scene with separated geometries
//     * and a looped camera animation.
//     */
//    @Test
//    void composedSeparatedSceneForBonusLoop() { // no very mach Resolution!!!!!!!!!!!!!!!!
//        new File("images/bonus").mkdirs();
//
//        Scene scene = buildScene();
//        Point focus = new Point(-200, -600, -350);
//        double radiusX = 4000;
//        double radiusZ = 2500;
//        double baseHeight = 400;
//        double heightAmplitude = 500;
//
//        int totalFrames = 10;
//        int angleStepDegrees = 72;
//
//        for (int frameIndex = 0; frameIndex < totalFrames; frameIndex++) {
//            double angleRad = Math.toRadians(frameIndex * angleStepDegrees);
//            double x = radiusX * Math.cos(angleRad);
//            double z = radiusZ * Math.sin(angleRad);
//            double y = baseHeight + heightAmplitude * Math.sin(angleRad);
//
//            Point camLocation = focus.add(new Vector(x, y, z));
//
//            Camera cam = Camera.getBuilder()
//                    .setLocation(camLocation)
//                    .setDirection(focus, new Vector(0, 1, 0))
//                    .setVpDistance(1300)
//                    .setResolution(800, 450)
//                    .setVpSize(800, 450)
//                    .setRayTracer(scene, RayTracerType.SIMPLE)
//                    .build();
//
//            cam.renderImage().writeToImage(String.format("bonus/composedSceneLooped_%04d", frameIndex));
//        }
//    }

//    version with looped  camera animation involved threads
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

//    @Test
//    void glossyReflectionTest() {
//        Scene scene = new Scene("Glossy Reflection Test")
//                .setBackground(new Color(30, 70, 120))
//                .setAmbientLight(new AmbientLight(new Color(WHITE)));
//
//        scene.geometries.add(
//                new Sphere(100d, new Point(0, 0, -1000))
//                        .setEmission(new Color(50, 80, 150))
//                        .setMaterial(new Material()
//                                .setKT(0.9)
//                                .setKR(0.1)
//                                .setKS(0.8)
//                                .setKD(0.1)
//                                .setShininess(300)
//                                .setGlossiness(0.1, 50)
//                        ),
//
//                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
//                        .setEmission(new Color(20, 50, 100))
//                        .setMaterial(new Material()
//                                .setKR(0.7)
//                                .setKS(0.5)
//                                .setShininess(150)
//                                .setGlossiness(0.2, 80)
//                        )
//
//        );
//
//        scene.lights.add(
//                new SpotLight(new Color(1000, 600, 600),
//                        new Point(0, 300, -800),
//                        new Vector(0, -1, -1))
//        );
//
//        Camera camera = Camera.getBuilder()
//                .setLocation(new Point(0, 0, 1000))
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setVpDistance(1000)
//                .setVpSize(500, 500)
//                .setResolution(800, 800)
//                .setRayTracer(scene, RayTracerType.SIMPLE)
//                .build();
//
//        camera.renderImage()
//                .writeToImage("glossyReflectionTest");
//    }

//    @Test
//    void reflectiveSpheresOnWater() {
//
//        scene.geometries.add(
//                // Main sphere with deep burgundy color and strong highlight band
//                new Sphere(280d, new Point(0, -150, -800))
//                        .setEmission(new Color(60, 10, 30)) // כהה יותר — כמו יין
//                        .setMaterial(new Material()
//                                .setKD(0.2)
//                                .setKS(1.0)
//                                .setShininess(1000)
//                                .setKR(0.2)
//                                .setGlossiness(0.15, 4, 100)), // הרבה יותר קרניים
//
//                new Plane(new Point(0, -600, 0), new Vector(0, 1, 0.07))
//                        .setEmission(new Color(60, 120, 180))
//                        .setMaterial(new Material()
//                                .setKR(0.1)              // לא גבוה מדי - אחרת זה ראי
//                                .setKD(0)             // מים לא מפזרים הרבה
//                                .setKS(0)
//                                .setShininess(0)),
//                //.setGlossiness(0.2, 200)),// טשטוש החזרה
//
//                new Plane(new Point(0, 0, -5000), new Vector(0, 0, 1)) // מישור מאחור
//                        .setEmission(new Color(30, 60, 90))
//                        .setMaterial(new Material()
//                                .setKD(0.8)        // פיזור טוב
//                                .setKS(0.1)        // לא מבריק מדי
//                                .setShininess(50)  // ברק עדין
//                                .setKR(0.0)        // לא מחזיר כלל
//                                .setKT(0.0))     // לא שקוף
//        );
//
//        scene.lights.add(
//                new SpotLight(new Color(600, 600, 600),
//                        new Point(0, 0, 1400),
//                        new Vector(0, 0, -1))
//                        .setKl(0.0002).setKq(0.0001)
//        );
//
//        // Lower ambient light for contrast
//        scene.setAmbientLight(new AmbientLight(new Color(4, 6, 10)));
//
//        scene.lights.add(
//                new SpotLight(new Color(1200, 1000, 1000),
//                        new Point(0, 400, -1000),
//                        new Vector(0, -1, 0.1)) // פוגע בחלק העליון בזווית שטוחה
//                        .setKl(0.0003).setKq(0.0001)
//        );
//
//        scene.lights.add(
//                new SpotLight(new Color(1200, 1200, 1000),
//                        new Point(-120, 150, -1200),
//                        new Vector(120, -150, 400).normalize())
//                        .setKl(0.00005).setKq(0.00001)
//        );
//        scene.lights.add(
//                new DirectionalLight(new Color(100, 100, 150), new Vector(-1, -0.3, -1))
//        );
//
//        scene.lights.add(
//                new SpotLight(new Color(1200, 1200, 1000),
//                        new Point(120, 150, -1200),
//                        new Vector(-120, -150, 400).normalize())
//                        .setKl(0.00005).setKq(0.00001)
//        );
//        scene.lights.add(
//                new SpotLight(new Color(1500, 1500, 1500),
//                        new Point(-200, 200, -1200),
//                        new Vector(200, -350, 400).normalize())  // אל הכדור
//                        .setKl(0.0003).setKq(0.0001));
//
//        scene.lights.add(
//                new SpotLight(new Color(1500, 1500, 1500),
//                        new Point(-200, 200, -1200),
//                        new Vector(200, -350, 400).normalize())  // אל הכדור
//                        .setKl(0.0003).setKq(0.0001));
//
//        // Light panel polygon behind the camera, acting as an area light source
//        scene.geometries.add(
//                new Polygon(
//                        new Point(-50000, 250, 1500),   // Much wider
//                        new Point(50000, 250, 1500),
//                        new Point(50000, -2000, 1500),    // Narrower height for band effect
//                        new Point(-50000, -2000, 1500)
//                )
//                        .setEmission(new Color(1200, 1200, 1200)) // Higher intensity
//                        .setMaterial(new Material()
//                                .setKD(0.0)
//                                .setKS(0.0)
//                                .setKR(0.0)
//                                .setKT(0.0))
//        );
//
//        // Camera
//        cameraBuilder
//                .setLocation(new Point(0, -200, 1000))
//                .setDirection(new Point(0, 0, -800), new Vector(0, 1, 0))
//                .setVpDistance(1000)
//                .setVpSize(1600, 1600)
//                .setResolution(800, 800)
//                .build()
//                .renderImage()
//                .writeToImage("reflectiveSpheresOnWater_Final_Attempt");
//    }

}
