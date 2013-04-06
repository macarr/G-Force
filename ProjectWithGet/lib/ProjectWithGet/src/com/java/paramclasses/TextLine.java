/**
 * This class acts as a replacement for long parameter lists. Ideally there should be accessor and mutator methods. However, due to
 * time constraints that goal was not achievable and, therefore, the member fields have been made public.
 */

package com.java.paramclasses;

public class TextLine {
	public int lineNum;
	public String line;
	public int charNum;
	
	public TextLine(int lineNum, String line, int charNum){
		this.lineNum = lineNum;
		this.line = line;
		this.charNum = charNum;
	}
}

