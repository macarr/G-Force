package com.java.pdfmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.jpedal.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;

//The class that helps bind the F1 key to the help-screen.
class HelpAction extends AbstractAction{
	public void actionPerformed( ActionEvent tf ){
		new HelpView();
	}
}
	
//To show the help-screen.
class HelpView extends JFrame{
	public HelpView(){
		JTextPane helpPane = new JTextPane();
		helpPane.setText("To convert a file to PDF:\n1) First open an Ascii file by clicking on 'Open Ascii File'.\n" +
						 "2) Select your desired font, font-size, and spacing.\n3) Click on 'Save as PDF & Preview'." +
					     "\n\nTo make a change to the font settings or spacing:\n1) Choose the desired settings from the drop down menus.\n" +
					     "2) Click on 'Save as PDF & Preview'.");
		helpPane.setEditable(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(helpPane);
		this.setSize(400, 500);
		this.setVisible(true);
	}
}

//Class 'UIController' sets up the components on the right-hand side of the User-Interface
class UIController extends JPanel{
	
	//To open an ascii file.
	private JButton openButton;
	
	//To convert to Pdf.
	private JButton convertButton;
	
	//To save the Pdf file.
	private JButton saveButton;
	
	//To launch the Pdf file in a real Pdf reader like Acrobat Reader.
	private JButton launchPdfButton;
	
	//JComboBox holding the font-name values.
	private JComboBox<String> fontNamesCombo;
	
	//JComboBox holding the font-size values.
	private JComboBox<String> fontSizesCombo;
	
	//JComboBox holding the spacing values.
	private JComboBox<String> spacingCombo;
	
	//Reference to the 'UIMiddleLayer'.
	private final UIMiddleLayer data;
	
	//'chosenFontName' is an object that holds the selected value from the 'fontNamesCombo' and makes this value accessible from
	//multiple methods.  
	private String chosenFontName;
	
	//'chosenFontSize' is an object that holds the selected value from the 'fontSizesCombo' and makes this value accessible from
	//multiple methods.
	private float chosenFontSize;
	
	//'chosenSpacing' is an object that holds the selected value from the 'fontSpacingCombo' and makes this value accessible from
	//multiple methods.
	private float chosenSpacing;
	
	//The font-names that shall be used
	private String [] fontNames = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC };
	
	//The font-sizes that shall be used
	private String [] fontSizes = {"8", "9", "10", "11", "12", "13", "14"};
	
	//The spacing values that shall be used
	private String [] spacing = {"1", "2", "3", "4", "5", "6", "7", "8"};
	
