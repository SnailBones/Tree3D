
/**
 * SimpleCanvas.java
 *
 * Part of the basic graphics Template.
 *
 */

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

import libraries.AdditiveComposite;
import libraries.GifSequenceWriter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;


//stores the tree's generation rules, generates and draws tree

public class TreeWorld extends JPanel implements MouseInputListener, KeyListener
{
    Random rand = new Random();
    Color colorBackground = Color.black;
    Color colorAxis = Color.red;
    Color rootColor = Color.lightGray;
    TreeBranch3D tree;
    double controlSize = 1;
    double rootSize;
    ArrayList <TreeBranch3D> activeBranches = new ArrayList <TreeBranch3D>();
    TreeData3D trunk_rules;
    TreeData3D branch_rules;
    TreeData3D trunk_tip_rules;
    TreeData3D branch_tip_rules;
    boolean enable_tip_rules = false;
    Point3D goal = new Point3D(0,-1,0);
    
    double rotation; //camera angle
    
    Timer timer = new Timer();
    TimerTask task;
    Timer rotate_timer = new Timer();
    TimerTask rotate_task;
    
    public TreeWorld ()
	 {
    	trunk_tip_rules = trunk_rules = TreeData3D.DefaultTrunk();
    	branch_tip_rules = branch_rules = TreeData3D.DefaultBranch();
		setPreferredSize(new Dimension(900,900));
		setBackground(Color.black);
		//startTree();
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
        setFocusable(true);

    }
    
    private void startTree() {
		 if (task != null)
			 task.cancel();
		activeBranches.clear();
    	double average_size = (trunk_rules.size+branch_rules.size)/2;
    	rootSize = 120/average_size*controlSize;
    	tree = new TreeBranch3D(0, -100, 0, rootSize, rootColor);
		activeBranches.add(tree);
		setTimer();
    }
    
    
    private class MyTimerTask extends TimerTask {
        public void run() {
            if (!step()) {	//step until tree is grown
        		cancel();
            }
        }
    }
    
    private boolean step(){
    	ArrayList<TreeBranch3D> newBranches = new ArrayList<TreeBranch3D>();
    	ArrayList<TreeBranch3D> deadBranches = new ArrayList<TreeBranch3D>();
    	for (TreeBranch3D branch: activeBranches) {
    		if (branch.grow(10)){
        		TreeBranch3D baby_trunk;
        		TreeBranch3D baby_branch;
	    		if (enable_tip_rules) {
	        		double size = branch.size/rootSize;
	        		baby_trunk = branch.makeBaby(TreeData3D.blend(trunk_tip_rules, trunk_rules, size), goal);
	        		baby_branch = branch.makeBaby(TreeData3D.blend(branch_tip_rules, branch_rules, size), goal);
	    		}
	    		else {	//use root rules for whole tree
	          		baby_trunk = branch.makeBaby(trunk_rules, goal);
	        		baby_branch = branch.makeBaby(branch_rules, goal);
	    			
	    		}
	    		if (baby_trunk != null) {
	    			newBranches.add(baby_trunk);
	    		}
	    		if (baby_branch != null) {
	    			newBranches.add(baby_branch);
	    		}
	    		deadBranches.add(branch);
    		}
    	}
    	for (TreeBranch3D branch : deadBranches) {
    		activeBranches.remove(branch);
    	}
    	activeBranches.addAll(newBranches);
    	repaint();
    	if (activeBranches.isEmpty()) {
    		return false;
    	}
    	return true;

    }
    
    public void setMode(boolean rules) {
    	enable_tip_rules = rules;
		branch_tip_rules = branch_rules;
		trunk_tip_rules = trunk_rules;
    }
   
    public void setRules(TreeData3D rules, int branch_number){
	 		if (branch_number == 0)
				{trunk_rules = rules;}
			else
				{branch_rules = rules;}
 		startTree();
    }
    public void setRules(TreeData3D rules1, TreeData3D rules2, int branch_number){
	 		if (branch_number == 0)
				{trunk_rules = rules1;
				trunk_tip_rules = rules2;}
			else
				{branch_rules = rules1;
				branch_tip_rules = rules2;}
 		startTree();
    }
    
    public void randomizeRules(){
    	trunk_rules = TreeData3D.Random(trunk_rules.min_size, trunk_rules.color_warp);
    	branch_rules = TreeData3D.Random(branch_rules.min_size, branch_rules.color_warp);
    	if (enable_tip_rules) {
        	trunk_tip_rules = TreeData3D.Random(trunk_rules.min_size, trunk_rules.color_warp);
        	branch_tip_rules = TreeData3D.Random(branch_rules.min_size, branch_rules.color_warp);	
    	}
 		startTree();
    }
    
    
    
    public void setRootColor(Color c) {
    	rootColor = c;
    	startTree();
    }
    
    public void setRootSize(Double s) {
    	controlSize = s;
    	startTree();
    }
  
    public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
	
