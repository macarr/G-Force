package com.java.pdfmaker;

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

import javax.swing.JFrame;

public class TabUIMainWindow extends JFrame{
  // serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 4168022201508007551L;
	private boolean windowIsShown = false;

	public TabUIMainWindow(){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final TabUIViewPane outputArea = new TabUIViewPane();
		TabFileManager data = new TabFileManager(outputArea);
		
		setSize(new Dimension(size.width - 300, size.height - 200));
		final TabUIControlPane conPan = new TabUIControlPane(new Dimension(getSize().width/5, getSize().height/3), data);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(conPan, BorderLayout.EAST);
		
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				String osVersion = System.getProperty("os.name");
				if(osVersion.startsWith("Windows"))
					new File(""+System.getenv("TEMP")+"/temp.pdf").delete();
				else
					new File("/tmp/temp.pdf").delete();
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