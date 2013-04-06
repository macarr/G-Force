package com.java.tabinput;

import static org.junit.Assert.*;

import java.util.ArrayList;
import com.java.tabinput.InputParser;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest11 {

InputParser input7 = new InputParser("./TestCases/input11test.txt");
	
	String test7ExpectTitle = "Blahdblasdkfb";
	String test7ExpectSubtitle = "Blasjdfewnfrjewnfr";
	ArrayList<ArrayList<String>> test7Expect = new ArrayList<ArrayList<String>>();
	boolean testexpectedsufficientinput = true;
	
	@Before
	public void testInputParser() {
ArrayList<String> test5ExpectSub = new ArrayList<String>();		
		
		test5ExpectSub.add("|---------3----------10-----0-------0---------------7------------");
		test5ExpectSub.add("|-----0---------10--------------0-------0-------5-7--------------");
		test5ExpectSub.add("|-------2----------0----------2-------2-------0------------------");
		test5ExpectSub.add("|---------------------------------2------------------------------");
		test5ExpectSub.add("|---2---------------------3--------------------------------------");
		test5ExpectSub.add("|-0-----------7-----------------------------0--------------------");

				
		test7Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
				
		test5ExpectSub.add("|-----------15----------10-----0-------0---------------7--------|");
		test5ExpectSub.add("|---10-------------10--------------0-------0-------5-7----7-----|");
		test5ExpectSub.add("|------0--------------0----------2-------2-------0----------0---|");
		test5ExpectSub.add("|--------12--------------------------2------------------------7-|");
		test5ExpectSub.add("|----------------------------3----------------------------------|");
		test5ExpectSub.add("|-0--------------7-----------------------------0----------------|");

				
		test7Expect.add(test5ExpectSub);
		test5ExpectSub = new ArrayList<String>();
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
