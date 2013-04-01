package com.java.tabui;

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

//Class 'TabUIController' sets up the components on the right-hand side of the User-Interface. An object of this class can also
//be displayed on the left side of the user-interface.
public class TabUIControlPane extends JPanel{
  
	//serialization variable - used for GUI elements, mostly to get rid of the warning messages that were being shown
	private static final long serialVersionUID = 228385549718664150L;
	
	//OpenPane houses the openButton.
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
					
					//If status is 0, it means that the file did not have sufficient data to display, and therefore, some controls
					//need to be disabled.
					if(status == 0){
						convertSavePane.disableComponents();
						enableErrorLog();
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Nothing to display.", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					
					//Status == 1 means that at least some meaningful data was found in the input file.
					else if(status == 1){
						//The appropriate controls are enabled.
						fontNamesCombo.setEnabled(true);
						fontSizeField.setEnabled(true);
						fontSizeField.setText("10");
						spacingField.setEnabled(true);
						spacingField.setText("5");
						convertButton.setEnabled(true);
						convertDisabled = false;
						enableErrorLog();
					}
				}
			});
				
			//Setting  the horizontal alignment of the 'openButton'.
			openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			//Using the FontMatrics class, we ensure that the buttons are always wide enough to fully display their labels.
			FontMetrics metrics = getFontMetrics(getFont()); 
			
			//All the buttons should be wide enough to display the string "Save Current Pdf" because it is the longest
			//button label that we are using.
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			
			//Setting the maximum and minimum size of the 'openButton'. Width of the button is always same. It is only the height
			//that changes with window resize.
			openButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			openButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			openButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding some extra space before the 'openButton'.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
				
			//Adding the 'openButton' to the 'openPane'.
			add(openButton);
			
			//Adding some extra space after the 'openButton'.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
		}
	}
	
	//Class ConvertSavePane houses the convertButton, the saveButton, and the components using which the user enters values.
	private class ConvertSavePane extends JPanel{
		
		public ConvertSavePane(int buttonHeight){
			//Using the FontMatrics class, we ensure that the buttons are always wide enough to fully display their labels.
			FontMetrics metrics = getFontMetrics(getFont()); 
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			
			//fontNamesCombo is a JComboBox that holds the font-names that the user can select from.
			if(fontNamesCombo == null){
				fontNamesCombo = new JComboBox<String>(fontNames);
			}
			
			//setting the default item of fontNamesCombo
			fontNamesCombo.setSelectedIndex(0);
			
			//Configuring the size of fontNamesCombo.
			fontNamesCombo.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
			fontNamesCombo.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
			fontNamesCombo.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			
			fontNamesComboPane = new JPanel(new BorderLayout());
			fontNamesComboPane.add(fontNamesCombo);
			
			//Setting a titled border for fontNamesComboPane.
			fontNamesComboBorder = new TitledBorder("Select Font-Name from Below:");
			fontNamesComboBorder.setTitleColor(new Color(0, 85, 255));
			fontNamesComboPane.setBorder(fontNamesComboBorder);
			
			//fontSizeField is a textfield that allows the user to enter the font-size.
			if(fontSizeField == null){
				fontSizeField = new JTextField();
			}
			
			//fontSizeField.setEditable(true);
			fontSizeField.setBackground(new Color(238, 238, 238));
		
			//Setting the size of fontSizeField.
			fontSizeField.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
			fontSizeField.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
			fontSizeField.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			
			fontSizeFieldPane = new JPanel(new BorderLayout());
			fontSizeFieldPane.add(fontSizeField);
			
			//Setting a titled border for fontSizeFieldPane.
			fontSizeFieldBorder = new TitledBorder("Enter Font-Size Below:");
			fontSizeFieldBorder.setTitleColor(new Color(0, 85, 255));
			fontSizeFieldPane.setBorder(fontSizeFieldBorder);
			
			//'spacingField' is a textfield that allows the user to enter the spacing value.
			if(spacingField == null){
				spacingField = new JTextField();
				
			}
			
			//spacingField.setEditable(true);
			spacingField.setBackground(new Color(238, 238, 238));
		
			//Setting the size
			spacingField.setMaximumSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			spacingField.setMinimumSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			spacingField.setPreferredSize(new Dimension(new Dimension(buttonWidth, buttonHeight)));
			
			spacingFieldPane = new JPanel(new BorderLayout());
			spacingFieldPane.add(spacingField);
			
			//Setting a titled border for the spacingFieldPane.
			spacingFieldBorder = new TitledBorder("Enter Spacing Below:");
			spacingFieldBorder.setTitleColor(new Color(0, 85, 255));
			spacingFieldPane.setBorder(spacingFieldBorder);
			
			//convertButton sets in motion the steps needed to convert the selected Ascii file to a proper Pdf file.
			convertButton = new JButton("Convert to Pdf");
			
			//An anonymous inner class is used as the event-listener for the 'convertButton'. The anonymous class is used because this code
			//is not being used anywhere else in the program.
			convertButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					//Extracting the value in fontNamesCombo and assigning it to chosenFontName for later use.
					chosenFontName = fontNamesCombo.getSelectedItem().toString();
					
					//Making sure that value was entered in the two text-fields.
					if(!fontSizeField.getText().equals("") && !spacingField.getText().equals("")){
						try{
							//Getting the values in the text-fields and assigning them to the appropriate Strings for later use. 
							chosenFontSize = Float.parseFloat(fontSizeField.getText());
							chosenSpacing = Float.parseFloat(spacingField.getText());
					
							//Invoking the data.convertFile method, which takes the necessary steps to convert the Ascii to Pdf. 
							data.convertFile(chosenFontName, chosenFontSize, chosenSpacing);
							
							//SaveButton should now be enabled.
							saveButton.setEnabled(true);
							saveDisabled = false;
							//enableErrorLog();
						}
						
						//If the user enters non-numbers in the text-fields.
						catch(NumberFormatException ex){
							JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Font-Size and spacing values must be numbers.", "Wrong Input Type", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					//If the user leaves any of the text_fields blank.
					else{
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Please enter both font-Size and spacing.", "Value Missing", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		
			//Setting the horizontal alignment of the convertButton.
			convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			//Setting the maximum and minimum size of the convertButton.
			convertButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			convertButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			convertButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
			
			//saveButton sets in motion the steps needed to save a file.
			saveButton = new JButton("Save Current Pdf");
			
			if(saveDisabled){
				saveButton.setEnabled(false);
			}
			
			//Anonymous inner class is being used as ActionListener because this code is not in use anywhere else in the program
			saveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//data.saveFile() is being invoked to save the current Pdf file.
					destinationPath = data.saveFile(chosenFontName, chosenFontSize, chosenSpacing);
				}
			});
			
			//Setting the horizontal alignment of the saveButton.
			saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			//Setting the size of saveButton.
			saveButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			saveButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			saveButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			//Adding extra space.
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
		
		//Disables certain components.
		public void disableComponents(){
			convertDisabled = true;
			saveDisabled = true;
			
			//fontnamesCombo is initially disabled, because the user has not yet opened any Ascii file.
			fontNamesCombo.setEnabled(false);
			
			//fontSizeField is initially disabled, because the user has not yet opened any Ascii file.
			fontSizeField.setEnabled(false);
			
			//spacingField is initially disabled, because the user has not yet opened any Ascii file.
			spacingField.setEnabled(false);
			
			//ConvertButton is initially disabled, because the user has not yet opened any Ascii file.
			convertButton.setEnabled(false);
			
			//saveButton is initially disabled, because the user has not yet opened any Ascii file.
			saveButton.setEnabled(false);
			
		}
	}
	
	//ControlPane holds the openPane and convertSavePane
	private class ControlPane extends JPanel{
		public ControlPane(JPanel openPane, JPanel convertSavePane){
			//Setting the layout of ControlPane to BoxLayout.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding the openPane and convertSavePane to this ControlPane.
			add(openPane);
			add(convertSavePane);
						
			//Setting up a bottom border for this ControlPane that is 4 pixels wide.
			setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		}
	}
	
	//InfoPane holds the errLogButton and helpButton.
	private class InfoPane extends JPanel{
		private JButton errLogButton = null;
		private JButton helpButton = null;
		
		public InfoPane(int buttonHeight){
			//Instantiating the errLogButton.  
			errLogButton = new JButton("View Error Log");
				
			//Setting the ActionListener for errLogButton. An anonymous inner class is used because this code is used no where else in the program.
			errLogButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						String current = "";
						
						//Taking care of some platform dependent task in order to access a hidden file.
						String osVersion = System.getProperty("os.name");
						String errorLog;
						
						//Taking care of some platform dependent task in order to access a hidden file.
						if(osVersion.startsWith("Windows"))
							errorLog = ""+System.getenv("TEMP")+"/T2PDFErr.txt";
						else
							errorLog = "/tmp/T2PDFErr.txt";
						
						//A window where the error-log file shall be displayed.
						InfoView errLogView = new InfoView("Error Log");
						
						//InputStream for the error-log file.
						BufferedReader in = new BufferedReader(new FileReader(errorLog));
						
						//Reading the file until null is reached.
						current = in.readLine();
						
						while(current != null) {
							errLogView.append(current);
							current = in.readLine();
						}
						
						//Closing the stream.
						in.close();
					}
					catch(Exception ex){
						JOptionPane.showMessageDialog(TabUIControlPane.this.getParent(), "Unexpected Error Occured", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
				
			//Setting  the horizontal alignment of the errLogButton.
			errLogButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			FontMetrics metrics = getFontMetrics(getFont()); 
			int buttonWidth = metrics.stringWidth("Save Current Pdf");
			
			//Setting the maximum and minimum size of the errLogButton.
			errLogButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			errLogButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			errLogButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			//if errLogDisabled is true, the button is disabled.
			if(errLogDisabled){
				errLogButton.setEnabled(false);
			}
			
			//The 'BoxLayout' helps us in rendering a properly sized errLogButton for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			//Adding some extra space before the errLogButton.
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
				
			//Adding the errLogButton to the InfoPane.
			add(errLogButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			
			
			//Instantiating the helpButton.  
			helpButton = new JButton("Help");
				
			//Setting the ActionListener for helpButton. An anonymous inner class is used because this code is used no where else in the program.
			helpButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String helpMessage = 	"To convert a file to PDF:" +
							"				\n1) First open a text tablature file by clicking on 'Open Input File'.\n" +
							 				"2) Select your desired font-name, font-size, and spacing.\n3) Click on 'Convert to PDF'." +
							 				"\n\nTo make a change to the font settings or spacing:\n1) Make the desired changes in font-name, font-size, or spacing fields.\n" +
							 				"2) Click on 'Convert to PDF'." +
							 				"\n\nTo save a file as Pdf:\n1) After clicking on the 'Convert to Pdf' button, wait for the convertion to complete and\n" +
							 				"the file to be displayed in Pdf format on the left.\n2) Click on the 'Save Current Pdf' button and save the file at the desired location." +
							 				"\n\nTo view the error-log:\n1) Click on the 'View Error Log' button whenever it is active.";
					
					InfoView helpView = new InfoView("Help");	
					helpView.append(helpMessage);
				}
			});
				
			//Setting  the horizontal alignment of the helpButton.
			helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				
			//Setting the maximum and minimum size of the helpButton.
			helpButton.setMaximumSize(new Dimension(buttonWidth + 40, buttonHeight));
			helpButton.setMinimumSize(new Dimension(buttonWidth + 40, buttonHeight));
			helpButton.setPreferredSize(new Dimension(buttonWidth + 40, buttonHeight));
		
			//The 'BoxLayout' helps us in rendering a properly sized helpButton for this particular application.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
			//Adding the helpButton to the InfoPane.
			add(helpButton);
			add(Box.createRigidArea(new Dimension(0, buttonHeight/2)));
			
			//Adding bottom border for the InfoPane. 
			setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		}
		
		//Disables the errLogButton whenever necessary.
		public void disableComponents(){
			errLogButton.setEnabled(false);
		}
	}
	
	//FinalControlPane holds together all the other JPanels above.
	private class FinalControlPane extends JPanel{
		public FinalControlPane(JPanel controlPane, JPanel infoPane){
			setLayout(new BorderLayout());
			
			//An empty JLabel is added just to fill up the top. 
			JLabel filler = new JLabel(" ");
			filler.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
			
			//Adding things to the FinalControlPane.
			add(filler, BorderLayout.NORTH);
			add(controlPane, BorderLayout.CENTER);
			add(infoPane, BorderLayout.SOUTH);
		}
	}
	
	//To convert to Pdf.
	private JButton convertButton = null;
	
	//To save the Pdf file.
	private JButton saveButton = null;
	
	//JComboBox holding the font-name values.
	private JComboBox<String> fontNamesCombo = null;
	
	//JTextField holding the font-size value.
	private JTextField fontSizeField = null;
	
	//JTextField holding the spacing value.
	private JTextField spacingField = null;
	
	//The path where the file shall be saved.
	private String destinationPath;
	
	//Initially the convertButton should be disabled.
	private boolean convertDisabled = true;
	
	//Initially the saveButton should be disabled.
	private boolean saveDisabled = true;
	
	//Initially the errLogButton should be disabled.
	private boolean errLogDisabled = true;
	
	//JPanel to hold the fontNamesCombo.
	private JPanel fontNamesComboPane;
	
	//JPanel to hold the fontSizeField.
	private JPanel fontSizeFieldPane;
	
	////JPanel to hold the spacingField.
	private JPanel spacingFieldPane;
	
	//Titled border for the fontNamesCombo.
	private TitledBorder fontNamesComboBorder;
	
	//Titled border for the fontSizeField.
	private TitledBorder fontSizeFieldBorder;
	
	//Titled border for the spacingFieldBorder.
	private TitledBorder spacingFieldBorder;
	
	//The references to the JPanels defined above.
	private OpenPane openPane;
	private ConvertSavePane convertSavePane;
	private ControlPane controlPane;
	private InfoPane infoPane;
	private FinalControlPane finalControlPane;

	//Reference to the TabFileManager.
	private final TabFileManager data;
	
	//chosenFontName is an object that holds the selected value from the fontNamesCombo and makes this value accessible from
	//multiple methods.  
	private String chosenFontName;
	
	//chosenFontSize is an object that holds the selected value from the fontSizeField and makes this value accessible from
	//multiple methods.
	private float chosenFontSize;
	
	//chosenSpacing is an object that holds the selected value from the spacingField and makes this value accessible from
	//multiple methods.
	private float chosenSpacing;
	
	//A value based on which the height of a button is determined.
	private int buttonHeightFactor = 8;
	
	//The font-names that shall be used
	private String [] fontNames = {BaseFont.HELVETICA, BaseFont.HELVETICA_BOLD, BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, 
								   BaseFont.TIMES_BOLDITALIC };
	
	
	//The TabUIControlPane constructor.  
	public TabUIControlPane(Dimension size, final TabFileManager data){
		
		//this.data holds the reference to the TabFileManager object. 
		this.data = data;
	
		//Setting the amount of space on the UI that this TabUIControlPane would occupy.
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		//Setting the layout of this TabUIControlPane.
		setLayout(new BorderLayout());
		
		//InitializeComponents() takes the steps to set up the TabUIControlPane.
		initializeComponents();
	}

	//Enables the errLogButton.
	public void enableErrorLog(){
		infoPane.errLogButton.setEnabled(true);
		errLogDisabled = false;
	}
	
	//InitializeComponents() takes the steps to set up the TabUIControlPane.
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
		int buttonWidth = metrics.stringWidth("Select Font-Name from Below:");
		
		//Setting the size for this TabUIControlPane. When the window is resized, the area around buttons changes.
		setMaximumSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		setMinimumSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		setPreferredSize(new Dimension(buttonWidth + 40 + size.width/3, size.height));
		
		removeAll();
		
		//Upon window resize, initializeComponents() is called again.
		initializeComponents();
		revalidate();
		repaint();
	}
}
