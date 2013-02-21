package com.tab2pdf.cse2311;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.itextpdf.text.DocumentException;

class UIModel{
	public UIView subject;
	String inputPath = "";
	String outputPath = "files/output.txt";
	private ArrayList<ArrayList<String>> contents;

	public UIModel(UIView subject){
		this.subject = subject;
	}
	public int loadFile(){
		JFileChooser fC = new JFileChooser();
		int status = 0;

		int returnVal = fC.showOpenDialog(subject);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			inputPath = fC.getSelectedFile().toString();
			contents = inputConverter(inputPath);
			subject.showAsciiFile(contents);
			status = 1;
		}
		return status;
	}

	public void convertFile(String fontName, float fontSize, float spacing){
		ArrayList<ArrayList<String>> fileContents = new ArrayList<ArrayList<String>>();
		//System.out.println(contents.size());

		for(int blockNum = 1; blockNum < contents.size(); blockNum++){
			fileContents.add(contents.get(blockNum));
		}
		try {
			new PdfMaker(contents.get(0), fileContents, outputPath, fontName, fontSize, spacing).createPDF();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		subject.PdfRenderer(outputPath);
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

		//header = block;
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