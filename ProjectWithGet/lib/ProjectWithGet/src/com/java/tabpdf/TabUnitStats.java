/**TabUnitStats.java
 * 
 * This class receives as input its own information, and simply stores them. The information it stores are its beginning index within
 * the TabUnitsBlock, the frequency of its beginning bars, the number of repeats (0 in case of no repeat), the index of the
 * repeat-value within the TabUnitsBlck (-1 when there is no repeat), and the horizontal space requirements in points for this unit.
 */
package com.java.tabpdf;

public class TabUnitStats{
	private int beginningBarIndex;
	private int beginningBarFreq;
	private int repValueIndex;
	private String repValue;
	private float horizontalSpaceNeeds;
	
	public TabUnitStats(int beginningBarIndex, int beginningBarFreq, int repValueIndex, String repValue){
		
		this.beginningBarIndex = beginningBarIndex;
		this.beginningBarFreq = beginningBarFreq;
		this.repValueIndex = repValueIndex;
		this.repValue = repValue;
	}
	
	public int getBeginningBarIndex(){
		return beginningBarIndex;
	}
	
	public int getBeginningBarFreq(){
		return beginningBarFreq;
	}
	
	public int getRepValueIndex(){
		return repValueIndex;
	}
	
	public String getRepValue(){
		return repValue;
	}
	
	public float getHorizontalSpaceNeeds(){
		return horizontalSpaceNeeds;
	}
	
	public void setHorizontalSpaceNeeds(float horizontalSpaceNeeds){
		this.horizontalSpaceNeeds = horizontalSpaceNeeds;
	}
}

