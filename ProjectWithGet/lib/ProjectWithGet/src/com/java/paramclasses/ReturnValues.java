/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

package com.java.paramclasses;

public class ReturnValues {
	public float xPos;
	public float yPos;
	
	public ReturnValues(float xPos, float yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
}
