package renderer;

import geometries.*;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import primitives.Vector;
import scene.Scene;

import java.util.*;

public class BvhTests {

    private Geometries createColoredBox(Point base, double size, Material mat, Color color) {
        Point p1 = base;
        Point p2 = p1.add(new Vector(size, 0, 0));
        Point p3 = p2.add(new Vector(0, size, 0));
        Point p4 = p1.add(new Vector(0, size, 0));
        Point p5 = p1.add(new Vector(0, 0, size));
        Point p6 = p2.add(new Vector(0, 0, size));
        Point p7 = p3.add(new Vector(0, 0, size));
        Point p8 = p4.add(new Vector(0, 0, size));

        Polygon front = new Polygon(p1, p2, p3, p4);
        Polygon back = new Polygon(p5, p6, p7, p8);
        Polygon right = new Polygon(p2, p3, p7, p6);
        Polygon left = new Polygon(p1, p4, p8, p5);
        Polygon top = new Polygon(p4, p3, p7, p8);
        Polygon bottom = new Polygon(p1, p2, p6, p5);

        for (Polygon face : List.of(front, back, right, left, top, bottom)) {
            face.setMaterial(mat);
            face.setEmission(color);
        }

        return new Geometries(front, back, right, left, top, bottom);
    }

    @Test
    void manyObjectsTestNoImprove() {
        Scene scene = new Scene("1000 tiny shapes scene")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Random rand = new Random(123); // לצורך שחזור תוצאה

        for (int i = 0; i < 1000; i++) {
            double x = rand.nextDouble(-500, 500);
            double y = rand.nextDouble(-500, 500);
            double z = rand.nextDouble(-1500, -300);

            int type = i % 3; // 0 = sphere, 1 = box, 2 = cylinder

            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            Material material = new Material()
                    .setKD(0.3)
                    .setKS(0.2)
                    .setShininess(20)
                    .setKR(0.02);

            switch (type) {
                case 0 -> {
                    scene.geometries.add(
                            new Sphere(3, new Point(x, y, z))
                                    .setEmission(color)
                                    .setMaterial(material)
                    );
                }
                case 1 -> {
                    double size = 4;
                    Point p1 = new Point(x, y, z);
                    Point p2 = p1.add(new Vector(size, 0, 0));
                    Point p3 = p2.add(new Vector(0, size, 0));
                    Point p4 = p1.add(new Vector(0, size, 0));
                    Point p5 = p1.add(new Vector(0, 0, size));
                    Point p6 = p2.add(new Vector(0, 0, size));
                    Point p7 = p3.add(new Vector(0, 0, size));
                    Point p8 = p4.add(new Vector(0, 0, size));
                    scene.geometries.add(
                            new Polygon(p1, p2, p3, p4),
                            new Polygon(p5, p6, p7, p8),
                            new Polygon(p1, p2, p6, p5),
                            new Polygon(p4, p3, p7, p8),
                            new Polygon(p1, p4, p8, p5),
                            new Polygon(p2, p3, p7, p6)
                    );
                }
                case 2 -> {
                    scene.geometries.add(
                            new Cylinder(
                                    1.5,
                                    new Ray(new Point(x, y, z), new Vector(0, 1, 0)),
                                    6.0
                            ).setEmission(color)
                                    .setMaterial(material)
                    );
                }
            }

        }

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(1, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(500, 500)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("manyObjectsTestNoImprove");
    }

    @Test
    void manyObjectsTestWithONlyBoxes() {
        Scene scene = new Scene("1000 tiny shapes scene")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Random rand = new Random(123); // לצורך שחזור תוצאה

        for (int i = 0; i < 1000; i++) {
            double x = rand.nextDouble(-500, 500);
            double y = rand.nextDouble(-500, 500);
            double z = rand.nextDouble(-1500, -300);

            int type = i % 3; // 0 = sphere, 1 = box, 2 = cylinder

            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            Material material = new Material()
                    .setKD(0.3)
                    .setKS(0.2)
                    .setShininess(20)
                    .setKR(0.02);

            switch (type) {
                case 0 -> {
                    scene.geometries.add(
                            new Sphere(3, new Point(x, y, z))
                                    .setEmission(color)
                                    .setMaterial(material)
                    );
                }
                case 1 -> {
                    double size = 4;
                    Point p1 = new Point(x, y, z);
                    Point p2 = p1.add(new Vector(size, 0, 0));
                    Point p3 = p2.add(new Vector(0, size, 0));
                    Point p4 = p1.add(new Vector(0, size, 0));
                    Point p5 = p1.add(new Vector(0, 0, size));
                    Point p6 = p2.add(new Vector(0, 0, size));
                    Point p7 = p3.add(new Vector(0, 0, size));
                    Point p8 = p4.add(new Vector(0, 0, size));
                    scene.geometries.add(
                            new Polygon(p1, p2, p3, p4),
                            new Polygon(p5, p6, p7, p8),
                            new Polygon(p1, p2, p6, p5),
                            new Polygon(p4, p3, p7, p8),
                            new Polygon(p1, p4, p8, p5),
                            new Polygon(p2, p3, p7, p6)
                    );
                }
                case 2 -> {
                    scene.geometries.add(
                            new Cylinder(
                                    1.5,
                                    new Ray(new Point(x, y, z), new Vector(0, 1, 0)),
                                    6.0
                            ).setEmission(color)
                                    .setMaterial(material)
                    );
                }
            }

        }
        scene.geometries.setBoundingBox();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(1, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(500, 500)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("manyObjectsTestWithONlyBoxes");
    }

    @Test
    void manyObjectsTestWithbuildBVH() {
        Scene scene = new Scene("1000 tiny shapes scene")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Random rand = new Random(123); // לצורך שחזור תוצאה

        for (int i = 0; i < 1000; i++) {
            double x = rand.nextDouble(-500, 500);
            double y = rand.nextDouble(-500, 500);
            double z = rand.nextDouble(-1500, -300);

            int type = i % 3; // 0 = sphere, 1 = box, 2 = cylinder

            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            Material material = new Material()
                    .setKD(0.3)
                    .setKS(0.2)
                    .setShininess(20)
                    .setKR(0.02);

            switch (type) {
                case 0 -> {
                    scene.geometries.add(
                            new Sphere(3, new Point(x, y, z))
                                    .setEmission(color)
                                    .setMaterial(material)
                    );
                }
                case 1 -> {
                    double size = 4;
                    Point p1 = new Point(x, y, z);
                    Point p2 = p1.add(new Vector(size, 0, 0));
                    Point p3 = p2.add(new Vector(0, size, 0));
                    Point p4 = p1.add(new Vector(0, size, 0));
                    Point p5 = p1.add(new Vector(0, 0, size));
                    Point p6 = p2.add(new Vector(0, 0, size));
                    Point p7 = p3.add(new Vector(0, 0, size));
                    Point p8 = p4.add(new Vector(0, 0, size));
                    scene.geometries.add(
                            new Polygon(p1, p2, p3, p4),
                            new Polygon(p5, p6, p7, p8),
                            new Polygon(p1, p2, p6, p5),
                            new Polygon(p4, p3, p7, p8),
                            new Polygon(p1, p4, p8, p5),
                            new Polygon(p2, p3, p7, p6)
                    );
                }
                case 2 -> {
                    scene.geometries.add(
                            new Cylinder(
                                    1.5,
                                    new Ray(new Point(x, y, z), new Vector(0, 1, 0)),
                                    6.0
                            ).setEmission(color)
                                    .setMaterial(material)
                    );
                }
            }

        }
        scene.geometries.buildBVH(); // Build BVH tree after adding each geometry

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(1, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(500, 500)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("manyObjectsTestWithbuildBVH");
    }

    @Test
    void manyObjectsWithHierarchyTest() {
        Scene scene = new Scene("1000 objects hierarchical scene")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Random rand = new Random(123);
        List<Geometries> groups = new ArrayList<>();

        // Create 10 groups of 100 geometries each
        for (int g = 0; g < 10; g++) {
            Geometries group = new Geometries();

            for (int i = 0; i < 100; i++) {
                double x = rand.nextDouble(-500, 500);
                double y = rand.nextDouble(-500, 500);
                double z = rand.nextDouble(-1500, -300);

                int type = (g * 100 + i) % 3;

                Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

                Material material = new Material()
                        .setKD(0.3)
                        .setKS(0.2)
                        .setShininess(20)
                        .setKR(0.02);

                switch (type) {
                    case 0 -> group.add(
                            new Sphere(3, new Point(x, y, z))
                                    .setEmission(color)
                                    .setMaterial(material)
                    );
                    case 1 -> group.add(createColoredBox(new Point(x, y, z), 4, material, color));
                    case 2 -> group.add(
                            new Cylinder(
                                    1.5,
                                    new Ray(new Point(x, y, z), new Vector(0, 1, 0)),
                                    6.0
                            ).setEmission(color).setMaterial(material)
                    );
                }
            }

            group.setBoundingBox(); // Set bounding box per group
            groups.add(group);
            System.out.println("Group box: " + group.getBoundingBox());
        }

        for (Geometries g : groups) {
            scene.geometries.add(g);
        }

        scene.geometries.setBoundingBox(); // Bounding box for the entire scene

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(500, 500)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("manyObjectsHierarchicalScene");
    }

    @Test
    void manyObjectsTestWith10000Shapes() {
        Scene scene = new Scene("10000 tiny shapes scene")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Random rand = new Random(123); // קבוע לשחזור

        for (int i = 0; i < 100000; i++) {
            double x = rand.nextDouble(-500, 500);
            double y = rand.nextDouble(-500, 500);
            double z = rand.nextDouble(-1500, -300);

            int type = i % 3; // 0 = sphere, 1 = box, 2 = cylinder

            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            Material material = new Material()
                    .setKD(0.3)
                    .setKS(0.2)
                    .setShininess(20)
                    .setKR(0.02);

            switch (type) {
                case 0 -> {
                    scene.geometries.add(
                            new Sphere(3, new Point(x, y, z))
                                    .setEmission(color)
                                    .setMaterial(material)
                    );
                }
                case 1 -> {
                    double size = 4;
                    Point p1 = new Point(x, y, z);
                    Point p2 = p1.add(new Vector(size, 0, 0));
                    Point p3 = p2.add(new Vector(0, size, 0));
                    Point p4 = p1.add(new Vector(0, size, 0));
                    Point p5 = p1.add(new Vector(0, 0, size));
                    Point p6 = p2.add(new Vector(0, 0, size));
                    Point p7 = p3.add(new Vector(0, 0, size));
                    Point p8 = p4.add(new Vector(0, 0, size));
                    scene.geometries.add(
                            new Polygon(p1, p2, p3, p4),
                            new Polygon(p5, p6, p7, p8),
                            new Polygon(p1, p2, p6, p5),
                            new Polygon(p4, p3, p7, p8),
                            new Polygon(p1, p4, p8, p5),
                            new Polygon(p2, p3, p7, p6)
                    );
                }
                case 2 -> {
                    scene.geometries.add(
                            new Cylinder(
                                    1.5,
                                    new Ray(new Point(x, y, z), new Vector(0, 1, 0)),
                                    6.0
                            ).setEmission(color)
                                    .setMaterial(material)
                    );
                }
            }
        }

        scene.geometries.buildBVH();
        //scene.geometries.buildBVH1(); // בונה את העץ לאחר ההוספה

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(1, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(500, 500)
                .setResolution(1000, 1000)
                .setMultithreading(12)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        long start = System.nanoTime();
        camera.renderImage().writeToImage("manyObjectsTestWith10000Shapes");
        long end = System.nanoTime();
        System.out.println("⏱️ Render took: " + (end - start) / 1_000_000.0 + " ms");
    }
}