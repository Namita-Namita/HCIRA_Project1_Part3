// Point class with values x and y to store x and y coordinates.
public class Point
{
	public double x, y;
	// default constructor
	public Point(){
		this.x = 0; this.y = 0;
	}
	//constructor to assign x and y values.
	public Point(double x, double y)
	{	this.x = x; this.y = y;	}
	// distance method to calculate distance between two points x and y
	public double distance( Point other)
  {
    return Math.sqrt(Math.pow(other.x-x,2)+Math.pow(other.y-y ,2));
  }
}