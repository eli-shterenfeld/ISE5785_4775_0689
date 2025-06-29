package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * BVH rendering tests with performance measurements.
 * <p>
 * Tests three setups:
 * - Flat CBR only (no hierarchy)
 * - Manual BVH hierarchy
 * - Automatic BVH with SAH
 * <p>
 * Each test runs with and without multithreading (MT/NoMT).
 * Render time is printed in milliseconds with the test name.
 */
public class BvhTests {

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    // =========================== for idny hirrarcy ============================================

    /**
     * Creates a complex tree structure at the specified base point.
     * This method builds a tree with trunk, branches, leaves, and roots.
     *
     * @param basePoint the point where the tree's base is located
     * @return a Geometries object representing the entire tree structure
     */
    public static Geometries createTreeAt(Point basePoint) {
        Geometries tree = new Geometries();

        double baseX = basePoint.getX();
        double baseY = basePoint.getY();
        double baseZ = basePoint.getZ();

        // === גזע העץ – בסיס משושה ===
        tree.add(
                new Polygon(
                        new Point(baseX - 60, baseY, baseZ - 40),
                        new Point(baseX - 30, baseY, baseZ - 90),
                        new Point(baseX + 30, baseY, baseZ - 90),
                        new Point(baseX + 60, baseY, baseZ - 40),
                        new Point(baseX + 30, baseY, baseZ + 10),
                        new Point(baseX - 30, baseY, baseZ + 10))
                        .setEmission(new Color(100, 60, 30))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(50))
        );

