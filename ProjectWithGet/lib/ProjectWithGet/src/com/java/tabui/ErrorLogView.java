package com.java.tabui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class ErrorLogView extends JFrame{
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = -2695173424680673513L;
	private JTextArea summarizedArea;
	private JTextArea extendedArea;
	private JPanel summarizedPane;
	private JPanel extendedPane;
	private JTabbedPane errorLogPane;
	
	
	public ErrorLogView(String title){
		setTitle(title);
		summarizedArea = new JTextArea();
		summarizedArea.setEditable(false);
		
		extendedArea = new JTextArea();
		extendedArea.setEditable(false);
		
		summarizedPane = new JPanel(new BorderLayout());
		extendedPane = new JPanel(new BorderLayout());
		
		summarizedPane.add(new JScrollPane(summarizedArea), BorderLayout.CENTER);
		extendedPane.add(new JScrollPane(extendedArea), BorderLayout.CENTER);
		
		errorLogPane = new JTabbedPane();
		errorLogPane.addTab("Summarized Error Log", summarizedPane);
		errorLogPane.addTab("Extended Error Log", extendedPane);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(errorLogPane);
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
		
	public void append(ArrayList<String> summarized, ArrayList<String> extended){
		for(int i = 0; i < summarized.size(); i++){
			summarizedArea.append(summarized.get(i) + "\n");
		}
		
		for(int i = 0; i < extended.size(); i++){
			extendedArea.append(extended.get(i) + "\n");
		}
	}
}
