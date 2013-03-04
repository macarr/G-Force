package com.java.pdfmaker;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.itextpdf.text.Rectangle;

//UIMiddleLayer takes care of the File input/output and miscellaneous tasks.
public class UIMiddleLayer{
	//'outputArea' is the 'UIView' where the screen output takes place. 
	public UIView outputArea;
	
	//String 'input' holds the input file path, which is initially empty.
	String inputPath = "";
	
	//The temporary output path.
	String outputPath = "Temp";
	
	//If the user decides to save the file, the file-path would be stored in 'destinationPath'.
	String destinationPath = "";
	
	//'The contents of the input file are stored in the ArrayList 'contents'.
	private ArrayList<ArrayList<String>> contents;
	
	private inputParser in;
	
	private ArrayList<String> header = new ArrayList<String>();
	
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
		
		//'status' is a flag which is returned to the caller of the 'loadFile', based on which, the caller enables some buttons.  
		int status = 0;
		
		int returnVal = fC.showOpenDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			inputPath = fC.getSelectedFile().toString();
			
			if(!(inputPath.substring(inputPath.length()-4).equals(".txt"))) {
				JOptionPane.showMessageDialog(outputArea, ""+inputPath+" is not a .txt file. " +
						"Please select a file with the .txt extension", "Invalid file", JOptionPane.ERROR_MESSAGE);
				return status;
			}
			
			in = new inputParser(inputPath);
			
			header.add(in.getTitle());
			header.add(in.getSubtitle());
			
			//If the user chose a file, that file's name is sent to the method 'inputConverter', which sends back an ArrayList filled with
			//the contents of the file.
			contents = in.getData();
			
			//The Ascii file is displayed on the screen.
			outputArea.showAsciiFile(in);
			
			//'Status' = 1 means that now it is OK to enable the 'convertButton'.
			status = 1;
		}
		return status;
	}
	
	//'saveFile' allows the user to save a file by displaying a file-saver dialog-box.
	public void saveFile(final String fontName, final float fontSize, final float spacing) {
		JFileChooser fc = new JFileChooser();
		
		fc.setSelectedFile(new File("*.pdf"));
		int returnVal = fc.showSaveDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			destinationPath = fc.getSelectedFile().toString();
		}
	
		outputArea.displayStatusUpdate("Saving Pdf File...", false);
		Thread PdfMakerThread = new Thread(){
			public void run(){
				new PdfMaker(header, contents, new Rectangle(pageWidth, pageHeight), destinationPath, fontName, fontSize, spacing).createPDF();
				interrupt();
				outputArea.displayStatusUpdate("File Saved.", false);
			}
		};
		
		PdfMakerThread.start();
	}
	
	//'convertFile' converts the Ascii file into Pdf Format and displays it in the 'outputArea'.
	public void convertFile(final String fontName, final float fontSize, final float spacing, final JButton saveButton, final JButton launchPdfButton){
		
		//First making the outputArea's status-label blank 
		outputArea.displayStatusUpdate(" ", false);
	
		//Before we begin to convert the Ascii file into Pdf, we notify the user.
		outputArea.displayStatusUpdate("Converting to Pdf...", true);
		
		//We use a 'Thread' for the conversion.
		final Thread pdfMakerThread = new Thread(){
			public void run(){
				File tempFolder = new File(outputPath);
				if(!tempFolder.isDirectory()){
					tempFolder.mkdir();
				}
				
				//The 'createPdf' method of 'PdfMaker' returns true if all the music data fit on the screen. Otherwise it returns false.
				boolean fullSuccess = new PdfMaker(header, contents, new Rectangle(pageWidth, pageHeight), outputPath + "/temp.pdf", fontName, fontSize, spacing).createPDF();
				
				
				
				//The 'showPdf' method of the 'UIView' class takes in the outputPath (The location where the temporary Pdf file is stored).
				outputArea.showPdfFile(outputPath + "/temp.pdf");
				
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
		
		pdfMakerThread.start();
	}
}