import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

class TreeWorldControlPanel extends JPanel implements ActionListener, ChangeListener
 {
	
	//trunk or branch, root or twig
	int branch_number = 1;
	int end_number = 0;
	
	TreeWorld sCanvas;
	TreeData3D myRules;
	
	JSlider min_size_slider, angle_slider, angle_warp_slider, size_slider, size_warp_slider, grow_direction_slider;
	JSlider start_size_slider, whorl_slider, whorl_warp_slider, color_warp_slider;
	final JColorChooser colorChooser;
	static double eye_size = 1;
	static double nose_size = 1;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4, b5, b6;
	private boolean auto_updating = false;	//prevents calls on auto-updated slider values
 
	
 	public TreeWorldControlPanel(TreeWorld cp)
	{
		sCanvas = cp;
 		myRules = sCanvas.branch_rules;
 		myRules.color_warp = 0;
		setLayout(new GridLayout(0,1));


		//setLayout(new BorderLayout());
//		String name;
// 		if (branch_number == 0) {
// 			name = "trunk";
// 		}
// 		else {name = "branch";}
 		
		b1 = new JButton("branch");
		b1.setActionCommand("switch_branch");
		b1.addActionListener((ActionListener) this);
		
		b5 = new JButton("root");
		b5.setActionCommand("switch_end");
		b5.addActionListener((ActionListener) this);
		
		
		b2 = makeButton("reset");
		b3 = makeButton("random");
		b6 = makeButton("random all");
		b4 = makeButton("photo");

		
		JPanel butts = new JPanel();
		butts.setLayout(new GridLayout(2,3));
		butts.add(b1);
		butts.add(b5);
		butts.add(b2);
		butts.add(b3);
		butts.add(b6);
		butts.add(b4);
		

		JPanel sliders= new JPanel();
		sliders.setLayout(new GridLayout(0,1));
		start_size_slider = makeSlider(sliders, "size", 10, 500, 100);
		min_size_slider = makeSlider(sliders, "intricacy - low values my cause lag", 5, 100, 10);
		size_slider = makeSlider(sliders, "internode ratio", 10, 95, 80);
		size_warp_slider = makeSlider(sliders, "internode variation", 0, 50, 15);
		angle_slider = makeSlider(sliders, "axil", 0, 180, 30);
		angle_warp_slider = makeSlider(sliders, "axil variation", 0, 90, 10);
		
		whorl_slider = makeSlider(sliders, "whorl", 0, 360, 0);
		whorl_warp_slider = makeSlider(sliders, "whorl variation", 0, 180, 0);
		
		grow_direction_slider = makeSlider(sliders, "levity", -100, 100, 0);
		color_warp_slider = makeSlider(sliders, "gaeity", 0, 50, 4);
		

		JPanel cols = new JPanel();
		cols.setLayout(new BorderLayout());
		
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setPreviewPanel(new JPanel());
		
		cols.add(colorChooser, BorderLayout.CENTER);
		
		JPanel doodads = new JPanel();
		doodads.setLayout(new GridLayout(0,1));
		doodads.add(butts);
		doodads.add(cols);
		add(doodads);
		add(sliders);	
		
		updateSliders();
		updateCanvas();
   }
 	
 	private JButton makeButton(String name) {
		JButton button = new JButton();
		button.setActionCommand(name);
		button.setText(name);
		button.addActionListener((ActionListener) this);
		return button;
 		
 	}
 	
 	private JSlider makeSlider(JPanel panel, String label, int min, int max, int start) {
		JSlider mySlider= new JSlider(JSlider.HORIZONTAL,min,max,start);
		mySlider.addChangeListener(this);
		mySlider.setMajorTickSpacing(max-min);
		mySlider.setMinorTickSpacing(10);
		mySlider.setPaintTicks(true);
		mySlider.setPaintLabels(true);


		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		//p.setLayout(new GridLayout());
		JLabel lab = new JLabel(label+ ": " +start);
		lab.setAlignmentX( CENTER_ALIGNMENT);
		//JLabel num = new JLabel(label + ": " +start);
		lab.setAlignmentX( CENTER_ALIGNMENT);
		//lab.setAlignmentY(TOP_ALIGNMENT);
		p.add(lab);
		p.add(mySlider);
		panel.add(p);
		
		
	   mySlider.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	lab.setText(label + ": " + mySlider.getValue());
		    	if (!auto_updating && !mySlider.getValueIsAdjusting())
		    		updateRules();
		      }
		    });
		return mySlider;
 	}
 	
 	private void updateRules() {
 		sCanvas.setRootSize(start_size_slider.getValue()/100.0);
 		int min_size = min_size_slider.getValue();
 		double angle = Math.toRadians((double)angle_slider.getValue());
 		double angle_warp = Math.toRadians((double)angle_warp_slider.getValue());
 		double whorl = Math.toRadians((double)whorl_slider.getValue());
 		double whorl_warp = Math.toRadians((double)whorl_warp_slider.getValue());
 		double size = (double)size_slider.getValue()/100;
 		double size_warp = (double)size_warp_slider.getValue()/100;
 		myRules = new TreeData3D();
 		//myRules = TreeSettings.DefaultBranch();
 		myRules.min_size = min_size;
 		myRules.angle = angle;
 		myRules.angle_warp = angle_warp;
 		myRules.size = size;
 		myRules.size_warp = size_warp;
 		myRules.whorl = whorl;
 		myRules.whorl_warp = whorl_warp;
 		
 		myRules.grow_lean = (double)grow_direction_slider.getValue()/100;
 		myRules.color_warp = color_warp_slider.getValue();
 		updateCanvas();
 	}
 	
 	private void updateSliders() {
 		int size = (int)(myRules.size*100);
		int size_warp = (int)(myRules.size_warp*100);
 		int angle = (int)Math.toDegrees(myRules.angle);
 		int angle_warp = (int)Math.toDegrees(myRules.angle_warp);
 		int whorl = (int)Math.toDegrees(myRules.whorl);
 		int whorl_warp = (int)Math.toDegrees(myRules.whorl_warp);
 		int grow_direction = (int)(myRules.grow_lean*100);
 		auto_updating = true;
 		min_size_slider.setValue(myRules.min_size);
 		angle_slider.setValue(angle);
 		angle_warp_slider.setValue(angle_warp);
 		size_slider.setValue(size);
 		size_warp_slider.setValue(size_warp);
 		grow_direction_slider.setValue(grow_direction);
 		
 		whorl_slider.setValue(whorl);
 		whorl_warp_slider.setValue(whorl_warp);
 		
 		color_warp_slider.setValue(myRules.color_warp);
 		auto_updating = false;
 	}
 	private void updateCanvas() {
 		sCanvas.setRules(myRules, branch_number, end_number);
 	}
 	
 	private void updateButtons() {
 		System.out.println("branch number: " + branch_number + " end number: " + end_number);
 		if (end_number == 0) {
 			b5.setText("root");
 			if (branch_number == 0){
 				b1.setText("trunk");
 				b2.setText("default trunk");
 	 			myRules = sCanvas.trunk_rules;
 			}
 			else {
 				b1.setText("branch");
 				b2.setText("default branch");
 	 			myRules = sCanvas.branch_rules;
 			}
 		}
 		else {
 			b5.setText("leaves");
 			if (branch_number == 0){
 				b1.setText("trunk");
 				b2.setText("reset trunk tip");
 	 			myRules = sCanvas.trunk_tip_rules;
 			}
 			else {
 				b1.setText("branch");
 				b2.setText("reset branch tip");
 	 			myRules = sCanvas.branch_tip_rules;
 			}
 		}
   }
 	


