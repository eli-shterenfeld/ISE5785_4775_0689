package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Test class for {@link ImageWriter}.
 * <p>
 * Generates 800x500 image divided into a red grid (16x10)
 * with yellow-filled cells. Useful for visually verifying pixel rendering.
 * </p>
 */
public class ImageWriterTest {

    /**
     * Generates an image with a red grid over a yellow background and saves it to a file.
     * Demonstrates basic usage of the ImageWriter class.
     */
    @Test
    void imageWriterTest() {
        int width = 801;
        int height = 501;
        int gridX = 16;
        int gridY = 10;

        int intervalX = width / gridX;   // 50
        int intervalY = height / gridY;  // 50

        ImageWriter writer = new ImageWriter(width, height);

        Color gridColor = new Color(255, 0, 0);   // red
        Color fillColor = new Color(255, 255, 0);   // yellow

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                writer.writePixel(x, y, x % intervalX == 0 || y % intervalY == 0 ? gridColor : fillColor);
            }
        }

        writer.writeToImage("grid");
    }
}