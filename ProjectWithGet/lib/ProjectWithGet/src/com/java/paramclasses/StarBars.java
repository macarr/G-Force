package com.java.paramclasses;

import java.util.ArrayList;

public class StarBars{
	public ArrayList<Integer> starIndexes;
	public int barFreq; 
	public int repIndex; 
	public String repNum;
	
	public StarBars(ArrayList<Integer> starIndexes, int barFreq, int repIndex, String repNum){
		this.starIndexes = starIndexes;
		this.barFreq = barFreq;
		this.repIndex = repIndex;
		this.repNum = repNum;
	}
}
