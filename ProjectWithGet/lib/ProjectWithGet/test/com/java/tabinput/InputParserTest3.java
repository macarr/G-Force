package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest3 {
	InputParser input4 = new InputParser("./TestCases/input3test.txt");

	//Expected outcome
		String test4ExpectTitle = "Blahdblasdkfb";
		String test4ExpectSubtitle = "Blasjdfewnfrjewnfr";
		ArrayList<ArrayList<String>> test4Expect = new ArrayList<ArrayList<String>>();
		boolean testexpectedsufficientinput = true;
		
	@Before
	public void testInputParser() {
ArrayList<String> test4ExpectSub = new ArrayList<String>();		
		
		test4ExpectSub.add("|---------3----------10-----0-------0---------------7--");
		test4ExpectSub.add("|-----0---------10--------------0-------0-------5s7----");
		test4ExpectSub.add("|-------2----------0----------2-------2-------0--------");
		test4ExpectSub.add("|---------------------------------2--------------------");
		test4ExpectSub.add("|---2---------------------3----------------------------");
		test4ExpectSub.add("|-0-----------7-----------------------------0----------");
		
		test4Expect.add(test4ExpectSub);
		test4ExpectSub = new ArrayList<String>();
		
		test4ExpectSub.add("|-----------15----------10-----0-------0---------------7--------|");
		test4ExpectSub.add("|---10-------------10--------------0-------0-------5s7----7-----|");
		test4ExpectSub.add("|------0--------------0----------2-------2-------0----------0---|");
		test4ExpectSub.add("|--------12--------------------------2------------------------7-|");
		test4ExpectSub.add("|----------------------------3----------------------------------|");
		test4ExpectSub.add("|-0--------------7-----------------------------0----------------|");
		
		test4Expect.add(test4ExpectSub);
		test4ExpectSub = new ArrayList<String>();
		
		test4ExpectSub.add("||---3--------2--------0-----0-0-----|3---3--------2-----|---------7-------|---------5-------|");
		test4ExpectSub.add("||---------------------------------0-||------------------|-----5s7---7-----|-----3s5---5-----|");
		test4ExpectSub.add("||*----2-0------2-0------2-0-----2--*||-----2-0------2-0-|---0---------0---|-------------0---|");
		test4ExpectSub.add("||*---------0-----------------------*||----------0-------|---------------2-|---2-----------2-|");
		test4ExpectSub.add("||-3-----------------2---------------||-3----------------|-----------------|-2---------------|");
		test4ExpectSub.add("||-----------------------------------||------------------|-0---------------|-----------------|");
		
		test4Expect.add(test4ExpectSub);
	}
/*
	@Test
	public void testReadFile() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testGetTitle() {
		String input4Title = input4.getTitle();
		assertEquals(test4ExpectTitle, input4Title);
	}

	@Test
	public void testGetSubtitle() {
		String input4Subtitle = input4.getSubtitle();
		assertEquals(test4ExpectSubtitle, input4Subtitle);
	}

	@Test
	public void testGetData() {
		ArrayList<ArrayList<String>>input4Data = input4.getData();
		assertEquals(test4Expect, input4Data);
	}
/*
	@Test
	public void testGetStartLineNum() {
		fail("Not yet implemented");
	}
*/
	

}
