/**
 * TabFileManager takes care of the File input/output and miscellaneous tasks.
 */

package com.java.tabui;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.itextpdf.text.Rectangle;
import com.java.paramclasses.ErrorInformation;
import com.java.paramclasses.TextDetails;
import com.java.tabinput.InputParser;
import com.java.tabpdf.TabToPdfConverter;

public class TabFileManager{
   
	public TabUIViewPane outputArea;	//outputArea is the TabUIViewPane where the screen output takes place.
	String inputPath;					//String inputPath holds the input file path, which is initially empty.
	String outputPath;					//The temporary output path.
	String destinationPath = "";		//If the user decides to save the file, the file-path would be stored in destinationPath.
	private InputParser in;
	int pageWidth=612;					//Width of the pdf document's pages, which is the standard paper-width.
	int pageHeight=792;					//Height of the pdf document's pages, , which is the standard paper-height.
	
	ErrorInformation errInfo;			//To hold the returned reference from Tab2PdfConverter's createPdf() method.

	/**
	 * The TabFileManager constructor.
	 */
	public TabFileManager(TabUIViewPane outputArea){
		this.outputArea = outputArea;
		outputPath = getTempDir();
	}

	/**
	 * loadFile displays the JFileChooser which allows the user to choose the input file.  
	 */
	public int loadFile(){
		System.gc();
		outputArea.displayStatusUpdate(" ", false);
		//'status' is a flag which is returned to the caller of the loadFile, based on which the caller method enables/disables 
		//some buttons.  
		
		//Means the user has not opened any file yet.
		int status = -1;
		
		JFileChooser fC;
		
		if(inputPath == null){
			fC = new JFileChooser();
		}
		else{
			fC = new JFileChooser(inputPath);
		}
		
		int returnVal = fC.showOpenDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			//Clear out any previous error information.
			errInfo = null;
			
			//Clear out any previous display information.
			outputArea.clearCenterPane();
			
			//The status = 0 means the user has opened a file.
			status = 0;
			inputPath = fC.getSelectedFile().toString();
	
			in = new InputParser(inputPath);
	
			//If the file the user opened has enough informatin in it to show.
			if(in.getData().size() > 0){
				//The Ascii file is displayed on the screen.
				outputArea.showAsciiFile(in);
				status = 1;
			}
		}
		return status;
	}

	/**
	 * saveFile allows the user to save a file by displaying a file-saver dialog-box.
	 */
	public String saveFile(final String fontName, final float fontSize, final float spacing) {
		JFileChooser fc;
		if(destinationPath == null)
			fc = new JFileChooser();
		else
			fc = new JFileChooser(destinationPath);

		//Creating a file with the same name as the title.
		File file = new File(in.getTitle() + ".pdf");
		
		fc.setSelectedFile(file);
		int returnVal = fc.showSaveDialog(outputArea);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			destinationPath = fc.getSelectedFile().toString();
	
			//To display the status update.
			outputArea.displayStatusUpdate("Saving Pdf File...", false);
			Thread PdfMakerThread = new Thread(){
				public void run(){
					//Creating the Tab2PdfConverter object.
					new TabToPdfConverter(in, new Rectangle(pageWidth, pageHeight), destinationPath, new TextDetails(fontName, fontSize, spacing)).createPDF();
					outputArea.displayStatusUpdate("File Saved.", false);
				}
			};
	
			PdfMakerThread.start();
		}
		
		//Cleaning up any temporary file.
		if(file.exists()){
			file.delete();
		}
		
		return destinationPath;
	}
	
	/**
	 * Returns some Operating System dependent information.
	 */
	public static String getTempDir() {
		String osVersion = System.getProperty("os.name");
		String temp;
		if(osVersion.startsWith("Windows"))
			temp = ""+System.getenv("TEMP")+"/";
		else
			temp = "/tmp/";
		return temp;
	}
	
	/**
	 * convertFile converts the Ascii file into Pdf Format and displays it in the 'outputArea'.
	 */
	public void convertFile(final String fontName, final float fontSize, final float spacing){
		//Before converting, any previous instance of the Pdf renderer is closed.
		outputArea.closePdfFile();
		System.gc();
		//First making the outputArea's status-label blank
		outputArea.displayStatusUpdate(" ", false);

		//Before we begin to convert the Ascii file into Pdf, we notify the user.
		outputArea.displayStatusUpdate("Converting to Pdf...", true);

		//We use a Thread for the conversion.
		Thread pdfMakerThread = new Thread(){
			public void run(){
				File tempFolder = new File(outputPath);
				if(!tempFolder.isDirectory()){
					tempFolder.mkdir();
				}

				//The createPdf method of Tab2PdfConverter returns an object that carries information about how successful the Pdf
				//conversion was.
				errInfo = new TabToPdfConverter(in, new Rectangle(pageWidth, pageHeight), outputPath + "temp.pdf", new TextDetails(fontName, fontSize, spacing)).createPDF();

				//The showPdfFile method of the TabUIViewPane class takes in the outputPath (The location where the temporary Pdf file is stored).
				outputArea.showPdfFile(outputPath + "temp.pdf");

				//If any of the music could not fit on the page, we need to notify the user through a dialog-box.
				if(!errInfo.flag){
					JOptionPane.showMessageDialog(outputArea, "Some music might not have fully fit on the Pdf document due to size. Please check the error log for more details.", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};

		pdfMakerThread.start();
	}
	
	/**
	 * Returns any error information generated during Pdf conversion. 
	 */
	public ArrayList<String> getErrorInfo(){
		ArrayList<String> returnVal = null;
		
		if(errInfo != null){
			returnVal = new ArrayList();
			
			//Copying the required information.
			for(int i = 0; i < errInfo.info.size(); i++){
					returnVal.add(errInfo.info.get(i));
			}
		}
		
		//Returning the copy.
		return returnVal;
	}
	
	/**
	 * Returns the InputParser.
	 */
	public InputParser getIn(){
		return in;
	}
}
