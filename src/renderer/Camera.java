package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a camera in a 3D rendering system.
 */
public class Camera implements Cloneable {

    /**
     * The location of the camera in 3D space.
     */
    private Point location = new Point(0, 0, 0);

    /**
     * The forward direction vector of the camera (points to where the camera is looking).
     */
    private Vector to;

    /**
     * The upward direction vector of the camera (defines the vertical orientation).
     */
    private Vector up;

    /**
     * The rightward direction vector of the camera (perpendicular to both 'to' and 'up').
     */
    private Vector right;

    /**
     * The width of the view plane.
     */
    private double vpWidth = 0.0;

    /**
     * The height of the view plane.
     */
    private double vpHeight = 0.0;

    /**
     * The distance from the camera to the view plane.
     */
    private double vpDistance = 0.0;

    /**
     * The point on the view plane corresponding to a pixel (i, j).
     */
    private Point viewPlaneCenter;

    /**
     * The image writer used for rendering the scene.
     */
    private ImageWriter imageWriter;

    /**
     * The ray tracer used for rendering the scene.
     */
    private RayTracerBase rayTracer;

    /**
     * The number of columns in the image.
     */
    int nX = 1;

    /**
     * The number of rows in the image.
     */
    int nY = 1;

    /**
     * Private constructor to prevent instantiation from outside the builder.
     */
    private Camera() {
    }

    /**
     * Returns a new builder instance for creating a Camera object.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Creates a builder initialized with the state of an existing camera.
     * Useful for modifying a copy.
     *
     * @param cam the existing camera
     * @return a new Builder initialized with the camera's parameters
     */
    public static Builder getBuilder(Camera cam) {
        return new Builder(cam);
    }

    /**
     * Builder class for creating Camera instances.
     */
    public static class Builder {

        /**
         * The Camera object being constructed by this builder.
         */
        private final Camera camera;

        /**
         * Default constructor initializes a new Camera instance.
         */
        public Builder() {
            camera = new Camera();
        }

        /**
         * Sets the location of the camera.
         *
         * @param p the location point
         * @return the current Builder instance
         */
        public Builder setLocation(Point p) {
            if (p == null) throw new IllegalArgumentException("Location cannot be null");
            camera.location = p;
            return this;
        }

