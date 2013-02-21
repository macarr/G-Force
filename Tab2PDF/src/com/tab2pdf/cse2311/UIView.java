package com.tab2pdf.cse2311;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jpedal.PdfDecoder;

public class UIView extends JPanel{
	private JPanel centerPane;
	private JLabel statusUpdate;
	private JTextArea asciiDisplay;
	private JProgressBar progressBar;

	public UIView(){
		setLayout(new BorderLayout());
		centerPane = new JPanel();
		centerPane.setBackground(Color.gray);
		add(centerPane, BorderLayout.CENTER);
	}

	public void showAsciiFile(ArrayList<ArrayList<String>> contents){
		asciiDisplay = new JTextArea(100, 100);
		asciiDisplay.append(contents.get(0).get(0) + "\n");
		asciiDisplay.append(contents.get(0).get(1) + "\n");
		asciiDisplay.append(contents.get(0).get(2) + "\n\n");

		for(int blockNum = 1; blockNum < contents.size(); blockNum++){
			for(int lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
				asciiDisplay.append(contents.get(blockNum).get(lineNum) + "\n");
			}
		}
		centerPane.add(new JScrollPane(asciiDisplay), BorderLayout.CENTER);
		centerPane.revalidate();
		centerPane.repaint();
	}

	public void PdfRenderer(String outputPath) {
		PdfDecoder tabMusic = new PdfDecoder();

		try {
			tabMusic.openPdfFile(outputPath);
			for(int pageNum = 1; pageNum <= tabMusic.getNumberOfPages(); pageNum++){
				tabMusic.decodePage(pageNum);
				tabMusic.setPageParameters(1, pageNum);
			}	

		}
		catch (Exception e) {
		}

		//first we clear up the centerPane
		centerPane.removeAll();
		//then we add the pdf onto the centerPane and display it
		centerPane.add(new JScrollPane(tabMusic, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

		centerPane.revalidate();
		centerPane.repaint();
		//statusLabel.setText(" ");
	}
}