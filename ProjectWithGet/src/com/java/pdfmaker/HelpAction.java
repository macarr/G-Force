package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTextPane;

//The class that helps bind the F1 key to the help-screen.
public class HelpAction extends AbstractAction{
	
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 6891593475959299916L;

	public void actionPerformed( ActionEvent tf ){
		new HelpView();
	}
}

class HelpView extends JFrame{
	
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = -2695173424680673513L;

	public HelpView(){
		JTextPane helpPane = new JTextPane();
		helpPane.setText("To convert a file to PDF:\n1) First open an text tablature file by clicking on 'Open .txt File'.\n" +
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