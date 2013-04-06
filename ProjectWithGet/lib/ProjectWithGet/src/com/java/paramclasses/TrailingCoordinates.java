/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

package com.java.paramclasses;

public class TrailingCoordinates{
	public float margin;
	public float xPos;
	public float yPos;
	
	public TrailingCoordinates(float margin, float xPos, float yPos){
		this.margin = margin;
		this.xPos = xPos;
		this.yPos = yPos;
	}
}