        // === חלקי גזע – 8 טבעות ===
        for (int i = 0; i < 8; i++) {
            double y0 = baseY + i * 60;
            double y1 = baseY + (i + 1) * 60;
            double r0 = 50 - i * 3;
            double r1 = r0 - 3;

            tree.add(
                    new Polygon(
                            new Point(baseX - r0, y0, baseZ - 50),
                            new Point(baseX - r1, y1, baseZ - 50),
                            new Point(baseX + r1, y1, baseZ - 50),
                            new Point(baseX + r0, y0, baseZ - 50))
                            .setEmission(new Color(120 - i * 5, 70 - i * 3, 35))
                            .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX + r0, y0, baseZ - 50),
                            new Point(baseX + r1, y1, baseZ - 50),
                            new Point(baseX + r1, y1, baseZ + 50),
                            new Point(baseX + r0, y0, baseZ + 50))
                            .setEmission(new Color(110 - i * 5, 65 - i * 3, 30))
                            .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX + r0, y0, baseZ + 50),
                            new Point(baseX + r1, y1, baseZ + 50),
                            new Point(baseX - r1, y1, baseZ + 50),
                            new Point(baseX - r0, y0, baseZ + 50))
                            .setEmission(new Color(115 - i * 5, 68 - i * 3, 32))
                            .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX - r0, y0, baseZ + 50),
                            new Point(baseX - r1, y1, baseZ + 50),
                            new Point(baseX - r1, y1, baseZ - 50),
                            new Point(baseX - r0, y0, baseZ - 50))
                            .setEmission(new Color(108 - i * 5, 63 - i * 3, 28))
                            .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(40))
            );
        }

        // === ענפים ראשיים ===
        double trunkTop = baseY + 450;

        // ימני עליון
        tree.add(
                new Triangle(new Point(baseX, trunkTop, baseZ),
                        new Point(baseX + 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX + 120, trunkTop + 70, baseZ + 80))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX + 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX + 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX + 220, trunkTop + 140, baseZ + 130))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX + 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX + 320, trunkTop + 230, baseZ + 200),
                        new Point(baseX + 290, trunkTop + 200, baseZ + 180))
                        .setEmission(new Color(70, 40, 18))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30))
        );

        // שמאלי עליון
        tree.add(
                new Triangle(new Point(baseX, trunkTop, baseZ),
                        new Point(baseX - 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX - 120, trunkTop + 70, baseZ + 80))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX - 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX - 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX - 220, trunkTop + 140, baseZ + 130))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX - 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX - 320, trunkTop + 230, baseZ + 200),
                        new Point(baseX - 290, trunkTop + 200, baseZ + 180))
                        .setEmission(new Color(70, 40, 18))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30))
        );

        // קדמי עליון
        tree.add(
                new Triangle(new Point(baseX, trunkTop, baseZ),
                        new Point(baseX + 50, trunkTop + 100, baseZ + 200),
                        new Point(baseX + 20, trunkTop + 70, baseZ + 180))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX + 50, trunkTop + 100, baseZ + 200),
                        new Point(baseX + 100, trunkTop + 170, baseZ + 300),
                        new Point(baseX + 70, trunkTop + 140, baseZ + 280))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30))
        );

        // אחורי עליון
        tree.add(
                new Triangle(new Point(baseX, trunkTop, baseZ),
                        new Point(baseX - 50, trunkTop + 100, baseZ - 200),
                        new Point(baseX - 20, trunkTop + 70, baseZ - 180))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30)),
                new Triangle(new Point(baseX - 50, trunkTop + 100, baseZ - 200),
                        new Point(baseX - 100, trunkTop + 170, baseZ - 300),
                        new Point(baseX - 70, trunkTop + 140, baseZ - 280))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(30))
        );

        // === עלים ימניים ושמאליים ===
        for (int i = 0; i < 15; i++) {
            double randX = Math.random() * 40 - 20;
            double randY = Math.random() * 30 - 15;
            double randZ = Math.random() * 100 - 50;

            double xR = baseX + 200 + i * 20 + randX;
            double yR = baseY + 500 + i * 15 + randY;
            double zR = baseZ + 100 + randZ;

            double xL = baseX - 200 - i * 20 - randX;
            double yL = yR;
            double zL = zR;

            tree.add(
                    new Triangle(new Point(xR, yR, zR),
                            new Point(xR + 25, yR + 15, zR + 10),
                            new Point(xR + 10, yR + 35, zR - 5))
                            .setEmission(new Color(30 + i, 120 + i * 2, 40 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3)),
                    new Triangle(new Point(xR, yR, zR),
                            new Point(xR + 10, yR + 35, zR - 5),
                            new Point(xR - 15, yR + 20, zR + 8))
                            .setEmission(new Color(25 + i, 110 + i * 2, 35 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3)),

                    new Triangle(new Point(xL, yL, zL),
                            new Point(xL - 25, yL + 15, zL + 10),
                            new Point(xL - 10, yL + 35, zL - 5))
                            .setEmission(new Color(35 + i, 130 + i * 2, 45 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3)),
                    new Triangle(new Point(xL, yL, zL),
                            new Point(xL - 10, yL + 35, zL - 5),
                            new Point(xL + 15, yL + 20, zL + 8))
                            .setEmission(new Color(30 + i, 125 + i * 2, 40 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3))
            );
        }

        // === עלים קדמיים ===
        for (int i = 0; i < 12; i++) {
            double randX = Math.random() * 30 - 15;
            double randY = Math.random() * 25 - 12;

            double x = baseX - 60 + i * 10 + randX;
            double y = baseY + 520 + i * 12 + randY;
            double z = baseZ + 250 + Math.random() * 80 - 40;

            tree.add(
                    new Triangle(new Point(x, y, z),
                            new Point(x + 20, y + 12, z + 8),
                            new Point(x + 8, y + 30, z - 3))
                            .setEmission(new Color(40 + i * 2, 140 + i * 3, 50 + i * 2))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3)),
                    new Triangle(new Point(x, y, z),
                            new Point(x + 8, y + 30, z - 3),
                            new Point(x - 12, y + 18, z + 6))
                            .setEmission(new Color(35 + i * 2, 135 + i * 3, 45 + i * 2))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10).setKT(0.3))
            );
        }

        // === כתר עליון ===
        double crownY = baseY + 600;
        tree.add(
                new Triangle(new Point(baseX - 200, crownY, baseZ + 200),
                        new Point(baseX + 200, crownY, baseZ + 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(40, 150, 60))
                        .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(20).setKT(0.2)),
                new Triangle(new Point(baseX - 200, crownY, baseZ + 200),
                        new Point(baseX, crownY + 150, baseZ),
                        new Point(baseX - 100, crownY + 50, baseZ - 200))
                        .setEmission(new Color(45, 160, 65))
                        .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(20).setKT(0.2)),
                new Triangle(new Point(baseX + 200, crownY, baseZ + 200),
                        new Point(baseX + 100, crownY + 50, baseZ - 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(50, 170, 70))
                        .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(20).setKT(0.2)),
                new Triangle(new Point(baseX - 100, crownY + 50, baseZ - 200),
                        new Point(baseX + 100, crownY + 50, baseZ - 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(55, 180, 75))
                        .setMaterial(new Material().setKD(0.5).setKS(0.2).setShininess(20).setKT(0.2))
        );

        // === שורשים ===
        tree.add(
                new Polygon(new Point(baseX - 80, baseY, baseZ - 20),
                        new Point(baseX - 120, baseY - 50, baseZ + 20),
                        new Point(baseX - 100, baseY - 100, baseZ + 40),
                        new Point(baseX - 60, baseY - 50, baseZ))
                        .setEmission(new Color(60, 40, 20))
                        .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10)),
                new Polygon(new Point(baseX + 80, baseY, baseZ - 20),
                        new Point(baseX + 120, baseY - 50, baseZ + 20),
                        new Point(baseX + 100, baseY - 100, baseZ + 40),
                        new Point(baseX + 60, baseY - 50, baseZ))
                        .setEmission(new Color(65, 45, 25))
                        .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10)),
                new Polygon(new Point(baseX, baseY, baseZ + 100),
                        new Point(baseX + 40, baseY - 50, baseZ + 140),
                        new Point(baseX + 20, baseY - 100, baseZ + 160),
                        new Point(baseX - 20, baseY - 50, baseZ + 120))
                        .setEmission(new Color(70, 50, 30))
                        .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10))
        );

        // === קופסת גבול לכל העץ ===
        tree.setBoundingBox();
        return tree;
    }

    /**
     * Creates a stone path as a Geometries node.
     * The path consists of multiple hexagonal stones arranged in a line.
     *
     * @return a Geometries object representing the stone path
     */
    public static Geometries createStonePath() {
        Geometries pathGroup = new Geometries();   // רמת-על של כל האבנים

        /* === הגדרות חומר וצבע משותפות === */
        Color stoneColor = new Color(120, 115, 100);
        Material stoneMaterial = new Material()
                .setKD(0.7).setKS(0.2).setShininess(20)
                .setKR(0.05);

        /* === אבנים “רגילות” לאורך השביל === */
        for (int i = 0; i < 25; i++) {
            double z = 300 - i * 250;             // הציר Z לאורך השביל

            for (int j = -1; j <= 1; j++) {       // רוחב השביל
                double x = j * 80;

                double randomX = x + (Math.random() - 0.5) * 20;
                double randomZ = z + (Math.random() - 0.5) * 30;
                double stoneSize = 35 + Math.random() * 15;

                pathGroup.add(
                        buildStone(randomX, randomZ, stoneSize,
                                stoneColor, stoneMaterial));
            }
        }

        /* === אבנים גדולות בצדדים לשוני === */
        for (int i = 0; i < 8; i++) {
            double z = 200 - i * 800;

            pathGroup.add(
                    buildStone(225, z, 60,
                            stoneColor.scale(0.9), stoneMaterial));
            pathGroup.add(
                    buildStone(-225, z, 60,
                            stoneColor.scale(0.9), stoneMaterial));
        }

        pathGroup.setBoundingBox();              // קופסה לרמת-העל
        return pathGroup;
    }

    /**
     * Builds a single hexagonal stone geometry.
     *
     * @param centerX  the X coordinate of the stone's center
     * @param centerZ  the Z coordinate of the stone's center
     * @param size     the size of the stone
     * @param color    the color of the stone
     * @param material the material properties of the stone
     * @return a Geometries object representing the stone
     */
    private static Geometries buildStone(double centerX, double centerZ, double size,
                                         Color color, Material material) {

        Geometries stone = new Geometries();

        Point[] top = new Point[6];
        Point[] bottom = new Point[6];

        double topY = -97;
        double bottomY = -99.5;

        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3.0;            // 60°
            double radiusVar = 0.8 + Math.random() * 0.4;
            double r = (size / 2.0) * radiusVar;

            double x = centerX + r * Math.cos(angle);
            double z = centerZ + r * Math.sin(angle);

            top[i] = new Point(x, topY, z);
            bottom[i] = new Point(x, bottomY, z);
        }

        /* --- פאות עליונות --- */
        for (int i = 1; i < 5; i++) {
            stone.add(new Triangle(top[0], top[i], top[i + 1])
                    .setEmission(color)
                    .setMaterial(material));
        }

        /* --- פאות תחתונות --- */
        for (int i = 1; i < 5; i++) {
            stone.add(new Triangle(bottom[0], bottom[i + 1], bottom[i])
                    .setEmission(color.scale(0.8))
                    .setMaterial(material));
        }

        /* --- צדדים (6*2 משולשים) --- */
        for (int i = 0; i < 6; i++) {
            int next = (i + 1) % 6;

            stone.add(new Triangle(bottom[i], top[i], top[next])
                    .setEmission(color.scale(0.9))
                    .setMaterial(material));
            stone.add(new Triangle(bottom[i], top[next], bottom[next])
                    .setEmission(color.scale(0.9))
                    .setMaterial(material));
        }

        stone.setBoundingBox();                 // קופסה לכל אבן בודדת
        return stone;
    }

    // =========================== for auto hierarchy ============================================

    /**
     * יוצרת עץ מורכב בנקודה נתונה
     *
     * @param scene     הסצנה להוספת הגיאומטריות
     * @param basePoint הנקודה שבה יוצב בסיס העץ (נקודת האמצע של הבסיס)
     */
    public static void createTreeAt(Scene scene, Point basePoint) {
        double baseX = basePoint.getX();
        double baseY = basePoint.getY();
        double baseZ = basePoint.getZ();

        // === גזע העץ - בנוי מפוליגונים ===
        // בסיס הגזע - פוליגון משושה
        scene.geometries.add(
                new Polygon(
                        new Point(baseX - 60, baseY, baseZ - 40),
                        new Point(baseX - 30, baseY, baseZ - 90),
                        new Point(baseX + 30, baseY, baseZ - 90),
                        new Point(baseX + 60, baseY, baseZ - 40),
                        new Point(baseX + 30, baseY, baseZ + 10),
                        new Point(baseX - 30, baseY, baseZ + 10))
                        .setEmission(new Color(100, 60, 30))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(50))
        );

        // חלקי גזע - 8 פוליגונים בגבהים שונים
        for (int i = 0; i < 8; i++) {
            double y = baseY + (i * 60);
            double topY = baseY + ((i + 1) * 60);
            double radius = 50 - (i * 3);
            double topRadius = radius - 3;

            scene.geometries.add(
                    new Polygon(
                            new Point(baseX - radius, y, baseZ - 50),
                            new Point(baseX - topRadius, topY, baseZ - 50),
                            new Point(baseX + topRadius, topY, baseZ - 50),
                            new Point(baseX + radius, y, baseZ - 50))
                            .setEmission(new Color(120 - i * 5, 70 - i * 3, 35))
                            .setMaterial(new Material()
                                    .setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX + radius, y, baseZ - 50),
                            new Point(baseX + topRadius, topY, baseZ - 50),
                            new Point(baseX + topRadius, topY, baseZ + 50),
                            new Point(baseX + radius, y, baseZ + 50))
                            .setEmission(new Color(110 - i * 5, 65 - i * 3, 30))
                            .setMaterial(new Material()
                                    .setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX + radius, y, baseZ + 50),
                            new Point(baseX + topRadius, topY, baseZ + 50),
                            new Point(baseX - topRadius, topY, baseZ + 50),
                            new Point(baseX - radius, y, baseZ + 50))
                            .setEmission(new Color(115 - i * 5, 68 - i * 3, 32))
                            .setMaterial(new Material()
                                    .setKD(0.5).setKS(0.2).setShininess(40)),

                    new Polygon(
                            new Point(baseX - radius, y, baseZ + 50),
                            new Point(baseX - topRadius, topY, baseZ + 50),
                            new Point(baseX - topRadius, topY, baseZ - 50),
                            new Point(baseX - radius, y, baseZ - 50))
                            .setEmission(new Color(108 - i * 5, 63 - i * 3, 28))
                            .setMaterial(new Material()
                                    .setKD(0.5).setKS(0.2).setShininess(40))
            );
        }

        // === ענפים ראשיים - בנויים ממשולשים ===
        double trunkTop = baseY + 450; // גובה הגזע

        // ענף ימני עליון
        scene.geometries.add(
                new Triangle(
                        new Point(baseX, trunkTop, baseZ),
                        new Point(baseX + 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX + 120, trunkTop + 70, baseZ + 80))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX + 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX + 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX + 220, trunkTop + 140, baseZ + 130))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX + 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX + 320, trunkTop + 230, baseZ + 200),
                        new Point(baseX + 290, trunkTop + 200, baseZ + 180))
                        .setEmission(new Color(70, 40, 18))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30))
        );

        // ענף שמאלי עליון
        scene.geometries.add(
                new Triangle(
                        new Point(baseX, trunkTop, baseZ),
                        new Point(baseX - 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX - 120, trunkTop + 70, baseZ + 80))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX - 150, trunkTop + 100, baseZ + 100),
                        new Point(baseX - 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX - 220, trunkTop + 140, baseZ + 130))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX - 250, trunkTop + 170, baseZ + 150),
                        new Point(baseX - 320, trunkTop + 230, baseZ + 200),
                        new Point(baseX - 290, trunkTop + 200, baseZ + 180))
                        .setEmission(new Color(70, 40, 18))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30))
        );

        // ענף קדמי עליון
        scene.geometries.add(
                new Triangle(
                        new Point(baseX, trunkTop, baseZ),
                        new Point(baseX + 50, trunkTop + 100, baseZ + 200),
                        new Point(baseX + 20, trunkTop + 70, baseZ + 180))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX + 50, trunkTop + 100, baseZ + 200),
                        new Point(baseX + 100, trunkTop + 170, baseZ + 300),
                        new Point(baseX + 70, trunkTop + 140, baseZ + 280))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30))
        );

        // ענף אחורי עליון
        scene.geometries.add(
                new Triangle(
                        new Point(baseX, trunkTop, baseZ),
                        new Point(baseX - 50, trunkTop + 100, baseZ - 200),
                        new Point(baseX - 20, trunkTop + 70, baseZ - 180))
                        .setEmission(new Color(80, 50, 25))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30)),

                new Triangle(
                        new Point(baseX - 50, trunkTop + 100, baseZ - 200),
                        new Point(baseX - 100, trunkTop + 170, baseZ - 300),
                        new Point(baseX - 70, trunkTop + 140, baseZ - 280))
                        .setEmission(new Color(75, 45, 20))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(30))
        );

        // === עלים - המון משולשים קטנים ===

        // עלים ימניים
        for (int i = 0; i < 15; i++) {
            double x = baseX + 200 + (i * 20) + Math.random() * 40 - 20;
            double y = baseY + 500 + (i * 15) + Math.random() * 30 - 15;
            double z = baseZ + 100 + Math.random() * 100 - 50;

            scene.geometries.add(
                    new Triangle(
                            new Point(x, y, z),
                            new Point(x + 25, y + 15, z + 10),
                            new Point(x + 10, y + 35, z - 5))
                            .setEmission(new Color(30 + i, 120 + i * 2, 40 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3)),

                    new Triangle(
                            new Point(x, y, z),
                            new Point(x + 10, y + 35, z - 5),
                            new Point(x - 15, y + 20, z + 8))
                            .setEmission(new Color(25 + i, 110 + i * 2, 35 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3))
            );
        }

        // עלים שמאליים
        for (int i = 0; i < 15; i++) {
            double x = baseX - 200 - (i * 20) + Math.random() * 40 - 20;
            double y = baseY + 500 + (i * 15) + Math.random() * 30 - 15;
            double z = baseZ + 100 + Math.random() * 100 - 50;

            scene.geometries.add(
                    new Triangle(
                            new Point(x, y, z),
                            new Point(x - 25, y + 15, z + 10),
                            new Point(x - 10, y + 35, z - 5))
                            .setEmission(new Color(35 + i, 130 + i * 2, 45 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3)),

                    new Triangle(
                            new Point(x, y, z),
                            new Point(x - 10, y + 35, z - 5),
                            new Point(x + 15, y + 20, z + 8))
                            .setEmission(new Color(30 + i, 125 + i * 2, 40 + i))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3))
            );
        }

        // עלים קדמיים
        for (int i = 0; i < 12; i++) {
            double x = baseX - 60 + (i * 10) + Math.random() * 30 - 15;
            double y = baseY + 520 + (i * 12) + Math.random() * 25 - 12;
            double z = baseZ + 250 + Math.random() * 80 - 40;

            scene.geometries.add(
                    new Triangle(
                            new Point(x, y, z),
                            new Point(x + 20, y + 12, z + 8),
                            new Point(x + 8, y + 30, z - 3))
                            .setEmission(new Color(40 + i * 2, 140 + i * 3, 50 + i * 2))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3)),

                    new Triangle(
                            new Point(x, y, z),
                            new Point(x + 8, y + 30, z - 3),
                            new Point(x - 12, y + 18, z + 6))
                            .setEmission(new Color(35 + i * 2, 135 + i * 3, 45 + i * 2))
                            .setMaterial(new Material()
                                    .setKD(0.6).setKS(0.1).setShininess(10)
                                    .setKT(0.3))
            );
        }

        // === כתר העץ - משולשים גדולים ===
        double crownY = baseY + 600;
        scene.geometries.add(
                new Triangle(
                        new Point(baseX - 200, crownY, baseZ + 200),
                        new Point(baseX + 200, crownY, baseZ + 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(40, 150, 60))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.2).setShininess(20)
                                .setKT(0.2)),

                new Triangle(
                        new Point(baseX - 200, crownY, baseZ + 200),
                        new Point(baseX, crownY + 150, baseZ),
                        new Point(baseX - 100, crownY + 50, baseZ - 200))
                        .setEmission(new Color(45, 160, 65))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.2).setShininess(20)
                                .setKT(0.2)),

                new Triangle(
                        new Point(baseX + 200, crownY, baseZ + 200),
                        new Point(baseX + 100, crownY + 50, baseZ - 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(50, 170, 70))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.2).setShininess(20)
                                .setKT(0.2)),

                new Triangle(
                        new Point(baseX - 100, crownY + 50, baseZ - 200),
                        new Point(baseX + 100, crownY + 50, baseZ - 200),
                        new Point(baseX, crownY + 150, baseZ))
                        .setEmission(new Color(55, 180, 75))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.2).setShininess(20)
                                .setKT(0.2))
        );

        // === שורשים - פוליגונים מתחת לאדמה ===
        scene.geometries.add(
                new Polygon(
                        new Point(baseX - 80, baseY, baseZ - 20),
                        new Point(baseX - 120, baseY - 50, baseZ + 20),
                        new Point(baseX - 100, baseY - 100, baseZ + 40),
                        new Point(baseX - 60, baseY - 50, baseZ))
                        .setEmission(new Color(60, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.1).setShininess(10)),

                new Polygon(
                        new Point(baseX + 80, baseY, baseZ - 20),
                        new Point(baseX + 120, baseY - 50, baseZ + 20),
                        new Point(baseX + 100, baseY - 100, baseZ + 40),
                        new Point(baseX + 60, baseY - 50, baseZ))
                        .setEmission(new Color(65, 45, 25))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.1).setShininess(10)),

                new Polygon(
                        new Point(baseX, baseY, baseZ + 100),
                        new Point(baseX + 40, baseY - 50, baseZ + 140),
                        new Point(baseX + 20, baseY - 100, baseZ + 160),
                        new Point(baseX - 20, baseY - 50, baseZ + 120))
                        .setEmission(new Color(70, 50, 30))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.1).setShininess(10))
        );
    }

    /**
     * Creates a stone path in the given scene.
     * The path consists of multiple hexagonal stones arranged in a line.
     *
     * @param scene the scene to which the stone path will be added
     */
    private void createStonePath(Scene scene) {
        // צבע האבנים - אפור בהיר עם גוון קרם
        Color stoneColor = new Color(120, 115, 100);

        // חומר האבנים - מט עם מעט ברק
        Material stoneMaterial = new Material()
                .setKD(0.7).setKS(0.2).setShininess(20)
                .setKR(0.05);

        // יצירת אבנים לאורך השביל
        for (int i = 0; i < 25; i++) {
            double z = 300 - (i * 250);  // אבנים לאורך הציר Z

            // יצירת כמה אבנים במקביל (רוחב השביל)
            for (int j = -1; j <= 1; j++) {
                double x = j * 80;  // מרווח בין אבנים ברוחב

                // הוספת מעט אקראיות למיקום
                double randomX = x + (Math.random() - 0.5) * 20;
                double randomZ = z + (Math.random() - 0.5) * 30;

                // גודל האבן - מעט משתנה
                double stoneSize = 35 + Math.random() * 15;

                // יצירת אבן פוליגונלית (משושה/מתומן)
                createStonePolygon(scene, randomX, randomZ, stoneSize, stoneColor, stoneMaterial);
            }
        }

        // הוספת כמה אבנים גדולות יותר בצדדים לשוני
        for (int i = 0; i < 8; i++) {
            double z = 200 - (i * 800);

            // אבן גדולה מצד ימין
            createStonePolygon(scene, 225, z, 60, stoneColor.scale(0.9), stoneMaterial);

            // אבן גדולה מצד שמאל
            createStonePolygon(scene, -225, z, 60, stoneColor.scale(0.9), stoneMaterial);
        }
    }

    /**
     * Creates a hexagonal stone polygon in the scene.
     * The stone is created with a slight random variation in size and shape.
     *
     * @param scene    the scene to which the stone will be added
     * @param centerX  the X coordinate of the stone's center
     * @param centerZ  the Z coordinate of the stone's center
     * @param size     the size of the stone
     * @param color    the color of the stone
     * @param material the material properties of the stone
     */
    private void createStonePolygon(Scene scene, double centerX, double centerZ, double size, Color color, Material material) {
        // יצירת אבן בצורת משושה (6 צדדים) עם גובה קטן
        Point[] topVertices = new Point[6];
        Point[] bottomVertices = new Point[6];

        double topY = -97;    // גובה עליון של האבן
        double bottomY = -99.5; // גובה תחתון של האבן

        // יצירת קודקודי המשושה
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3; // 60 מעלות לכל צד

            // הוספת מעט אקראיות לצורה (כדי שלא תהיה מושלמת)
            double radiusVariation = 0.8 + Math.random() * 0.4; // בין 0.8 ל-1.2
            double actualRadius = (size / 2) * radiusVariation;

            double x = centerX + actualRadius * Math.cos(angle);
            double z = centerZ + actualRadius * Math.sin(angle);

            topVertices[i] = new Point(x, topY, z);
            bottomVertices[i] = new Point(x, bottomY, z);
        }

        // יצירת הפן העליון
        for (int i = 1; i < 5; i++) {
            scene.geometries.add(
                    new Triangle(topVertices[0], topVertices[i], topVertices[i + 1])
                            .setEmission(color)
                            .setMaterial(material)
            );
        }

        // יצירת הפן התחתון
        for (int i = 1; i < 5; i++) {
            scene.geometries.add(
                    new Triangle(bottomVertices[0], bottomVertices[i + 1], bottomVertices[i])
                            .setEmission(color.scale(0.8)) // קצת יותר כהה מלמטה
                            .setMaterial(material)
            );
        }

        // יצירת הצדדים
        for (int i = 0; i < 6; i++) {
            int nextI = (i + 1) % 6;

            // כל צד מורכב משני משולשים
            scene.geometries.add(
                    new Triangle(bottomVertices[i], topVertices[i], topVertices[nextI])
                            .setEmission(color.scale(0.9)) // צדדים קצת יותר כהים
                            .setMaterial(material)
            );

            scene.geometries.add(
                    new Triangle(bottomVertices[i], topVertices[nextI], bottomVertices[nextI])
                            .setEmission(color.scale(0.9))
                            .setMaterial(material)
            );
        }
    }

    // =========================== NO MT OR NO CBR ============================================

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test does not use BVH or multi-threading.
     */
    @Test
    void polygonTreeNoBvhNoMT() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9)
                .setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);
        // === מצלמה מציר Z בגובה, מסתכלת לכיוון השלילי של Z ===
        final String TEST_NAME = "polygonTreeNoBvhNoMT";

        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                    // ⏱ start timer

        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        double sec = (System.nanoTime() - t0) / 1e9;    // ⏱ stop timer
        System.out.printf("⏱ %-30s : %.3f s%n", TEST_NAME, sec);
    }

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses BVH but does not use multi-threading.
     */
    @Test
    void polygonTreeWithMT() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9).setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);
        final String TEST_NAME = "polygonTreeWithMT";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start

        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(5)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long millis = (System.nanoTime() - t0) / 1_000_000;   // ⏱ stop → ms
        System.out.printf("⏱ %-30s : %d ms%n", TEST_NAME, millis);
    }
    // =========================== Stage 1 With boundingBox ============================================

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses BVH and multi-threading.
     */
    @Test
    void stage1PolygonTreeTest1WithCBR() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9).setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);

        scene.geometries.setBoundingBox();
        final String TEST_NAME = "stage1PolygonTreeTest1WithCBR";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                    // ⏱ start timer

