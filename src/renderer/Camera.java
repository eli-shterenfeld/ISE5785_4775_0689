package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

public class Camera implements Cloneable {

    private Point location;
    private Vector to, up, right;
    private double vpWidth = 0.0, vpHeight = 0.0;
    private double vpDistance = 0.0;
    private int nX = 0, nY = 0;

    private Camera() {

    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

    public static class Builder {

        private final Camera camera = new Camera();

        public Builder setLocation(Point p) {
            if (p == null) throw new IllegalArgumentException("Location cannot be null");
            camera.location = p;
            return this;
        }

        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null) throw new IllegalArgumentException("Vectors cannot be null");

            camera.to = to.normalize();
            camera.up = up.normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();
            return this;
        }

        public Builder setDirection(Point lookAt, Vector upApprox) {
            if (lookAt == null || upApprox == null)
                throw new IllegalArgumentException("LookAt point or up vector cannot be null");
            this.camera.to = lookAt.subtract(this.camera.location).normalize();
            this.camera.right = this.camera.to.crossProduct(upApprox).normalize();
            this.camera.up = this.camera.right.crossProduct(this.camera.to).normalize();
            return this;
        }

        public Builder setDirection(Point lookAt) {
            return setDirection(lookAt, new Vector(0, 1, 0));
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) throw new IllegalArgumentException("View plane size must be positive");
            camera.vpWidth = width;
            camera.vpHeight = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) throw new IllegalArgumentException("View plane distance must be positive");
            camera.vpDistance = distance;
            return this;
        }

        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) throw new IllegalArgumentException("Resolution must be positive");
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        public Camera build() {
            final String GENERAL_DESCRIPTION = "Missing rendering data";
            final String CAMERA_CLASS_NAME = Camera.class.getSimpleName();

            // בדיקה שהשדות קיימים
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

            if (camera.nX == 0)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "Resolution X");

            if (camera.nY == 0)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CAMERA_CLASS_NAME, "Resolution Y");

            // בדיקה נוספת: האם הערכים עצמם הגיוניים (לא שליליים או אפס אורך)
            if (camera.vpWidth < 0)
                throw new IllegalArgumentException("View Plane Width must be positive");

            if (camera.vpHeight < 0)
                throw new IllegalArgumentException("View Plane Height must be positive");

            if (camera.vpDistance < 0)
                throw new IllegalArgumentException("View Plane Distance must be positive");

            if (camera.nX < 0)
                throw new IllegalArgumentException("Resolution X must be positive");

            if (camera.nY < 0)
                throw new IllegalArgumentException("Resolution Y must be positive");

            if (camera.to.lengthSquared() == 0)
                throw new IllegalArgumentException("Direction To vector cannot be zero vector");

            if (camera.up.lengthSquared() == 0)
                throw new IllegalArgumentException("Direction Up vector cannot be zero vector");

            // חישוב וקטור right מחדש
            camera.right = camera.to.crossProduct(camera.up);

            // בדיקה שאחרי חישוב המכפלה הוקטורית לא קיבלנו וקטור אפס
            if (camera.right.lengthSquared() == 0)
                throw new IllegalArgumentException("Right vector resulted in zero vector — direction vectors might be parallel");

            // נירמול כל הוקטורים
            camera.to = camera.to.normalize();
            camera.up = camera.up.normalize();
            camera.right = camera.right.normalize();

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Unexpected error: Clone not supported", e);
            }
        }
    }

}


