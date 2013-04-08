package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest2 {

InputParser input3 = new InputParser("./TestCases/input2test.txt");
	
	//Expected outcome
	String test3ExpectTitle = "Not given";
	String test3ExpectSubtitle = "Not given";
	ArrayList<ArrayList<String>> test3Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
		ArrayList<String> test3ExpectSub = new ArrayList<String>();	
		test3ExpectSub.add("|-------------------------|-------------------------|");
		test3ExpectSub.add("|-----1-----1-----1-----1-|-----1-----1-----1-----1-|");
		test3ExpectSub.add("|---2-----2-----2-----2---|---2-----2-----2-----2---|");
		test3ExpectSub.add("|-2-----2-----2-----2-----|-2-----2-----2-----2-----|");
		test3ExpectSub.add("|-0-----------------------|-------------------------|");
		test3ExpectSub.add("|-------------------------|-3-----------------------|");
		
		test3Expect.add(test3ExpectSub);
	}

	@Test
	public void testGetTitle() {
		String input3Title = input3.getTitle();
		assertEquals(test3ExpectTitle, input3Title);
	}

	@Test
	public void testGetSubtitle() {
		String input3Subtitle = input3.getSubtitle();
		assertEquals(test3ExpectSubtitle, input3Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input3Data = input3.getData();
		assertEquals(test3Expect, input3Data);
	}
	
	
}
