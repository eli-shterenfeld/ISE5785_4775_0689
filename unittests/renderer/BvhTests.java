package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

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

    // פונקציה ליצירת שביל מאבנים פוליגונליות
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

    // פונקציה ליצירת אבן פוליגונלית בודדת
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

    @Test
    void polygonTreeTest1NoBvhNoMT() {
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
                .setKR(0.9).setGlossiness(0.3, 3, 50);

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
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))  // מציר Z בגובה, קצת הוזזתי מראשית הצירים
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))  // מסתכלת לכיוון Z שלילי
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("polygonTree1");
    }

    @Test
    void polygonTreeTest1WithMT() {
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
                .setKR(0.9).setGlossiness(0.3, 3, 50);

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
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))  // מציר Z בגובה, קצת הוזזתי מראשית הצירים
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))  // מסתכלת לכיוון Z שלילי
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("polygonTree1");
    }

    @Test
    void polygonTreeTest1WithBvh() {
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
                .setKR(0.9).setGlossiness(0.3, 3, 50);

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

        // === מצלמה מציר Z בגובה, מסתכלת לכיוון השלילי של Z ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))  // מציר Z בגובה, קצת הוזזתי מראשית הצירים
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))  // מסתכלת לכיוון Z שלילי
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("polygonTree1");
    }

    @Test
    void polygonTreeTest1WithBvhANDMT() {
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
                .setKR(0.9).setGlossiness(0.3, 3, 50);

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

        // === מצלמה מציר Z בגובה, מסתכלת לכיוון השלילי של Z ===
        cameraBuilder
                .setLocation(new Point(10, 50, 2000))  // מציר Z בגובה, קצת הוזזתי מראשית הצירים
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))  // מסתכלת לכיוון Z שלילי
                .setVpDistance(1000)
                .setVpSize(1500, 1500)
                .setResolution(800, 800)
                .setMultithreading(5)
                .build()
                .renderImage()
                .writeToImage("polygonTree1");
    }

