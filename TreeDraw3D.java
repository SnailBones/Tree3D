
/**
 * BasicDraw.java
 *
 *
 * Template for beginning graphics programs.
 *
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

public class TreeDraw3D
{

    public static void main(String[] args){
		TreeWorld myCanvas = new TreeWorld();
		TreeWorldControlPanel control = new TreeWorldControlPanel(myCanvas);
		JFrame myFrame = new JFrame();

		Container cp = myFrame.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(myCanvas, BorderLayout.CENTER);
		cp.add(control,BorderLayout.EAST);
		
		myFrame.setTitle("Tree 3D");
		myFrame.setSize(500,500);
		

		//Sets the window to close when upper right corner clicked.  
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Must use getContentPane() with JFrame.
		myFrame.add(myCanvas);
		myFrame.pack(); //resizes to preferred size for components.
		myFrame.setResizable(true);
		myFrame.setVisible(true);
    }
} // BasicDraw


