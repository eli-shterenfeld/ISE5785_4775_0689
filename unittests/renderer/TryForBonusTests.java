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
                .setDirection(new Point(-500, -400, -1000))
                .setVpDistance(2000)
                .setVpSize(2560, 1440)
                .setResolution(2560, 1440)
                .setDebugPrint(0.1)
                .setMultithreading(-1)
                .build()
                .renderImage()
                .writeToImage("GlassAndFireball");
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
                .setDirection(new Point(-500, -400, -1000), Vector.AXIS_Y)
                .setVpDistance(2000)
                .setVpSize(2560, 1440)
                .setResolution(2560, 1440)
                .setDebugPrint(1)
                .setMultithreading(-1)
                .build()
                .renderImage()
                .writeToImage("GlassAndFireballReflectedNoGlossiness");
    }

    /**
     * Produce a picture of a box, sphere and cylinder
     * The scene includes a floor, a sphere, a box (cube), and a cylinder,
     * all with specific materials and lighting.
     */
    @Test
    void boxSphereCylinderNoSoftShadow() {
        // Add floor (plane)
        scene.geometries.add(
                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1))
                        .setEmission(new Color(70, 70, 70))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(20))
        );

        // Add sphere
        scene.geometries.add(
                new Sphere(40, new Point(-40, -60, 50))
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Add box (cube, 6 polygons)
        double cubeSize = 70;
        Point cubeBase = new Point(40, 30, cubeSize / 2);
        scene.geometries.add(
                // Front face
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                // Back face
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                // 4 sides
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Add cylinder
        scene.geometries.add(
                new Cylinder(
                        25, new Ray(new Point(130, -30, 0), new Vector(0, 0, 1))
                        , 110
                ).setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Light source with soft shadow
        scene.lights.add(
                new PointLight(new Color(500, 500, 500), new Point(100, 150, 200))
                        .setKl(0.0008).setKq(0.0001)
        );

        // Ambient light
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30).scale(0.03)));

        cameraBuilder
                .setLocation(new Point(600, 650, 2000))
                .setLocation(new Point(70, 80, 320))
                .setDirection(new Point(0, 0, 0), new Vector(0, 0, 1))
                .setVpDistance(330)
                .setVpSize(350, 350)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("boxSphereCylinderNoSoftShadow");
    }

    /**
     * Produce a picture of a box, sphere and cylinder but with soft shadows.
     * The scene includes a floor, a sphere, a box (cube), and a cylinder,
     * all with specific materials and lighting.
     */
    @Test
    void boxSphereCylinderSoftShadow() {
        // Add floor (plane)
        scene.geometries.add(
                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1))
                        .setEmission(new Color(70, 70, 70))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(20))
        );

        // Add sphere
        scene.geometries.add(
                new Sphere(40, new Point(-40, -60, 50))
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Add box (cube, 6 polygons)
        double cubeSize = 70;
        Point cubeBase = new Point(40, 30, cubeSize / 2);
        scene.geometries.add(
                // Front face
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                // Back face
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                // 4 sides
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(-cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)),
                new Polygon(
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, cubeSize / 2, cubeSize / 2)),
                        cubeBase.add(new Vector(cubeSize / 2, -cubeSize / 2, cubeSize / 2))
                ).setEmission(new Color(80, 80, 80)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Add cylinder
        scene.geometries.add(
                new Cylinder(
                        25, new Ray(new Point(130, -30, 0), new Vector(0, 0, 1))
                        , 110
                ).setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40))
        );

        // Light source with soft shadow
        scene.lights.add(
                new PointLight(new Color(500, 500, 500), new Point(100, 150, 200))
                        .setAreaLightRadius(50, 100) // soft shadow radius
                        .setKl(0.0008).setKq(0.0001)
        );

        // Ambient light
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30).scale(0.03)));

        cameraBuilder
                .setLocation(new Point(600, 650, 2000))
                .setLocation(new Point(70, 80, 320))
                .setDirection(new Point(0, 0, 0), new Vector(0, 0, 1))
                .setVpDistance(330)
                .setVpSize(350, 350)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("boxSphereCylinderSoftShadow");
    }
}