//    @Test
//    void thousandSpectacularCylindersAndTubes() {
//        // === אלף גופים מרהיבים ושקופים ===
//
//        int CYLINDERS = 500;
//        int TUBES = 500;
//        double spiralLayers = 10;
//        double spiralTurns = 6;
//        double spiralHeight = 150;
//        double baseRadius = 60;
//        double tubeBaseRadius = 3.5;
//        double cylinderMinRad = 7, cylinderMaxRad = 19;
//        double cylinderMinHeight = 35, cylinderMaxHeight = 120;
//
//        Random rand = new Random(2025);
//
//        // === צילינדרים בספירלות מרובות ===
//        for (int i = 0; i < CYLINDERS; i++) {
//            double theta = 2 * Math.PI * spiralTurns * i / CYLINDERS;
//            double layer = (i % spiralLayers);
//            double y = -25 + spiralHeight * (layer / spiralLayers);
//            double r = baseRadius + 25 * Math.sin(layer + theta * 0.5);
//
//            double x = r * Math.cos(theta);
//            double z = -120 + r * Math.sin(theta);
//
//            double height = cylinderMinHeight + rand.nextDouble() * (cylinderMaxHeight - cylinderMinHeight);
//            double rad = cylinderMinRad + rand.nextDouble() * (cylinderMaxRad - cylinderMinRad);
//
//            Color color = new Color(
//                    2 + rand.nextInt(8),
//                    2 + rand.nextInt(8),
//                    2 + rand.nextInt(8)
//            );
//
//            Material mat = new Material()
//                    .setKD(0.15 + 0.15 * rand.nextDouble())
//                    .setKS(0.7 + 0.2 * rand.nextDouble())
//                    .setShininess(100 + rand.nextInt(120))
//                    .setKT(0.4 + 0.5 * rand.nextDouble())
//                    .setKR(0.2 + 0.3 * rand.nextDouble());
//
//            scene.geometries.add(
//                    new Cylinder(rad,
//                            new Ray(new Point(x, y, z), new Vector(0.1 * (rand.nextDouble() - 0.5), 1, 0.1 * (rand.nextDouble() - 0.5)).normalize()), height)
//                            .setEmission(color)
//                            .setMaterial(mat)
//            );
//        }
//
//        // === צינורות באשכולות וכדורים ===
//        for (int i = 0; i < TUBES; i++) {
//            double phi = 2 * Math.PI * i / TUBES;
//            double cluster = i % 40;
//            double clusterAngle = 2 * Math.PI * cluster / 40;
//            double r = 70 + 35 * Math.sin(clusterAngle + phi);
//
//            double y = -25 + spiralHeight * Math.abs(Math.sin(phi * spiralLayers));
//            double x = r * Math.cos(phi + clusterAngle);
//            double z = -120 + r * Math.sin(phi + clusterAngle);
//
//            Color color = new Color(
//                    6 + rand.nextInt(4),
//                    rand.nextInt(10),
//                    5 + rand.nextInt(6)
//            );
//
//            Material mat = new Material()
//                    .setKD(0.18 + 0.16 * rand.nextDouble())
//                    .setKS(0.7 + 0.2 * rand.nextDouble())
//                    .setShininess(120 + rand.nextInt(130))
//                    .setKT(0.3 + 0.6 * rand.nextDouble())
//                    .setKR(0.2 + 0.5 * rand.nextDouble());
//
//            scene.geometries.add(
//                    new Cylinder(tubeBaseRadius + rand.nextDouble() * 3,
//                            new Ray(
//                                    new Point(x, y, z),
//                                    new Vector(
//                                            Math.cos(phi + clusterAngle + 0.7),
//                                            Math.sin(phi * 2) + 0.2,
//                                            Math.sin(phi + clusterAngle - 0.7)).normalize()), 5000)
//
//                            .setEmission(color)
//                            .setMaterial(mat)
//            );
//        }
//
//        // === רקע ורצפה ===
//
//        // ─────────────────────────────
//// רצפה מתוחכמת – שקיפות/השתקפות
//// ─────────────────────────────
//        scene.geometries.add(
//                new Polygon(
//                        new Point(-5000, -25, 5000),   // ↖︎
//                        new Point(5000, -25, 5000),   // ↗︎
//                        new Point(5000, -25, -5000),   // ↘︎
//                        new Point(-5000, -25, -5000))   // ↙︎
//                        .setEmission(new Color(4, 4, 6))
//                        .setMaterial(new Material()
//                                .setKD(0.5).setKS(0.5).setShininess(100)
//                                .setKR(0.45).setKT(0.15))
//        );
//
//// ─────────────────────────────
//// קיר אחורי – גוון צבעוני
//// ─────────────────────────────
//        scene.geometries.add(
//                new Polygon(
//                        new Point(-5000, -500, -250),   // ↙︎
//                        new Point(5000, -500, -250),   // ↘︎
//                        new Point(5000, 5000, -250),  // ↗︎
//                        new Point(-5000, 5000, -250))  // ↖︎
//                        .setEmission(new Color(3, 2, 6))
//                        .setMaterial(new Material()
//                                .setKD(0.8).setKS(0.2).setShininess(25)
//                                .setKR(0.12))
//        );
//
//        // === תאורה מרובת מקורות ===
//
//        scene.setAmbientLight(new AmbientLight(new Color(2, 2, 5)));
//
//        // מקור ראשי - אור דרמטי
//        scene.lights.add(
//                new SpotLight(
//                        new Color(800, 700, 900),
//                        new Point(0, 150, 90),
//                        new Vector(0, -1, -1))
//                        .setKl(0.0001).setKq(0.00001)
//        );
//
//        // אורות צבעוניים נוספים
//        scene.lights.add(
//                new PointLight(
//                        new Color(320, 250, 560),
//                        new Point(200, 100, 0))
//                        .setKl(0.0004).setKq(0.00004)
//        );
//        scene.lights.add(
//                new PointLight(
//                        new Color(100, 450, 200),
//                        new Point(-200, 60, -135))
//                        .setKl(0.0005).setKq(0.00005)
//        );
//        scene.lights.add(
//                new SpotLight(
//                        new Color(470, 180, 320),
//                        new Point(50, 250, -40),
//                        new Vector(-0.4, -1, -1))
//                        .setKl(0.0003).setKq(0.00003)
//        );
//        scene.lights.add(
//                new DirectionalLight(
//                        new Color(25, 60, 80),
//                        new Vector(-0.35, -0.7, -0.7))
//        );
//
//        scene.geometries.buildBVH();
//        // === הגדרת המצלמה ===
//        cameraBuilder
//                .setLocation(new Point(167, 170, 320))
//                .setDirection(new Point(2, 17, -133), Vector.AXIS_Y)
//                .setVpDistance(370)
//                .setVpSize(400, 400)
//                .setResolution(1200, 1200)
//                .setRayTracer(scene, RayTracerType.SIMPLE)
//                .setMultithreading(4)
//                .build()
//                .renderImage()
//                .writeToImage("thousandSpectacularCylindersAndTubes");
//    }

}