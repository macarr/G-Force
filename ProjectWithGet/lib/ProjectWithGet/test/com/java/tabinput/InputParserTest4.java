package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest4 {

InputParser input5 = new InputParser("./TestCases/input4test.txt");
	
	String test5ExpectTitle = "Remembering Rain";
	String test5ExpectSubtitle = "Jim Matheos";
	ArrayList<ArrayList<String>> test5Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
ArrayList<String> test5ExpectSub = new ArrayList<String>();		
		
		test5ExpectSub.add("||------------------------------<12>----------|||");
		test5ExpectSub.add("||-<12>------------------------------<12>-----|||");
		test5ExpectSub.add("||*-----<5>-----------<7>--------------------*|||");
		test5ExpectSub.add("||*---------------<7>------------------------*|||");
		test5ExpectSub.add("||--------------------------------------------|||");
		test5ExpectSub.add("||--------------------------------------------|||");

				
		test5Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
				
		test5ExpectSub.add("||---------3----------10-----0-------0---------------7-|");
		test5ExpectSub.add("||-----0---------10--------------0-------0-------5s7---|");
		test5ExpectSub.add("||*------2----------0----------2-------2-------0-------|");
		test5ExpectSub.add("||*--------------------------------2-------------------|");
		test5ExpectSub.add("||---2---------------------3---------------------------|");
		test5ExpectSub.add("||-0-----------7-----------------------------0---------|");

				
		test5Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
		test5ExpectSub.add("|-----------15----------10-----0-------0---------------7-||");
		test5ExpectSub.add("|---10-------------10--------------0-------0-------5s7---||");
		test5ExpectSub.add("|------0--------------0----------2-------2-------0------*||");
		test5ExpectSub.add("|--------12--------------------------2------------------*||");
		test5ExpectSub.add("|----------------------------3---------------------------||");
		test5ExpectSub.add("|-0--------------7-----------------------------0---------||");

				
		test5Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
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
