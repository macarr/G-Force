package com.java.tabinput;

import static org.junit.Assert.*;
import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest5 {
InputParser input2 = new InputParser("./TestCases/input5test.txt");
	
	String test5ExpectTitle = "HAHA";
	String test5ExpectSubtitle = "ca";
	ArrayList<ArrayList<String>> test5Expect = new ArrayList<ArrayList<String>>();
	
	@Before
	public void testInputParser() {
ArrayList<String> test5ExpectSub = new ArrayList<String>();		
		
		test5ExpectSub.add("------------------------------<12>----------||");
		test5ExpectSub.add("-<12>------------------------------<12>-----||");
		test5ExpectSub.add("------<5>-----------<7>--------------------*||");
		test5ExpectSub.add("----------------<7>------------------------*||");
		test5ExpectSub.add("--------------------------------------------||");
		test5ExpectSub.add("--------------------------------------------||");

				
		test5Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
				
		test5ExpectSub.add("a------------------------------<12>----------||");
		test5ExpectSub.add("a-<12>------------------------------<12>-----||");
		test5ExpectSub.add("d------<5>-----------<7>--------------------*||");
		test5ExpectSub.add("g----------------<7>------------------------*||");
		test5ExpectSub.add("b--------------------------------------------||");
		test5ExpectSub.add("e--------------------------------------------||");

		test5Expect.add(test5ExpectSub);	
	}

	@Test
	public void testGetTitle() {
		String input5Title = input2.getTitle();
		assertEquals(test5ExpectTitle, input5Title);
	}

	@Test
	public void testGetSubtitle() {
		String input5Subtitle = input2.getSubtitle();
		assertEquals(test5ExpectSubtitle, input5Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input5Data = input2.getData();	
		assertEquals(test5Expect, input5Data);
	}

}
