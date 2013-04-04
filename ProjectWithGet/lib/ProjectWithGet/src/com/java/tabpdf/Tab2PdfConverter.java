/** 
 * Tab2PdfConverter.java
 * 
 * This class is responsible for printing the output to the Pdf file by using the aid of the TabUtilitiesBundle class.
 * From the InputParser parameter, it extracts the ArrarList<ArrayList<String>> that contains the input from the input-file. 
 * Each ArrayList<String> element is then converted into an object of class TabUnitsBlock and placed into an ArrayList<TabUnitsBlock>.
 * Afterwards it processes the individual units (measures) within each TabUnitsBlock (in the ArrayList<TabUnitsBlock>) one after the
 * other. After outputting each unit (measure), it checks whether there is space for the next unit (measure) within the current block 
 * to be printed adjacent to the unit (measure) that was just printed. If space permits, the next unit is printed adjacent to the last
 * printed unit. Otherwise a line break occurs and the unit is printed on the next available space. For example, the following is an
 * ArrayUnitsBlock containing 4 units (measures) of guitar tab.
 * 	|---------7-------|---------5-------|---------7-------|---------5---------|
 *	|-----5s7---7-----|-----3s5---5-----|-----5s7---7-----|-----3s5---5-------|
 *	|---0---------0---|-------------0---|---0---------0---|-------------0---0-|
 *	|---------------2-|---2-----------2-|---------------2-|---2-----------2---|
 *	|-----------------|-2---------------|-----------------|-2-----------------|
 *	|-0---------------|-----------------|-0---------------|-------------------|
 *	
 * The class first prints all 6 lines of the first unit (measure) followed by a check to see whether the second unit (measure) can be 
 * printed adjacent to the first. If so, it is printed on the same sets of lines. Otherwise, it is printed on the next available sets of 
 * lines. After all the units (measures) within a TabUnitsBlock are printed, a counter is incremented and the next TabUnitsBlock from the
 * ArrayList<TabUnitsBlock> is fetched and printed in the same way until the ArrayList<TabUnitsBlock> is exhausted.
 */

package com.java.tabpdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.java.paramclasses.CharCoordinates;
import com.java.paramclasses.StarBars;
import com.java.paramclasses.TextLine;
import com.java.tabinput.InputParser;

public class Tab2PdfConverter {
	private Document document;
	private PdfWriter writer;
	private ArrayList<TabUnitsBlock> contents;
	ArrayList<Integer> starIndexes = null;
	private InputParser in;
	private String fontName;
	private float fontSize;
	private float spacing;
	
	Rectangle pageSize;

	private TabUnitsBlock currBlockInfo;
	private int curDIndex = 0;

	private float yIncrement = 40f;
	private float margin;
	private float xPos;
	private float yPos;

	private int blockNum;
	private int lineNum;
	private int charNum = 0;
	private String line = "";

	private int skip = 0;

	private float []lastNumXPos = new float[6];
	private float []lastNumYPos = new float[6];

	private TabUtilitiesBundle tUB;

	public Tab2PdfConverter(InputParser in, Rectangle pageSize, String filePath, String fontName, float fontSize, float spacing) {
		this.in = in;
		this.spacing = spacing;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.pageSize = pageSize;
		
		extractTabBlocks(in.getData(), spacing);

		document = new Document(pageSize);

		try{
			writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		}
		catch(Exception e){
			e.printStackTrace();
		}

		
	}
	
	private void extractTabBlocks(ArrayList<ArrayList<String>> input, float spacing){
		contents = new ArrayList<TabUnitsBlock>();
		
		for(int index = 0; index < input.size(); index++){
			contents.add(new TabUnitsBlock(input.get(index), spacing));
		}
	}

