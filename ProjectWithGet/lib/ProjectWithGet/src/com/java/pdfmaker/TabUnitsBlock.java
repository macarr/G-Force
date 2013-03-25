package com.java.pdfmaker;

import java.util.ArrayList;

public class TabUnitsBlock{
	ArrayList<TabUnitStats> blockOfTabUnits;
	ArrayList<String> block;
	ArrayList<Integer> starIndexes;
	TabUnitStats unit;
	
	public TabUnitsBlock(ArrayList<String> block, float spacing){
		this.block = block;
		setUnitStats(block.get(1), spacing);
		setStarIndexes();
	}
	
	public ArrayList<Integer> getStarIndexes(){
		ArrayList<Integer> listCopy = new ArrayList<Integer>();
		
		for(int i = 0; i < starIndexes.size(); i++){
			listCopy.add(starIndexes.get(i));
			//System.out.println(starIndexes.get(i));
		}
		
		return listCopy;
	}
	
	private void setStarIndexes(){
		starIndexes = new ArrayList<Integer>();
		
		String line = block.get(2);
		
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == '*'){
				starIndexes.add(new Integer(i));
			}
		}
	}
	
	public void setUnitStats(String source, float spacing) {
		blockOfTabUnits = new ArrayList<TabUnitStats>();
		int numOfBlanks = 0;

		for(int ind = 0; ind < source.length(); ind++) {
			if(source.charAt(ind) == ' '){
				numOfBlanks++;
			}
			
			else if(source.charAt(ind) == '|'){

				//'indexRep' represents the index of the repetition-value. An index of -1 is initially being assigned, which would be 
				//replaced by the actual index value if a repetition-value is found. If no repetition value is found, a -1 would indicate that
				//when a check would be done.
				int indexRep = -1;
				//char repVal = '0';
				String repVal = "";

				if(Character.isDigit(block.get(0).charAt(ind))){

					//The index of the repeat-value is being stored.
					indexRep = ind;
					repVal += block.get(0).charAt(ind);
				}

				//We have come across the first '|' of the unit, so freq is 1 so far.
				int freq = 1;

				//We need to keep track of the index of the first '|' in each DIndexFreq, which is assigned to temp because ind would increase
				//in the next for loop.
				int temp = ind;
				ind++;

				//Calculating frequency of each occurrence of '|', as well as number of repeats
				for(; ind < source.length() && source.charAt(ind) == '|'; ind++){

					//If there is a repeat, the repeat-number is in the first line.
					if(Character.isDigit(block.get(0).charAt(ind))){

						//The index of the repeat-value is being stored.
						indexRep = ind;
						repVal += block.get(0).charAt(ind);
					}
					freq++;
				}

				unit = new TabUnitStats(temp, freq, indexRep, new String(repVal));
				blockOfTabUnits.add(unit);
				repVal = "";
			}
		}

		//If the first character is not '|', we have to explicitly tell the program where to start the block.
		if(source.charAt(0) != '|'){
			unit = new TabUnitStats(0, 0, -1, "0");
			blockOfTabUnits.add(0, unit);
		}

		//If the last index is not '|', we have to explicitly tell the program where to end the block.
		if(source.charAt(source.length() - 1) != '|'){
			unit = new TabUnitStats(source.length() - 1, 0, -1, "0");
			blockOfTabUnits.add(unit);
		}

		for(int ind = 0; ind < blockOfTabUnits.size()-1; ind++){
			blockOfTabUnits.get(ind).setHorizontalSpaceNeeds((((blockOfTabUnits.get(ind + 1).getBeginningBarIndex() + 
					blockOfTabUnits.get(ind + 1).getBeginningBarFreq())-(blockOfTabUnits.get(ind).getBeginningBarIndex()) - 
					numOfBlanks)*spacing) - ((blockOfTabUnits.get(ind).getBeginningBarFreq()-1)*5) - 
					((blockOfTabUnits.get(ind+1).getBeginningBarFreq()-1)*5));
		}
	}
	
	public TabUnitStats getUnitStats(int unitIndex){
		TabUnitStats unitStats = null;
		
		if(unitIndex < blockOfTabUnits.size()){
			unitStats = blockOfTabUnits.get(unitIndex);
		}
		
		return unitStats;
	}
	
	public int getSize(){
		return blockOfTabUnits.size();
	}
	
	public int getBlockSize(){
		return block.size();
	}
	
	public String get(int lineNum){
		return block.get(lineNum);
	}
}
