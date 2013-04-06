package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;
import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest10 {

InputParser input10 = new InputParser("./TestCases/input10test.txt");
	
	String test10ExpectTitle = "MAN";
	String test10ExpectSubtitle = "CA";
	ArrayList<ArrayList<String>> test10Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
ArrayList<String> test10ExpectSub = new ArrayList<String>();		
		
		test10ExpectSub.add("a------------------------------<12>----------");
		test10ExpectSub.add("a-<12>------------------------------<12>-----");
		test10ExpectSub.add("d------<5>-----------<7>---------------------");
		test10ExpectSub.add("g----------------<7>-------------------------");
		test10ExpectSub.add("b--------------------------------------------");
		test10ExpectSub.add("e--------------------------------------------");

				
		test10Expect.add(test10ExpectSub);
		test10ExpectSub = new ArrayList<String>();
				
		test10ExpectSub.add("------------------------------<12>----------a");
		test10ExpectSub.add("-<12>------------------------------<12>-----d");
		test10ExpectSub.add("------<5>-----------<7>--------------------*b");
		test10ExpectSub.add("----------------<7>------------------------*g");
		test10ExpectSub.add("--------------------------------------------e");
		test10ExpectSub.add("--------------------------------------------a");

				
		test10Expect.add(test10ExpectSub);
		test10ExpectSub = new ArrayList<String>();
		
	}
/*
	@Test
	public void testReadFile() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testGetTitle() {
		String input10Title = input10.getTitle();
		assertEquals(test10ExpectTitle, input10Title);
	}

	@Test
	public void testGetSubtitle() {
		String input10Subtitle = input10.getSubtitle();
		assertEquals(test10ExpectSubtitle, input10Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input10Data = input10.getData();	
		assertEquals(test10Expect, input10Data);
	}
/*
	@Test
	public void testGetStartLineNum() {
		fail("Not yet implemented");
	}
*/
	

}