        /**
         * Sets the forward and upward direction vectors of the camera.
         *
         * @param to forward direction vector
         * @param up upward direction vector
         * @return the current Builder instance
         */
        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null) throw new IllegalArgumentException("Vectors cannot be null");
            camera.to = to.normalize();
            camera.up = up.normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();
            return this;
        }

        /**
         * Sets the camera direction using a look-at point and an approximate up vector.
         *
         * @param lookAt   the point to look at
         * @param upApprox the approximate up vector
         * @return the current Builder instance
         */
        public Builder setDirection(Point lookAt, Vector upApprox) {
            if (lookAt == null || upApprox == null)
                throw new IllegalArgumentException("LookAt point or up vector cannot be null");
            this.camera.to = lookAt.subtract(this.camera.location).normalize();
            this.camera.right = this.camera.to.crossProduct(upApprox).normalize();
            this.camera.up = this.camera.right.crossProduct(this.camera.to).normalize();
            return this;
        }

        /**
         * Sets the camera direction using a look-at point and a default up vector.
         *
         * @param lookAt the point to look at
         * @return the current Builder instance
         */
        public Builder setDirection(Point lookAt) {
            return setDirection(lookAt, new Vector(0, 1, 0));
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the current Builder instance
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0)
                throw new IllegalArgumentException("View plane size must be positive");
            camera.vpWidth = width;
            camera.vpHeight = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to the view plane
         * @return the current Builder instance
         */
        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) throw new IllegalArgumentException("View plane distance must be positive");
            camera.vpDistance = distance;
            return this;
        }

        /**
         * Sets the resolution of the camera.
         *
         * @param nX number of columns
         * @param nY number of rows
         * @return this Builder instance for method chaining
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive");
            this.camera.nX = nX;
            this.camera.nY = nY;
            return this;
        }

        /**
         * Finalizes and returns the constructed Camera object after validating its configuration.
         *
         * @return the built Camera instance
         */
        public Camera build() {
            final String GENERAL_DESCRIPTION = "Missing rendering data";
            final String CAMERA_CLASS_NAME = Camera.class.getSimpleName();

            // Check that required fields exist
            if (camera.location == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "Location");
            if (camera.to == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "Direction To");
            if (camera.up == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "Direction Up");

            // Additional validation: check if values are logical (non-negative and non-zero length)
            if (alignZero(camera.vpWidth) <= 0)
                throw new IllegalArgumentException("View Plane Width must be positive");
            if (alignZero(camera.vpHeight) <= 0)
                throw new IllegalArgumentException("View Plane Height must be positive");
            if (alignZero(camera.vpDistance) <= 0)
                throw new IllegalArgumentException("View Plane Distance must be positive");

            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive");

            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null); // no scene, placeholder
            }

            // Initialize the image writer
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            // Recalculate the right vector
            camera.right = camera.to.crossProduct(camera.up);

            // Normalize all vectors
            camera.to = camera.to.normalize();
            camera.up = camera.up.normalize();
            camera.right = camera.right.normalize();

            // Calculate the point on the view plane corresponding to the camera's location
            camera.viewPlaneCenter = camera.location.add(camera.to.scale(camera.vpDistance));

            // Return a clone of camera to avoid exposing internal references
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Unexpected error: Clone not supported", e);
            }
        }

        /**
         * Sets the ray tracer for the camera based on the given scene and type.
         *
         * @param scene the scene to be rendered
         * @param type  the type of ray tracer to use
         * @return the builder (for method chaining)
         */
        public Builder setRayTracer(scene.Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE -> camera.rayTracer = new SimpleRayTracer(scene);
                default -> camera.rayTracer = null;
            }
            return this;
        }

        /**
         * Moves the camera by a specified offset vector and updates the focus point.
         *
         * @param offset the vector to move the camera
         * @param focus  the new focus point for the camera
         * @return the current Builder instance
         */
        public Builder move(Vector offset, Point focus) {
            camera.location = camera.location.add(offset);
            camera.to = focus.subtract(camera.location).normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();
            return this;
        }

        /**
         * Rotates the camera around the 'to' vector by a specified angle in degrees.
         *
         * @param angleDegrees the angle in degrees to rotate the camera
         * @return the current Builder instance
         */
        public Builder rotateAroundTo(double angleDegrees) {
            if (camera.up == null || camera.to == null)
                throw new IllegalStateException("Cannot rotate camera before setting direction");

            double angleRad = Math.toRadians(angleDegrees);

            Vector axis = camera.to;
            Vector vector = camera.up;

            if (vector == null || alignZero(vector.lengthSquared()) == 0)
                throw new IllegalStateException("Cannot rotate the zero vector");

            if (axis == null || alignZero(axis.lengthSquared()) == 0)
                throw new IllegalArgumentException("Cannot rotate around a zero axis");

            axis = axis.normalize();

            double cos = Math.cos(angleRad);
            double sin = Math.sin(angleRad);
            double dot = vector.dotProduct(axis);

            Vector part1 = (alignZero(cos) == 0) ? null : vector.scale(cos);
            Vector part2 = (alignZero(sin) == 0) ? null : axis.crossProduct(vector).scale(sin);
            Vector part3 = (alignZero(dot * (1 - cos)) == 0) ? null : axis.scale(dot * (1 - cos));

            Vector rotated = null;
            if (part1 != null) rotated = part1;
            if (part2 != null) rotated = (rotated == null) ? part2 : rotated.add(part2);
            if (part3 != null) rotated = (rotated == null) ? part3 : rotated.add(part3);

            camera.up = rotated.normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();

            return this;
        }

        /**
         * Initializes the builder from an existing camera.
         *
         * @param original the original camera to copy
         */
        public Builder(Camera original) {
            if (original == null)
                throw new IllegalArgumentException("Camera cannot be null");

            camera = new Camera(); // Create a new camera instance
            // Copying fields from the original camera into a new one
            camera.location = original.location;
            camera.to = original.to;
            camera.up = original.up;
            camera.right = original.right;
            camera.vpWidth = original.vpWidth;
            camera.vpHeight = original.vpHeight;
            camera.vpDistance = original.vpDistance;
            camera.nX = original.nX;
            camera.nY = original.nY;
            camera.imageWriter = original.imageWriter;
            camera.rayTracer = original.rayTracer;
            camera.viewPlaneCenter = original.viewPlaneCenter;
        }
    }

    /**
     * Constructs ray from camera's location to the center of a given pixel in the view plane.
     *
     * @param nX number of columns
     * @param nY number of rows
     * @param j  pixel's x index (column)
     * @param i  pixel's y index (row)
     * @return resulting Ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate pixel size
        double rX = vpWidth / nX;
        double rY = vpHeight / nY;

        // Offsets from the center
        double xJ = (j - (nX - 1) * 0.5) * rX;
        double yI = -(i - (nY - 1) * 0.5) * rY;

        // Calculate the pixel center
        Point pixelCenter = viewPlaneCenter;

        if (!isZero(xJ)) pixelCenter = pixelCenter.add(right.scale(xJ));
        if (!isZero(yI)) pixelCenter = pixelCenter.add(up.scale(yI));

        return new Ray(location, pixelCenter.subtract(location));
    }

    /**
     * Casts a ray through the center of a given pixel, traces it using the ray tracer,
     * and writes the resulting color to the image.
     *
     * @param j pixel column index (X)
     * @param i pixel row index (Y)
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Renders the image by casting rays through each pixel and writing the resulting color.
     *
     * @return this camera object (for chaining)
     */
    public Camera renderImage() {
        for (int j = 0; j < nY; j++) {
            for (int i = 0; i < nX; i++) {
                castRay(i, j);
            }
        }
        return this;
    }

    /**
     * Overlays a grid on the rendered image, coloring every nth row and column.
     *
     * @param interval spacing between grid lines (in pixels)
     * @param color    color of the grid lines
     * @return this camera object (for chaining)
     */
    public Camera printGrid(int interval, Color color) {
        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                if (x % interval == 0 || y % interval == 0) {
                    imageWriter.writePixel(x, y, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the final image to a file with the given name (without extension).
     *
     * @param filename the name of the image file (without extension)
     * @return this camera object (for chaining)
     */
    public Camera writeToImage(String filename) {
        imageWriter.writeToImage(filename);
        return this;
    }
}