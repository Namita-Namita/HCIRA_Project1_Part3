// Point class with values x and y values to store the co-ordinates.
public class Point
{
	public double x, y;
	// default constructor
	public Point(){
		this.x = 0; this.y = 0;
	}
	//constructor to initialize x and y
	public Point(double x, double y)
	{	this.x = x; this.y = y;	}
	
	public double distance( Point other)
  {
    return Math.sqrt(Math.pow(other.x-x,2)+Math.pow(other.y-y ,2));
  }
}