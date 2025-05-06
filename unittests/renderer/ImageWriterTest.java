package renderer;

import primitives.Color;

public class ImageWriterTest {
    public static void main(String[] args) {
        int width = 800;
        int height = 500;
        int gridX = 16;
        int gridY = 10;

        int intervalX = width / gridX;   // 50
        int intervalY = height / gridY;  // 50

        ImageWriter writer = new ImageWriter(width, height);

        Color gridColor = new Color(255, 0, 0);     // אדום
        Color fillColor = new Color(255, 255, 0);   // צהוב

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x % intervalX == 0 || y % intervalY == 0)
                    writer.writePixel(x, y, gridColor);
                else
                    writer.writePixel(x, y, fillColor);
            }
        }

        writer.writeToImage("grid");  // חשוב!
    }
}