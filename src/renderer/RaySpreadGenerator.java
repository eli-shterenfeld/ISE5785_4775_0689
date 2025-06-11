package renderer;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

public class RaySpreadGenerator {

    /**
     * Generates a beam of rays around a base direction.
     *
     * @param baseRay the central ray (e.g., refraction or reflection)
     * @param normal  the surface normal at the point of intersection
     * @param radius  the maximum deviation angle
     * @param count   number of rays to generate
     * @return list of rays deviated from the base direction
     */
//    public static List<Ray> generateDiskSampleRays(Ray baseRay, Vector normal, double radius, double screenDistance, int count) {
//        List<Ray> rays = new ArrayList<>(count);
//
//        Vector dir = baseRay.getDirection();
//        Point origin = baseRay.getHead();
//
//        // Build orthonormal basis (u, v)
//        Vector u = dir.findAnyOrthogonal().normalize();
//        Vector v = dir.crossProduct(u).normalize();
//
//        ThreadLocalRandom rand = ThreadLocalRandom.current();
//
//        for (int i = 0; i < count; i++) {
//            double angle = rand.nextDouble(0, 2 * Math.PI);
//            double r = Math.sqrt(rand.nextDouble()) * radius;
//
//            Vector offset = u.scale(r * Math.cos(angle)).add(v.scale(r * Math.sin(angle)));
//            Vector newDir = origin.add(dir.scale(screenDistance)).add(offset).subtract(origin).normalize();
//
//            if (newDir.dotProduct(normal) <= 0) continue;
//
//            rays.add(new Ray(origin, newDir, normal));
//        }
//
//        return rays;
//    }
//    public static List<Ray> generateDiskSampleRays(Ray baseRay, Vector normal, double radius, double screenDistance, int count) {
//        List<Ray> rays = new ArrayList<>(count);
//
//        Vector dir = baseRay.getDirection();
//        Point origin = baseRay.getHead();
//
//        Vector u = dir.findAnyOrthogonal().normalize();
//        Vector v = dir.crossProduct(u).normalize();
//        Point targetCenter = origin.add(dir.scale(screenDistance));
//
//        int radialSamples = (int) Math.sqrt(count);
//        int angularSamples = count / radialSamples;
//        double angleStep = 2.0 * Math.PI / angularSamples;
//
//        for (int i = 1; i <= radialSamples; i++) {
//            double r = radius * (i + 0.5) / radialSamples;
//
//            for (int j = 0; j < angularSamples; j++) {
//                double angle = angleStep * (j + 0.001);
//                double uComponent = r * Math.cos(angle);
//                double vComponent = r * Math.sin(angle);
//
//                if (isZero(uComponent) && isZero(vComponent)) uComponent = Math.copySign(1e-10, uComponent);
//
//                Vector offset = u.scale(uComponent).add(v.scale(vComponent));
//                Vector newDir = targetCenter.add(offset).subtract(origin);
//
//                if (newDir.dotProduct(normal) > 0) rays.add(new Ray(origin, newDir, normal));
//            }
//        }
//
//        return rays;
//    }
    public static List<Ray> generateDiskSampleRays(Ray baseRay, Vector normal, double radius, double screenDistance, int count) {
        List<Ray> rays = new ArrayList<>(count);

        Vector dir = baseRay.getDirection();
        Point origin = baseRay.getHead();

        // Build orthonormal basis (u, v)
        Vector u = dir.findAnyOrthogonal().normalize();
        Vector v = dir.crossProduct(u).normalize();
        Point targetCenter = origin.add(dir.scale(screenDistance));

        // Grid dimensions
        int radialSamples = (int) Math.sqrt(count);
        int angularSamples = count / radialSamples;

        // Pre-compute step sizes for jittering
        double radialStep = radius / radialSamples;
        double angleStep = 2.0 * Math.PI / angularSamples;

        for (int i = 0; i < radialSamples; i++) {
            // Define radial bounds for this ring
            double rMin = radius * i / radialSamples;
            double rMax = radius * (i + 1) / radialSamples;

            for (int j = 0; j < angularSamples; j++) {
                // Define angular bounds for this sector
                double angleMin = angleStep * j;
                double angleMax = angleStep * (j + 1);

                // Jitter within the cell bounds
                double r = Util.random(rMin, rMax);
                double angle = Util.random(angleMin, angleMax);

                // Handle center point (r=0) case - use small random radius
                if (isZero(r)) r = Util.random(0, radialStep * 0.1); // Small radius to avoid zero vector

                // Convert polar to cartesian with jittered values
                double uComponent = r * Math.cos(angle);
                double vComponent = r * Math.sin(angle);

                // Safety check for zero vectors (should be rare with jittering)
                if (isZero(uComponent) && isZero(vComponent))
                    uComponent = Math.copySign(1e-10, uComponent != 0 ? uComponent : 1.0);

                // Build offset vector and ray direction
                Vector offset = u.scale(uComponent).add(v.scale(vComponent));
                Vector newDir = targetCenter.add(offset).subtract(origin);

                // Ensure ray points forward relative to surface normal
                if (newDir.dotProduct(normal) > 0) rays.add(new Ray(origin, newDir, normal));
            }
        }

        return rays;
    }

}