// === מצלמה מציר Z בגובה, מסתכלת לכיוון Z-שלילי ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)// MT-ON
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000; // ⏱ stop → milliseconds
        System.out.printf("⏱ %-30s : %d ms%n", TEST_NAME, ms);
    }

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses BVH, multi-threading, and antialiasing.
     */
    @Test
    void stage1PolygonTreeTest1WithCBRANDMT() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9).setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);

        scene.geometries.setBoundingBox();
        final String TEST_NAME = "stage1PolygonTreeTest1WithCBRANDMT";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start timer

// === מצלמה מציר Z בגובה, מסתכלת לכיוון Z-שלילי ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(5)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000;    // ⏱ stop → milliseconds
        System.out.printf("⏱ %-35s : %d ms%n", TEST_NAME, ms);
    }

    // =========================== Stage 2 With manualHierarchy ============================================

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses manual hierarchy without multi-threading.
     */
    @Test
    void stage2PolygonTreeTest_manualHierarchyCBRNoMT() {
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        /* ========== 1. רצפה ========= */
        Geometries ground = new Geometries(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)));

        /* ========== 2. אגם (היררכיה מקומית) ========= */
        int EDGE_COUNT = 50; // הקטנתי כדי שיהיה יותר מהיר
        double centerX = 0, centerY = -90, centerZ = 1500;
        double radiusX = 280, radiusZ = 600, noise = 0.05;
        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;
            double nX = 1 + (Math.random() - 0.5) * 2 * noise;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noise;
            rim[i] = new Point(centerX + radiusX * nX * Math.cos(ang),
                    centerY,
                    centerZ + radiusZ * nZ * Math.sin(ang));
        }

        Material waterMat = new Material()
                .setKD(0.05).setKS(0.3).setKR(0.9).setGlossinessReflacted(0.04, 4, 50);
        Color waterCol = new Color(30, 40, 50);

        // יצירת חלוקה היררכית של האגם
        Geometries lakeQuarter1 = new Geometries(); // רבעים 1-4
        Geometries lakeQuarter2 = new Geometries();
        Geometries lakeQuarter3 = new Geometries();
        Geometries lakeQuarter4 = new Geometries();

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            Geometry triangle = new Triangle(center, rim[i], rim[next])
                    .setEmission(waterCol)
                    .setMaterial(waterMat);

            // חלוקה לרבעים לפי זווית
            if (i < EDGE_COUNT / 4) {
                lakeQuarter1.add(triangle);
            } else if (i < EDGE_COUNT / 2) {
                lakeQuarter2.add(triangle);
            } else if (i < 3 * EDGE_COUNT / 4) {
                lakeQuarter3.add(triangle);
            } else {
                lakeQuarter4.add(triangle);
            }
        }

        // איחוד הרבעים בצורה היררכית
        Geometries lakeHalf1 = new Geometries(lakeQuarter1, lakeQuarter2);
        Geometries lakeHalf2 = new Geometries(lakeQuarter3, lakeQuarter4);
        Geometries lake = new Geometries(lakeHalf1, lakeHalf2);

        /* ========== 3. שביל אבנים ========= */
        Geometries stonePath = createStonePath();

        /* ========== 4. עצים (היררכיה עמוקה יותר) ========= */
        // יצירת קבוצות קטנות של עצים (כל 5 עצים יחד)
        Geometries rightTreesGroup1 = new Geometries();
        Geometries rightTreesGroup2 = new Geometries();
        Geometries rightTreesGroup3 = new Geometries();
        Geometries rightTreesGroup4 = new Geometries();

        for (int i = 0; i < 10; i++) { // הקטנתי ל-10 עצים בלי
            double z = 198 - i * 320;
            double x = 600 - i * 13;
            Geometries tree = createTreeAt(new Point(x, -100, z));

            if (i < 3) {
                rightTreesGroup1.add(tree);
            } else if (i < 5) {
                rightTreesGroup2.add(tree);
            } else if (i < 8) {
                rightTreesGroup3.add(tree);
            } else {
                rightTreesGroup4.add(tree);
            }
        }

        // איחוד הקבוצות בצורה היררכית
        Geometries rightTreesSection1 = new Geometries(rightTreesGroup1, rightTreesGroup2);
        Geometries rightTreesSection2 = new Geometries(rightTreesGroup3, rightTreesGroup4);
        Geometries rightTrees = new Geometries(rightTreesSection1, rightTreesSection2);

        // אותו דבר עבור עצים שמאל
        Geometries leftTreesGroup1 = new Geometries();
        Geometries leftTreesGroup2 = new Geometries();
        Geometries leftTreesGroup3 = new Geometries();
        Geometries leftTreesGroup4 = new Geometries();

        for (int i = 0; i < 10; i++) {
            double z = 198 - i * 320;
            double x = -600 + i * 13;
            Geometries tree = createTreeAt(new Point(x, -100, z));

            if (i < 3) {
                leftTreesGroup1.add(tree);
            } else if (i < 5) {
                leftTreesGroup2.add(tree);
            } else if (i < 8) {
                leftTreesGroup3.add(tree);
            } else {
                leftTreesGroup4.add(tree);
            }
        }

        Geometries leftTreesSection1 = new Geometries(leftTreesGroup1, leftTreesGroup2);
        Geometries leftTreesSection2 = new Geometries(leftTreesGroup3, leftTreesGroup4);
        Geometries leftTrees = new Geometries(leftTreesSection1, leftTreesSection2);

        /* אפשרות: רמה נוספת שמאגדת שורת עצים ימין+שמאל */
        Geometries forest = new Geometries(rightTrees, leftTrees);

        /* ========== 5. הוספת כל הקבוצות לשכבה העליונה בצורה היררכית ========= */
        // יצירת היררכיה עליונה
        Geometries naturalElements = new Geometries(lake, forest); // אלמנטים טבעיים
        Geometries groundElements = new Geometries(ground, stonePath); // אלמנטי קרקע

        // הוספה לסצנה
        scene.geometries.add(naturalElements);
        scene.geometries.add(groundElements);


        /* ========== 6. תאורה ========= */
        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005));

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6)));

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005));

        /* ========== 7. מצלמה ורינדור ========= */
        scene.geometries.box = null;

        final String TEST_NAME = "stage2PolygonTreeTest_manualHierarchyCBRNoMT";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start timer

        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000;    // ⏱ stop → milliseconds
        System.out.printf("⏱ %-40s : %d ms%n", TEST_NAME, ms);
    }

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses manual hierarchy with multi-threading.
     */
    @Test
    void stage2PolygonTreeTest_manualHierarchyCBRAMDMT() {
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        /* ========== 1. רצפה ========= */
        Geometries ground = new Geometries(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)));
        // אל תקרא ל-setBoundingBox() על מישור - הוא אינסופי

        /* ========== 2. אגם (היררכיה מקומית) ========= */
        int EDGE_COUNT = 50; // הקטנתי כדי שיהיה יותר מהיר
        double centerX = 0, centerY = -90, centerZ = 1500;
        double radiusX = 280, radiusZ = 600, noise = 0.05;
        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;
            double nX = 1 + (Math.random() - 0.5) * 2 * noise;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noise;
            rim[i] = new Point(centerX + radiusX * nX * Math.cos(ang),
                    centerY,
                    centerZ + radiusZ * nZ * Math.sin(ang));
        }

        Material waterMat = new Material()
                .setKD(0.05).setKS(0.3).setKR(0.9).setGlossinessReflacted(0.04, 4, 50);
        Color waterCol = new Color(30, 40, 50);

        // יצירת חלוקה היררכית של האגם
        Geometries lakeQuarter1 = new Geometries(); // רבעים 1-4
        Geometries lakeQuarter2 = new Geometries();
        Geometries lakeQuarter3 = new Geometries();
        Geometries lakeQuarter4 = new Geometries();

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            Geometry triangle = new Triangle(center, rim[i], rim[next])
                    .setEmission(waterCol)
                    .setMaterial(waterMat);

            // חלוקה לרבעים לפי זווית
            if (i < EDGE_COUNT / 4) {
                lakeQuarter1.add(triangle);
            } else if (i < EDGE_COUNT / 2) {
                lakeQuarter2.add(triangle);
            } else if (i < 3 * EDGE_COUNT / 4) {
                lakeQuarter3.add(triangle);
            } else {
                lakeQuarter4.add(triangle);
            }
        }

        // איחוד הרבעים בצורה היררכית
        Geometries lakeHalf1 = new Geometries(lakeQuarter1, lakeQuarter2);
        Geometries lakeHalf2 = new Geometries(lakeQuarter3, lakeQuarter4);
        Geometries lake = new Geometries(lakeHalf1, lakeHalf2);

        /* ========== 3. שביל אבנים ========= */
        Geometries stonePath = createStonePath();

        /* ========== 4. עצים (היררכיה עמוקה יותר) ========= */
        // יצירת קבוצות קטנות של עצים (כל 5 עצים יחד)
        Geometries rightTreesGroup1 = new Geometries();
        Geometries rightTreesGroup2 = new Geometries();
        Geometries rightTreesGroup3 = new Geometries();
        Geometries rightTreesGroup4 = new Geometries();

        for (int i = 0; i < 10; i++) { // הקטנתי ל-10 עצים בלי
            double z = 198 - i * 320;
            double x = 600 - i * 13;
            Geometries tree = createTreeAt(new Point(x, -100, z));

            if (i < 3) {
                rightTreesGroup1.add(tree);
            } else if (i < 5) {
                rightTreesGroup2.add(tree);
            } else if (i < 8) {
                rightTreesGroup3.add(tree);
            } else {
                rightTreesGroup4.add(tree);
            }
        }

        // איחוד הקבוצות בצורה היררכית
        Geometries rightTreesSection1 = new Geometries(rightTreesGroup1, rightTreesGroup2);
        Geometries rightTreesSection2 = new Geometries(rightTreesGroup3, rightTreesGroup4);
        Geometries rightTrees = new Geometries(rightTreesSection1, rightTreesSection2);

        // אותו דבר עבור עצים שמאל
        Geometries leftTreesGroup1 = new Geometries();
        Geometries leftTreesGroup2 = new Geometries();
        Geometries leftTreesGroup3 = new Geometries();
        Geometries leftTreesGroup4 = new Geometries();

        for (int i = 0; i < 10; i++) {
            double z = 198 - i * 320;
            double x = -600 + i * 13;
            Geometries tree = createTreeAt(new Point(x, -100, z));

            if (i < 3) {
                leftTreesGroup1.add(tree);
            } else if (i < 5) {
                leftTreesGroup2.add(tree);
            } else if (i < 8) {
                leftTreesGroup3.add(tree);
            } else {
                leftTreesGroup4.add(tree);
            }
        }

        Geometries leftTreesSection1 = new Geometries(leftTreesGroup1, leftTreesGroup2);
        Geometries leftTreesSection2 = new Geometries(leftTreesGroup3, leftTreesGroup4);
        Geometries leftTrees = new Geometries(leftTreesSection1, leftTreesSection2);

        /* אפשרות: רמה נוספת שמאגדת שורת עצים ימין+שמאל */
        Geometries forest = new Geometries(rightTrees, leftTrees);

        /* ========== 5. הוספת כל הקבוצות לשכבה העליונה בצורה היררכית ========= */
        // יצירת היררכיה עליונה
        Geometries naturalElements = new Geometries(lake, forest); // אלמנטים טבעיים
        Geometries groundElements = new Geometries(ground, stonePath); // אלמנטי קרקע

        // הוספה לסצנה
        scene.geometries.add(naturalElements);
        scene.geometries.add(groundElements);


        /* ========== 6. תאורה ========= */
        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005));

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6)));

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005));

        /* ========== 7. מצלמה ורינדור ========= */
        scene.geometries.box = null;

        final String TEST_NAME = "stage2PolygonTreeTest_manualHierarchyCBRAMDMT";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start timer

        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(5)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000;    // ⏱ stop → milliseconds
        System.out.printf("⏱ %-40s : %d ms%n", TEST_NAME, ms);
    }

    // =========================== Stage 3 With autoHierarchy ============================================

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses auto hierarchy without multi-threading.
     */
    @Test
    void polygonTreeBuildBVHTestWithBvh() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9).setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);

        scene.geometries.buildBVH();

        final String TEST_NAME = "polygonTreeBuildBVHTestWithBvh";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start timer (ns)

