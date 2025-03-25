package primitives;

public class Point {

   protected Double3 xyz;
   public static Point ZERO = new Point(0.0,0.0,0.0);

    public Point(Double d1 , Double d2 , Double d3)
    {
        xyz = new Double3(d1,d2,d3);
    }
    public Point(Double3 d)
    {
        this(d.d1(), d.d2(), d.d3());
    }

    public Vector subtract (Point p)
    {
        return new Vector(xyz.subtract(p.xyz));
    }

    public Point add(Vector v)
    {
        return new Point(this.xyz.add(v.xyz));
    }

    public double distanceSquared(Point p)
    {
        return (this.xyz.d1() - p.xyz.d1()) * (this.xyz.d1() - p.xyz.d1()) + (this.xyz.d2() - p.xyz.d2()) * (this.xyz.d2() - p.xyz.d2()) + (this.xyz.d3() - p.xyz.d3()) * (this.xyz.d3() - p.xyz.d3());
    }

    public double distance(Point p)
    {
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public String toString() {
        return xyz.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && xyz.equals(other.xyz);
    }


}
