
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
	
    public static RotationMatrix rotateToward(Point3D start, Point3D destination, double amount) {
    	Point3D perp = Point3D.crossProduct(start, destination);
    	double distance = Point3D.angleBetween(start, destination);
    	if (distance == 0){
    		return new RotationMatrix();
    	}
    	else if (distance >= Math.PI){
    		perp = new Point3D(0,0, 1);
    	}
    	RotationMatrix rm = RotationMatrix.rotateAxis(perp, amount*distance); 	
    	return rm;
    }
}