	//The 'UIController' constructor. It takes two parameters- one defining the preferred size for this 'UIConstructor', and the second one
	//carrying the reference to the 'UIMiddleLayer'.  
	public UIController(Dimension size, final UIMiddleLayer data){
		
		//Setting the amount of space on the UI that this 'UIController' would occupy.
		setPreferredSize(new Dimension(size.width, size.height));
		
		//this.data holds the reference to the 'MiddleLayer' object 
		this.data = data;
		
		//Instantiating the openButton.  
		openButton = new JButton("Open Ascii File");
		
		//Setting the ActionListener for 'openButton'. An anonymous inner class is used because this code is used no where else in the program.
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//The method 'data.loadFile()' is invoked to display the 'JFileChooser', so that the user can choose her/his desired file.
				int status  = data.loadFile();
			
				//If status is '1', it means that the user chose a file, and therefore, the following GUI components need to be made active.
				//A '0' means that the user decided to close the 'JFileChooser' without selecting any file. 
				if(status == 1){
					fontNamesCombo.setEnabled(true);
					fontSizesCombo.setEnabled(true);
					spacingCombo.setEnabled(true);
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
		openButton.setMaximumSize(new Dimension(150, 60));
		openButton.setMinimumSize(new Dimension(150, 60));
		
		//'openPane' holds the 'openButton'.
		JPanel openPane = new JPanel();
		
		//The 'BoxLayout' helps us in rendering a properly sized 'openButton' for this particular application.
		openPane.setLayout(new BoxLayout(openPane, BoxLayout.Y_AXIS));
				
		//Adding some extra space before the 'openButton'.
		openPane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Adding the 'openButton' to the 'openPane'.
		openPane.add(openButton);
		openPane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//'fontNamesCombo' is a JComboBox that holds the font-names that the user can select from.
		fontNamesCombo = new JComboBox<String>(fontNames);
		
		//'fontnamesCombo' is initially disabled, because the user has not yet opened any Ascii file.
		fontNamesCombo.setEnabled(false);
		
		//setting the default item of 'fontNamesCombo'
		fontNamesCombo.setSelectedIndex(0);
		
		//Setting a titled border for the 'fontNameCombo'.
		fontNamesCombo.setBorder(BorderFactory.createTitledBorder("Select Font:"));
		
		//When 'fontNamesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		fontNamesCombo.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		fontNamesCombo.getActionMap().put("showHelp", new HelpAction());
		
		//'fontSizesCombo' is a JComboBox that holds the font-sizes that the user can select from.
		fontSizesCombo = new JComboBox<String>(fontSizes);
		fontSizesCombo.setEditable(true);
		
		//'fontSizesCombo' is initially disabled, because the user has not yet opened any Ascii file.
		fontSizesCombo.setEnabled(false);
		
		//The default item shown on the 'fontSizesCombo' is the one appearing approximately halfway down the combo's list.
		fontSizesCombo.setSelectedIndex((fontSizesCombo.getItemCount()-1)/2);
		
		//Setting a titled border for the 'fontSizesCombo'.
		fontSizesCombo.setBorder(BorderFactory.createTitledBorder("Select Font Size:"));
		
		//When 'fontSizesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		fontSizesCombo.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		fontSizesCombo.getActionMap().put("showHelp", new HelpAction());
		
		//'spacingCombo' is a JComboBox that holds the spacing values that the user can select from. The user can input numbers into
		//'spacingCombo'.
		spacingCombo = new JComboBox<String>(spacing);
		spacingCombo.setEditable(true);
		
		//'spacingCombo' is initially disabled, because the user has not yet opened any Ascii file.
		spacingCombo.setEnabled(false);
		
		//The default item shown on the 'spacingCombo' is the one appearing approximately halfway down the combo's list.
		spacingCombo.setSelectedIndex((spacingCombo.getItemCount()-1)/2);
		
		//Setting a titled border for the 'spacingCombo'.
		spacingCombo.setBorder(BorderFactory.createTitledBorder("Select Spacing:"));
		
		//When 'spacingSizesCombo' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		spacingCombo.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		spacingCombo.getActionMap().put("showHelp", new HelpAction());

		//'convertButton' sets in motion the steps needed to convert the selected Ascii file to a proper Pdf file.
		convertButton = new JButton("Convert to Pdf");
		
		//'convertButton' is initially disabled because no Ascii file has yet been selected.
		convertButton.setEnabled(false);
		
		//An anonymous inner class is used as the event-listener for the 'convertButton'. The anonymous class is used because this code
		//is not being used anywhere else in the program.
		convertButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//extracting the chosen values from the three 'JComboBoxes' and assigning them to the corresponding value-holder. These
				//value-holders make the job of extracting the 'JComboBox' values easier later on in the program.
				chosenFontName = fontNamesCombo.getSelectedItem().toString();
				chosenFontSize = Float.parseFloat(fontSizesCombo.getSelectedItem().toString());
				chosenSpacing = Float.parseFloat(spacingCombo.getSelectedItem().toString());
				
				//Invoking the 'data.convertButton', which creates the 'PdfMaker' object, and invokes its 'createPdf' method. The saveButton
				//and launchButton are passed as references so that they be made active after the current file has been converted to Pdf.
				data.convertFile(chosenFontName, chosenFontSize, chosenSpacing, saveButton, launchPdfButton);
			}
		});
	
		//Setting the horizontal alignment of the convertButton.
		convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//When 'convertButton' has focus, pressing the 'F1' key would trigger a 'HelpAction', which would display the 'HelpView'.
		convertButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		convertButton.getActionMap().put("showHelp", new HelpAction());
		
		//Setting the maximum and minimum size of the 'convertButton.
		convertButton.setMaximumSize(new Dimension(150, 60));
		convertButton.setMinimumSize(new Dimension(150, 60));
		
		//'saveButton' sets in motion the steps needed to save a file.
		saveButton = new JButton("Save Current Pdf");
		
		//'saveButton' is initially not active because the file has not yet been converted to Pdf. 
		saveButton.setEnabled(false);
		
		//Anonymous inner class is being used as 'ActionListener' because this code is not in use anywhere else in the program
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//'data.saveFile' is being invoked to save the current Pdf file.
				data.saveFile(chosenFontName, chosenFontSize, chosenSpacing);
				//Once the file has been saved, the saveButton is set to inactive again because no new change has been made to the file
				//yet, and therefore nothing new needs to be saved at this point.
				saveButton.setEnabled(false);

			}
		});
		
		//Setting the horizontal alignment of the 'saveButton'
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		saveButton.getActionMap().put("showHelp", new HelpAction());
		saveButton.setMaximumSize(new Dimension(150, 60));
	
		//convertSavePane would hold the drop-down menus, the 'convertButton', and the 'saveButton'.
		JPanel convertSavePane = new JPanel();
		convertSavePane.setLayout(new BoxLayout(convertSavePane, BoxLayout.Y_AXIS));
		
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Adding the items to the 'convertSavePane'.
		convertSavePane.add(fontNamesCombo);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		convertSavePane.add(fontSizesCombo);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		convertSavePane.add(spacingCombo);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		convertSavePane.add(convertButton);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		convertSavePane.add(saveButton);
		convertSavePane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Setting up a top border for converSavePane that is 4 pixels wide.
		convertSavePane.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, new Color(156, 138, 165)));
		
		//'controlPane' holds the JPanels 'openPane' 'launchPdfPane', and 'convertSavePane'.
		JPanel controlPane = new JPanel();
		
		//Setting the layout of controlPane to BoxLayout.
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
		
		//Adding the 'openPane and 'convertSavePane' to 'controlPane'.
		controlPane.add(openPane);
		controlPane.add(convertSavePane);
		
		//Setting up a bottom border for the 'controlPane' that is 4 pixels wide.
		controlPane.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		
		JLabel helpTip = new JLabel("Press F1 on Keyboard for Help.");
		helpTip.setForeground(new Color(150, 0, 165));
		helpTip.setHorizontalAlignment(JLabel.CENTER);
		helpTip.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));

		//Initializing the 'launchPdfButton'.
		launchPdfButton = new JButton("View in Acrobat");
		launchPdfButton.setEnabled(false);
		launchPdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		launchPdfButton.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
		launchPdfButton.getActionMap().put("showHelp", new HelpAction());
		launchPdfButton.setMaximumSize(new Dimension(150, 60));
				
		//'launchPdfPane' holds the 'launchPdfButton'.
		JPanel launchPdfPane = new JPanel(); 
				
		//Setting the layout of the 'launchPdfPane' to BoxLayout.
		launchPdfPane.setLayout(new BoxLayout(launchPdfPane, BoxLayout.Y_AXIS));
				
		//Adding some extra space before and after the 'launchPdfButton'.
		launchPdfPane.add(Box.createRigidArea(new Dimension(0, 20)));
		launchPdfPane.add(launchPdfButton);
		launchPdfPane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Adding bottom border for launchPdfpane. 
		launchPdfPane.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(156, 138, 165)));
		
		//Declaring 'finalControlPane' and adding the three JPanel objects.
		JPanel finalControlPane = new JPanel(new BorderLayout());
		finalControlPane.add(helpTip, BorderLayout.NORTH);
		finalControlPane.add(controlPane, BorderLayout.CENTER);
		finalControlPane.add(launchPdfPane, BorderLayout.SOUTH);
		
		//Setting the layout of this JPanel (UIControls), and adding the controlPane
		setLayout(new BorderLayout());
		add(finalControlPane, BorderLayout.NORTH);
	}
}

