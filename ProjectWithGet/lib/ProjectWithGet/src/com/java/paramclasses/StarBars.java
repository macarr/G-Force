/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

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
