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
	
	public InputParser(String inputPath)
	{
		
		ArrayList<String> block = new ArrayList<String>();
		this.contents = new ArrayList<ArrayList<String>>();
		
		try{
		
			//Input file
			BufferedReader in = new BufferedReader(new FileReader(inputPath));
			
			//Get rid of all preceding empty lines
			String current = "";
			
			//System.out.println(System.getProperty("user.dir"));
			BufferedWriter out = new BufferedWriter(new FileWriter("C:/CSE2311/errlog.txt"));
			
			  
			current = in.readLine();
			
			if(current != null){
				current = current.trim();
			}
			
			lineNum++;
			
			while(current != null && current.equals("")) {
				current = in.readLine().trim();
				
				lineNum++;
			}		
			
			//Reading in the title.
			if(current.toLowerCase().startsWith("title")){
				title = current.substring(6);
			}
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' ){
				title = current;
			}
			else if(current.charAt(0) == '|' || current.charAt(0) == '-'){
				title = "Not given";
				block.add(current);
			}
					
			//Get rid of all empty lines, provided a title was provided
			current = in.readLine();
			
			if(current != null){
				current = current.trim();
			}
			
			lineNum++;
			while(current != null && current.equals("")) {	
				current = in.readLine().trim();
				lineNum++;
			}
			
			//Reading in the subtitle
			if(current.toLowerCase().startsWith("subtitle")){
				subtitle = current.substring(9);
			}
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' ){
				subtitle = current;
			}
			else if(current.charAt(0) == '|' || current.charAt(0) == '-'){
				subtitle = "Not given";
				block.add(current);
			}
			
			current = in.readLine();
			
			if(current != null){
				current = current.trim();
			}
			
			lineNum++;
			
			while(current != null) {
				//Any empty lines and garbage lines (i.e. lines that don't start with '|' are passed over
				//Use for loop all the way until the next blank line
				if (current.matches("((\\|+|[EBGDA0-9ebgda-]|-).*(\\|+|-))(.+)(-|\\|+|[A-Z0-9a-z])") && current.contains("-")){
		 			if(block.size() == 0){
		 				//System.out.println("if: " + current);
		 				block.add(current);
		 			}
		 			else if(current.length() == block.get(block.size()-1).length()){
		 				//System.out.println("else if: " + current);
		 				block.add(current);
		 			}
		 			
		 			else{
		 				//System.out.println("else: " + current);
		 				contents.add(block);
		 				block = new ArrayList<String>();
		 				block.add(current);
		 			}
				}
				
				else if(current.equals("")){
					//System.out.println("other else: ");
					if(block.size() > 0){
						contents.add(block);
						block = new ArrayList<String>();
					}
	 			}
				
				else{
					out.append("Line-number " + lineNum + " \"" + current + "\" could not be read.");
					out.newLine();
				}
				
				current = in.readLine();
				if(current != null){
					current = current.trim();
				}
				lineNum++;
			}
			
			if(!block.isEmpty()){
				this.contents.add(block);
			}
			
			contentsCopy = new ArrayList<ArrayList<String>>();
			
			for(int i = 0; i < contents.size(); i++){
				if(!(contents.get(i).size() < 6) && !(contents.get(i).size() > 6)){
					contentsCopy.add(contents.get(i));
				}
			}
			
			contents = contentsCopy;
			contentsCopy = null;
						
			in.close();
			out.close();
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getSubtitle()
	{
		return this.subtitle;
	}
	
	public ArrayList<ArrayList<String>> getData()
	{
		return this.contents;
	}
}