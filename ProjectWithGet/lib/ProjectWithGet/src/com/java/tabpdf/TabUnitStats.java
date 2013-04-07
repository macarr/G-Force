/**
 * This class receives as input its own information, and simply stores them. The information it stores are its beginning index within
 * the TabUnitsBlock, the frequency of its beginning bars, the number of repeats (0 in case of no repeat), the index of the
 * repeat-value within the TabUnitsBlck (-1 when there is no repeat), and the horizontal space requirements in points for this unit.
 */
package com.java.tabpdf;

public class TabUnitStats{
	private int beginningBarIndex;	//Index of the left bars of this unit (measure).
	private int beginningBarFreq;	//Frequency of the left bars.
	private int repValueIndex;		//Index of the repetition value.
	private String repValue;		//The repetition value.
	private float horizontalSpaceNeeds;		//Width in points of this unit.
	
	/**
	 * TabUnitStats constructor.
	 */
	public TabUnitStats(int beginningBarIndex, int beginningBarFreq, int repValueIndex, String repValue){
		this.beginningBarIndex = beginningBarIndex;
		this.beginningBarFreq = beginningBarFreq;
		this.repValueIndex = repValueIndex;
		this.repValue = repValue;
	}
	
	/**
	 * Returns the beginning bar index of this unit. 
	 */
	public int getBeginningBarIndex(){
		return beginningBarIndex;
	}
	
	/**
	 * Returns the frequency of the beginning bars.
	 */
	public int getBeginningBarFreq(){
		return beginningBarFreq;
	}
	
	/**
	 * Returns the index of the repetition values.	 
	 * */
	public int getRepValueIndex(){
		return repValueIndex;
	}
	
	/**
	 * Returns the reititon value.
	 */
	public String getRepValue(){
		return repValue;
	}
	
	/**
	 * Returns the width in points of this unit.
	 */
	public float getHorizontalSpaceNeeds(){
		return horizontalSpaceNeeds;
	}
	
	/**
	 * Sets the width in points of this unit.
	 */
	public void setHorizontalSpaceNeeds(float horizontalSpaceNeeds){
		this.horizontalSpaceNeeds = horizontalSpaceNeeds;
	}
}