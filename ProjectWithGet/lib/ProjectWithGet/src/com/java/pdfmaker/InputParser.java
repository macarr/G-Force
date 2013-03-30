package com.java.pdfmaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class InputParser {

	String title = "";
	String subtitle = "";
	ArrayList<ArrayList<String>> contents;
	ArrayList<ArrayList<String>> contentsCopy;
	int lineNum = 0;
	String regex = "((\\|+|[EBGDA0-9ebgda-]|-).*(\\|*|-*)(.+)(-|\\|+|[A-Z0-9a-z]))";
	ArrayList<String> block;
	String errorLog = "";
	
	
	public InputParser(String inputPath)
	{
		String osVersion = System.getProperty("os.name");
		//String errorLog;
		if(osVersion.startsWith("Windows"))
			errorLog = ""+System.getenv("TEMP")+"/T2PDFErr.txt";
		else
			errorLog = "/tmp/T2PDFErr.txt";
		
		block = new ArrayList<String>();
		contents = new ArrayList<ArrayList<String>>();
		readFile(inputPath);
	}
	
	public void readFile(String inputPath){
		try{
		
			//Input file stream.
			BufferedReader in = new BufferedReader(new FileReader(inputPath));
			
			String current = "";
			
			//The output stream for error-log file
			BufferedWriter out = new BufferedWriter(new FileWriter(errorLog));
			
			current = in.readLine();
			
			//If file is empty then return.
			if(current == null){
				out.append("File is empty.");
				out.close();
				return;
			}
			
			//Else trim() is called.
			else{
				current = current.trim();
			}
			
			lineNum++;
			
			//Pass all empty lines.
			while(current != null && current.equals("")) {
				current = in.readLine();
				
				if(current != null){
					current = current.trim();
				}
			}
			
			//If file has ended at this point then we have insufficient data to show.
			if(current == null){
				out.append("Insufficient data in file.");
				out.close();
				return;
			}
			
			//Else trim() is called.
			else {
				current = current.trim();
			}
			
			lineNum++;
			
			//If the string 'title' is present. 
			if(current.toLowerCase().startsWith("title")){
				title = current.substring(6);
			}
			
			//Otherwise, if line does not start with '|' or '-'.
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' ){
				title = current;
			}
			
			//Otherwise, if line starts with '|' or '-'.
			else if(current.charAt(0) == '|' || current.charAt(0) == '-'){
				title = "Not given";
				block.add(current);
			}
					
			//Read the next line.
			current = in.readLine();
			
			//If file has come to an end at this point then return.
			if(current == null){
				out.append("Insufficient data in file.");
				out.close();
				return;
			}
			
			//Otherwise trim() is called.
			else {
				current = current.trim();
			}
			
			lineNum++;
			
			//Pass over all empty lines.
			while(current != null && current.equals("")) {	
				current = in.readLine();
				
				if(current != null){
					current = current.trim();
				}
				lineNum++;
			}
			
			//If file has come to an end at this point then return.
			if(current == null){
				out.append("Insufficient data in file.");
				out.close();
				return;
			}
			
			//Otherwise trim() is called.
			else {
				current = current.trim();
			}
			
			lineNum++;
			
			//If the string 'subtitle' is present.
			if(current.toLowerCase().startsWith("subtitle")){
				subtitle = current.substring(9);
			}
			
			//Otherwise, if line does not start with '|' or '-'.
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' ){
				subtitle = current;
			}
			
			//Otherwise, if line starts with '|' or '-'.
			else if(current.charAt(0) == '|' || current.charAt(0) == '-'){
				subtitle = "Not given";
				block.add(current);
			}
			
			
			//Read the next line.
			current = in.readLine();
			
			//If file has come to an end at this point then return.
			if(current == null){
				out.append("Insufficient data in file.");
				out.close();
				return;
			}
			
			//Otherwise trim() is called.
			else {
				current = current.trim();
			}	
			
			//At this point we skip over all lines that are not music
			while(current != null && !current.matches(regex)){
				current = in.readLine();
				
				lineNum++;
				
				if(current != null){
					current = current.trim();
				}
			}
			
			
			
			while(current != null) {
				//If music is found.
				if (current.matches(regex) && current.contains("-")){
		 			//If current block size is 0, simply add the music.
					if(block.size() == 0){
		 				block.add(current);
		 			}
		 			
					//Else, if last line of block is of the same length as current.
					else if(current.length() == block.get(block.size()-1).length()){
		 				//If both current and the last line on block begin with alphabet.
						if(Character.isAlphabetic(current.charAt(0)) && 
		 						Character.isAlphabetic(block.get(block.size() - 1).charAt(0))){
		 						block.add(current);
		 				}
						
						//Else, if both current and last line of block start with the same character. 
		 				else if(current.charAt(0) == block.get(block.size() - 1).charAt(0)){
		 					block.add(current);
		 				}
		 			}
		 			
		 			//If current is of different length from the last line of block.
					else{
		 				contents.add(block);
		 				block = new ArrayList<String>();
		 				block.add(current);
		 			}
				}
				
				//Else, if empty-line is found.
				else if(current.equals("")){
					//If block is not empty.
					if(block.size() > 0){
						contents.add(block);
						block = new ArrayList<String>();
					}
	 			}
				
				//Otherwise, error data is written because current did not match any music.
				else{
					out.append("Line-number " + lineNum + " \"(" + current + "\") could not be extracted.");
					out.newLine();
				}
				
				//Next line from the file is read.
				current = in.readLine();
				if(current != null){
					current = current.trim();
				}
				lineNum++;
			}
			
			//After exiting the last loop, if the block is found non-empty then it is added to contents.
			if(!block.isEmpty()){
				this.contents.add(block);
			}
			
			//contentCopy will hold only the elements from contents whose size is 6.
			contentsCopy = new ArrayList<ArrayList<String>>();
			
			for(int i = 0; i < contents.size(); i++){
				//If the size of any element of contents is not 6, it is not copied.
				if(!(contents.get(i).size() < 6) && !(contents.get(i).size() > 6)){
					contentsCopy.add(contents.get(i));
				}
			}
			
			//Once the needed elements from contents (the ones with size 6) have been copied over to contentsCopy,
			//contentsCopy is assigned to contents, which now holds only the elements whose size is 6.
			contents = contentsCopy;
			contentsCopy = null;
						
			//The input and output streams are closed.
			in.close();
			out.close();
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//Returns the title.
	public String getTitle()
	{
		return this.title;
	}
	
	//Returns the subtitle.
	public String getSubtitle()
	{
		return this.subtitle;
	}
	
	//Returns the contents.
	public ArrayList<ArrayList<String>> getData()
	{
		return this.contents;
	}
}
