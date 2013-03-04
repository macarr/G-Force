package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;

//'UIView' is where all the screen output is displayed. 
public class UIView extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3272926203643823070L;

	//JPanel on which both Ascii and PDf documents are displayed.
	private JPanel centerPane;
	
	//Shows the status of certain tasks.
	private JLabel statusUpdateLabel;
	
	//Shows the Ascii file 
	private JTextArea asciiDisplay;
	
	//The progressbar
	private JProgressBar progressBar;
	private JScrollPane scroller;
	
	//Class 'SwingController' is from the open-source library 'IcePdf' to enable displaying Pdf documents within java applications. 
	SwingController controller;
	
	//UIView constructor.
	public UIView(){
		setLayout(new BorderLayout());
		statusUpdateLabel = new JLabel(" ");
		statusUpdateLabel.setForeground(new Color(98, 0, 255));
		centerPane = new JPanel(new BorderLayout());
		centerPane.setBackground(Color.gray);
		add(statusUpdateLabel, BorderLayout.NORTH);
		add(centerPane, BorderLayout.CENTER);
	}
	
	public void closePdfFile(){
		if(controller != null){
			controller.closeDocument();
		}
		
	}
	
	//Shows the status of a task. When necessary, a progress-bar is also displayed. 
	public void displayStatusUpdate(String status, boolean showProgressBar){
		statusUpdateLabel.setText(status);
		if(showProgressBar){
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			centerPane.removeAll();
			centerPane.setLayout(new BorderLayout());
			centerPane.add(progressBar, BorderLayout.NORTH);
			centerPane.revalidate();
			centerPane.repaint();
		}
	}
	
	//This method displays the Ascii file when the user opens it.
	public void showAsciiFile(ArrayList<ArrayList<String>> contents){
		//First making the status-label blank 
		statusUpdateLabel.setText(" ");
		asciiDisplay = new JTextArea(100, 100);
		asciiDisplay.setFont(new Font("MonoSpaced", Font.PLAIN, 15));
		asciiDisplay.append(contents.get(0).get(0) + "\n");
		asciiDisplay.append(contents.get(0).get(1) + "\n");
		asciiDisplay.append(contents.get(0).get(2) + "\n\n");
		
		for(int blockNum = 1; blockNum < contents.size(); blockNum++){
			for(int lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
				asciiDisplay.append(contents.get(blockNum).get(lineNum) + "\n");
			}
			asciiDisplay.append("\n");
		}
		centerPane.removeAll();
		centerPane.setLayout(new BorderLayout());
		centerPane.add(new JScrollPane(asciiDisplay), BorderLayout.CENTER);
		centerPane.revalidate();
		centerPane.repaint();
	}
	
	//This method shows the Pdf file using the open-source library 'JPedal', developed by IDR Solutions.
	public void showPdfFile(String outputPath) {
		
		controller = new SwingController();

		SwingViewBuilderPane factory = new SwingViewBuilderPane(controller);
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		controller.openDocument(outputPath);
		
		centerPane.removeAll();
		centerPane.add(viewerComponentPanel);
		centerPane.revalidate();
		
		
		
		displayStatusUpdate(" ", false);
		
	}
}