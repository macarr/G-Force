package com.java.pdfmaker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

class inputParser {

	String title = "";
	String subtitle = "";
	ArrayList<ArrayList<String>> contents;
	
	public inputParser(String inputPath)
	{
		
		ArrayList<String> block = new ArrayList<String>();
		this.contents = new ArrayList<ArrayList<String>>();
		Boolean Btitle = true;
		Boolean Bsubtitle = true;
		
		try{
		
			//Input file
			BufferedReader in = new BufferedReader(new FileReader(inputPath));
			
			//Get rid of all preceding empty lines
			String current = in.readLine();
			while(current.equals(""))
				current = in.readLine();
					
			//Reading in the title.
			if(current.toLowerCase().startsWith("title"))
				this.title = current.substring(6);
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' )
				this.title = current;
			else{
				this.title = "Not given";
				Btitle = false;
				Bsubtitle = false;
			}
					
			//Get rid of all empty lines, provided a title was provided
			if(Btitle == true)
			{
				current = in.readLine();
				while(current.equals(""))
					current = in.readLine();
			}
					
			//Reading in the subtitle
			if(current.toLowerCase().startsWith("subtitle"))
				this.subtitle = current.substring(9);
			else if(current.charAt(0) != '|' && current.charAt(0) != '-' )
				this.subtitle = current;
			else{
				this.subtitle = "Not given";
				Bsubtitle = false;
			}
			
			//Get rid of all empty lines, provided a subtitle was provided
			if(Bsubtitle == true)
			{
				current = in.readLine();
				while(current != null && (current.equals("") || current.charAt(0) != '|' ))
					current = in.readLine();
			}
			
			
			int line = 1;
	 	
			while(current != null) {
				//Any empty lines and garbage lines (i.e. lines that don't start with '|' are passed over
				if(current.equals("") || current.charAt(0) != '|');
				else if (line > 6)
				{
					this.contents.add(block);
					block = new ArrayList<String>();
		 			line = 2;
		 			block.add(current);
				}
				else{
		 			block.add(current);
		 			line++;
				}
				current = in.readLine();
			}
			
			if(!block.isEmpty())
				this.contents.add(block);
			
			in.close();
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