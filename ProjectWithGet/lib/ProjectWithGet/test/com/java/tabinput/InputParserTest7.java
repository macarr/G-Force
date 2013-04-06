package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;
import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest7 {

InputParser input7 = new InputParser("./TestCases/input7test.txt");
	
	String test7ExpectTitle = "DDu";
	String test7ExpectSubtitle = "Not given";
	ArrayList<ArrayList<String>> test7Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
ArrayList<String> test5ExpectSub = new ArrayList<String>();		
		
		test5ExpectSub.add("|-------------------------|-------------------------|");
		test5ExpectSub.add("|-----1-----1-----1-----1-|-----1-----1-----1-----1-|");
		test5ExpectSub.add("|---2-----2-----2-----2---|---2-----2-----2-----2---|");
		test5ExpectSub.add("|-2-----2-----2-----2-----|-2-----2-----2-----2-----|");
		test5ExpectSub.add("|-0-----------------------|-------------------------|");
		test5ExpectSub.add("|-------------------------|-3-----------------------|");

				
		test7Expect.add(test5ExpectSub);
	}
/*
	@Test
	public void testReadFile() {
		fail("Not yet implemented");
	}

*/
	@Test
	public void testGetTitle() {
		String input7Title = input7.getTitle();

		assertEquals(test7ExpectTitle, input7Title);
	}

	@Test
	public void testGetSubtitle() {
		String input7Subtitle = input7.getSubtitle();

		assertEquals(test7ExpectSubtitle, input7Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input7Data = input7.getData();

		assertEquals(test7Expect, input7Data);
	}
/*
	@Test
	public void testGetStartLineNum() {
		fail("Not yet implemented");
	}
*/
	

}
