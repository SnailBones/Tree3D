//this class stores the rules used to generate a particular tree.


import java.awt.Color;
import java.util.Random;

public class TreeData3D {
	int min_size;
	
	//angle between branch and trunk or angle of bend in trunk
	double angle, angle_warp;
	//size relative to last node
	double size, size_warp;
	//which direction the next branch will come from
	double whorl, whorl_warp;
	
	
	//1 is up, 0 is random, -1 is down.
	double grow_lean = 0;
	
	int color_warp = 1;
	
	private static Random rand = new Random();
	public TreeData3D() {
		
	}
	public static TreeData3D DefaultTrunk() {
		TreeData3D sets = new TreeData3D();
		
		sets.grow_lean = 0;

		sets.angle = 0;
		sets.angle_warp = Math.PI/9;
		
		sets.size = .8;
		sets.size_warp = .2;
		
		sets.whorl = Math.PI*2/3;
		sets.whorl_warp = 0;
		
		sets.color_warp = 30;
		
		sets.min_size = 5;
		
		return sets;
	}
	public static TreeData3D DefaultBranch() {
		TreeData3D sets = new TreeData3D();
		
		sets.grow_lean = 0;
		
		sets.angle = Math.PI/3.5;
		sets.angle_warp = Math.PI/6;
		
		sets.size = .7;
		sets.size_warp = .3;
		
		sets.whorl = 0;
		sets.whorl_warp = 0;
		
		sets.color_warp = 3;
		
		sets.min_size = 10;
		
		return sets;
	}
	Random random;
	public static TreeData3D Random(int min_size, int color_warp) {
		
		TreeData3D sets = new TreeData3D();
		
		sets.grow_lean= rand.nextDouble()*2-1;
		
		
		sets.angle = Math.abs(rand.nextGaussian())*Math.PI/2;
		sets.angle_warp = Math.abs(rand.nextGaussian())*Math.PI/4;
		
		sets.size = rand.nextGaussian()/12 + .75;
		while (sets.size > .95 || sets.size < 0) {
			System.out.println("you got a size of " + sets.size + " so i'm correcting it!");
			sets.size = sets.size*.75+.2;
		}
		sets.size_warp = rand.nextDouble()* Math.PI/2;
		
		sets.whorl = rand.nextDouble()*Math.PI;
		sets.whorl_warp = Math.abs(rand.nextGaussian())*Math.PI/4;
		
		
		sets.color_warp = color_warp;
		
		sets.min_size = min_size;
		
		return sets;
	}
	
    public static double wiggleAngle(double value, double level) {
    	return (value + (rand.nextDouble()-.5)*level);//%(Math.PI/2);
    }
	
    public static double wiggle(double value, double level) {
    	return value + (rand.nextDouble()-.5)*level;
    }
    
    public static Color wiggleColor(Color color, int level){
        int shake = level*2+1;
        
        int[] rgb = new int[] {color.getRed(), color.getGreen(), color.getBlue()};
        //extra green and yellow
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
	
	
	public RotationMatrix getMatrix() {
		RotationMatrix matrix = new RotationMatrix(3,3);
		RotationMatrix y_rotate = RotationMatrix.rotateY(wiggle(whorl, whorl_warp));
		RotationMatrix z_rotate = RotationMatrix.rotateX(wiggle(angle, angle_warp));
		return matrix.multiply(y_rotate).multiply(z_rotate);
		
	}
	
	public static double lerp(double d1, double d2, double ratio) {
		return (d1*(1-ratio) + d2*ratio);
	}
	
    public static TreeData3D blend(TreeData3D d1, TreeData3D d2, double ratio) {
		TreeData3D d = new TreeData3D();
		d.grow_lean = lerp(d1.grow_lean, d2.grow_lean, ratio);
		d.angle = lerp(d1.angle, d2.angle, ratio);
		d.angle_warp = lerp(d1.angle_warp, d2.angle_warp, ratio);
		d.size = lerp(d1.size, d2.size, ratio);
		d.size_warp = lerp(d1.size_warp, d2.size_warp, ratio);
		d.whorl = lerp(d1.whorl, d2.whorl, ratio);
		d.whorl_warp = lerp(d1.whorl_warp, d2.whorl_warp, ratio);
		
		d.color_warp = (int)(d1.color_warp*(1-ratio) + d2.color_warp*ratio);
		d.min_size = d2.min_size;
    	return d;
    	
    }
}
