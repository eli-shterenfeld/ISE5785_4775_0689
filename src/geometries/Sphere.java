package geometries;

import primitives.*;

import java.util.List;
import static primitives.Util.*;


/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a Sphere with a given radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }


    public List<Point> findIntersections(Ray ray) {

        //וקטור מראש הקרן למרכז המעגל
       Vector u = center.subtract(ray.getHead());

        //תוספת שלי, אם הקרן מחוץ לכדור וגם הזווית בין u לכיוון הקרן גדולה מ 90 אז אין חיתוכים
       if(alignZero(u.dotProduct(ray.getDirection())) <= 0 && alignZero(u.length()) >= radius)
           return null;
           

       //צד אחד של המשולש
       double tm = u.dotProduct(ray.getDirection());
       //המרחק בריבוע ממרכז המעגל
       double dSquared = u.lengthSquared() - tm*tm;

       //אם המרחק גדול או שווה לרדיוס
       if(alignZero(Math.sqrt(dSquared) - radius) >= 0)
           return null;

       double th = Math.sqrt(radius*radius - dSquared);


       //בודק כל מקרה
        if(alignZero(tm - th) > 0 && alignZero(tm + th) > 0)
            return List.of( ray.getPoint(tm-th), ray.getPoint(tm + th));
        if(alignZero(tm - th) <= 0 && alignZero(tm + th) > 0)
            return List.of(ray.getPoint(tm + th));
        if(alignZero(tm - th) <= 0 && alignZero(tm + th) <= 0)
           return null;
        //if(alignZero(tm - th) > 0 && alignZero(tm + th) <= 0)
        //   return null;

        return null;
    }

}
