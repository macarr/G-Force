/**
 * This class reads and parses input from an input file containing guitar tabs.
 */
package com.java.tabinput;

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
	ArrayList<ArrayList<String>> contents;	//Holds all the blocks read during input.
	ArrayList<ArrayList<String>> validContents;	//Holds only the blocks that have 6 lines.
	ArrayList<Integer> lineNumBlocks;	//holds the starting line of every block within input file.
	ArrayList<Integer> validLineNums;	//holds the starting line of valid blocks within input file.
	int lineNum = 0;	
	String regex = "((\\|+|[EBGDA0-9ebgda-]|-).*(\\|*|-*)(.+)(-|\\|+|[A-Z0-9a-z]))";	//The regular expression.
	ArrayList<String> block;	//The block where inputs are stored.
	String summarizedErrorLog = "summarized_log.txt";	//file name for short error log.
	String extendedErrorLog = "extended_log.txt";		//file name for full error log.
	
	/**
	 * InputParser constructor.
	 */
	public InputParser(String inputPath){
		block = new ArrayList<String>();
		contents = new ArrayList<ArrayList<String>>();
		validContents = new ArrayList<ArrayList<String>>();
		lineNumBlocks = new ArrayList<Integer>();
		validLineNums = new ArrayList<Integer>();
		readFile(inputPath);
	}
	
	/**
	 * This method reads the input file and places the lines in the appropriate block. 
	 */
	public void readFile(String inputPath){
		try{
		
			//Input file stream.
			BufferedReader in = new BufferedReader(new FileReader(inputPath));
			
			String current = "";
			
			//The output streams for the error-log files.
			BufferedWriter out = new BufferedWriter(new FileWriter(summarizedErrorLog));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(extendedErrorLog));
			
			
			current = in.readLine();
			
			//Pass all empty lines.
			while(current != null && current.trim().equals("")) {
				lineNum++;
				current = in.readLine();
			}
			
			//If file has ended at this point.
			if(current == null){
				out.append("File is empty.");
				out1.append("File is empty.");
				out.close();
				out1.close();
				return;
			}
			
			//Else trim() is called.
			else {
				lineNum++;
				current = current.trim();
			}
			
			//If the string 'title' is present. 
			if(current.toLowerCase().startsWith("title")){
				title = current.substring(6);
			}
			
			//Otherwise, if line does not start with '|' or '-'.
			else if(!current.matches(regex)){
				title = current;
			}
			
			//Otherwise, if line starts with '|' or '-'.
			else if(current.matches(regex)){
				title = "Not given";
				block.add(current);
				lineNumBlocks.add(new Integer(lineNum));
			}
					
			//Read the next line.
			current = in.readLine();
			
			//Pass all empty lines.
			while(current != null && current.trim().equals("")) {
				lineNum++;
				current = in.readLine();
			}
			
			//If file has ended at this point.
			if(current == null){
				out.append("Only title found.");
				out1.append("Only title found.");
				out.close();
				out1.close();
				return;
			}
			
			//Else trim() is called.
			else {
				lineNum++;
				current = current.trim();
			}
			
			//If the string 'subtitle' is present.
			if(current.toLowerCase().startsWith("subtitle")){
				subtitle = current.substring(9);
			}
			
			//Otherwise, if line does not start with '|' or '-'.
			else if(!current.matches(regex)){
				subtitle = current;
			}
			
			//Otherwise, if line starts with '|' or '-'.
			else if(current.matches(regex)){
				subtitle = "Not given";
				block.add(current);
				lineNumBlocks.add(new Integer(lineNum));
			}
			
			//Read the next line.
			current = in.readLine();
			
			//Pass all empty lines.
			while(current != null && current.equals("")) {
				lineNum++;
				current = in.readLine();
			}
			
			//If file has come to an end at this point then return.
			if(current == null){
				out.append("Only title and subtitle found.");
				out1.append("Only title and subtitle found.");
				out.close();
				out1.close();
				return;
			}
			
			//Else trim() is called.
			else {
				lineNum++;
				current = current.trim();
			}
			
			
			//At this point we skip over all lines that are not music.
			while(current != null && !current.matches(regex)){
				current = in.readLine();
				
				if(current != null){
					lineNum++;
					current = current.trim();
				}
			}
			
			while(current != null) {
				//If music is found.
				if (current.matches(regex) && current.contains("-")){
		 			//If current block size is 0, simply add the music.
					if(block.size() == 0){
		 				block.add(current);
		 				lineNumBlocks.add(new Integer(lineNum));
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
		 				lineNumBlocks.add(new Integer(lineNum));
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
					//First adding the block to contents.
	 				if(block.size() > 0){
	 					contents.add(block);
	 					block = new ArrayList<String>();
	 				}
	 				
					//Error data is written to the extended error-log.
	 				out1.append("Line-number " + lineNum + " (\"" + current + "\") was not recognized as valid input. Please refer to the documentation for valid input format.");
					out1.newLine();
				}
				
				//Next line from the file is read.
				current = in.readLine();
				if(current != null){
					current = current.trim();
					lineNum++;
				}
			}
			
			//After exiting the loop, if the block is found non-empty then it is added to contents.
			if(!block.isEmpty()){
				this.contents.add(block);
			}
			
			//Blocks with size less than or greater than 6 are copied to validContents.
			for(int i = 0; i < contents.size(); i++){
				//If the size of any element of contents is not 6, it is not copied.
				if(!(contents.get(i).size() < 6) && !(contents.get(i).size() > 6)){
					validContents.add(contents.get(i));
					validLineNums.add(lineNumBlocks.get(i));
					
				}
				//If the number of lines in a dropped block is only 1, the information goes into the extended log only.
				else if(contents.get(i).size() == 1){ 
					out1.append("The block comprising the single line (\"" + contents.get(i).get(0) + "\") on line " +
							lineNumBlocks.get(i).toString() + " was dropped due to incompitable number of lines during input.");
					out1.newLine();
				}
				//If the number of lines in a dropped block is between 1 and 3, the information goes into the extended log only.
				else if(contents.get(i).size() > 1 && contents.get(i).size() <= 3){
					out1.append("The block starting with (\"" + contents.get(i).get(0) + "\")" + " and ending with (\"" +
							contents.get(i).get(contents.get(i).size() - 1) + "\") on lines (" + lineNumBlocks.get(i).toString() +
							"-" + (lineNumBlocks.get(i).intValue() + contents.get(i).size()-1) + ") were dropped due to incompatible number of lines during input.");
							
					out1.newLine();
				}
				//If the number of lines in a dropped block is greater than 3, the information goes into both the summarized and extended logs.
				else if(contents.get(i).size() > 3){
					//Writing to summarized log.
					out.append("The block starting with (\"" + contents.get(i).get(0) + "\")" + " and ending with (\"" +
							contents.get(i).get(contents.get(i).size() - 1) + "\") on lines (" + lineNumBlocks.get(i).toString() +
							"-" + (lineNumBlocks.get(i).intValue() + contents.get(i).size()-1) + ") were dropped due to incompatible number of lines during input.");
							
					out.newLine();
					
					//Writing to extended log.
					out1.append("The block starting with (\"" + contents.get(i).get(0) + "\")" + " and ending with (\"" +
							contents.get(i).get(contents.get(i).size() - 1) + "\") on lines (" + lineNumBlocks.get(i).toString() +
							"-" + (lineNumBlocks.get(i).intValue() + contents.get(i).size()-1) + ") were dropped due to incompatible number of lines during input.");
							
					out1.newLine();
				}
			}
			
			//At the end if validContents has size 0, the appropriate information is displayed.  
			if(validContents.size() == 0){
				out.write("The contents found in the input file were insufficient to be displayed.");
				out1.write("The contents found in the input file were insufficient to be displayed.");
			}
			
			//The input and output streams are closed.
			in.close();
			out.close();
			out1.close();
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns the title.
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Returns the subtitle.
	 */
	public String getSubtitle(){
		return subtitle;
	}
	
	/**
	 * Returns the valid contents.
	 */
	public ArrayList<ArrayList<String>> getData(){
		return validContents;
	}
	
	/**
	 * Returns the starting line number of a valid block within the input file.
	 */
	public int getStartLineNum(int index){
		return validLineNums.get(index);
	}
}
