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
			int blockNum = 1;

			//Instantiate a character counter that will contain 6 line lengths at a time
			int [] charCount = new int[6];
			charCount[0] = current.length();
	 	
			while(current != null) {
				//Any empty lines and garbage lines (i.e. lines that don't start with '|' are passed over
				if(current.equals("") || current.charAt(0) != '|');
				else if (line > 6)
				{
					//Check if all lengths of the block are the same. 
					if(lengthChecker(charCount))
					{
						this.contents.add(block);
					}
					else
					{
						//TO DO: give error to say that blockNum was not printed since it did 
						//not have equal number of characters on each line. 
						System.out.println("The " + blockNum + "th set of lines were not printed due to a mismatching number of characters on each line.");
					}
					blockNum++;
					block = new ArrayList<String>();
		 			block.add(current);
		 			
					charCount = new int[6];
					charCount[0] = current.length();
		 			line = 2;
				}
				else{
		 			block.add(current);
		 			charCount[line - 1] = current.length();
		 			line++;		 					
				}
				current = in.readLine();
			}
			
			if(!block.isEmpty())
				if(lengthChecker(charCount) && line == 6)
					this.contents.add(block);
				else
					System.out.println("Not proper >:C");
			//Must throw error that says that the music may not have printed properly, and some or all music was not printed...
			//Send users to formatting guidelines (make one) saying that music needs to be in sets of 6 lines!
			
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
	
	private boolean lengthChecker(int[] A)
	{
		for(int i = 1; i < 6; i++)
		{
			if(A[i] != A[0])
				return false;
		}
		
		return true;
	}
	
	/*
	 * Method equalizes the lengths of the array by 
	 * 1)Finding the largest number of characters between each bar
	 * 2)Add characters between bars to make them equal
	 */
	/*
	private ArrayList<String> lengthEqualizer (ArrayList<String> block, int blockNum)
	{
		ArrayList<String> newBlock = new ArrayList<String>();
		
		//Regex consists of a vertical bar and any character following (or not if last character)
		//This is to account for the case where we have repeat signs, and the first character will have
		//a number following a bar where the other lines do not..
		//However, this will be a problem if we have double bars somewhere and not on a line that should...
		String[] tokens = block.get(0).split("|(.?)");
		
		//tokenNum holds the number of bars 
		int tokenNum = tokens.length;
		
		//Array holds the number of characters between each bar
		int[] max = new int[tokens.length];				
		for (int i = 0; i < tokens.length; i++)
		     max[i] = tokens[i].length();
		
		String[][] tokenArray = new String[6][tokens.length];
		tokenArray[0] = tokens;
		
		for (int i = 1; i < block.size(); i++)
		{
			tokens = block.get(i).split("|(.?)");
			
			if(tokens.length != tokenNum)
			{
				//MUST throw error that says that this block does not have equal number of bars
				//Program must ask the user to reformat this bar, and reject this input.
			}
			
			for (int j = 0; j < tokens.length; j++)
			{
				if (tokens[j].length() > max[j])
					max[j] = tokens[j].length();
			}

		}
		
		String line = "";
		String add = "";
		
		for (int j = 0; j < tokenArray.length; j++)
		{
			for(int k = 0; k < tokenArray[j].length; k++)
			{
				if(tokenArray[j][k].length() != max[k])
				{
					for (int i = 1; i <= (max[k] - tokenArray[j][k].length()); i ++)
						add = add + "-";
					line  = line + tokenArray[j][k] + add;
				}
				else
					line = line + tokenArray[j][k];
			}
			newBlock.add(line);
		}
		
		return newBlock;
		
	}
	*/
}