// === מצלמה מציר Z בגובה, מסתכלת לכיוון Z-שלילי ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000;    // ⏱ stop → milliseconds
        System.out.printf("⏱ %-40s : %d ms%n", TEST_NAME, ms);
    }

    /**
     * Test for a scene with a polygonal tree and a large lake.
     * This test uses BVH, multi-threading, and anti-aliasing.
     */
    @Test
    void polygonTreeBuildBVHWithBvhANDMT() {
        // === רצפה ===
        scene.geometries.add(
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 25, 15))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.3).setShininess(50)
                                .setKR(0.0))
        );

        // === BIG LAKE, CENTERED IN FRONT ===
        int EDGE_COUNT = 100;
        double centerX = 0;      // ממש במרכז
        double centerY = -90;      // 10 יח' מעל ה-Plane (-100)
        double centerZ = 1500;     // 500 יח' לפני המצלמה (Z=2000)

        double radiusX = 280;      // צר יותר, לא עולה על הגזעים
        double radiusZ = 600;      // עומק כמו קודם
        double noiseFact = 0.05;

        Point center = new Point(centerX, centerY, centerZ);
        Point[] rim = new Point[EDGE_COUNT];

        for (int i = 0; i < EDGE_COUNT; i++) {
            double ang = 2 * Math.PI * i / EDGE_COUNT;

            double nX = 1 + (Math.random() - 0.5) * 2 * noiseFact;
            double nZ = 1 + (Math.random() - 0.5) * 2 * noiseFact;

            double x = centerX + radiusX * nX * Math.cos(ang);
            double z = centerZ + radiusZ * nZ * Math.sin(ang);

            rim[i] = new Point(x, centerY, z);
        }

        Material waterMat = new Material()
                .setKD(0.05)
                .setKS(0.3)
                .setKR(0.9)
                .setGlossinessReflacted(0.04, 4, 50);

        Color waterCol = new Color(30, 40, 50);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int next = (i + 1) % EDGE_COUNT;
            scene.geometries.add(
                    new Triangle(center, rim[i], rim[next])
                            .setEmission(waterCol)
                            .setMaterial(waterMat)
            );
        }

        // === תאורה ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25)));

        scene.lights.add(
                new SpotLight(new Color(1200, 1000, 800),
                        new Point(0, 800, 500),
                        new Vector(0, -1, -1).normalize())
                        .setKl(0.00001).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(180, 200, 220),
                        new Vector(1, -0.8, -0.6))
        );

        scene.lights.add(
                new PointLight(new Color(300, 400, 300),
                        new Point(-200, 200, 100))
                        .setKl(0.001).setKq(0.0005)
        );

        // עצים בצד ימין - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = 600 - (i * 13);    // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // עצים בצד שמאל - עם אלכסון פנימה
        for (int i = 0; i < 20; i++) {
            double z = 198 - (i * 320);  // מרחק של 120 יחידות בין עצים (יותר רווח)
            double x = -600 + (i * 13);   // אלכסון פנימה - כל עץ קצת יותר פנימה
            createTreeAt(scene, new Point(x, -100, z));
        }

        // === שביל מאבנים ===
        createStonePath(scene);

        scene.geometries.buildBVH();

        final String TEST_NAME = "polygonTreeBuildBVHWithBvhANDMT";
        System.out.println("▶ " + TEST_NAME + " — rendering…");

        long t0 = System.nanoTime();                       // ⏱ start timer (ns)

// === מצלמה מציר Z בגובה, מסתכלת לכיוון Z-שלילי ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))      // קצת הוזזתי מראשית הצירים
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(5)                      // MT-ON
                .build()
                .renderImage()
                .writeToImage(TEST_NAME);

        long ms = (System.nanoTime() - t0) / 1_000_000;    // ⏱ stop → milliseconds
        System.out.printf("⏱ %-40s : %d ms%n", TEST_NAME, ms);
    }

}