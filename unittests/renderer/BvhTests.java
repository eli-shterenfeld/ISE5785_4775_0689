package renderer;

import geometries.Sphere;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.Random;

public class BvhTests {

    private Scene createBvhTestScene() {
        Scene scene = new Scene("BVH Test Scene")
                .setBackground(new Color(10, 10, 10))
                .setAmbientLight(new AmbientLight(new Color(50, 50, 50)));

        Random rand = new Random(123); // קבוע כדי לשחזר

        for (int i = 0; i < 1000; i++) {
            // נקודה רנדומלית במרחב גדול
            double x = rand.nextDouble(-500, 500);
            double y = rand.nextDouble(-500, 500);
            double z = rand.nextDouble(-800, -200); // שיהיו מול המצלמה

            // רדיוס אקראי קטן
            double radius = rand.nextDouble(5, 15);

            // צבע רנדומלי
            Color color = new Color(
                    rand.nextInt(156) + 100, // הבהרה להבדיל ביניהם
                    rand.nextInt(156) + 100,
                    rand.nextInt(156) + 100
            );

            // חומר עם קצת החזר ופיזור לא אחיד
            Material material = new Material()
                    .setKD(rand.nextDouble(0.4, 0.8))
                    .setKS(rand.nextDouble(0.2, 0.5))
                    .setShininess(rand.nextInt(100) + 20);

            scene.geometries.add(
                    new Sphere(radius, new Point(x, y, z))
                            .setEmission(color)
                            .setMaterial(material)
            );
        }

        // אור עיקרי חזק בצד
        scene.lights.add(
                new PointLight(new Color(1200, 1000, 1000), new Point(-100, -100, 100))
                        .setKl(0.0003).setKq(0.00005)
        );

        // אור נוסף רך מהצד השני לאיזון
        scene.lights.add(
                new PointLight(new Color(300, 300, 500), new Point(300, 200, -100))
                        .setKl(0.0005).setKq(0.0001)
        );

        return scene;
    }

    @Test
    void renderBvhTest() {
        Scene scene = createBvhTestScene();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(800, 800)
                .build();

        camera.renderImage()
                .writeToImage("bvhTest");
    }
}