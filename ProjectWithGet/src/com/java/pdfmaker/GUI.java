package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import javax.swing.JFrame;

public class GUI extends JFrame{
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 4168022201508007551L;

	public GUI(){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final UIView outputArea = new UIView();
		FileManager data = new FileManager(outputArea);
		UIControlPanel ui = new UIControlPanel(new Dimension(size.width/5, size.height/3), data);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(ui, BorderLayout.EAST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				new File("C:/CSE2311/temp.pdf").delete();
			}
		});
	
		setVisible(true);
	}
	
	public static void main(String args[]){
		new GUI();
		
	}
}