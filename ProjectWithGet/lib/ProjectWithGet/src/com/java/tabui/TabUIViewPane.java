/**
 * TabUIViewPane is where the output of this program is displayed.
 */
package com.java.tabui;

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

import com.java.tabinput.InputParser;
 
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
	
	/**
	 * TabUIViewPane constructor.
	 */
	public TabUIViewPane(){
		setLayout(new BorderLayout());
		statusUpdateLabel = new JLabel(" ");
		statusUpdateLabel.setForeground(new Color(98, 0, 255));
		centerPane = new JPanel(new BorderLayout());
		centerPane.setBackground(Color.gray);
		
		add(statusUpdateLabel, BorderLayout.NORTH);
		add(centerPane, BorderLayout.CENTER);
	}
	
	/**
	 * Closes the ice-pdf controller.
	 */
	public void closePdfFile(){
		if(controller != null){
			controller.closeDocument();
		}
	}
	
	/**
	 * Shows the status of a task. When necessary, a progress-bar is also displayed. 	 
	 * */
	public void displayStatusUpdate(String status, boolean showProgressBar){
		statusUpdateLabel.setText(status);
		
		//If showProgressBar is true.
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
	
	/**
	 * This method displays the Ascii file when the user opens it.
	 */
	public void showAsciiFile(InputParser in){
		//First making the status-label blank 
		statusUpdateLabel.setText(" ");
		asciiDisplay = new JTextArea(100, 100);
		asciiDisplay.setEditable(false);
		asciiDisplay.setFont(new Font("MonoSpaced", Font.PLAIN, 15));
		asciiDisplay.append("                     " + in.getTitle() + "\n");
		asciiDisplay.append("                     " + in.getSubtitle() + "\n\n");
		asciiDisplay.setCaretPosition(0);
		
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
	
	/**
	 * This method shows the Pdf file using the open-source library 'ice-pdf'.
	 */
	public void showPdfFile(String outputPath) {
		//Setting up the components necessary to display the Pdf.
		controller = new SwingController();
		SwingViewBuilderPane viewPane = new SwingViewBuilderPane(controller);
		JPanel viewerComponentPanel = viewPane.buildViewerPanel();
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		
		//Opening the Pdf for display.
		controller.openDocument(outputPath);
		
		centerPane.removeAll();
		centerPane.add(viewerComponentPanel);
		centerPane.revalidate();
		
		displayStatusUpdate(" ", false);
	}
	
	/**
	 * Clears the centerPane.
	 */
	public void clearCenterPane(){
		centerPane.removeAll();
		centerPane.revalidate();
		centerPane.repaint();
	}
}