	public boolean createPDF() {
		
		boolean fullPdfWritten = true;
		
		margin = document.left();
		xPos = margin;
		yPos = document.top() - yIncrement;

		document.open();

		tUB = new TabUtilitiesBundle(writer.getDirectContent(), fontName, fontSize, spacing, pageSize);
		tUB.setLineWidth(0.5f);
		tUB.processHeader(in, document.getPageSize().getWidth(), document.top());

		
		currBlockInfo = contents.get(0);
		starIndexes = currBlockInfo.getStarIndexes();
		
		//looping through all the blocks in order to extract their contents and create the pdf 
		//file using the right format.
		for(blockNum = 0; blockNum < contents.size();){
			//at the beginning of each new block the y-position and x-position need to be set
			yPos = document.top() - yIncrement;
			xPos = margin;

			if(currBlockInfo.getUnitStats(curDIndex).getHorizontalSpaceNeeds() > document.right()-document.left()){
				fullPdfWritten = false;
				//System.out.println(in.getStartLineNum(currBlockInfo.getBlock()) + ": " + currBlockInfo.getBlock().get(0));

				String errorLog=com.java.tabui.TabFileManager.getTempDir()+"/T2PDFErr.txt";

				//String osVersion = System.getProperty("os.name");
				//String errorLog = "";
				//String errorLog;
				//if(osVersion.startsWith("Windows"))
					//errorLog = ""+System.getenv("TEMP")+"T2PDFErr.txt";
				//else
				//	errorLog = "/tmp/T2PDFErr.txt";

				
				try{
					
					//The output stream for error-log file
					BufferedWriter out = new BufferedWriter(new FileWriter("log_file", true));
					int startLine = in.getStartLineNum(blockNum);
					
					out.append("Measure number " + (curDIndex + 1) + " from lines (" + startLine + "-" + (startLine + currBlockInfo.getNumberOfLines()-1) +
							") of the input file" + " went past the right margin of the PDf document during conversion, and might not have fully fit.");
					out.newLine();
					out.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			//calling this method to draw the beginning 6 horizontal lines
			tUB.processBeginningHorizontalLines(contents.get(blockNum).getNumberOfLines(), document.left(), yPos);
			yPos = document.top()-yIncrement;

			//skip > 0 means that the block was split and the trailing '|'s were printed before the split,
			//and suppressed on the current position. Instead, the single '|' would be printed, which is done
			//through maintaining the skip variable and a call to processSingleBars.
			if(skip > 0){
				tUB.processSingleBars(contents.get(blockNum).getNumberOfLines(), xPos, yPos);
				margin += spacing;
				yPos = document.top() - yIncrement;
			}

			//yPos = document.top() - yIncrement;
			//at the beginning of each block the xPos needs to be set to the margin.
			for(lineNum = 0; lineNum < contents.get(blockNum).getNumberOfLines(); lineNum++){
				line = contents.get(blockNum).getLine(lineNum);

				xPos = margin;

				processUnitLine();
				//after printing the parts of each line, we need to move to the next 
				yPos -= fontSize;
			}

			curDIndex++;
			skip = 0;

			//if current character index is less than the index of the first of the last set of '|'s
			if(curDIndex < currBlockInfo.getNumberOfUnits()-1){
				processMidBlockReInit();
			}
			//otherwise we need to print the last sets of '|'s for the current block and increase the 
			//blockNum variable.
			else{
				processEndBlockReInit();
			}

			/*if(blockNum < contents.size()){
				currBlockInfo = new TabUnitsBlock(contents.get(blockNum), spacing);
				starIndexes = currBlockInfo.getStarIndexes();
			}*/	
			//if there is no more space left to join the next block right after the current block, set the margin
			//to the left end of the document, and yIncrement is increased to set the gap between the current block
			//and the next.
			//Please Note: On bigger spacing if the requirement is to keep the double bar part of the current lines,
			//then we have to change the following code a little bit.
			if(xPos + currBlockInfo.getUnitStats(curDIndex).getHorizontalSpaceNeeds() > document.right()) {
				processInsufficientSpaceReInit();
			}
		}
		
		tUB.processEndingHorizontalLines(xPos, yPos);
		
		document.close();

		return fullPdfWritten;
	}

	private void processUnitLine(){
		//the idea is to absorb the characters starting with '|' or '||' and ending right before
		//the next '|' or '||'s
		for(charNum = currBlockInfo.getUnitStats(curDIndex).getBeginningBarIndex() + skip; curDIndex + 1 < currBlockInfo.getNumberOfUnits() && charNum < (currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarIndex());){
			//if(xPos + (currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarFreq()*spacing) -
			//(currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarFreq() - 1) * 5 <= document.right()){
				
				if(line.charAt(charNum) == '|' || contents.get(blockNum).getLine(1).charAt(charNum) == '|'){
					tUB.processCurrentBars(new TextLine(lineNum, "", charNum), 
							new StarBars(starIndexes,
							currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq(), 
							currBlockInfo.getUnitStats(curDIndex).getRepValueIndex(), 
							currBlockInfo.getUnitStats(curDIndex).getRepValue()), 
							xPos, yPos);
					
					
					charNum += currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq();
					xPos += ((currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq()-1) * 5) + spacing;
				}

				else if(line.charAt(charNum) == '<'){
					String number = "";
					charNum++;
					for(; Character.isDigit(line.charAt(charNum)); charNum++) {
						number += line.charAt(charNum);
					}

					//need to advance charNum one more time to point right after the '>'
					charNum++;
					tUB.processInputWithinTriangle(number, xPos, yPos);
					xPos += (number.length()+2)*spacing;
				}	

				//if character is '-', then a line with width the value of 'spacing' would be drawn onto the
				//pdf file
				else if(line.charAt(charNum) == '-'){
					tUB.processDashes(xPos, yPos);
					charNum++;
					xPos += spacing;
				}

				//if the character is letter or digit it would be printed onto the pdf file 
				else if(Character.isDigit(line.charAt(charNum))){
					String number = "";

					for(; Character.isDigit(line.charAt(charNum)) ; charNum++) {
						number += line.charAt(charNum);
					}

					tUB.processDigit(number, new TextLine(lineNum, line, charNum), new CharCoordinates(xPos, yPos, document.right(), lastNumXPos, lastNumYPos));
					xPos += (number.length())*spacing;
				}

				else if(line.charAt(charNum) == 's') {
					tUB.processSlideUp(xPos, yPos);
					charNum++;
					xPos += spacing;
				}

				else if(line.charAt(charNum) == '*') {
					boolean nextCharIsBar = (line.charAt(charNum + 1) == '|');
					tUB.processStar(xPos, yPos, nextCharIsBar);
					xPos += spacing;
					
					charNum++;
				}

				else if(line.charAt(charNum) == ' ') {
					charNum++;
				}

				//write code for the cases that have not been handled
				else if(line.charAt(charNum) == 'h'){
					tUB.processH(xPos, yPos);
					charNum++;
					xPos += spacing;
				}

				else if(line.charAt(charNum) == 'p'){
					tUB.processP(xPos, yPos);
					charNum++;
					xPos += spacing;
				}

				else if(line.charAt(charNum) == 'e' || line.charAt(charNum) == 'B' || line.charAt(charNum) == 'G' || line.charAt(charNum) == 'D'
				|| line.charAt(charNum) == 'A' || line.charAt(charNum) == 'E'){
					//tUB.processChordID(line, charNum, xPos, yPos);
					//xPos += spacing;
					charNum++;
				}
				else{
					tUB.processUnknown(xPos, yPos);
					charNum++;
					xPos += spacing;
				}
			//}


			//else{
				//charNum++;
			//}
		}
	}

	private void processMidBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//checking if the next block can be fit next to the current. If not, we need to print the
		//trailing bars for the current block before the actual split that is coming up.
		if(xPos + currBlockInfo.getUnitStats(curDIndex).getHorizontalSpaceNeeds() > document.right()) {

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//processTrailingBars does that. Also the ending 6 horizontal lines are printed
			xPos = tUB.processTrailingBars(starIndexes, contents.get(blockNum).getNumberOfLines(), charNum, 
					currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq(), 
					currBlockInfo.getUnitStats(curDIndex).getRepValueIndex(), 
					currBlockInfo.getUnitStats(curDIndex).getRepValue(), margin, xPos, yPos);

			yPos = document.top()-yIncrement;

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//and the same '|'s should be suppressed before the continuation on the next position, which is
			//achieved through setting the skip variable.
			skip = currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq();
		}
	}

	private void processEndBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//If all the units of a block have been printed, it is time to call 'processtrailingBars to print
		//the last set of '|'s.
		xPos = tUB.processTrailingBars(starIndexes, contents.get(blockNum).getNumberOfLines(), charNum, currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq(), currBlockInfo.getUnitStats(curDIndex).getRepValueIndex(), currBlockInfo.getUnitStats(curDIndex).getRepValue(), margin, xPos, yPos);

		//margin and yPosition needs to be reset after processTrailingBars returns, so that the next
		//line that would be output is done at the proper location. 
		margin = xPos;
		yPos = document.top() - yIncrement;

		blockNum++;
		if(blockNum < contents.size()){
			currBlockInfo = contents.get(blockNum);
			starIndexes = currBlockInfo.getStarIndexes();
		}	
		curDIndex = 0;
	}

	private void processInsufficientSpaceReInit(){
		//first draw the 6 horizontal lines at the end of the current block
		tUB.processEndingHorizontalLines(xPos, yPos);
		yPos = document.top()-yIncrement;

		margin = document.left();
		xPos = margin;
		yIncrement += fontSize * 8;

		if(yPos - fontSize * 8 - fontSize*6  < document.bottom()){
			document.newPage();
			yIncrement = 40f;
			tUB.setFontSize(fontSize);
			tUB.setLineWidth(0.5f);
		}
	}
}
