package primitives;

public class Vector extends Point{

    public Vector(double d1,double d2,double d3)
    {
        super(d1,d2,d3);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero");
    }

    public Vector(Double3 double3)
    {
        super(double3);
         if(double3.equals(Double3.ZERO))
           throw new IllegalArgumentException("Vector zero");
    }

    public Vector add(Vector v)
    {
        return new Vector(v.xyz.add(this.xyz));
    }

    public Vector scale(double d)
    {
        return new Vector(this.xyz.scale(d));
    }

    public double dotProduct(Vector v)
    {
        return v.xyz.d1() * this.xyz.d1() + v.xyz.d2() * this.xyz.d2() + v.xyz.d3() * this.xyz.d3();
    }

    public Vector crossProduct(Vector v)
    {
        return new Vector( this.xyz.d2() * v.xyz.d3() - v.xyz.d2() *  this.xyz.d3(), - (this.xyz.d1() *  v.xyz.d3() - v.xyz.d1() * this.xyz.d3()), this.xyz.d1() * v.xyz.d2() - v.xyz.d1() *  this.xyz.d2());
    }

   public double lengthSquared()
   {
       return this.dotProduct(this);
   }

   public double length()
   {
       return Math.sqrt(lengthSquared());
   }

   public Vector normalize()
   {
       double length = length(); // chack if this ok..
       return new Vector(this.xyz.d1() / length, this.xyz.d2() / length, this.xyz.d3() / length);
   }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.xyz.equals(other.xyz);
    }



}
