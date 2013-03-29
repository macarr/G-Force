package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
	
	private class OpenPane extends JPanel{
		//To open an ascii file.
		private JButton openButton = null;
			
		public OpenPane(int buttonHeight){
			
			//Instantiating the openButton.  
			openButton = new JButton("Open Input File");
				
			//Setting the ActionListener for 'openButton'. An anonymous inner class is used because this code is used no where else in the program.
			openButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
						
					//The method 'data.loadFile()' is invoked to display the 'JFileChooser', so that the user can choose her/his desired file.
					int status  = data.loadFile();
					
					//if(status < 0){
						//initializeComponents();
					//}
					//If status is '1', it means that the user chose a file, and therefore, the following GUI components need to be made active.
					//A '0' means that the user decided to close the 'JFileChooser' without selecting any file. 
					if(status == 0){
						convertSavePane.disableComponents();
						enableErrorLog();
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Nothing to display", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					
					else if(status == 1){
						fontNamesCombo.setEnabled(true);
						fontNamesCombo.setBackground(new Color(255, 255, 255));
						fontNamesComboBorder.setTitleColor(new Color(0, 85, 255));
						fontSizeField.setEnabled(true);
						fontSizeField.setBackground(new Color(255, 255, 255));
						fontSizeField.setText("10");
						fontSizeFieldBorder.setTitleColor(new Color(0, 85, 255));
						spacingField.setEnabled(true);
						spacingField.setBackground(new Color(255, 255, 255));
						spacingField.setText("5");
						spacingFieldBorder.setTitleColor(new Color(0, 85, 255));
						convertButton.setEnabled(true);
						convertDisabled = false;
						enableErrorLog();
					}
				}
			});
				
			//Setting  the horizontal alignment of the 'openButton'.
			openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			FontMetrics metrics = getFontMetrics(getFont()); 
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			int height = metrics.getHeight();
			
			//Setting the maximum and minimum size of the 'openButton'.
			openButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			openButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			openButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding some extra space before the 'openButton'.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
				
			//Adding the 'openButton' to the 'openPane'.
			add(openButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
		}
	}
	
	private class ConvertSavePane extends JPanel{
		
		public ConvertSavePane(int buttonHeight){
			
			FontMetrics metrics = getFontMetrics(getFont()); 
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			
			//'fontNamesCombo' is a JComboBox that holds the font-names that the user can select from.
			if(fontNamesCombo == null){
				fontNamesCombo = new JComboBox<String>(fontNames);
			}
			
			//setting the default item of 'fontNamesCombo'
			fontNamesCombo.setSelectedIndex(0);
			
			fontNamesCombo.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
			fontNamesCombo.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
			fontNamesCombo.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			
			fontNamesComboPane = new JPanel(new BorderLayout());
			fontNamesComboPane.add(fontNamesCombo);
			
			//Setting a titled border for the 'fontNameCombo'.
			fontNamesComboBorder = new TitledBorder("Select Font from Below:");
			fontNamesComboBorder.setTitleColor(new Color(0, 85, 255));
			fontNamesComboPane.setBorder(fontNamesComboBorder);
			
			//'fontSizesCombo' is a JComboBox that holds the font-sizes that the user can select from.
			if(fontSizeField == null){
				fontSizeField = new JTextField();
			}
			
			fontSizeField.setEditable(true);
			fontSizeField.setBackground(new Color(238, 238, 238));
		
			
			
			fontSizeField.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
			fontSizeField.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
			fontSizeField.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			
			fontSizeFieldPane = new JPanel(new BorderLayout());
			fontSizeFieldPane.add(fontSizeField);
			
			//Setting a titled border for the 'fontSizeFieldPane'.
			fontSizeFieldBorder = new TitledBorder("Enter Font Size Below:");
			fontSizeFieldBorder.setTitleColor(new Color(0, 85, 255));
			fontSizeFieldPane.setBorder(fontSizeFieldBorder);
			
			//'spacingCombo' is a JComboBox that holds the spacing values that the user can select from. The user can input numbers into
			//'spacingCombo'.
			if(spacingField == null){
				spacingField = new JTextField();
				
			}
			
			spacingField.setEditable(true);
			spacingField.setBackground(new Color(238, 238, 238));
		
		

			spacingField.setMaximumSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			spacingField.setMinimumSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			spacingField.setPreferredSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			
			spacingFieldPane = new JPanel(new BorderLayout());
			spacingFieldPane.add(spacingField);
			
			//Setting a titled border for the 'spacingCombo'.
			spacingFieldBorder = new TitledBorder("Enter Spacing Below:");
			spacingFieldBorder.setTitleColor(new Color(0, 85, 255));
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
							data.convertFile(chosenFontName, chosenFontSize, chosenSpacing);
							saveButton.setEnabled(true);
							saveDisabled = false;
							//enableErrorLog();
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
			
			
			
			
			//Setting the maximum and minimum size of the 'convertButton.
			convertButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			convertButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			convertButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
			
			//'saveButton' sets in motion the steps needed to save a file.
			saveButton = new JButton("Save Current Pdf");
			
			if(saveDisabled){
				saveButton.setEnabled(false);
			}
			
			//'saveButton' is initially not active because the file has not yet been converted to Pdf. 
			//saveButton.setEnabled(false);
			
			//Anonymous inner class is being used as 'ActionListener' because this code is not in use anywhere else in the program
			saveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					//'data.saveFile' is being invoked to save the current Pdf file.
					destinationPath = data.saveFile(chosenFontName, chosenFontSize, chosenSpacing);
					//Once the file has been saved, the saveButton is set to inactive again because no new change has been made to the file
					//yet, and therefore nothing new needs to be saved at this point.
					//MATT NOTE: perhaps the client wants to be able to save to a different location - I'm commenting this line out for now
					//saveButton.setEnabled(false);
					if(destinationPath != ""){
						launchPdfButton.setEnabled(true);
					}

				}
			});
			
			//Setting the horizontal alignment of the 'saveButton'
			saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			
			saveButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			saveButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			saveButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
			
			//convertSavePane would hold the drop-down menus, the 'convertButton', and the 'saveButton'.
			//JPanel convertSavePane = new JPanel();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			add(Box.createRigidArea(new Dimension(0, getPreferredSize().height/12)));
			
			//Adding the items to the 'convertSavePane'.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			add(fontNamesComboPane);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			add(fontSizeFieldPane);
			add(Box.createRigidArea(new Dimension(0,buttonHeight/2)));
			add(spacingFieldPane);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			add(convertButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			add(saveButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			
			//Setting up a top border for converSavePane that is 4 pixels wide.
			setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, new Color(156, 138, 165)));
			
		}
		
		public void disableComponents(){
			convertDisabled = true;
			saveDisabled = true;
			
			//'fontnamesCombo' is initially disabled, because the user has not yet opened any Ascii file.
			fontNamesCombo.setEnabled(false);
			
			//'fontSizeField' is initially disabled, because the user has not yet opened any Ascii file.
			fontSizeField.setEnabled(false);
			
			//'spacingField' is initially disabled, because the user has not yet opened any Ascii file.
			spacingField.setEnabled(false);
			
			convertButton.setEnabled(false);
			saveButton.setEnabled(false);
			
		}
	}
	
	private class ControlPane extends JPanel{
		public ControlPane(JPanel openPane, JPanel convertSavePane){
			//Setting the layout of controlPane to BoxLayout.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding the 'openPane and 'convertSavePane' to 'controlPane'.
			add(openPane);
			add(convertSavePane);
						
			//Setting up a bottom border for the 'controlPane' that is 4 pixels wide.
			setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		}
	}
	
	
	
	private class InfoPane extends JPanel{
		private JButton errLogButton = null;
		private JButton helpButton = null;
		
		public InfoPane(int buttonHeight){
			//Instantiating the openButton.  
			errLogButton = new JButton("View Error Log");
				
			//Setting the ActionListener for 'openButton'. An anonymous inner class is used because this code is used no where else in the program.
			errLogButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						String current = "";
						String osVersion = System.getProperty("os.name");
						String errorLog;
						
						if(osVersion.startsWith("Windows"))
							errorLog = ""+System.getenv("TEMP")+"/T2PDFErr.txt";
						else
							errorLog = "/tmp/T2PDFErr.txt";
						
						InfoView errLogView = new InfoView();
						
						BufferedReader in = new BufferedReader(new FileReader(errorLog));
						
						current = in.readLine();
						
						while(current != null) {
							errLogView.append(current);
							current = in.readLine();
						}
						
						in.close();
					}
					catch(Exception ex){
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Unexpected Error Occured", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
				
			//Setting  the horizontal alignment of the 'openButton'.
			errLogButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			FontMetrics metrics = getFontMetrics(getFont()); 
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			int height = metrics.getHeight();
			
			//Setting the maximum and minimum size of the 'openButton'.
			errLogButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			errLogButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			errLogButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			if(errLogDisabled){
				errLogButton.setEnabled(false);
			}
			
			//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding some extra space before the 'openButton'.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
				
			//Adding the 'openButton' to the 'openPane'.
			add(errLogButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			
			
			//Instantiating the openButton.  
			helpButton = new JButton("Help");
				
			//Setting the ActionListener for 'openButton'. An anonymous inner class is used because this code is used no where else in the program.
			helpButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
						
					
				}
			});
				
			//Setting  the horizontal alignment of the 'openButton'.
			helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			//Setting the maximum and minimum size of the 'openButton'.
			helpButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			helpButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			helpButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding some extra space before the 'openButton'.
			//add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
				
			//Adding the 'openButton' to the 'openPane'.
			add(helpButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			
			//Adding bottom border for infoPane. 
			setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		}
		
		public void disableComponents(){
			errLogButton.setEnabled(false);
		}
	}
	
	private class FinalControlPane extends JPanel{
		public FinalControlPane(JPanel controlPane, JPanel infoPane){
			setLayout(new BorderLayout());
			
			JLabel filler = new JLabel(" ");
			filler.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
			
			add(filler, BorderLayout.NORTH);
			add(controlPane, BorderLayout.CENTER);
			add(infoPane, BorderLayout.SOUTH);
		}
	}
	
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
	
	private String destinationPath;
	
	private boolean convertDisabled = true;
	private boolean saveDisabled = true;
	private boolean errLogDisabled = true;
	
	private JPanel fontNamesComboPane;
	private JPanel fontSizeFieldPane;
	private JPanel spacingFieldPane;
	
	private TitledBorder fontNamesComboBorder;
	private TitledBorder fontSizeFieldBorder;
	private TitledBorder spacingFieldBorder;
	
	OpenPane openPane;
	ConvertSavePane convertSavePane;
	ControlPane controlPane;
	InfoPane infoPane;
	FinalControlPane finalControlPane;

	
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
	
	private int buttonHeightFactor = 8;
	
	//The font-names that shall be used
	private String [] fontNames = {BaseFont.HELVETICA, BaseFont.HELVETICA_BOLD, BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, 
								   BaseFont.TIMES_BOLDITALIC };
	
	
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
	}

	public void enableErrorLog(){
		infoPane.errLogButton.setEnabled(true);
		errLogDisabled = false;
	}
	
	public void initializeComponents(){
		openPane = new OpenPane(getPreferredSize().height/buttonHeightFactor);
		
		convertSavePane = new ConvertSavePane(getPreferredSize().height/buttonHeightFactor);
		
		controlPane = new ControlPane(openPane, convertSavePane);		
		
		infoPane = new InfoPane(getPreferredSize().height/buttonHeightFactor);
		finalControlPane = new FinalControlPane(controlPane, infoPane); 		
				
		add(finalControlPane, BorderLayout.NORTH);
		
		if(convertDisabled){
			convertSavePane.disableComponents();
		}
	}
	
	
	public void resizeComponent(Dimension size){
		FontMetrics metrics = getFontMetrics(getFont()); 
		int buttonWidth = metrics.stringWidth("Select Font from Below");
		
		setMaximumSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		setMinimumSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		setPreferredSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		
		removeAll();
		initializeComponents();
		revalidate();
		repaint();
	}
}
