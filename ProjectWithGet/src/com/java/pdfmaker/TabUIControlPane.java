package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.pdf.BaseFont;

//Class 'UIController' sets up the components on the right-hand side of the User-Interface
public class TabUIControlPane extends JPanel{
  
	// serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 228385549718664150L;

	//To open an ascii file.
	private JButton openButton = null;
	
	//To convert to Pdf.
	private JButton convertButton = null;
	
	//To save the Pdf file.
	private JButton saveButton = null;
	
	//To launch the Pdf file in a real Pdf reader like Acrobat Reader.
	private JButton launchPdfButton = null;
	
	//JComboBox holding the font-name values.
	private JComboBox<String> fontNamesCombo = null;
	
	//JComboBox holding the font-size values.
	private JTextField fontSizeField = null;
	
	//JComboBox holding the spacing values.
	private JTextField spacingField = null;
	
	private JPanel fontNamesComboPane;
	private JPanel fontSizeFieldPane;
	private JPanel spacingFieldPane;
	
	private TitledBorder fontNamesComboBorder;
	private TitledBorder fontSizeFieldBorder;
	private TitledBorder spacingFieldBorder;
	
	//Reference to the 'UIMiddleLayer'.
	private final TabFileManager data;
	
	//'chosenFontName' is an object that holds the selected value from the 'fontNamesCombo' and makes this value accessible from
	//multiple methods.  
	private String chosenFontName;
	
	//'chosenFontSize' is an object that holds the selected value from the 'fontSizesCombo' and makes this value accessible from
	//multiple methods.
	private float chosenFontSize;
	
	//'chosenSpacing' is an object that holds the selected value from the 'fontSpacingCombo' and makes this value accessible from
	//multiple methods.
	private float chosenSpacing;
	
	private int buttonWidthVaccum = 90;
	private int buttonHeightFactor = 8;
	
