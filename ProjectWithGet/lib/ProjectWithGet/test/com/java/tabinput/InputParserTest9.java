package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest9 {

InputParser input5 = new InputParser("./TestCases/input9test.txt");
	
	String test5ExpectTitle = "Hahhaa";
	String test5ExpectSubtitle = "CA";
	ArrayList<ArrayList<String>> test5Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
		ArrayList<String> test5ExpectSub = new ArrayList<String>();		
		
		test5ExpectSub.add("a------------------------------<12>----------b");
		test5ExpectSub.add("a-<12>------------------------------<12>-----g");
		test5ExpectSub.add("d------<5>-----------<7>--------------------*e");
		test5ExpectSub.add("g----------------<7>------------------------*b");
		test5ExpectSub.add("b--------------------------------------------a");
		test5ExpectSub.add("e--------------------------------------------g");

				
		test5Expect.add(test5ExpectSub);
	}

	@Test
	public void testGetTitle() {
		String input5Title = input5.getTitle();
		assertEquals(test5ExpectTitle, input5Title);
	}

	@Test
	public void testGetSubtitle() {
		String input5Subtitle = input5.getSubtitle();
		assertEquals(test5ExpectSubtitle, input5Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input5Data = input5.getData();	
		assertEquals(test5Expect, input5Data);
	}


}
