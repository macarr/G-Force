package com.java.tabui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Date;

import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class TabUIMainWindow extends JFrame{
  // serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 4168022201508007551L;
	private boolean windowIsShown = false;

	public TabUIMainWindow(){
		setTitle("Tab2Pdf");
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final TabUIViewPane outputArea = new TabUIViewPane();
		TabFileManager data = new TabFileManager(outputArea);
		
		setSize(new Dimension(size.width - 300, size.height - 200));
		final TabUIControlPane conPan = new TabUIControlPane(new Dimension(getSize().width/5, getSize().height/3), data);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		//start Splashscreen
		try
		{
			//loads the image file into an ImageIcon object
			ImageIcon img = new ImageIcon(this.getClass().getResource("/resource/splash.jpg"));
	
			//removes the title bar
			setUndecorated(true);
    	
			//gets dimensions of screen size(continued)
			double screenWidth = size.getWidth();
			double screenHeight = size.getHeight();
	
			//if image fits onto the screen
			if ((img.getIconWidth() <= (int)screenWidth) && (img.getIconHeight() <= (int)screenHeight))
			{
				//locates the image halfway across the screen and 1/4 - way down the screen,
				//depending on the resolution of the screen and the resolution of the picture.
			
				setLocation(((int)screenWidth - img.getIconWidth()) / 2 , ((int)(screenHeight) - img.getIconHeight()) / 2);
	
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
	
				//optional: set it to be invisible, but won't change anything
				//setVisible(false);
	
				//removes the JLabel from the Container c
				c.remove(x);

				//clears picture from memory
				this.dispose();
	
				//brings back the title bar for the rest of the program
				setUndecorated(false);
			
				//sets the location back to zero for the rest of the program
				setLocation(0,0);
			}
		}
		catch (NullPointerException e)
		{
			System.out.println("No picture File!");
			//System.exit(1);
		}
		//end Splashscreen
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(conPan, BorderLayout.EAST);
		
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				String tempPath = TabFileManager.getTempDir();
				new File(""+tempPath+"/temp.pdf").delete();
				System.exit(0);
			}
		});
		
		addComponentListener(new ComponentAdapter(){  
			public void componentResized(ComponentEvent e) {
					conPan.resizeComponent(new Dimension(getSize().width/5, getSize().height/3));
					conPan.revalidate();
					conPan.repaint();
				
		    }
			
		});
		
		setMinimumSize(new Dimension(193, size.height - 400));
		setVisible(true);
	}
}
