package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

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
     * The forward direction vector of the camera.
     */
    private Vector to, up, right;

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
     * Builder class for creating Camera instances.
     */
    public static class Builder {

        /**
         * The Camera object being constructed by this builder.
         */
        private final Camera camera = new Camera();

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
            if (width <= 0 || height <= 0) throw new IllegalArgumentException("View plane size must be positive");
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
            if (distance <= 0) throw new IllegalArgumentException("View plane distance must be positive");
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
            // not implemented yet
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

            if (camera.vpWidth == 0)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "View Plane Width");

            if (camera.vpHeight == 0)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "View Plane Height");

            if (camera.vpDistance == 0)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "View Plane Distance");

            // Additional validation: check if values are logical (non-negative and non-zero length)
            if (camera.vpWidth < 0)
                throw new IllegalArgumentException("View Plane Width must be positive");

            if (camera.vpHeight < 0)
                throw new IllegalArgumentException("View Plane Height must be positive");

            if (camera.vpDistance < 0)
                throw new IllegalArgumentException("View Plane Distance must be positive");

            if (camera.to.lengthSquared() == 0)
                throw new IllegalArgumentException("Direction To vector cannot be zero vector");

            if (camera.up.lengthSquared() == 0)
                throw new IllegalArgumentException("Direction Up vector cannot be zero vector");

            // Recalculate the right vector
            camera.right = camera.to.crossProduct(camera.up);

            // Validate that cross product did not result in a zero vector
            if (camera.right.lengthSquared() == 0)
                throw new IllegalArgumentException("Right vector resulted in zero vector — direction vectors might be parallel");

            // Normalize all vectors
            camera.to = camera.to.normalize();
            camera.up = camera.up.normalize();
            camera.right = camera.right.normalize();

            // Return a clone of camera to avoid exposing internal references
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Unexpected error: Clone not supported", e);
            }
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
        if (isZero(vpDistance))
            throw new IllegalArgumentException("View Plane distance cannot be zero");

        // Calculate pixel size
        double rX = vpWidth / nX;
        double rY = vpHeight / nY;

        // Offsets from the center
        double xJ = (j - (nX - 1) * 0.5) * rX;
        double yI = -(i - (nY - 1) * 0.5) * rY;

        // Start with center point of view plane
        Vector offset = to.scale(vpDistance);
        if (!isZero(xJ)) offset = offset.add(right.scale(xJ));
        if (!isZero(yI)) offset = offset.add(up.scale(yI));

        return new Ray(location, offset);
    }
}