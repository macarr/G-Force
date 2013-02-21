package com.tab2pdf.cse2311;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.itextpdf.text.pdf.BaseFont;

public class UIController extends JPanel implements ActionListener{
	//holds the drop-down menus and the 'convert' button
	private JPanel controlPane;
	private JButton openButton;
	private JButton convertButton;
	//JComboBox holding the font-name values
	private JComboBox<String> fontNamesCombo;
	//JComboBox holding the font-size values
	private JComboBox<String> fontSizesCombo;
	//JComboBox holding the spacing values
	private JComboBox<String> spacingCombo;
	private UIModel data;

	//The font-names that shall be used
	private String [] fontNames = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC };

	//The font-sizes that shall be used
	private String [] fontSizes = {"6", "7", "8", "9", "10", "11", "12", "13", "14"};

	//The spacing values that shall be used
	private String [] spacing = {"1", "2", "3", "4", "5", "6", "7", "8"};

	public UIController(Dimension size, UIModel data){
		this.data = data;
		openButton = new JButton("Open Ascii File");
		openButton.addActionListener(this);
		openButton.setPreferredSize(new Dimension(size.width, size.height/6));

		//littleTopPane holds the help-tip at the top and the open button
		JPanel littleTopPane = new JPanel();
		littleTopPane.setLayout(new BorderLayout());
		JLabel helpTip = new JLabel("Press F1 on keyboard for help.");
		helpTip.setForeground(new Color(182, 33, 45));
		littleTopPane.add(helpTip, BorderLayout.NORTH);
		littleTopPane.add(openButton, BorderLayout.SOUTH);

		//fontName is a JComboBox
		fontNamesCombo = new JComboBox<String>(fontNames);
		fontNamesCombo.setEnabled(false);
		fontNamesCombo.setSelectedIndex(0);
		fontNamesCombo.setBorder(BorderFactory.createTitledBorder("Select Font:"));		

		//fontSize is a JComboBox
		fontSizesCombo = new JComboBox<String>(fontSizes);
		fontSizesCombo.setEnabled(false);
		fontSizesCombo.setSelectedIndex(fontSizesCombo.getItemCount()-1);
		fontSizesCombo.setBorder(BorderFactory.createTitledBorder("Select Font Size:"));

		//spacing is a JComboBox
		spacingCombo = new JComboBox<String>(spacing);
		spacingCombo.setEnabled(false);
		spacingCombo.setSelectedIndex(4);
		spacingCombo.setBorder(BorderFactory.createTitledBorder("Select Spacing:"));

		//save button
		convertButton = new JButton("Convert to PDF");
		convertButton.setEnabled(false);
		convertButton.addActionListener(this);

		//convertPane would hold the drop-down menus and the 'Convert' button
		JPanel convertPane = new JPanel();
		convertPane.setLayout(new GridLayout(4, 1));

		//Adding the controls to the convertPane
		convertPane.add(fontNamesCombo);
		convertPane.add(fontSizesCombo);
		convertPane.add(spacingCombo);
		convertPane.add(convertButton);
		convertPane.setBorder(BorderFactory.createTitledBorder(""));

		controlPane = new JPanel(new BorderLayout());

		//Adding the littleTopPane and convertPane to the current JPanel
		controlPane.add(littleTopPane, BorderLayout.NORTH);
		controlPane.add(convertPane, BorderLayout.CENTER);

		//Setting the layout of this JPanel (UIControls), and adding the controlPane
		setLayout(new BorderLayout());
		controlPane.setPreferredSize(new Dimension(size.width, size.height));
		add(controlPane, BorderLayout.NORTH);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == openButton){
			int status  = data.loadFile();

			if(status == 1){
				fontNamesCombo.setEnabled(true);
				fontSizesCombo.setEnabled(true);
				spacingCombo.setEnabled(true);
				convertButton.setEnabled(true);
			}
		}
		else if(e.getSource() == this.convertButton){
			data.convertFile(fontNamesCombo.getSelectedItem().toString(), Float.parseFloat(fontSizesCombo.getSelectedItem().toString()), 
					Float.parseFloat(spacingCombo.getSelectedItem().toString()));
		}
	}
}
