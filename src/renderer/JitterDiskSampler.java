package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.PI;

/**
 * JitterDiskSampler generates rays in a disk pattern with jitter around a base ray.
 * This is useful for antialiasing and soft shadows in rendering.
 */
public class JitterDiskSampler implements RaySampler {

    /**
     * The number of rays to sample in a jitter disk pattern.
     */
    private static final double TWO_PI = 2.0 * PI;

    /**
     * A small epsilon value to handle floating-point precision issues.
     */
    private static final double EPSILON = 1e-10;

    @Override
    public List<Ray> sample(Ray baseRay, Vector normal, double radius, double distance, int count) {
        List<Ray> rays = new LinkedList<>();

        Vector dir = baseRay.getDirection();
        Point origin = baseRay.getHead();
        Vector u = dir.findAnyOrthogonal().normalize();
        Vector v = dir.crossProduct(u).normalize();
        Point targetCenter = origin.add(dir.scale(distance));

        int radialSamples = (int) Math.sqrt(count);
        int baseAngularSamples = count / radialSamples;
        int extraRays = count % radialSamples;

        double radialStep = radius / radialSamples;

        for (int i = 0; i < radialSamples; i++) {
            double rMin = radialStep * i;
            double rMax = radialStep * (i + 1);

            int angularSamples = baseAngularSamples + (i < extraRays ? 1 : 0);
            double angleStep = TWO_PI / angularSamples;

            for (int j = 0; j < angularSamples; j++) {
                double r = Util.random(rMin, rMax);
                if (r < EPSILON) r = radialStep * 0.05;

                double angle = Util.random(j * angleStep, (j + 1) * angleStep);
                Vector newDir = targetCenter
                        .add(u.scale(r * Math.cos(angle)))
                        .add(v.scale(r * Math.sin(angle)))
                        .subtract(origin);

                if (newDir.dotProduct(normal) > 0) rays.add(new Ray(origin, newDir, normal));
            }
        }

        return rays;
    }

}