//UIMiddleLayer takes care of the File input/output and miscellaneous tasks.
class UIMiddleLayer{
	//'outputArea' is the 'UIView' where the screen output takes place. 
	public UIView outputArea;
	
	//String 'input' holds the input file path, which is initially empty.
	String inputPath = "";
	
	//The temporary output path.
	String outputPath = "files/temp.pdf";
	
	//If the user decides to save the file, the file-path would be stored in 'destinationPath'.
	String destinationPath = "";
	
	//'The contents of the input file are stored in the ArrayList 'contents'.
	private ArrayList<ArrayList<String>> contents;
	
	//Width of the pdf document's pages, which is the standard paper-width.
	int pageWidth=612;
	
	//Height of the pdf document's pages, , which is the standard paper-height.
	int pageHeight=792;
	
	//The 'UIMiddleLayer' constructor, which takes as parameter a reference to the UIView where all screen output is rendered.
	public UIMiddleLayer(UIView outputArea){
		this.outputArea = outputArea;
	}
	
	//'loadFile' displays the 'JFileChooser' which allows the user to choose the input file.  
	public int loadFile(){
		JFileChooser fC = new JFileChooser();
		
		//'status' is a flag which is returned to the caller of the 'loafFile', based on which, the caller enables some buttons.  
		int status = 0;
		
		int returnVal = fC.showOpenDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			inputPath = fC.getSelectedFile().toString();
			
			//If the user chose a file, that file's name is sent to the method 'inputConverter', which sends back an ArrayList filled with
			//the contents of the file.
			contents = inputConverter(inputPath);
			
			//The Ascii file is displayed on the screen.
			outputArea.showAsciiFile(contents);
			
			//'Status' = 1 means that now it is OK to enable the 'convertButton'.
			status = 1;
		}
		return status;
	}
	
	//'saveFile' allows the user to save a file by displaying a file-saver dialog-box.
	public void saveFile(final String fontName, final float fontSize, final float spacing) {
		JFileChooser fc = new JFileChooser();
		final ArrayList<ArrayList<String>> fileContents = new ArrayList<ArrayList<String>>();
		
		int returnVal = fc.showSaveDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			destinationPath = fc.getSelectedFile().toString();
		}
		
		for(int blockNum = 1; blockNum < contents.size(); blockNum++){
			fileContents.add(contents.get(blockNum));
		}
	
		outputArea.displayStatusUpdate("Saving Pdf File...", false);
		Thread PdfMakerThread = new Thread(){
			public void run(){
				new PdfMaker(contents.get(0), fileContents, new Rectangle(pageWidth, pageHeight), destinationPath, fontName, fontSize, spacing).createPDF();
				interrupt();
				outputArea.displayStatusUpdate("File Saved.", false);
			}
		};
		
		PdfMakerThread.start();
	}
	
	//'convertFile' converts the Ascii file into Pdf Format and displays it in the 'outputArea'.
	public void convertFile(final String fontName, final float fontSize, final float spacing, final JButton saveButton, final JButton launchPdfButton){
		final ArrayList<ArrayList<String>> fileContents = new ArrayList<ArrayList<String>>();
		
		//First making the outputArea's status-label blank 
		outputArea.displayStatusUpdate(" ", false);
		
		//The first element of contents is the file-header data.
		for(int blockNum = 1; blockNum < contents.size(); blockNum++){
			fileContents.add(contents.get(blockNum));
		}
	
		//Before we begin to convert the Ascii file into Pdf, we notify the user.
		outputArea.displayStatusUpdate("Converting to Pdf...", true);
		
		//We use a 'Thread' for the conversion.
		Thread PdfMakerThread = new Thread(){
			public void run(){
				
				//The 'createPdf' method of 'PdfMaker' returns true if all the music data fit on the screen. Otherwise it returns false.
				boolean fullSuccess = new PdfMaker(contents.get(0), fileContents, new Rectangle(pageWidth, pageHeight), outputPath, fontName, fontSize, spacing).createPDF();
				
				//The 'showPdf' method of the 'UIView' class takes in the outputPath (The location where the temporary Pdf file is stored).
				outputArea.showPdfFile(outputPath);
				
				//Following the conversion, the 'saveButton' is enabled.
				saveButton.setEnabled(true);

				//If any of the music could not fit on the page, need to notify the user through a dialog-box.
				if(!fullSuccess){
					JOptionPane.showMessageDialog(outputArea, "Some Music Could not Fully Fit due to the Large Size.", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
				
				//Interrupting the 'Thread'.
				interrupt();
			}
		};
		
		PdfMakerThread.start();
	}
	
	public ArrayList<ArrayList<String>> inputConverter(String inputPath){
		ArrayList<ArrayList<String>> cont;

		ArrayList<String> block = new ArrayList<String>();
		String s = "";
		String cur = "";
		cont = new ArrayList<ArrayList<String>>();

		try{
		//Input file
		BufferedReader in = new BufferedReader(new FileReader(inputPath));

		block.add(in.readLine().substring(6));
		block.add(in.readLine().substring(9));
		block.add(in.readLine().substring(8));
	
		cont.add(block);
		
		block = new ArrayList<String>();
		in.readLine();

		int line = 1;
		s = "";
	 	
		
		while((cur = in.readLine()) != null) {
	 		
			if(line <= 6) {
	 			block.add(new String(cur));
	 			line++;
			}
			else {
				cont.add(block);
				block = new ArrayList<String>();
	 			line = 1;
			}
		}
	
		in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return cont;
	}
}

//'UIView' is where all the screen output is displayed. 
class UIView extends JPanel{
	
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
		centerPane.removeAll();
		controller = new SwingController();

		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		centerPane.add(viewerComponentPanel);
		displayStatusUpdate(" ", false);
		controller.openDocument(outputPath);
	}
}

public class GUI extends JFrame{
	public GUI(){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		final UIView outputArea = new UIView();
		UIMiddleLayer data = new UIMiddleLayer(outputArea);
		UIController ui = new UIController(new Dimension(size.width/5, size.height/3), data);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(outputArea, BorderLayout.CENTER);
		c.add(ui, BorderLayout.EAST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				outputArea.closePdfFile();
				new File("files/temp.pdf").delete();
			}
		});
	
		setVisible(true);
	}
	
	public static void main(String args[]){
		GUI gui = new GUI();
		
	}
}
