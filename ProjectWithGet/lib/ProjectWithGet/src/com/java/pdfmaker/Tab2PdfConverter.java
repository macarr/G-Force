package com.java.pdfmaker;

import java.io.FileOutputStream;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class Tab2PdfConverter {
	private Document document;
	private PdfWriter writer;
	private ArrayList<TabUnitsBlock> contents;
	ArrayList<Integer> starIndexes = null;
	private InputParser in;

	private float fontSize;
	private float spacing;

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
		this.fontSize = fontSize;
		
		extractTabBlocks(in.getData(), spacing);

		document = new Document(pageSize);

		try{
			writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		}
		catch(Exception e){
			e.printStackTrace();
		}

		document.open();

		tUB = new TabUtilitiesBundle(writer.getDirectContent(), fontName, fontSize, spacing, pageSize);
		tUB.setLineWidth(0.5f);
		tUB.processHeader(in, document.getPageSize().getWidth(), document.top());
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
			}
			//calling this method to draw the beginning 6 horizontal lines
			tUB.processBeginningHorizontalLines(contents.get(blockNum).getBlockSize(), document.left(), yPos);
			yPos = document.top()-yIncrement;

			//skip > 0 means that the block was split and the trailing '|'s were printed before the split,
			//and suppressed on the current position. Instead, the single '|' would be printed, which is done
			//through maintaining the skip variable and a call to processSingleBars.
			if(skip > 0){
				tUB.processSingleBars(contents.get(blockNum).getBlockSize(), xPos, yPos);
				margin += spacing;
				yPos = document.top() - yIncrement;
			}

			//yPos = document.top() - yIncrement;
			//at the beginning of each block the xPos needs to be set to the margin.
			for(lineNum = 0; lineNum < contents.get(blockNum).getBlockSize(); lineNum++){
				line = contents.get(blockNum).get(lineNum);

				xPos = margin;

				processUnitLine();
				//after printing the parts of each line, we need to move to the next 
				yPos -= fontSize;
			}

			curDIndex++;
			skip = 0;

			//if current character index is less than the index of the first of the last set of '|'s
			if(curDIndex < currBlockInfo.getSize()-1){
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

		document.close();

		return fullPdfWritten;
	}

	private void processUnitLine(){
		//the idea is to absorb the characters starting with '|' or '||' and ending right before
		//the next '|' or '||'s
		for(charNum = currBlockInfo.getUnitStats(curDIndex).getBeginningBarIndex() + skip; curDIndex + 1 < currBlockInfo.getSize() && charNum < (currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarIndex());){
			//if(xPos + (currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarFreq()*spacing) -
			//(currBlockInfo.getUnitStats(curDIndex+1).getBeginningBarFreq() - 1) * 5 <= document.right()){
				
				if(line.charAt(charNum) == '|' || contents.get(blockNum).get(1).charAt(charNum) == '|'){
					tUB.processCurrentBars(starIndexes, lineNum, charNum, currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq(), 
							currBlockInfo.getUnitStats(curDIndex).getRepValueIndex(), currBlockInfo.getUnitStats(curDIndex).getRepValue(), 
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

					tUB.processDigit(number, lineNum, line, charNum, xPos, yPos, lastNumXPos, lastNumYPos, document.right());
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
			xPos = tUB.processTrailingBars(starIndexes, contents.get(blockNum).getBlockSize(), charNum, 
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
		xPos = tUB.processTrailingBars(starIndexes, contents.get(blockNum).getBlockSize(), charNum, currBlockInfo.getUnitStats(curDIndex).getBeginningBarFreq(), currBlockInfo.getUnitStats(curDIndex).getRepValueIndex(), currBlockInfo.getUnitStats(curDIndex).getRepValue(), margin, xPos, yPos);

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