	//The font-names that shall be used
	private String [] fontNames = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC };
	
	
	//The 'UIController' constructor. It takes two parameters- one defining the preferred size for this 'UIConstructor', and the second one
	//carrying the reference to the 'UIMiddleLayer'.  
	public TabUIControlPane(Dimension size, final TabFileManager data){
		
		//this.data holds the reference to the 'MiddleLayer' object 
		this.data = data;
	
		//Setting the amount of space on the UI that this 'UIController' would occupy.
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		//Setting the layout of this JPanel (UIControls), and adding the controlPane
		setLayout(new BorderLayout());
		
		initializeComponents();
		disableRelevantControls();
	}
	
	private void disableRelevantControls(){
		//'fontnamesCombo' is initially disabled, because the user has not yet opened any Ascii file.
		fontNamesCombo.setEnabled(false);
		fontNamesComboPane.setEnabled(false);
		
		//'fontSizesCombo' is initially disabled, because the user has not yet opened any Ascii file.
		fontSizeField.setEnabled(false);
		fontSizeFieldPane.setEnabled(false);
		
		//'spacingCombo' is initially disabled, because the user has not yet opened any Ascii file.
		spacingField.setEnabled(false);
		spacingFieldPane.setEnabled(false);
		
		//'convertButton' is initially disabled because no Ascii file has yet been selected.
		convertButton.setEnabled(false);
		
		//'saveButton' is initially not active because the file has not yet been converted to Pdf. 
		saveButton.setEnabled(false);
		
		launchPdfButton.setEnabled(false);
	}
	
	public void initializeComponents(){
		JPanel openPane = initilizeOpenPane();
		JPanel convertSavePane = initializeConvertSavePane();
		JPanel controlPane = initializeControlPane(openPane, convertSavePane);		
		JPanel launchPdfPane = initializeLaunchPdfPane();
		JPanel finalControlPane = initializeFinalControlPane("Press F1 on Keyboard for Help.", controlPane, launchPdfPane); 		
				
		
		add(finalControlPane, BorderLayout.NORTH);
	}
	
	private JPanel initilizeOpenPane(){
		//Instantiating the openButton.  
		openButton = new JButton("Open Input File");
			
		//Setting the ActionListener for 'openButton'. An anonymous inner class is used because this code is used no where else in the program.
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					
				//The method 'data.loadFile()' is invoked to display the 'JFileChooser', so that the user can choose her/his desired file.
				int status  = data.loadFile();
				
				//If status is '1', it means that the user chose a file, and therefore, the following GUI components need to be made active.
				//A '0' means that the user decided to close the 'JFileChooser' without selecting any file. 
				if(status == 1){
					fontNamesCombo.setEnabled(true);
					fontNamesComboBorder.setTitleColor(new Color(0, 85, 255));
					fontSizeField.setEnabled(true);
					fontSizeFieldBorder.setTitleColor(new Color(0, 85, 255));
					spacingField.setEnabled(true);
					spacingFieldBorder.setTitleColor(new Color(0, 85, 255));
					convertButton.setEnabled(true);
				}
			}
		});
			
		//Setting  the horizontal alignment of the 'openButton'.
		openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
		//When 'openButton' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		openButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		openButton.getActionMap().put("showHelp", new HelpAction());
			
		//Setting the maximum and minimum size of the 'openButton'.
		openButton.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		openButton.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		openButton.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
			
		//'openPane' holds the 'openButton'.
		JPanel openPane = new JPanel();
			
		//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
		openPane.setLayout(new BoxLayout(openPane, BoxLayout.Y_AXIS));
					
		//Adding some extra space before the 'openButton'.
		openPane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
			
		//Adding the 'openButton' to the 'openPane'.
		openPane.add(openButton);
		openPane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));

		return openPane;
	}
	
	private JPanel initializeConvertSavePane(){
		
		//'fontNamesCombo' is a JComboBox that holds the font-names that the user can select from.
		if(fontNamesCombo == null)
			fontNamesCombo = new JComboBox<String>(fontNames);
		
		//setting the default item of 'fontNamesCombo'
		fontNamesCombo.setSelectedIndex(0);
		
		//When 'fontNamesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		fontNamesCombo.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		fontNamesCombo.getActionMap().put("showHelp", new HelpAction());
		
		fontNamesCombo.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		fontNamesCombo.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		fontNamesCombo.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		
		fontNamesComboPane = new JPanel(new BorderLayout());
		fontNamesComboPane.add(fontNamesCombo);
		
		//Setting a titled border for the 'fontNameCombo'.
		fontNamesComboBorder = new TitledBorder("Select Font from Below:");
		fontNamesComboBorder.setTitleColor(new Color(190, 190, 190));
		fontNamesComboPane.setBorder(fontNamesComboBorder);
		
		//'fontSizesCombo' is a JComboBox that holds the font-sizes that the user can select from.
		if(fontSizeField == null)
			fontSizeField = new JTextField();
		
		
		fontSizeField.setEditable(true);
		fontSizeField.setBackground(new Color(238, 238, 238));
	
		//When 'fontSizesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		fontSizeField.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		fontSizeField.getActionMap().put("showHelp", new HelpAction());
		
		fontSizeField.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		fontSizeField.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		fontSizeField.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		
		fontSizeFieldPane = new JPanel(new BorderLayout());
		fontSizeFieldPane.add(fontSizeField);
		
		//Setting a titled border for the 'fontSizeFieldPane'.
		fontSizeFieldBorder = new TitledBorder("Enter Font Size Below:");
		fontSizeFieldBorder.setTitleColor(new Color(190, 190, 190));
		fontSizeFieldPane.setBorder(fontSizeFieldBorder);
		
		//'spacingCombo' is a JComboBox that holds the spacing values that the user can select from. The user can input numbers into
		//'spacingCombo'.
		if(spacingField == null)
			spacingField = new JTextField();
		
		spacingField.setEditable(true);
		spacingField.setBackground(new Color(238, 238, 238));
	
		//When 'spacingSizesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		spacingField.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		spacingField.getActionMap().put("showHelp", new HelpAction());

		spacingField.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		spacingField.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		spacingField.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		
		spacingFieldPane = new JPanel(new BorderLayout());
		spacingFieldPane.add(spacingField);
		
		//Setting a titled border for the 'spacingCombo'.
		spacingFieldBorder = new TitledBorder("Enter Spacing Below:");
		spacingFieldBorder.setTitleColor(new Color(190, 190, 190));
		spacingFieldPane.setBorder(spacingFieldBorder);

		
		//'convertButton' sets in motion the steps needed to convert the selected Ascii file to a proper Pdf file.
		convertButton = new JButton("Convert to Pdf");
		
	
		
		//An anonymous inner class is used as the event-listener for the 'convertButton'. The anonymous class is used because this code
		//is not being used anywhere else in the program.
		convertButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//extracting the chosen values from the three 'JComboBoxes' and assigning them to the corresponding value-holder. These
				//value-holders make the job of extracting the 'JComboBox' values easier later on in the program.
				chosenFontName = fontNamesCombo.getSelectedItem().toString();
				
				if(!fontSizeField.getText().equals("") && !spacingField.getText().equals("")){
					try{
						chosenFontSize = Float.parseFloat(fontSizeField.getText());
						chosenSpacing = Float.parseFloat(spacingField.getText());
				
						//Invoking the 'data.convertButton', which creates the 'PdfMaker' object, and invokes its 'createPdf' method. The saveButton
						//and launchButton are passed as references so that they be made active after the current file has been converted to Pdf.
						data.convertFile(chosenFontName, chosenFontSize, chosenSpacing, saveButton, launchPdfButton);
					}
					catch(NumberFormatException ex){
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Font-Size and spacing values must be numbers.", "Wrong Input Type", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				else{
					JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Please enter both font-Size and spacing.", "Value Missing", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	
		//Setting the horizontal alignment of the convertButton.
		convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//When 'convertButton' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		convertButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		convertButton.getActionMap().put("showHelp", new HelpAction());
		
		//Setting the maximum and minimum size of the 'convertButton.
		convertButton.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		convertButton.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		convertButton.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		
		//'saveButton' sets in motion the steps needed to save a file.
		saveButton = new JButton("Save Current Pdf");
		
	
		
		//Anonymous inner class is being used as 'ActionListener' because this code is not in use anywhere else in the program
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//'data.saveFile' is being invoked to save the current Pdf file.
				data.saveFile(chosenFontName, chosenFontSize, chosenSpacing);
				//Once the file has been saved, the saveButton is set to inactive again because no new change has been made to the file
				//yet, and therefore nothing new needs to be saved at this point.
				//MATT NOTE: perhaps the client wants to be able to save to a different location - I'm commenting this line out for now
				//saveButton.setEnabled(false);

			}
		});
		
		//Setting the horizontal alignment of the 'saveButton'
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		saveButton.getActionMap().put("showHelp", new HelpAction());
		
		saveButton.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		saveButton.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		saveButton.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		
		//convertSavePane would hold the drop-down menus, the 'convertButton', and the 'saveButton'.
		JPanel convertSavePane = new JPanel();
		convertSavePane.setLayout(new BoxLayout(convertSavePane, BoxLayout.Y_AXIS));
		
		convertSavePane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		
		//Adding the items to the 'convertSavePane'.
		convertSavePane.add(fontNamesComboPane);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		convertSavePane.add(fontSizeFieldPane);
		convertSavePane.add(Box.createRigidArea(new Dimension(0,getPreferredSize().height/12)));
		convertSavePane.add(spacingFieldPane);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		convertSavePane.add(convertButton);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		convertSavePane.add(saveButton);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		
		//Setting up a top border for converSavePane that is 4 pixels wide.
		convertSavePane.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, new Color(156, 138, 165)));
	
		return convertSavePane;
	}
	
	private JPanel initializeControlPane(JPanel openPane, JPanel convertSavePane){
		//'controlPane' holds the JPanels 'openPane' 'launchPdfPane', and 'convertSavePane'.
		JPanel controlPane = new JPanel();
				
		//Setting the layout of controlPane to BoxLayout.
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
				
		//Adding the 'openPane and 'convertSavePane' to 'controlPane'.
		controlPane.add(openPane);
		controlPane.add(convertSavePane);
				
		//Setting up a bottom border for the 'controlPane' that is 4 pixels wide.
		controlPane.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));

		return controlPane;
	}
	
	private JPanel initializeLaunchPdfPane(){
		//Initializing the 'launchPdfButton'.
		
		launchPdfButton = new JButton("View in Acrobat");
		
		
		launchPdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		launchPdfButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		launchPdfButton.getActionMap().put("showHelp", new HelpAction());
				
		launchPdfButton.setMaximumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		launchPdfButton.setMinimumSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
		launchPdfButton.setPreferredSize(new Dimension(getPreferredSize().width - buttonWidthVaccum, getPreferredSize().height/buttonHeightFactor));
						
		//'launchPdfPane' holds the 'launchPdfButton'.
		JPanel launchPdfPane = new JPanel(); 
						
		//Setting the layout of the 'launchPdfPane' to BoxLayout.
		launchPdfPane.setLayout(new BoxLayout(launchPdfPane, BoxLayout.Y_AXIS));
						
		//Adding some extra space before and after the 'launchPdfButton'.
		launchPdfPane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
		launchPdfPane.add(launchPdfButton);
		launchPdfPane.add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
				
		//Adding bottom border for launchPdfpane. 
		launchPdfPane.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));

		return launchPdfPane;
	}
	

	
	private JPanel initializeFinalControlPane(String message, JPanel controlPane, JPanel launchPdfPane){
		JLabel helpTip = new JLabel(message);
		helpTip.setForeground(new Color(150, 0, 165));
		helpTip.setHorizontalAlignment(JLabel.CENTER);
		helpTip.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));

				
		//Declaring 'finalControlPane' and adding the three JPanel objects.
		JPanel finalControlPane = new JPanel(new BorderLayout());
		finalControlPane.add(helpTip, BorderLayout.NORTH);
		finalControlPane.add(controlPane, BorderLayout.CENTER);
		finalControlPane.add(launchPdfPane, BorderLayout.SOUTH);

		return finalControlPane;
	}
	
	public void resize(Dimension size){
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		removeAll();
		initializeComponents();
		revalidate();
		repaint();
	}
}
