package com.tab2pdf.cse2311;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.itextpdf.text.DocumentException;

class UIModel{
	public InputConverter data = new InputConverter("", "files/output.txt");

	public UIModel(UIView subject){
		this.data.subject = subject;
	}
	public int loadFile(){
		JFileChooser fC = new JFileChooser();
		int status = 0;

		int returnVal = fC.showOpenDialog(data.subject);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			data.inputPath = fC.getSelectedFile().toString();
			data.contents = inputConverter(data.inputPath);
			data.subject.showAsciiFile(data.contents);
			status = 1;
		}
		return status;
	}

	public void convertFile(String fontName, float fontSize, float spacing){
		ArrayList<ArrayList<String>> fileContents = new ArrayList<ArrayList<String>>();
		//System.out.println(contents.size());

		for(int blockNum = 1; blockNum < data.contents.size(); blockNum++){
			fileContents.add(data.contents.get(blockNum));
		}
		try {
			new PdfMaker(data.contents.get(0), fileContents, data.outputPath, fontName, fontSize, spacing).createPDF();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		data.subject.PdfRenderer(data.outputPath);
	}



	
}