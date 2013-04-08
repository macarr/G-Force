package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest {
	String test1ExpectTitle = " TEST1, YAY!";
	String test1ExpectSubtitle = " Blahblahblah";
	
	ArrayList<ArrayList<String>> test1Expect = new ArrayList<ArrayList<String>>();
	InputParser input1 = new InputParser("./TestCases/input1test.txt");
	boolean testexpectedsufficientinput = false;

	@Before
	public void testInputParser() {
		ArrayList<String> test1ExpectSub = new ArrayList<String>();
	}

	@Test
	public void testGetTitle() {
		String input1Title = input1.getTitle();

		assertEquals(test1ExpectTitle, input1Title);
	}

	@Test
	public void testGetSubtitle() {
		String input1Subtitle = input1.getSubtitle();

		assertEquals(test1ExpectSubtitle, input1Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input1Data = input1.getData();

		assertEquals(test1Expect, input1Data);
	}


}
