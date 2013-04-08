/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

package com.java.paramclasses;

public class TextDetails {
	public String fontName;
	public float fontSize;
	public float spacing;
	
	public TextDetails(String fontName, float fontSize, float spacing){
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.spacing = spacing;
	}
}
