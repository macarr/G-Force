package com.java.tabui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;



public class InfoView extends JFrame{
	
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = -2695173424680673513L;
	private JTextArea infoArea;
	public InfoView(String title){
		setTitle(title);
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(infoArea));
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	public void append(String info){
		infoArea.append(info + "\n");
	}
}
