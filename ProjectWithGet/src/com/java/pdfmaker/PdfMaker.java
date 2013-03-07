package com.java.pdfmaker;

import java.io.FileOutputStream;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class PdfMaker {
  private Document document;
	private PdfWriter writer;
	private ArrayList<ArrayList<String>> contents;
	private inputParser in;

	private float fontSize;
	private float spacing;

	private ArrayList<DIndexFreq> dIndexes;
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

	public PdfMaker(inputParser in, Rectangle pageSize, String filePath, String fontName, float fontSize, float spacing) {
		this.in = in;
		this.contents = in.getData();
		this.spacing = spacing;
		this.fontSize = fontSize;

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

	public boolean createPDF() {
		boolean fullPdfWritten = true;

		margin = document.left();
		xPos = margin;
		yPos = document.top() - yIncrement;

		dIndexes = getDIndexes(contents.get(0).get(1));
 
		//looping through all the blocks in order to extract their contents and create the pdf 
		//file using the right format.
		for(blockNum = 0; blockNum < contents.size();){
			//at the beginning of each new block the y-position and x-position need to be set
			yPos = document.top() - yIncrement;
			xPos = margin;

			if(dIndexes.get(curDIndex).spaceNeeds > document.right()-document.left()){
				fullPdfWritten = false;
			}
			//calling this method to draw the beginning 6 horizontal lines
			tUB.processBeginningHorizontalLines(contents.get(blockNum).size(), document.left(), yPos);
			yPos = document.top()-yIncrement;

			//skip > 0 means that the block was split and the trailing '|'s were printed before the split,
			//and suppressed on the current position. Instead, the single '|' would be printed, which is done
			//through maintaining the skip variable and a call to drawSingleBars.
			if(skip > 0){
				tUB.drawSingleBars(contents.get(blockNum).size(), xPos, yPos);
				yPos = document.top() - yIncrement;
			}

			//yPos = document.top() - yIncrement;
			//at the beginning of each block the xPos needs to be set to the margin.
			for(lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
				line = contents.get(blockNum).get(lineNum);

				xPos = margin;

				processUnitLine();
				//after printing the parts of each line, we need to move to the next 
				yPos -= fontSize;
			}

			curDIndex++;
			skip = 0;

			//if current character index is less than the index of the first of the last set of '|'s
			if(curDIndex < dIndexes.size()-1){
				processMidBlockReInit();
			}
			//otherwise we need to print the last sets of '|'s for the current block and increase the 
			//blockNum variable.
			else{
				processEndBlockReInit();
			}

			if(blockNum < contents.size())
				dIndexes = getDIndexes(contents.get(blockNum).get(1));

			//if there is no more space left to join the next block right after the current block, set the margin
			//to the left end of the document, and yIncrement is increased to set the gap between the current block
			//and the next.
			//Please Note: On bigger spacing if the requirement is to keep the double bar part of the current lines,
			//then we have to change the following code a little bit.
			if(xPos + dIndexes.get(curDIndex).spaceNeeds > document.right()) {
				processInsufficientSpaceReInit();
			}
		}

		document.close();

		return fullPdfWritten;
	}

	private void processUnitLine(){
		//the idea is to absorb the characters starting with '|' or '||' and ending right before
		//the next '|' or '||'s
		for(charNum = dIndexes.get(curDIndex).index + skip; curDIndex + 1 < dIndexes.size() && charNum < (dIndexes.get(curDIndex+1).index);){
			if(xPos <= document.right()){
				if(line.charAt(charNum) == '|'){
					tUB.processCurrentBars(curDIndex, lineNum, charNum, dIndexes.get(curDIndex).freq, dIndexes.get(curDIndex).repIndex, dIndexes.get(curDIndex).repNum, xPos, yPos);
					charNum += dIndexes.get(curDIndex).freq;
					xPos += dIndexes.get(curDIndex).freq * spacing;
				}

				else if(line.charAt(charNum) == '<'){
					String number = "";
					charNum++;
					for(; line.charAt(charNum) != '>'; charNum++) {
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
					tUB.slideUp(xPos, yPos);
					charNum++;
					xPos += spacing;
				}

				else if(line.charAt(charNum) == '*') {
					tUB.processStar(xPos, yPos);
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
					tUB.processChordID(line, charNum, xPos, yPos);
					xPos += spacing;
					charNum++;
				}
			}


			else{
				charNum++;
			}
		}
	}

	private void processMidBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//checking if the next block can be fit next to the current. If not, we need to print the
		//trailing bars for the current block before the actual split that is coming up.
		if(xPos + dIndexes.get(curDIndex).spaceNeeds > document.right()) {

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//processTrailingBars does that. Also the ending 6 horizontal lines are printed
			xPos = tUB.processTrailingBars(curDIndex, contents.get(blockNum).size(), charNum, dIndexes.get(curDIndex).freq, dIndexes.get(curDIndex).repIndex, dIndexes.get(curDIndex).repNum, margin, xPos, yPos);

			yPos = document.top()-yIncrement;

			//if a block was split from the middle, the trailing '|'s should be printed before the split.
			//and the same '|'s should be suppressed before the continuation on the next position, which is
			//achieved through setting the skip variable.
			skip = dIndexes.get(curDIndex).freq;
		}
	}

	private void processEndBlockReInit(){
		margin = xPos;
		yPos = document.top() - yIncrement;

		//If all the units of a block have been printed, it is time to call 'processtrailingBars to print
		//the last set of '|'s.
		xPos = tUB.processTrailingBars(curDIndex, contents.get(blockNum).size(), charNum, dIndexes.get(curDIndex).freq, dIndexes.get(curDIndex).repIndex, dIndexes.get(curDIndex).repNum, margin, xPos, yPos);

		//margin and yPosition needs to be reset after processTrailingBars returns, so that the next
		//line that would be output is done at the proper location. 
		margin = xPos;
		yPos = document.top() - yIncrement;

		blockNum++;
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

	public ArrayList<DIndexFreq> getDIndexes(String source) {
		ArrayList<DIndexFreq> dIndexes = new ArrayList<DIndexFreq>();

		for(int ind = 0; ind < source.length(); ind++) {
			if(source.charAt(ind) == '|'){

				//'indexRep' represents the index of the repetition-value. An index of -1 is initially being assigned, which would be 
				//replaced by the actual index value if a repetition-value is found. If no repetition value is found, a -1 would indicate that
				//when a check would be done.
				int indexRep = -1;
				char repVal = 0;

				//We have come across the first '|' of the unit, so freq is 1 so far.
				int freq = 1;

				//We need to keep track of the index of the first '|' in each DIndexFreq, which is assigned to temp because ind would increase
				//in the next for loop.
				int temp = ind;
				ind++;

				//Calculating frequency of each occurrence of '|', as well as number of repeats
				for(; ind < source.length() && source.charAt(ind) == '|'; ind++){

					//If there is a repeat, the repeat-number is in the first line.
					if(Character.isDigit(contents.get(blockNum).get(0).charAt(ind))){

						//The index of the repeat-value is being stored.
						indexRep = ind;
						repVal = contents.get(blockNum).get(0).charAt(ind);
					}
					freq++;
				}

				DIndexFreq data = new DIndexFreq();
				data.index = temp;
				data.freq = freq;
				data.repIndex = indexRep;
				data.repNum = repVal;
				dIndexes.add(data);
			}
		}

		//If the first character is not '|', we have to explicitly tell the program where to start the block.
		if(source.charAt(0) != '|'){
			DIndexFreq data = new DIndexFreq();
			data.index = 0;
			data.freq = 0;
			data.repIndex = -1;
			data.repNum = 0;
			dIndexes.add(0, data);
		}

		//If the last index is not '|', we have to explicitly tell the program where to end the block.
		if(source.charAt(source.length() - 1) != '|'){
			DIndexFreq data = new DIndexFreq();
			data.index = source.length() - 1;
			data.freq = 0;
			data.repIndex = -1;
			data.repNum = 0;
			dIndexes.add(data);
		}

		for(int ind = 0; ind < dIndexes.size()-1; ind++){
			dIndexes.get(ind).spaceNeeds = ((dIndexes.get(ind + 1).index + dIndexes.get(ind + 1).freq)-(dIndexes.get(ind).index))*spacing;
		}
		return dIndexes;
	}

	class DIndexFreq{
		int index;
		int freq;
		int repIndex;
		char repNum;
		float spaceNeeds;
	}
}
