package com.tab2pdf.cse2311;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class InputConverter {
	public UIView subject;
	public String inputPath;
	public String outputPath;
	public ArrayList<ArrayList<String>> contents;

	public InputConverter(String inputPath, String outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public ArrayList<ArrayList<String>> convertInput(){
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