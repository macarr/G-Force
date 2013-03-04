package com.java.pdfmaker;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextPane;

//To show the help-screen.
public class HelpView extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2695173424680673513L;

	public HelpView(){
		JTextPane helpPane = new JTextPane();
		helpPane.setText("To convert a file to PDF:\n1) First open an text tablature file by clicking on 'Open Source File'.\n" +
						 "2) Select your desired font, font-size, and spacing.\n3) Click on 'Convert to PDF'." +
					     "\n\nTo make a change to the font settings or spacing:\n1) Choose the desired settings from the drop down menus.\n" +
					     "2) Click on 'Convert to PDF'.");
		helpPane.setEditable(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(helpPane);
		this.setSize(400, 500);
		this.setVisible(true);
	}
}