@Override
public void actionPerformed(ActionEvent e) {
 	   if ("switch_branch".equals(e.getActionCommand())) {
 		   branch_number = (branch_number+1)%2;
 		   updateButtons();
// 	 		if (branch_number == 0) {
// 	 			b1.setText("trunk");
// 	 			b2.setText("default trunk");
// 	 			//b5.setText("root");
// 	 			myRules = sCanvas.trunk_rules;
// 	 		}
// 	 		else {
// 	 			b1.setText("branch");
// 	 			b2.setText("default branch");
// 	 			myRules = sCanvas.branch_rules;}
 	   }
 	   else if ("switch_end".equals(e.getActionCommand())) {
 		   end_number = (end_number+1)%2;
 		   updateButtons();
// 	 		if (end_number == 0) {
// 	 			b5.setText("root");
// 	 			if (branch_number == 0){
// 	 				b2.setText("default trunk");
// 	 	 			myRules = sCanvas.trunk_rules;
// 	 			}
// 	 			else {
// 	 				b2.setText("default branch");
// 	 	 			myRules = sCanvas.branch_rules;
// 	 			}
// 	 		}
// 	 		else {
// 	 			b5.setText("leaves");
// 	 			if (branch_number == 0){
// 	 				b2.setText("reset trunk tip");
// 	 	 			myRules = sCanvas.trunk_tip_rules;
// 	 			}
// 	 			else {
// 	 				b2.setText("reset branch tip");
// 	 	 			myRules = sCanvas.branch_tip_rules;
// 	 			}
// 	 			b5.setText("leaves");}
 	   }
 	   else if ("reset".equals(e.getActionCommand())){
 		   if (end_number > 0) {
	 		   if (branch_number == 0) {
	 			   myRules = sCanvas.trunk_rules;
	 			   updateCanvas();
	 		   }
	 		   else {myRules = sCanvas.branch_rules;
	 	 			updateCanvas();
	 		   }
 		   }
 		   else {
	 		   if (branch_number == 0) {
	 			   myRules = TreeData3D.DefaultTrunk();
	 			   updateCanvas();
	 		   }
	 		   else {myRules = TreeData3D.DefaultBranch();
	 	 			updateCanvas();
	 		   }
 		   }
 	   }
 	   else if ("random".equals(e.getActionCommand())){
 		   myRules = TreeData3D.Random(myRules.min_size, myRules.color_warp);
 		   updateCanvas();
 	   }
 	   else if ("random all".equals(e.getActionCommand())){
 		   	sCanvas.randomizeRules();
 	   }
 	   else if ("photo".equals(e.getActionCommand())) {
 		   //System.out.println("you clicked on photo!");
 		   sCanvas.saveToGIF();
 	   }
 		updateSliders();
	}


@Override
public void stateChanged(ChangeEvent ev) {
	   if (ev.getSource().equals(colorChooser.getSelectionModel())) {
		   sCanvas.setRootColor(colorChooser.getColor());
	   }
	
}
}