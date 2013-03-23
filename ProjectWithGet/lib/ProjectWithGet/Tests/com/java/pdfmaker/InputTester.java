package com.java.pdfmaker;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

import com.itextpdf.text.DocumentException;


public class InputTester {

	@Test
	public void testTitles() throws DocumentException, IOException
	{
		/*input1text.txt contents:
		Title: TEST1, YAY!

		Subtitle= Blahblahblah*/
		
		inputConverter input1 = new inputConverter("./TestCases/input1test.txt");
		String input1Title = input1.getTitle();
		String input1Subtitle = input1.getSubtitle();
		ArrayList<ArrayList<String>>input1Data = input1.getData();
		
		//Expected outcome
		String test1ExpectTitle = " TEST1, YAY!";
		String test1ExpectSubtitle = " Blahblahblah";
		ArrayList<ArrayList<String>> test1Expect = new ArrayList<ArrayList<String>>();
		ArrayList<String> test1ExpectSub = new ArrayList<String>();

		assertEquals(test1ExpectTitle, input1Title);
		assertEquals(test1ExpectSubtitle, input1Subtitle);
		assertEquals(test1Expect, input1Data);
		
		
		
		/*input2test.txt contents:
		 * 

			HAHA, I am rebellious and don't put a title field


			I am even more rebellious for not putting Subtitle=


		 */
		
		inputConverter input2 = new inputConverter("./TestCases/input2test.txt");
		String input2Title = input2.getTitle();
		String input2Subtitle = input2.getSubtitle();
		ArrayList<ArrayList<String>>input2Data = input2.getData();
		
		//Expected outcome
		String test2ExpectTitle = "HAHA, I am rebellious and don't put a title field";
		String test2ExpectSubtitle = "I am even more rebellious for not putting Subtitle=";
		ArrayList<ArrayList<String>> test2Expect = new ArrayList<ArrayList<String>>();
		ArrayList<String> test2ExpectSub = new ArrayList<String>();

		assertEquals(test2ExpectTitle, input2Title);
		assertEquals(test2ExpectSubtitle, input2Subtitle);
		assertEquals(test2Expect, input2Data);
		
		
		

		/*input3test.txt contents:
		 * 

	
			|-------------------------|-------------------------|
			|-----1-----1-----1-----1-|-----1-----1-----1-----1-|
			|---2-----2-----2-----2---|---2-----2-----2-----2---|
			|-2-----2-----2-----2-----|-2-----2-----2-----2-----|
			|-0-----------------------|-------------------------|
			|-------------------------|-3-----------------------|
		 */

		
		inputConverter input3 = new inputConverter("./TestCases/input3test.txt");
		String input3Title = input3.getTitle();
		String input3Subtitle = input3.getSubtitle();
		ArrayList<ArrayList<String>>input3Data = input3.getData();
		
		//Expected outcome
		String test3ExpectTitle = "Not given";
		String test3ExpectSubtitle = "Not given";
		ArrayList<ArrayList<String>> test3Expect = new ArrayList<ArrayList<String>>();
		ArrayList<String> test3ExpectSub = new ArrayList<String>();	
	
		
		test3ExpectSub.add("|-------------------------|-------------------------|");
		test3ExpectSub.add("|-----1-----1-----1-----1-|-----1-----1-----1-----1-|");
		test3ExpectSub.add("|---2-----2-----2-----2---|---2-----2-----2-----2---|");
		test3ExpectSub.add("|-2-----2-----2-----2-----|-2-----2-----2-----2-----|");
		test3ExpectSub.add("|-0-----------------------|-------------------------|");
		test3ExpectSub.add("|-------------------------|-3-----------------------|");
		
		test3Expect.add(test3ExpectSub);
		
		assertEquals(test3ExpectTitle, input3Title);
		assertEquals(test3ExpectSubtitle, input3Subtitle);
		assertEquals(test3Expect, input3Data);
		

		/*input4test.txt contents:
		 * 

			Blahdblasdkfb
			
			Subtitle:Blasjdfewnfrjewnfr
			These lines should be ignored
			ignore
			ignorenignieowqnf
			aidsfnwoeinfroqiewnf
			sodinfoweinf
			
			ahsdfowiefho
			
			!!! hahahahahahahaha
			
			
			|---------3----------10-----0-------0---------------7--|
			|-----0---------10--------------0-------0-------5s7----|
			|-------2----------0----------2-------2-------0--------|
			|---------------------------------2--------------------|
			|---2---------------------3----------------------------|
			|-0-----------7-----------------------------0----------|
			|-----------15----------10-----0-------0---------------7--------|
			|---10-------------10--------------0-------0-------5s7----7-----|
			|------0--------------0----------2-------2-------0----------0---|
			|--------12--------------------------2------------------------7-|
			|----------------------------3----------------------------------|
			|-0--------------7-----------------------------0----------------|
			
			
			garbage
			garbgageaegea
			PUAHAHAHA
			ASDIFEWINFW			
			||---3--------2--------0-----0-0-----|3---3--------2-----|---------7-------|---------5-------|
			||---------------------------------0-||------------------|-----5s7---7-----|-----3s5---5-----|
			||*----2-0------2-0------2-0-----2--*||-----2-0------2-0-|---0---------0---|-------------0---|
			||*---------0-----------------------*||----------0-------|---------------2-|---2-----------2-|
			||-3-----------------2---------------||-3----------------|-----------------|-2---------------|
			||-----------------------------------||------------------|-0---------------|-----------------|
		*/
		
		inputConverter input4 = new inputConverter("./TestCases/input4test.txt");
		String input4Title = input4.getTitle();
		String input4Subtitle = input4.getSubtitle();
		ArrayList<ArrayList<String>>input4Data = input4.getData();
		
		//Expected outcome
		String test4ExpectTitle = "Blahdblasdkfb";
		String test4ExpectSubtitle = "Blasjdfewnfrjewnfr";
		ArrayList<ArrayList<String>> test4Expect = new ArrayList<ArrayList<String>>();
		ArrayList<String> test4ExpectSub = new ArrayList<String>();		
		
		test4ExpectSub.add("|---------3----------10-----0-------0---------------7--|");
		test4ExpectSub.add("|-----0---------10--------------0-------0-------5s7----|");
		test4ExpectSub.add("|-------2----------0----------2-------2-------0--------|");
		test4ExpectSub.add("|---------------------------------2--------------------|");
		test4ExpectSub.add("|---2---------------------3----------------------------|");
		test4ExpectSub.add("|-0-----------7-----------------------------0----------|");
		
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
		
		assertEquals(test4ExpectTitle, input4Title);
		assertEquals(test4ExpectSubtitle, input4Subtitle);
		assertEquals(test4Expect, input4Data);
		
		/*input5test.txt contents:
		 * 	TITLE=Remembering Rain
			SUBTITLE=Jim Matheos
			SPACING=4.4
			
			||------------------------------<12>----------||
			||-<12>------------------------------<12>-----||
			||*-----<5>-----------<7>--------------------*||
			||*---------------<7>------------------------*||
			||--------------------------------------------||
			||--------------------------------------------||
			
			||---------3----------10-----0-------0---------------7-|
			||-----0---------10--------------0-------0-------5s7---|
			||*------2----------0----------2-------2-------0-------|
			||*--------------------------------2-------------------|
			||---2---------------------3---------------------------|
			||-0-----------7-----------------------------0---------|
			
			|-----------15----------10-----0-------0---------------7-||
			|---10-------------10--------------0-------0-------5s7---||
			|------0--------------0----------2-------2-------0------*||
			|--------12--------------------------2------------------*||
			|----------------------------3---------------------------||
			|-0--------------7-----------------------------0---------||
		*/


		
		inputConverter input5 = new inputConverter("./TestCases/input5test.txt");
		String input5Title = input5.getTitle();
		String input5Subtitle = input5.getSubtitle();
		ArrayList<ArrayList<String>>input5Data = input5.getData();		
		
		String test5ExpectTitle = "Remembering Rain";
		String test5ExpectSubtitle = "Jim Matheos";
		ArrayList<ArrayList<String>> test5Expect = new ArrayList<ArrayList<String>>();
		ArrayList<String> test5ExpectSub = new ArrayList<String>();		
			
		test5ExpectSub.add("||------------------------------<12>----------||");
		test5ExpectSub.add("||-<12>------------------------------<12>-----||");
		test5ExpectSub.add("||*-----<5>-----------<7>--------------------*||");
		test5ExpectSub.add("||*---------------<7>------------------------*||");
		test5ExpectSub.add("||--------------------------------------------||");
		test5ExpectSub.add("||--------------------------------------------||");

				
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
				
		assertEquals(test5ExpectTitle, input5Title);
		assertEquals(test5ExpectSubtitle, input5Subtitle);
		assertEquals(test5Expect, input5Data);
				
				
	}	
		
}
