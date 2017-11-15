import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

class TreeWorldControlPanel extends JPanel implements ActionListener, ChangeListener
 {
	
	TreeWorld sCanvas;
	TreeData3D myRules;
	TreeData3D tipRules;
	
	boolean advancedMode = false;//use twice the sliders
	int branch_number = 1;	//trunk or branch
	
	JPanel min_size_slider, angle_slider, angle_warp_slider, size_slider, size_warp_slider, grow_direction_slider;
	JPanel start_size_slider, whorl_slider, whorl_warp_slider, color_warp_slider;
	final JColorChooser colorChooser;
	JPanel sliders;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4, b5, b6;
	private boolean auto_updating = false;	//prevents calls on auto-updated slider values
 
	
 	public TreeWorldControlPanel(TreeWorld cp)
	{
		sCanvas = cp;
 		myRules = sCanvas.branch_rules;
		setLayout(new GridLayout(0,1));

 		
		b1 = new JButton("branch");
		b1.setActionCommand("switch_branch");
		b1.addActionListener((ActionListener) this);
		
		b5 = new JButton("advanced mode");
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
		

		sliders= new JPanel();
		drawSliders();
		

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
 	
 	private  void drawSliders() {
 		sliders.removeAll();
		sliders.setLayout(new GridLayout(0,1));
		start_size_slider = makeSlider(sliders, "size", 10, 500, 100, false);
		min_size_slider = makeSlider(sliders, "intricacy - low values my cause lag", 5, 100, 10, false);
		size_slider = makeSlider(sliders, "node ratio", 10, 95, 80, true);
		size_warp_slider = makeSlider(sliders, "node variation", 0, 50, 15, true);
		angle_slider = makeSlider(sliders, "axil", 0, 180, 30, true);
		angle_warp_slider = makeSlider(sliders, "axil variation", 0, 90, 10, true);
		
		whorl_slider = makeSlider(sliders, "whorl", 0, 360, 0, true);
		whorl_warp_slider = makeSlider(sliders, "whorl variation", 0, 180, 0, true);
		
		grow_direction_slider = makeSlider(sliders, "levity", -100, 100, 0, true);
		color_warp_slider = makeSlider(sliders, "gaiety", 0, 50, 4, true);
 		
 	}
 	
 	private JPanel makeSlider(JPanel panel, String label, int min, int max, int start, boolean two) {
		JSlider mySlider= new JSlider(JSlider.HORIZONTAL,min,max,start);
		mySlider.addChangeListener(this);
		mySlider.setMajorTickSpacing(max-min);
		mySlider.setMinorTickSpacing(10);
		mySlider.setPaintTicks(true);
		mySlider.setPaintLabels(true);
		

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JLabel lab = new JLabel(label+ ": " +start);
		lab.setAlignmentX( CENTER_ALIGNMENT);
		p.add(lab);
		//p.add(mySlider);
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.add(mySlider);
		
		if (advancedMode && two) {
			JSlider slider= new JSlider(JSlider.HORIZONTAL,min,max,start);
			slider.addChangeListener(this);
			slider.setMajorTickSpacing(max-min);
			slider.setMinorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			p1.add(mySlider);
			p1.add(slider);
			slider.addChangeListener(new ChangeListener() {
				      public void stateChanged(ChangeEvent event) {
				    	lab.setText(label + ": " + mySlider.getValue() + " to " + slider.getValue());
				    	if (!auto_updating && !slider.getValueIsAdjusting())
				    		updateRules();
				      }
				    });
			mySlider.addChangeListener(new ChangeListener() {
			      public void stateChanged(ChangeEvent event) {
			    	lab.setText(label + ": " + mySlider.getValue() + " to "+ slider.getValue());
			    	if (!auto_updating && !mySlider.getValueIsAdjusting())
			    		updateRules();
			      }
			    });
		}
		else{
				mySlider.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	lab.setText(label + ": " + mySlider.getValue());
		    	if (!auto_updating && !mySlider.getValueIsAdjusting())
		    		updateRules();
		      }
		    });
		}

		p.add(p1);
		panel.add(p);
		//add(p);
		

		return p1;
 	}
 	
 	private double getAngle(JPanel sliders, int i) {
 		Component comps[] = sliders.getComponents();

 		JSlider slider = (JSlider)comps[i];
 		return Math.toRadians((double)slider.getValue());
 	}
 	
 	private double getDouble(JPanel sliders, int i) {
 		Component comps[] = sliders.getComponents();
 		JSlider slider = (JSlider)comps[i];
 		return (double)slider.getValue()/100;
 	}
 	
 	private int getInt(JPanel sliders, int i) {
 		Component comps[] = sliders.getComponents();
 		JSlider slider = (JSlider)comps[i];
 		return (int)slider.getValue();
 	}
 	
 	private void updateRule(TreeData3D data, int i) {
 		data.angle = getAngle(angle_slider, i);
 		data.angle_warp = getAngle(angle_warp_slider, i);
 		data.size = getDouble(size_slider, i);
 		data.size_warp = getDouble(size_warp_slider, i);
 		data.whorl = getAngle(whorl_slider, i);
 		data.whorl_warp = getAngle(whorl_warp_slider, i);
 		
 		data.grow_lean = getAngle(grow_direction_slider, i);
 		data.color_warp = getInt(color_warp_slider, i);
 	}
 	
 	private void updateRules() {
 		sCanvas.setRootSize(getDouble(start_size_slider, 0));
 		myRules = new TreeData3D();
 		//myRules = TreeSettings.DefaultBranch();
 		myRules.min_size = getInt(min_size_slider, 0);
 		updateRule(myRules, 0);
 		
 		if (advancedMode) {
 			updateRule(tipRules, 1);
 		}
 		updateCanvas();
 	}
 	
 	private void updateInt(JPanel sliders, int i, int value) {
 		Component comps[] = sliders.getComponents();
 		JSlider slider = (JSlider)comps[i];
 		slider.setValue((int)value);
 		
 	}
 	private void updateDouble(JPanel sliders, int i, double value) {
 		Component comps[] = sliders.getComponents();
 		JSlider slider = (JSlider)comps[i];
 		slider.setValue((int)(value*100));
 		
 	}
 	private void updateAngle(JPanel sliders, int i, double value) {
 		Component comps[] = sliders.getComponents();
 		JSlider slider = (JSlider)comps[i];
 		slider.setValue((int)(Math.toDegrees(value)));
 		
 	}
 	
 	private void updateSliderSet(TreeData3D data, int i) {
 		updateAngle(angle_slider, i, data.angle);
 		updateAngle(angle_warp_slider, i, data.angle_warp);
 		updateDouble(size_slider, i, data.size);
 		updateDouble(size_warp_slider, i, data.size_warp);
 		updateAngle(whorl_slider, i, data.whorl);
 		updateAngle(whorl_warp_slider, i, data.whorl_warp);
 		updateAngle(grow_direction_slider, i, data.grow_lean);
 		updateInt(color_warp_slider, i, data.color_warp);
 	}
 	
 	private void updateSliders() {
 		auto_updating = true;
 		((JSlider)(start_size_slider.getComponents()[0])).setValue((int)(sCanvas.controlSize*100));
 		updateInt(min_size_slider, 0, myRules.min_size);
 		updateSliderSet(myRules, 0);
 		if (advancedMode) {
 			updateSliderSet(tipRules, 1);
 		}
 		auto_updating = false;
 	}
 	private void updateCanvas() {
 		if (advancedMode) {
 			sCanvas.setRules(myRules, tipRules, branch_number);
 		}
 		else {
 			sCanvas.setRules(myRules, branch_number);
 		}
 	}
 	
 	private void updateButtons() {
 		System.out.println("branch number trunk number");
		if (branch_number == 0){
			b1.setText("trunk");
			b2.setText("default trunk");
 			myRules = sCanvas.trunk_rules;
 			if (advancedMode) {
 				tipRules = sCanvas.trunk_tip_rules;
 			}
		}
		else {
			b1.setText("branch");
			b2.setText("default branch");
 			myRules = sCanvas.branch_rules;
 			if (advancedMode) {
 	 			tipRules = sCanvas.branch_tip_rules;
 			}
		}
   }
 	


