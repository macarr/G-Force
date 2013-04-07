/**
 * This class shows a window that displays the errors that were generated during the program's execution.
 */
package com.java.tabui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class ErrorLogView extends JFrame{
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = -2695173424680673513L;
	private JTextArea summarizedArea;	//JTextArea where the summary log is shown.
	private JTextArea extendedArea;		//JTextArea where the extended log is shown.
	private JPanel summarizedPane;		
	private JPanel extendedPane;
	private JTabbedPane errorLogPane;
	private JButton saveSummarizedButton;	//To save the summary log
	private JButton saveExtendedButton;		//To save the extended log.
	private ArrayList<String> summarized; 
	private ArrayList<String> extended;
	
	/**
	 * ErrorLogView constructor.
	 */
	public ErrorLogView(String title, final ArrayList<String> summarized, final ArrayList<String> extended){
		setTitle(title);
		
		this.summarized = summarized;
		this.extended = extended;
		
		summarizedArea = new JTextArea();
		summarizedArea.setEditable(false);
		
		extendedArea = new JTextArea();
		extendedArea.setEditable(false);
		
		summarizedPane = new JPanel(new BorderLayout());
		extendedPane = new JPanel(new BorderLayout());
		
		//A JPanel to hold the saveSummarizedButton.
		JPanel summarizedButtonPane = new JPanel(new GridLayout(1, 3));
		
		//Instantiating the saveSummerizedButton and registering an ActionListener.
		saveSummarizedButton = new JButton("Save Summarized Error Log");
		saveSummarizedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser();
				File file = new File("summarized_log.txt");
				
				//Setting the default file.
				fc.setSelectedFile(file);
				int returnVal = fc.showSaveDialog(ErrorLogView.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					String destinationPath = fc.getSelectedFile().toString();
					try{
						BufferedWriter out = new BufferedWriter(new FileWriter(destinationPath));
						
						//Looping through the ArrayList and writing the contents to the file.
						for(int i = 0; i < summarized.size(); i++){
							out.write(summarized.get(i));
							out.newLine();
						}
						out.close();
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
				//If file exists at this point, simply delete it. Please note that this operation does not affect the file that the
				//user may have saved.
				if(file.exists()){
					file.delete();
				}
			}
		});
		
		//A quick way of doing button placement. 
		summarizedButtonPane.add(new JLabel());
		summarizedButtonPane.add(saveSummarizedButton);
		summarizedButtonPane.add(new JLabel());
		
		//A JPanel to hold the saveExtendedButton.
		JPanel extendedButtonPane = new JPanel(new GridLayout(1, 3));
		
		//Instantiating the saveExtendedButton and registering an ActionListener.
		saveExtendedButton = new JButton("Save Extended Error Log");
		saveExtendedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser();
				File file = new File("extended_log.txt");
				
				//Setting the default file.
				fc.setSelectedFile(file);
				int returnVal = fc.showSaveDialog(ErrorLogView.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					String destinationPath = fc.getSelectedFile().toString();
					try{
						BufferedWriter out = new BufferedWriter(new FileWriter(destinationPath));
						
						//Looping through the ArrayList and writing the contents to the file.
						for(int i = 0; i < extended.size(); i++){
							out.write(extended.get(i));
							out.newLine();
						}
						out.close();
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
				//If file exists at this point, simply delete it. Please note that this operation does not affect the file that the
				//user may have saved.
				if(file.exists()){
					file.delete();
				}
			}
		});
		
		//A quick way of doing button placement.
		extendedButtonPane.add(new JLabel());
		extendedButtonPane.add(saveExtendedButton);
		extendedButtonPane.add(new JLabel());
		
		summarizedPane.add(new JScrollPane(summarizedArea), BorderLayout.CENTER);
		summarizedPane.add(summarizedButtonPane, BorderLayout.SOUTH);
		
		extendedPane.add(new JScrollPane(extendedArea), BorderLayout.CENTER);
		extendedPane.add(extendedButtonPane, BorderLayout.SOUTH);
		
		errorLogPane = new JTabbedPane();
		
		//Adding the JPanels as tabs.
		errorLogPane.addTab("Summarized Error Log", summarizedPane);
		errorLogPane.addTab("Extended Error Log", extendedPane);
		
		append();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(errorLogPane);
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Populates the TextAreas with information.
	 */
	private void append(){
		for(int i = 0; i < summarized.size(); i++){
			summarizedArea.append(summarized.get(i) + "\n");
		}
		
		for(int i = 0; i < extended.size(); i++){
			extendedArea.append(extended.get(i) + "\n");
		}
	}
}
