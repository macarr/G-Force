package com.java.pdfmaker;

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

