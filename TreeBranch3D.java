import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class TreeBranch3D {
	Point3D root;
	Point3D tip;
	RotationMatrix rotation;
	double growth_size = 0;
	double size;
	Color color;
	ArrayList<TreeBranch3D> babies = new ArrayList<TreeBranch3D>();
	
	public TreeBranch3D(double x, double y, double z, double s, Color c) {
		root = new Point3D(x, y, z);
		size = s;
		//angle = a;
		rotation = new RotationMatrix(3,3);
		color = c;
		tip = rotation.multiply(new Point3D(0, -size, 0));
	}
	
	public TreeBranch3D(Point3D p, double s, RotationMatrix a, Color c) {
		root = p;
		size = s;
		rotation = a;
		color = c;
	}
	
	public Point3D rotateTip(Point3D goal, double levity) {	//set correct matrix and return tip
		if (levity > 0){
			Point3D tip = new Point3D(0, -size, 0);
			Point3D rtip = rotation.multiply(tip);
			RotationMatrix followLight = RotationMatrix.rotateToward(rtip, goal, levity);
			rotation = followLight.multiply(rotation);
			Point3D ltip = rotation.multiply(tip);
			return ltip;
		}
		else if (levity < 0) {
			Point3D tip = new Point3D(0, -size, 0);
			goal = new Point3D(-goal.getX(), -goal.getY(), -goal.getZ());
			Point3D rtip = rotation.multiply(tip);
			RotationMatrix followLight = RotationMatrix.rotateToward(rtip, goal, -levity);
			rotation = followLight.multiply(rotation);
			Point3D ltip = rotation.multiply(tip);
			return ltip;
		}
		else {
			Point3D tip = new Point3D(0, -size, 0);
			Point3D ltip = rotation.multiply(tip);
			return ltip;
		}
	}
	
	public Point3D getGrowTip(double size) {//get tip while it's growing
		Point3D ltip = rotation.multiply(new Point3D(0, -size, 0));
		return ltip;
	}
	
	public Point3D getTip() {
		return root.add(tip);
	}
	
	public static Point3D rotateAroundY(Point3D p3d, double sin_a, double cos_a){
		 double y = p3d.getY();
		 // from matrix for multiplication
		 double x = sin_a*p3d.getX() - cos_a*p3d.getZ();
		 double z = cos_a*p3d.getX() + sin_a*p3d.getZ();
		 return new Point3D(x, y, z);
	}
	
	public void drawTwoLines(Graphics2D g2d, double x_start, double y_start, double x_end, double y_end, double gap, double end_gap) {
		double x_gap = gap*(y_end-y_start);
		double y_gap = -gap*(x_end-x_start);
		double x_end_gap = end_gap*(y_end-y_start);
		double y_end_gap = -end_gap*(x_end-x_start);
		g2d.draw(new Line2D.Double(x_start+x_gap, y_start+y_gap, x_end+x_end_gap, y_end+y_end_gap));
		g2d.draw(new Line2D.Double(x_start-x_gap, y_start-y_gap, x_end-x_end_gap, y_end-y_end_gap));
	}
	
	public void drawMe(Graphics2D g2d, double sin_a, double cos_a) {
		drawMe(g2d, sin_a, cos_a, size);
		
	}
	
	public void drawMe(Graphics2D g2d, double sin_a, double cos_a, double lastSize) {
		double space_color = .3;
		Point3D tip=getGrowTip(growth_size).add(root);
		Point3D r_tip = rotateAroundY(tip, sin_a, cos_a);
		Point3D r_root = rotateAroundY(root, sin_a, cos_a);
		int place_in_space = (int)((r_tip.z+r_root.z)/2*space_color);
		int red = Math.max(0, Math.min(color.getRed()-place_in_space, 255));
		int green = Math.max(0, Math.min(color.getGreen()-place_in_space, 255));
		int blue = Math.max(0, Math.min(color.getBlue()-place_in_space, 255));
		Color newColor = new Color(red, green, blue);
//		g2d.setColor(Color.black);
		//g2d.setStroke(new BasicStroke((int)(size/7)));
		g2d.setStroke(new BasicStroke((int)(size/7), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		//g2d.draw(new Line2D.Double(r_tip.getX(), r_tip.getY(), r_root.getX(), r_root.getY()));
		g2d.setColor(newColor);
		g2d.draw(new Line2D.Double(r_tip.getX(), r_tip.getY(), r_root.getX(), r_root.getY()));
//		g2d.setStroke(new BasicStroke());
//		if (lastSize>20) {
//			double ratio = lastSize/size;
//			//System.out.println(ratio);
//			//double ratio = 0.5;
//			double width = 1/14.0;
//			double new_width = width*ratio;
//			drawTwoLines(g2d, r_tip.getX(), r_tip.getY(), r_root.getX(), r_root.getY(), width, new_width);
//		}
//		else {
//			g2d.draw(new Line2D.Double(r_tip.getX(), r_tip.getY(), r_root.getX(), r_root.getY()));
//		}
		for (TreeBranch3D baby: babies) {
			if (baby != null)
				baby.drawMe(g2d, sin_a, cos_a, size);
		}
	}
	
	public TreeBranch3D makeBaby(TreeData3D rules, Point3D goal) {

		// stop growing if less than threshold
		if (size < rules.min_size) {
			return null;
		}
		


		double size = this.size*rules.size;
		RotationMatrix new_rotation = rotation.multiply(rules.getMatrix());
		
		TreeBranch3D baby = new TreeBranch3D(getTip(), size, new_rotation,  TreeData3D.wiggleColor(color, rules.color_warp));
		baby.tip = baby.rotateTip(goal, rules.grow_lean);
		babies.add(baby);

		return baby;
	}
	
	public boolean grow(double g_s) {
		growth_size += g_s;
		if (growth_size>size) {
			growth_size = size;
			return true;
		}
		return false;
	}

}
