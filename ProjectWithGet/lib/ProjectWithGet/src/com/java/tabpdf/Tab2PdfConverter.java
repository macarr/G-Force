/** 
 * This class is responsible for calling the appropriate methods of the TabUtilitiesBundle class in order to write the contents of
 * the Pdf file.
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
import com.java.paramclasses.ErrorInformation;
import com.java.paramclasses.ReturnValues;
import com.java.paramclasses.StarBars;
import com.java.paramclasses.TextLine;
import com.java.paramclasses.TrailingCoordinates;
import com.java.tabinput.InputParser;

public class Tab2PdfConverter {
	private Document document;	//The Pdf document.
	private PdfWriter writer;	//the document writer.
	private ArrayList<TabUnitsBlock> contents;	//ArrayList containing the TabUnitsBlock objects.
	ArrayList<Integer> starIndexes = null;	//ArrayList containing the indexes of the '*'s within each block. 
	private InputParser in;
	private String fontName;
	private float fontSize;
	private float spacing;
	
	Rectangle pageSize;		//The size of the Pdf document.

	private TabUnitsBlock currBlock;
	private int curUnitIndex = 0;	//Index of the current unit (measure) within a block.

	private float yIncrement = 40f;	//Helps maintain the vertical position.
	private float margin;
	private float xPos;
	private float yPos;

	private int blockNum;	//The index of the current block.
	private int lineNum;
	private int charNum = 0;
	private String line = "";

	private int skip = 0;	//Used under some conditions.

	private float []lastNumXPos = new float[6];	//Maintains the x position of the last number on each of the six lines. 
	private float []lastNumYPos = new float[6];	//Maintains the y position of the last number on each of the six lines.

	private TabUtilitiesBundle tUB;

	/**
	 * Tab2PdfConverter constructor.
	 */
	public Tab2PdfConverter(InputParser in, Rectangle pageSize, String filePath, String fontName, float fontSize, float spacing) {
		this.in = in;
		this.spacing = spacing;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.pageSize = pageSize;
		
		//Called to instantiate TabUnitBlock objects from the input.
		extractTabBlocks(in.getData(), spacing);	 

		document = new Document(pageSize);

		//Instantiating the Pdf writer.
		try{
			writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method extracts the elements from the input ArrayList and instantiates TabUnitsBlock objects. The objects are then stored
	 * in the contents ArrayList.
	 */
	private void extractTabBlocks(ArrayList<ArrayList<String>> input, float spacing){
		contents = new ArrayList<TabUnitsBlock>();
		
		for(int index = 0; index < input.size(); index++){
			contents.add(new TabUnitsBlock(input.get(index), spacing));
		}
	}

	/**
	 * This is the method that takes all the necessary steps to create the Pdf document.
	 */
	public ErrorInformation createPDF() {
		//To check whether there was space for the whole document to be written. 
		boolean fullPdfWritten = true;
		
		//ArrayList where any error-info shall be stored.
		ArrayList<String> errorInfo = new ArrayList<String>();
		
		margin = document.left();
		xPos = margin;
		
		//Setting the top margin
		yPos = document.top() - yIncrement;

		document.open();

		tUB = new TabUtilitiesBundle(writer.getDirectContent(), fontName, fontSize, spacing, pageSize);
		
		//Setting the line width within the Pdf.
		tUB.setLineWidth(0.5f);
		
		//The header is being written.
		tUB.processHeader(in, document.getPageSize().getWidth(), document.top());

		currBlock = contents.get(0);
		
		//Getting the indexes of the '*' characters within the block. 
		starIndexes = currBlock.getStarIndexes();
		
		//looping through all the blocks in order to extract their contents and create the pdf 
		//file using the right format.
		for(blockNum = 0; blockNum < contents.size();){
			//at the beginning of each new block the y-position and x-position need to be set.
			yPos = document.top() - yIncrement;
			xPos = margin;

			//If a unit (measure) does not fit.
			if(currBlock.getUnitStats(curUnitIndex).getHorizontalSpaceNeeds() > document.right()-document.left()){
				fullPdfWritten = false;

				try{
					//Getting the start-line number of the current block within the input file. 
					int startLine = in.getStartLineNum(blockNum);
					
					//Adding error-info to the ArrayList.
					errorInfo.add("Measure number " + (curUnitIndex + 1) + " from lines (" + startLine + "-" + (startLine + currBlock.getNumberOfLines()-1) +
							") of the input file" + " went past the right margin of the PDf document during conversion, and might not have fully fit.");
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

			for(lineNum = 0; lineNum < contents.get(blockNum).getNumberOfLines(); lineNum++){
				line = contents.get(blockNum).getLine(lineNum);

				//At the beginning of each line the xPos needs to be set to the margin.
				xPos = margin;
				
				//Called to process a line.
				processUnitLine();
				
				//After printing the parts of each line, we need to move to the next. 
				yPos -= fontSize;
			}

			//The next unit to be processed from the block.
			curUnitIndex++;
			skip = 0;

			//If current character index is less than the index of the first of the last set of '|'s within a block
			if(curUnitIndex < currBlock.getNumberOfUnits()-1){
				processMidBlockReInit();
			}
			
			//Otherwise we need to print the last sets of '|'s for the current block and increase the 
			//blockNum variable.
			else{
				processEndBlockReInit();
			}

			//if there is no more space left to join the next block right after the current block.
			if(xPos + currBlock.getUnitStats(curUnitIndex).getHorizontalSpaceNeeds() > document.right()) {
				processInsufficientSpaceReInit();
			}
		}
		
		tUB.processEndingHorizontalLines(xPos, yPos);
		
		document.close();

		return new ErrorInformation(fullPdfWritten, errorInfo);
	}

	/**
	 * This method processes individual lines from a unit
	 */
	private void processUnitLine(){
		
		//the idea is to absorb the characters starting with '|' or '||' and ending right before
		//the next '|' or '||'s.
		for(charNum = currBlock.getUnitStats(curUnitIndex).getBeginningBarIndex() + skip; curUnitIndex + 1 < currBlock.getNumberOfUnits() && charNum < (currBlock.getUnitStats(curUnitIndex+1).getBeginningBarIndex());){
				
			//In the following If-else conditions the individual characters are processed.	
			if(line.charAt(charNum) == '|' || contents.get(blockNum).getLine(1).charAt(charNum) == '|'){
				tUB.processCurrentBars(new TextLine(lineNum, "", charNum), 
				new StarBars(starIndexes,
				currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq(), 
				currBlock.getUnitStats(curUnitIndex).getRepValueIndex(), 
				currBlock.getUnitStats(curUnitIndex).getRepValue()), 
				xPos, yPos);
					
				//Character position is incremented.
				charNum += currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq();
				xPos += ((currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq()-1) * 5) + spacing;
			}

			else if(line.charAt(charNum) == '<'){
				String number = "";
				charNum++;
				for(; Character.isDigit(line.charAt(charNum)); charNum++) {
					number += line.charAt(charNum);
				}

				//Need to advance charNum one more time to point right after the '>'.
				charNum++;
				tUB.processInputWithinTriangle(number, xPos, yPos);
				xPos += (number.length()+2)*spacing;
			}	

			else if(line.charAt(charNum) == '-'){
				tUB.processDashes(xPos, yPos);
				charNum++;
				xPos += spacing;
			}
 
			else if(Character.isDigit(line.charAt(charNum))){
				String number = "";

				for(; Character.isDigit(line.charAt(charNum)) ; charNum++) {
					number += line.charAt(charNum);
				}

					
				ReturnValues returnValues = tUB.processDigit(number, new TextLine(lineNum, line, charNum), new CharCoordinates(xPos, yPos, document.right(), lastNumXPos[lineNum], lastNumYPos[lineNum]));
				lastNumXPos[lineNum] = returnValues.xPos;
				lastNumYPos[lineNum] = returnValues.yPos;
					
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
				tUB.processChordID(lineNum, line, charNum, xPos, yPos);
				//xPos += spacing;
				charNum++;
			}
				
			else{
				tUB.processUnknown(xPos, yPos);
				charNum++;
				xPos += spacing;
			}
		}
	}

	/**
	 * This method is called if a block was split from the middle.
	 */
	private void processMidBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//checking if the next block can be fit next to the current. If not, we need to print the
		//trailing bars for the current block before the actual split that is coming up.
		if(xPos + currBlock.getUnitStats(curUnitIndex).getHorizontalSpaceNeeds() > document.right()) {

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//processTrailingBars does that. Also the ending 6 horizontal lines are printed
			xPos = tUB.processTrailingBars(new StarBars(starIndexes,
					currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq(),
					currBlock.getUnitStats(curUnitIndex).getRepValueIndex(),
					currBlock.getUnitStats(curUnitIndex).getRepValue()), contents.get(blockNum).getNumberOfLines(), charNum, 
					new TrailingCoordinates(margin, xPos, yPos));

			yPos = document.top()-yIncrement;

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//and the same '|'s should be suppressed before the continuation on the next position, which is
			//achieved through setting the skip variable.
			skip = currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq();
		}
	}

	/**
	 * This method is called if a block ends with the unit, and there was no need to split the block up.
	 */
	private void processEndBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//If all the units of a block have been printed, it is time to call 'processtrailingBars to print
		//the last set of '|'s.
		xPos = tUB.processTrailingBars(new StarBars(starIndexes,
				currBlock.getUnitStats(curUnitIndex).getBeginningBarFreq(),
				currBlock.getUnitStats(curUnitIndex).getRepValueIndex(),
				currBlock.getUnitStats(curUnitIndex).getRepValue()), contents.get(blockNum).getNumberOfLines(), charNum, 
				new TrailingCoordinates(margin, xPos, yPos));

		//margin and yPosition needs to be reset after processTrailingBars returns, so that the next
		//line that would be output is done at the proper location. 
		margin = xPos;
		yPos = document.top() - yIncrement;

		blockNum++;
		if(blockNum < contents.size()){
			currBlock = contents.get(blockNum);
			starIndexes = currBlock.getStarIndexes();
		}	
		curUnitIndex = 0;
	}

	/**
	 * This method checks whether the next unit (measure) can fit next to the current one.
	 */
	private void processInsufficientSpaceReInit(){
		//first draw the 6 horizontal lines at the end of the current block
		tUB.processEndingHorizontalLines(xPos, yPos);
		yPos = document.top()-yIncrement;

		margin = document.left();
		xPos = margin;
		yIncrement += fontSize * 8;

		//Checks whether there is need to create a new page.
		if(yPos - fontSize * 8 - fontSize*6  < document.bottom()){
			document.newPage();
			yIncrement = 40f;
			tUB.setFontSize(fontSize);
			tUB.setLineWidth(0.5f);
		}
	}
}
