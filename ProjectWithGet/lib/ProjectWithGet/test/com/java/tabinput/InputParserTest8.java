package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest8 {

InputParser input7 = new InputParser("./TestCases/input8test.tab");
	
	String test7ExpectTitle = "Haha";
	String test7ExpectSubtitle = "Ca";
	ArrayList<ArrayList<String>> test7Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
ArrayList<String> test7ExpectSub = new ArrayList<String>();		
		
		test7ExpectSub.add("------------------------------<12>----------");
		test7ExpectSub.add("-<12>------------------------------<12>-----");
		test7ExpectSub.add("------<5>-----------<7>---------------------");
		test7ExpectSub.add("----------------<7>-------------------------");
		test7ExpectSub.add("--------------------------------------------");
		test7ExpectSub.add("--------------------------------------------");
	
		test7Expect.add(test7ExpectSub);	
	}

	@Test
	public void testGetTitle() {
		String input5Title = input7.getTitle();
		assertEquals(test7ExpectTitle, input5Title);
	}

	@Test
	public void testGetSubtitle() {
		String input5Subtitle = input7.getSubtitle();
		assertEquals(test7ExpectSubtitle, input5Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input5Data = input7.getData();	
		assertEquals(test7Expect, input5Data);
	}

	

}
