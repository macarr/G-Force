package com.java.pdfmaker;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jpedal.*;
import org.jpedal.fonts.FontMappings;

import com.itextpdf.text.pdf.BaseFont;



public class GUI extends JFrame {
  private JPanel rightPane;
	private JPanel buttonPane;
	private JPanel controlPane;
	private JButton open;
	private JButton convertPDF;
	private JButton convertASCII;
	private JButton save;
	private JButton close;
	private JComboBox<String> fontNames;
	private JComboBox<String> spacing;
	private JComboBox<String> fontSizes;
	private JInternalFrame pane;
	
	String filePath = "C:/CSE2311/output.pdf";
	String [] fonts = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC }; 
	
	String [] s = {"1", "2", "3", "4", "5", "6", "7", "8"};
	public GUI(){
		buttonPane = new JPanel(new GridLayout(5, 1, 0, 10));
		open = new JButton("Open File");
		convertPDF = new JButton("Convert to PDF");
		convertASCII = new JButton("Convert to ASCII");
		save = new JButton("Save File");
		close = new JButton("Close File");
		
		buttonPane.add(open);
		buttonPane.add(convertPDF);
		buttonPane.add(convertASCII);
		buttonPane.add(save);
		buttonPane.add(close);
		buttonPane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		controlPane = new JPanel(new GridLayout(3, 1));
		
		fontNames = new JComboBox<String>(fonts);
		fontNames.setSelectedIndex(fontNames.getItemCount() - 1);
		//currentName = fontNames.getSelectedItem().toString();
		fontNames.setBorder(BorderFactory.createTitledBorder("Select Font:"));
		fontNames.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Thread t = new Thread() {
					public void run() {
						try{
							new PdfMaker(filePath, fontNames.getSelectedItem().toString(), Float.parseFloat(fontSizes.getSelectedItem().toString()), Float.parseFloat(spacing.getSelectedItem().toString())).createPDF();
						
							PdfRenderer();
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();
            }
		});
		
		fontSizes = new JComboBox<String>(s);
		//for(int i = 1; i < 20; i++) {
		//	fontSizes.addItem(Integer.toString(i));
		//}
		fontSizes.setSelectedIndex(fontSizes.getItemCount()-1);

		fontSizes.setBorder(BorderFactory.createTitledBorder("Font Size:"));
		
		fontSizes.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Thread t = new Thread() {
					public void run() {
						try{
							new PdfMaker(filePath, fontNames.getSelectedItem().toString(), Float.parseFloat(fontSizes.getSelectedItem().toString()), Float.parseFloat(spacing.getSelectedItem().toString())).createPDF();
						
							PdfRenderer();
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();
            }
		});
		
		
		spacing = new JComboBox<String>(s);
		//for(int i = 1; i < 20; i++) {
		//	spacing.add(Integer.toString(i));
		//}
		spacing.setSelectedIndex(spacing.getItemCount() - 1);
		spacing.setBorder(BorderFactory.createTitledBorder("Spacing:"));
		spacing.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Thread t = new Thread() {
					public void run() {
						try{
							new PdfMaker(filePath, fontNames.getSelectedItem().toString(), Float.parseFloat(fontSizes.getSelectedItem().toString()), Float.parseFloat(spacing.getSelectedItem().toString())).createPDF();
						
							PdfRenderer();
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();
            }
		});
		

		
		
		controlPane.add(new JScrollPane(fontNames));
		controlPane.add(new JScrollPane(fontSizes));
		//sliderPane.add(new JLabel());
		controlPane.add(new JScrollPane(spacing));
		
		//sliderPane.add(new JLabel());
		controlPane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		rightPane = new JPanel(new GridLayout(4, 1, 0, 20));
		rightPane.add(buttonPane);
		//rightPane.add(new JScrollPane(fontNames), BorderLayout.CENTER);
		rightPane.add(controlPane);
		rightPane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(rightPane, BorderLayout.EAST);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void PdfRenderer() {
		PdfDecoder pdfView = new PdfDecoder(true); //Set to true as I don't want to render it to screen in this tutorial
		
		try {
			pdfView.openPdfFile(filePath);
		    FontMappings.setFontReplacements();
		    pdfView.decodePage(1);
		    pdfView.setPageParameters(2, 1);
		}
		catch (Exception e) {
		}
		
		 
		
		getContentPane().remove(pdfView);
		getContentPane().validate();
		getContentPane().add(new JScrollPane(pdfView), BorderLayout.CENTER);
		repaint();
		validate();
	}
	
	public static void main (String args[]) {
		GUI gui = new GUI();
		gui.PdfRenderer();
	}
}
