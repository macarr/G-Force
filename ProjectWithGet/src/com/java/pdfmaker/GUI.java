package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.jpedal.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;

public class GUI extends JFrame{
	public GUI(){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final UIView outputArea = new UIView();
		UIMiddleLayer data = new UIMiddleLayer(outputArea);
		UIController ui = new UIController(new Dimension(size.width/5, size.height/3), data);
		
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
		GUI gui = new GUI();
		
	}
}