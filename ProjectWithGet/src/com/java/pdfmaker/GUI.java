package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame{
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 4168022201508007551L;
	String filePath;

	public GUI(int flag) throws NullPointerException {
		
		filePath = "assets/splash.jpg";
		
		//terminates the program when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//creates a container for the GUI
		Container c = getContentPane();
		
		
		//Start SplashScreen
		File imageFile = new File(filePath);
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
		if(imageFile.exists()) {
			//loads the image file into an ImageIcon object
			ImageIcon img = new ImageIcon(filePath);
		
			//removes the title bar
	    	setUndecorated(true);
		
			//gets dimensions of screen size
	    	
			double screenWidth = size.getWidth();
			double screenHeight = size.getHeight();
		
			//if image fits onto the screen
			if ((img.getIconWidth() <= (int)screenWidth) && (img.getIconHeight() <= (int)screenHeight))
			{
				//locates the image halfway across the screen and 1/4 - way down the screen,
				//depending on the resolution of the screen and the resolution of the picture.
				setLocation(((int)screenWidth - img.getIconWidth()) / 2 , ((int)(screenHeight) - img.getIconHeight()) / 4);
		
				//loads the image into a JLabel object
				JLabel x = new JLabel(img);	
		
				//adds the JLabel object into the Container c
				c.add(x);
		
				pack();
		
				//sets the picture to be always on top
				setAlwaysOnTop(true);
		
				//displays the actual image
				setVisible(true);
		
				//holds the image for 3 seconds
				try
				{
					Thread.sleep(3000);
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
		
				//disables always on top for the rest of the program
				setAlwaysOnTop(false);
		
				//optional: set it to be invisible, but wont change anything
				//setVisible(false);
		
				//removes the JLabel from the Container c
				c.remove(x);
		
				//clears picture from memory
				this.dispose();
		
				//brings back the title bar for the rest of the program
				setUndecorated(false);
			}
			//End SplashScreen
		}
		
		final UIView outputArea = new UIView();
		FileManager data = new FileManager(outputArea);
		UIControlPanel ui = new UIControlPanel(new Dimension(size.width/5, size.height/3), data);
		
		//Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(ui, BorderLayout.EAST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				new File("Temp/temp.pdf").delete();
			}
		});
	
		setVisible(true);
		
	}
	
	public GUI() {
		
		//terminates the program when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//creates a container for the GUI
		Container c = getContentPane();

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final UIView outputArea = new UIView();
		FileManager data = new FileManager(outputArea);
		UIControlPanel ui = new UIControlPanel(new Dimension(size.width/5, size.height/3), data);
		
		//Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(ui, BorderLayout.EAST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				new File("Temp/temp.pdf").delete();
			}
		});
		
		setVisible(true);
	}
	
	public static void main(String args[]){
		try {
			new GUI(1);
		} catch (NullPointerException e) {
			System.err.println("Splash image not found!");
			new GUI();
		}
	}
}