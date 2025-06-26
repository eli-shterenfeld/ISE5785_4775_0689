package renderer;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

/**
 * Integration test for rendering a full scene loaded from a JSON file.
 * <p>
 * Verifies end-to-end rendering with BVH and multithreading enabled.
 */
public class FinalSceneJsonTest {

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
     * Loads a complex scene from JSON, builds BVH, sets up a camera,
     * renders the image with multithreading, and writes it to a file.
     */
    @Test
    void composedSceneFromJson() {
        Scene scene = JsonScene.importScene("scenes/final_scene (1).json");
        scene.geometries.buildBVH();
        cameraBuilder
                .setLocation(new Point(0, 200, 950)) // נמוך יותר וקרוב יותר
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -0.05))// מבט קדימה בגובה העיניים
                .setVpDistance(1200)
                .setVpSize(1600, 900)
                .setResolution(1000, 1000)
                .setMultithreading(4)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("composedSceneFromJson");
    }
}
