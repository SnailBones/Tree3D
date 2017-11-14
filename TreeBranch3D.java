import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TreeBranch3D {
	Random rand = new Random();
	Point3D root;
	//double angle;//angle from up to down to up again. latitude.
	//double direction = 0; // angle around branch vector. longitude.
	RotationMatrix rotation;
	double size;
	Point3D tip;
	//boolean side = false;
	Color color;
	ArrayList<TreeBranch3D> babies = new ArrayList<TreeBranch3D>();
	
	public TreeBranch3D(double x, double y, double z, double s, Color c) {
		root = new Point3D(x, y, z);
		size = s;
		//angle = a;
		rotation = new RotationMatrix(3,3);
		color = c;
		this.makeTip(1);
	}
	
	public TreeBranch3D(Point3D p, double s, RotationMatrix a, Color c) {
		root = p;
		size = s;
		//angle = a;
		rotation = a;
		//a.print();
		color = c;
		//makeTip(.2);
	}
	
//	public RotationMatrix getNewRotation(RotationMatrix r, double direction, double angle) {
//		double pheta = direction;
//		double radius = size * Math.sin(angle);
//		double y = root.getY() - size * Math.cos(angle);
//		double x = root.getX() + radius * Math.cos(pheta);
//		double z = root.getZ() + radius * Math.sin(pheta);
//		rotation = RotationMatrix.rotateY(direction).multiply(r);
//		rotation = RotationMatrix.rotateX(angle).multiply(rotation);
//		//rotation.multiply(RotationMatrix.rotateY(direction));
//		//rotation = new RotationMatrix(3,3);
//		return rotation;
//	}
	public void makeTip(double levity) {
		//levity = .5;
		if (levity > 0){
			Point3D tip = new Point3D(0, -size, 0);
			Point3D rtip = rotation.multiply(tip);
			RotationMatrix followLight = RotationMatrix.rotateToward(rtip, tip, levity);
			//rotation = rotation.multiply(followLight);
			rotation = followLight.multiply(rotation);
			Point3D ltip = rotation.multiply(tip);
			this.tip = ltip;
		}
		else if (levity < 0) {
			Point3D tip = new Point3D(0, -size, 0);
			Point3D goal = new Point3D(0, size, 0);
			Point3D rtip = rotation.multiply(tip);
			RotationMatrix followLight = RotationMatrix.rotateToward(rtip, goal, -levity);
			//rotation = rotation.multiply(followLight);
			rotation = followLight.multiply(rotation);
			Point3D ltip = rotation.multiply(tip);
			this.tip = ltip;
		}
		else {
			Point3D tip = new Point3D(0, -size, 0);
			Point3D ltip = rotation.multiply(tip);
			this.tip = ltip;
		}
	}
	
	public Point3D getTip() {
		return root.add(tip);
	}
	
	private Point3D rotateAroundY(Point3D p3d, double sin_a, double cos_a){
		 double y = p3d.getY();
		 // from matrix for multiplication
		 double x = sin_a*p3d.getX() - cos_a*p3d.getZ();
		 double z = cos_a*p3d.getX() + sin_a*p3d.getZ();
//		 double x = Math.sin(camera_angle)*p3d.getX() - Math.cos(camera_angle)*p3d.getZ();
//		 double z = Math.cos(camera_angle)*p3d.getX() + Math.sin(camera_angle)*p3d.getZ();
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
		Point3D tip=getTip();
		Point3D r_tip = rotateAroundY(tip, sin_a, cos_a);
		Point3D r_root = rotateAroundY(root, sin_a, cos_a);
		int place_in_space = (int)((r_tip.z+r_root.z)/2*space_color);
		int red = Math.max(0, Math.min(color.getRed()-place_in_space, 255));
		int green = Math.max(0, Math.min(color.getGreen()-place_in_space, 255));
		int blue = Math.max(0, Math.min(color.getBlue()-place_in_space, 255));
		//System.out.println("red, green, blue: " + red + " " + green +" "+ blue);
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
	
	public TreeBranch3D makeBaby(TreeData3D rules) {

		// stop growing if less than threshold
		if (size < rules.min_size) {
			return null;
		}
		


		double size = this.size*rules.size;
		RotationMatrix new_rotation = rotation.multiply(rules.getMatrix());
		
		TreeBranch3D baby = new TreeBranch3D(getTip(), size, new_rotation,  wiggleColor(color, rules.color_warp));
		baby.makeTip(rules.grow_lean);
		babies.add(baby);

		return baby;
	}

	double addAngle(double angle, double amount){
		return (angle+amount)%(Math.PI/2);
	}
	
	//angle can be between 0 and 2 pi
   
   private Color wiggleColor(Color color, int level){
      int shake = level*2+1;
      
      int[] rgb = new int[] {color.getRed(), color.getGreen(), color.getBlue()};
      //extra green
      rgb[1]+=level;
      rgb[0]+=level;
      for (int i = 0; i<3; i++){
            int new_color = rgb[i]+rand.nextInt(shake)-level;
            if (new_color < 0){
               new_color = 0;
            }
            else if (new_color >255){
               new_color = 255;
            }
            rgb[i] = new_color;
      }
      color = new Color(rgb[0], rgb[1], rgb[2]);
      return color;
    }
}
