
public class RotationMatrix
{
	double [][] array;
	public RotationMatrix() {
		array = new double[3][3];
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 3; j++) {
    			if (i == j) {
    				array[i][j] = 1;
    			}
    			else {
    				array[i][j] = 0;
    			}
    		}
    	}
	}
	public RotationMatrix(int rows, int columns, double value) {
		array = new double[rows][columns];
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < columns; j++) {
    			array[i][j] = value;
    		}
    	}
	}
	
	public RotationMatrix(int rows, int columns) {
		array = new double[rows][columns];
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < columns; j++) {
    			if (i == j) {
    				array[i][j] = 1;
    			}
    			else {
    				array[i][j] = 0;
    			}
    		}
    	}
	}
	public RotationMatrix(double[][] ar) {
		array = ar;
	}
	public void setRow(int i, double[] row) {
		array[i] = row;
	}
	public double[][] getArray(){
		return array;
	}
	
	public int rows(){
		return array.length;
	}
	
	public int columns(){
		return array[0].length;
	}
	
	public RotationMatrix copy() {
		RotationMatrix matrix = new RotationMatrix(array);
		return matrix;
	}
		
	public void addRow(double[] row) {
		double[][] new_arr = new double[array.length+1][];
		for (int i = 0; i<array.length; i++) {
			new_arr[i]=array[i];
		}
		new_arr[-1]=row;
	}
	
	
	public void print() {
		System.out.println(" - - - MATRIX - - -");
    	for (int i = 0; i < array.length; i++) {
    		System.out.print(" |  ");
    		for (int j = 0; j < array[0].length; j++) {
    			System.out.print(" " + array[i][j] + " ");
    		}
    		System.out.println(" |");
    	}
    	System.out.println(" - - - END - - -");
	}
	
	public RotationMatrix add(RotationMatrix other) {
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			array[i][j] += other.array[i][j];
    		}
    	}
		return this;
	}
	public RotationMatrix add(Double number) {
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			array[i][j] += number;
    		}
    	}
		return this;
	}
	public RotationMatrix multiply(Double number) {
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			array[i][j] *= number;
    		}
    	}
		return this;
	}
	public RotationMatrix multiply(RotationMatrix other) {
		//int size = Math.min(rows(), columns());
//		double[][] product = new double[rows()][other.columns()];
//    	for (int i = 0; i < array.length; i++) {
//    		for (int j = 0; j < array[0].length; j++) {
//    			double num = 0;
//    	    	for (int k = 0; k < array.length; k++) {
//	    			num += array[i][k] * other.array[k][j];
//	    		}
//    	    	product[i][j] = num;
//    	    	
//    		}
//    	}
//    	Matrix matrix = new Matrix(product);
		return new RotationMatrix(multiply(other.array));
	}
	
	public Point3D multiply(Point3D point) {
		return new Point3D(multiply(point.getArray()));
	}
	
	public double[][] multiply(double[][] other) {
		//int size = Math.min(rows(), columns());
		double[][] product = new double[rows()][other[0].length];
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			double num = 0;
    	    	for (int k = 0; k < array.length; k++) {
	    			num += array[i][k] * other[k][j];
	    		}
    	    	product[i][j] = num;
    	    	
    		}
    	}
		return product;
	}
	public double[] multiply(double[] other) {
		//multiply by a single column array
		//int size = Math.min(rows(), columns());
		double[]product = new double[other.length];
		for (int j = 0; j < array.length; j++) {
			double num = 0;
	    	for (int k = 0; k < array[0].length; k++) {
    			num += array[j][k] * other[k];
    		}
	    	product[j] = num;
	    	
		}
		return product;
	}
	
	public double determinant() {
		if (rows() != 2 || columns() != 2) {
			return  Double.NaN;
		}
		return array[0][0]*array[1][1]-array[0][1]*array[1][0];
	}
	
	public RotationMatrix inverse() {
		if (rows() != 2 || columns() != 2) {
			return  null;
		}
		double inv_det = 1/determinant();
		double[][] inverse = new double[2][2];
		inverse[0][0]=array[1][1]*inv_det;
		inverse[1][1]=array[0][0]*inv_det;
		inverse[0][1]=-array[0][1]*inv_det;
		inverse[1][0]=-array[1][0]*inv_det;
		return new RotationMatrix(inverse);
	}
	public static RotationMatrix rotateX(double pheta) {
		double[][] a = { {1, 0, 0},
						{0, Math.cos(pheta), -Math.sin(pheta)},
						{0, Math.sin(pheta), Math.cos(pheta)} };
		RotationMatrix x = new RotationMatrix(a);
		return x;
	}
	public static  RotationMatrix rotateY(double pheta) {
		double[][] a = { {Math.cos(pheta), 0, Math.sin(pheta)},
						{0, 1, 0},
						{-Math.sin(pheta), 0, Math.cos(pheta)} };
		RotationMatrix x = new RotationMatrix(a);
		return x;
	}
	
	public static RotationMatrix rotateZ(double pheta) {
		double[][] a = { {Math.cos(pheta), -Math.sin(pheta), 0},
						{Math.sin(pheta), Math.cos(pheta), 0},
						{0, 0, 1} };
		RotationMatrix x = new RotationMatrix(a);
		return x;
	}
	//converts to unit vector and rotates on a custom axis
	public static RotationMatrix rotateAxis(Point3D axis, double pheta) {
		//System.out.println("length is " + axis.getLength());
		axis.normalize();
		//System.out.println("length is " + axis.getLength());
		double s = Math.sin(pheta);
		double c = Math.cos(pheta);
		double x = axis.getX();
		double y = axis.getY();
		double z = axis.getZ() ;
		double[][] a = { {c+(1-c)*x*x, (1-c)*x*y-s*z, (1-c)*x*z+s*y},
						{(1-c)*x*y+s*z, c+(1-c)*y*y, (1-c)*y*z-s*x},
						{(1-c)*x*z-s*y, (1-c)*y*z+s*x, c+(1-c)*z*z} };
		RotationMatrix m = new RotationMatrix(a);
		return m;
	}
	
