package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;

//'UIView' is where all the screen output is displayed. 
public class TabUIViewPane extends JPanel{
  
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
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
	public TabUIViewPane(){
		setLayout(new BorderLayout());
		statusUpdateLabel = new JLabel(" ");
		statusUpdateLabel.setForeground(new Color(98, 0, 255));
		centerPane = new JPanel(new BorderLayout());
		centerPane.setBackground(Color.gray);
		
		//displays image on the grey screen before the ascii file is opened
		try {
			ImageIcon image = new ImageIcon("assets/splash.jpg");
			JLabel label = new JLabel("", image, JLabel.CENTER);
			centerPane.add( label, BorderLayout.CENTER );
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
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
	public void showAsciiFile(InputParser in){
		//First making the status-label blank 
		statusUpdateLabel.setText(" ");
		asciiDisplay = new JTextArea(100, 100);
		asciiDisplay.setFont(new Font("MonoSpaced", Font.PLAIN, 15));
		asciiDisplay.append(in.getTitle() + "\n");
		asciiDisplay.append(in.getSubtitle() + "\n\n");
		
		ArrayList<ArrayList<String>> contents = in.getData();
		
		for(int blockNum = 0; blockNum < contents.size(); blockNum++){
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

		SwingViewBuilderPane viewPane = new SwingViewBuilderPane(controller);
		JPanel viewerComponentPanel = viewPane.buildViewerPanel();
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		controller.openDocument(outputPath);
		
		centerPane.removeAll();
		centerPane.add(viewerComponentPanel);
		centerPane.revalidate();
		
		
		
		displayStatusUpdate(" ", false);
	}
}
