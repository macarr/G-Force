/**
 * This class acts as a replacement when one boolean and one ArrList<String> are returned by a method. Ideally there should be accessor
 * and mutator methods. However, due to time constraints that goal was not achievable and, therefore, the member fields have been made
 * public.
 */

package com.java.paramclasses;

import java.util.ArrayList;

public class ErrorInformation {
	public boolean flag;
	public ArrayList<String> info;
	
	public ErrorInformation(boolean flag, ArrayList<String> info){
		this.flag = flag;
		this.info = info;
	}
}
