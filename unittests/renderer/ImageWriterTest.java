package renderer;

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
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int width = 800;
        int height = 500;
        int gridX = 16;
        int gridY = 10;

        int intervalX = width / gridX;   // 50
        int intervalY = height / gridY;  // 50

        ImageWriter writer = new ImageWriter(width, height);

        Color gridColor = new Color(255, 0, 0);   // red
        Color fillColor = new Color(255, 255, 0);   // yellow

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x % intervalX == 0 || y % intervalY == 0)
                    writer.writePixel(x, y, gridColor);
                else
                    writer.writePixel(x, y, fillColor);
            }
        }

        writer.writeToImage("grid");
    }
}