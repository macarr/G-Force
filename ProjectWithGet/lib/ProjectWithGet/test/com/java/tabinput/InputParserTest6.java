package com.java.tabinput;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest6 {

InputParser input6 = new InputParser("./TestCases/input6test.txt");
	
	String test6ExpectTitle = "";
	String test6ExpectSubtitle = "";
	ArrayList<ArrayList<String>> test6Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = false;
	
	@Before
	public void testInputParser() {
		ArrayList<String> test6ExpectSub = new ArrayList<String>();
	}

	@Test
	public void testGetTitle() {
		String input6Title = input6.getTitle();

		assertEquals(test6ExpectTitle, input6Title);
	}

	@Test
	public void testGetSubtitle() {
		String input6Subtitle = input6.getSubtitle();

		assertEquals(test6ExpectSubtitle, input6Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input6Data = input6.getData();

		assertEquals(test6Expect, input6Data);
	}



}
