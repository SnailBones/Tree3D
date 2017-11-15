//stores an x, y, and z used for points and vectors
public class Point3D {
	double x;
	double y;
	double z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(double[] ar) {
		this.x = ar[0];
		this.y =ar[1];
		this.z = ar[2];
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double x) {
		this.y = x;
	}
	public void setZ(double x) {
		this.z = x;
	}

	public double[] getArray() {
		return new double[] {x, y, z};
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public Point3D VectorTo(Point3D other) {
		//treats vector as point for time being
		double new_x = other.x-x;
		double new_y = other.y-y;
		double new_z = other.z-z;
		return new Point3D(new_x, new_y, new_z);
	}
	
    public static double distanceSquared(Point3D me, Point3D other) {
    	double x2 = (other.x-me.x)*(other.x-me.x);
    	double y2 = (other.y-me.y)*(other.y-me.y);
    	double z2 = (other.z-me.z)*(other.z-me.z);
    	return x2+y2+z2;
    	
    }
    
    public static double distance(Point3D me, Point3D other) {
    	return Math.sqrt(distanceSquared(me, other));
    }
    
    public static double[] subtract(double[] p1, double[] p2) {
    	double[] result = new double[p1.length];
    	for (int i = 0; i < p1.length; i++) {
    		result[i] = p1[i]-p2[i];
    	}
    	return result;
    }
    public static Point3D subtract(Point3D p1, Point3D p2) {
    	return new Point3D(subtract(p1.getArray(), p2.getArray()));
    }
    
    public Point3D add(Point3D p2) {
    	return new Point3D(x + p2.x, y + p2.y, z+p2.z);
    }
    public static double dotProduct(double[] m1, double[] m2) {
    	double result = 0;
    	for (int i = 0; i < m1.length; i++) {
    		result += m1[i]*m2[i];
    	}
		return result;
    	
    }
    
    public static double lengthOf(double[] vect) {
    	double z2= 0;
    	for (int i = 0; i < vect.length; i++) {
    		z2 += vect[i]*vect[i];
    	}
    	return Math.sqrt(z2);
    	
    }
    
    
    public static double[] crossProduct(double[] m1, double[] m2) {
    	double[] result = new double[m1.length];
    	result[0] = m1[1]*m2[2]-m1[2]*m2[1];
    	result[1] = m1[2]*m2[0]-m1[0]*m2[2];
    	result[2] = m1[0]*m2[1]-m1[1]*m2[0];
		return result;
    }
    
    public static Point3D crossProduct(Point3D p1, Point3D p2) {
    	return new Point3D(crossProduct(p1.getArray(), p2.getArray()));
    }
    
    
    
    //normal vector from 3 points on a plane
    public static Point3D normalToPlane(Point3D p1, Point3D p2, Point3D p3) {
    	Point3D v1 = subtract(p1,  p2);
    	Point3D v2 = subtract(p2,  p3);
    	return crossProduct(v1, v2);
    }
    
    public static double cosBetween(double[] v1, double[] v2) {
		double dot = dotProduct(v1, v2);
		double lengths = lengthOf(v1)*lengthOf(v2);
		
		double cospheta = dot/lengths;
		return cospheta;
    }
    
    public static double cosBetween(Point3D p1, Point3D p2) {
    	return cosBetween(p1.getArray(),p2.getArray());
    }
    
    public static double angleBetween(double[] v1, double[] v2) {
		double angle = Math.acos(cosBetween(v1, v2));
    	return angle;	
    }
    
    public static double degreesBetween(Point3D p1, Point3D p2) {
    	return Math.toDegrees(angleBetween(p1.getArray(), p2.getArray()));
    }
    
    public static double angleBetween(Point3D p1, Point3D p2) {
    	return angleBetween(p1.getArray(), p2.getArray());
    }
    
    public double getLength() {
    	double z2= x*x+y*y+z*z;
    	return Math.sqrt(z2);
    }
    
    public void normalize() {
    	double distance = getLength();
    	x = x/distance;
    	y = y/distance;
    	z = z/distance;
    }
    
    
    public void printPoint() {
    	System.out.println("{ x: " + x + ", y: " + y + ", z: " + z + " }"); 
    }
    
}
