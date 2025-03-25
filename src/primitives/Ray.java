package primitives;

public class Ray {

    Point head;
    Vector direction;

    public Ray(Point p,Vector v)
    {
        head = p;
        direction = v.normalize();
    }

    @Override
    public String toString() {
        return head.toString() + direction.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && head.equals(other.head) && direction.equals(other.direction);
    }


}