@Override
public void actionPerformed(ActionEvent e) {
 	   if ("switch_branch".equals(e.getActionCommand())) {
 		   branch_number = (branch_number+1)%2;
 		   updateButtons();
 	   }
 	   else if ("switch_end".equals(e.getActionCommand())) {
 		   advancedMode = !advancedMode;
 		   sCanvas.setMode(advancedMode);
 		   if (advancedMode) {
 			   b5.setText("simple mode");
 			   tipRules = myRules;
 		   }
 		   else {
 			   b5.setText("advanced mode");
 		   }
 		   System.out.println("advanced mode is: " + advancedMode);
 		   drawSliders();
 		   revalidate();
 	   }
 	   else if ("reset".equals(e.getActionCommand())){
 		   if (branch_number == 0) {
 			   myRules = TreeData3D.DefaultTrunk();
 			   updateCanvas();
 		   }
 		   else {myRules = TreeData3D.DefaultBranch();
 	 			updateCanvas();
 		   }
 	   }
 	   else if ("random".equals(e.getActionCommand())){
 		   myRules = TreeData3D.Random(myRules.min_size, myRules.color_warp);
 		   if (advancedMode) {
 			   tipRules = TreeData3D.Random(myRules.min_size, tipRules.color_warp);
 		   }
 		   updateCanvas();
 	   }
 	   else if ("random all".equals(e.getActionCommand())){
 		   sCanvas.randomizeRules();
 		   updateButtons();
 	   }
 	   else if ("photo".equals(e.getActionCommand())) {
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