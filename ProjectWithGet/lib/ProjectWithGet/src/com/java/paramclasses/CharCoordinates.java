/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

package com.java.paramclasses;

public class CharCoordinates{
	public float xPos;
	public float yPos;
	public float rightMargin;
	public float lastNumXPos;
	public float lastNumYPos;
	
	public CharCoordinates(float xPos, float yPos, float rightMargin, float lastNumXPos, float lastNumYPos){
		this.xPos = xPos;
		this.yPos = yPos;
		this.rightMargin = rightMargin;
		this.lastNumXPos = lastNumXPos;
		this.lastNumYPos = lastNumYPos;
	}
}