//	public double[] getQuaternion() {
//		double m00 = array[0][0];
//		double m01 = array[0][1];
//		double m02 = array[0][2];
//		double m10 = array[1][0];
//		double m11 = array[1][1];
//		double m12 = array[1][2];
//		double m20 = array[2][0];
//		double m21 = array[2][1];
//		double m22 = array[2][2];
//		double tr = m00 + m11 + m22;
//		double qw, qx, qy, qz;
//		if (tr > 0) { 
//		  double S = Math.sqrt(tr+1.0) * 2; // S=4*qw 
//		  qw = 0.25 * S;
//		  qx = (m21 - m12) / S;
//		  qy = (m02 - m20) / S; 
//		  qz = (m10 - m01) / S; 
//		} else if ((m00 > m11)&(m00 > m22)) { 
//		  double S = Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx 
//		  qw = (m21 - m12) / S;
//		  qx = 0.25 * S;
//		  qy = (m01 + m10) / S; 
//		  qz = (m02 + m20) / S; 
//		} else if (m11 > m22) { 
//		  double S = Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
//		  qw = (m02 - m20) / S;
//		  qx = (m01 + m10) / S; 
//		  qy = 0.25 * S;
//		  qz = (m12 + m21) / S; 
//		} else { 
//		  double S = Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
//		  qw = (m10 - m01) / S;
//		  qx = (m02 + m20) / S;
//		  qy = (m12 + m21) / S;
//		  qz = 0.25 * S;
//		}
//		return new double[] {qw, qx, qy, qz};
//	}
    public static RotationMatrix rotateToward(Point3D start, Point3D destination, double amount) {
    	//Point3D perp = crossProduct(destination, this);
    	Point3D perp = Point3D.crossProduct(start, destination);
    	//perp.printPoint();
    	double distance = Point3D.angleBetween(start, destination);
    	if (distance == 0){
    		return new RotationMatrix();
    	}
    	else if (distance >= Math.PI){
    		perp = new Point3D(0,0, 1);
    	}
    	//RotationMatrix zam = RotationMatrix.rotateAxis(perp, amount*distance);
    	RotationMatrix rm = RotationMatrix.rotateAxis(perp, amount*distance);
    	//Point3D twist = zam.multiply(this);
    	//Point3D twist = new Point3D(x*(1-amount)+destination.x*amount, y*(1-amount)+destination.y*amount, z*(1-amount)+destination.z*amount);
    	
    	return rm;
    }
}