		Graphics2D g2d = (Graphics2D)g;
		g2d.setXORMode(Color.white);
		g2d.setComposite(new AdditiveComposite(getBackground()));
		g2d.translate(getWidth()/2,getHeight()-50);//origin is on bottom of window
		tree.drawMe(g2d, Math.sin(rotation), Math.cos(rotation));
	 }
    
    private void setTimer() {
		 timer = new Timer();
		 task = new MyTimerTask();
		 timer.scheduleAtFixedRate(task, 0, 50);
    }
	
	public void growGIF(){
		double rotate = Math.PI/60;
		int end_frames = 20;
		String name = "grow_tree";
		File saveFile;
	    int n = 0;
    	do {
    		n++;
    		saveFile = new File(name + "_" + n +".gif");
    	}
    	while (saveFile.exists());
		System.out.println("saving  growth as: " + saveFile.getAbsolutePath());
	  try{

		 FileImageOutputStream outputstream = new FileImageOutputStream(saveFile);

	    GifSequenceWriter writer = new GifSequenceWriter( outputstream, BufferedImage.TYPE_INT_RGB, 1, true);
		startTree();
		task.cancel();
		int i = 0;
	    while (step()) {
	    	rotate(rotate);
	    	BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	  	    Graphics g = image.getGraphics();
	  	    printAll(g);
	  	    writer.writeToSequence(image);
	  	    i++;
	    	System.out.print(i + "-");
	    }
	    System.out.println();
	    for (int a = 0; a < end_frames; a++) {
	    	rotate(Math.PI*2/120);
	    	BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	  	    Graphics g = image.getGraphics();
	  	    printAll(g);
	  	    writer.writeToSequence(image);
	  	    i++;
	    	System.out.print(i + "-");
	    }
	    writer.close();
	    outputstream.close();
	    System.out.println();
		System.out.println("saved to gif: " + saveFile.getAbsolutePath());
	  }
	  catch(Exception e){
		  System.out.println("Oops!");
		  e.printStackTrace();
	  }

}
    
	public void saveToGIF(){
			String name = "tree";
			File saveFile;
		    int n = 0;
	    	do {
	    		n++;
	    		saveFile = new File(name + "_" + n +".gif");
	    	}
	    	while (saveFile.exists());
			System.out.println("saving as: " + saveFile.getAbsolutePath());
		  try{

			 FileImageOutputStream outputstream = new FileImageOutputStream(saveFile);

		    GifSequenceWriter writer = new GifSequenceWriter( outputstream, BufferedImage.TYPE_INT_RGB, 1, true);
    	    for (int i = 0; i < 60; i++) {
    	    	rotate(Math.PI*2/60);
    	    	BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    	  	    Graphics g = image.getGraphics();
    	  	    printAll(g);
    	  	    writer.writeToSequence(image);
    	    	System.out.print(i + "-");
    	    }
		    writer.close();
		    outputstream.close();
		    System.out.println();
			System.out.println("saved to gif: " + saveFile.getAbsolutePath());
		  }
		  catch(Exception e){
			  System.out.println("Oops!");
			  e.printStackTrace();
		  }

	}
    
    
    public void saveImage(String image_path) {
	    File saveFile = new File(image_path + ".png");
	    int i = 0;
    	while (saveFile.exists()) {
    		saveFile = new File(image_path + i +".png");
    		i++;
    	}
    
	    BufferedImage awtImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics g = awtImage.getGraphics();
	    printAll(g);
		try {
		    // retrieve image
		    ImageIO.write(awtImage, "png", saveFile);
			System.out.println("Saving your image to " + (saveFile.getAbsolutePath()) );
		} catch (IOException e) {
			System.out.println("can't save image :(");
		    e.printStackTrace();
		}
	}

    //click to make a new graph
	 public void mouseClicked(MouseEvent e)
	 {
		 goal = getWorldCoordinates(e.getX(), e.getY()); 
		 startTree();
	 }
	 
    //Empty methods to satisfy MouseListener interface
	 public void mouseEntered(MouseEvent e){
		 requestFocus();
	 }
	 
	 public void mouseExited(MouseEvent e){
		 //task.cancel();
	 }
	  
	 public void mousePressed(MouseEvent e){
	 }
	 	
	 public void mouseReleased(MouseEvent e){
	 }
	 
	public void rotate(double amount) {
		rotation = (rotation + amount)%(Math.PI*2);
		repaint();
	}
	
	//get direction in 3space of mouse
	public Point3D getWorldCoordinates(int xxx, int yyy) {
		double x = -(double)xxx+getWidth()/2;
		double y= (double)yyy-getHeight();;
		Point3D point = new Point3D(x, y, 0);
		point = TreeBranch3D.rotateAroundY(point, Math.sin(-rotation), Math.cos(-rotation));
		point.printPoint();
		return point;
	}
	 
	@Override
	 public void mouseDragged(MouseEvent e) {
		goal = getWorldCoordinates(e.getX(), e.getY()); 
		//rotate(.01);
	 }

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_LEFT:
	            rotate(.05);
	            break;
	        case KeyEvent.VK_RIGHT :
	            rotate(-.05);
	            break;
	        case KeyEvent.VK_SPACE :
	        	saveImage("my_tree");
	        	break;
	        case KeyEvent.VK_G :
	        	growGIF();
	        	break;
	     }
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 	
		



}// SimpleCanvas
