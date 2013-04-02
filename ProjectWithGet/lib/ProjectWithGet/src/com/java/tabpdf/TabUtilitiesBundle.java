package com.java.tabpdf;

import java.util.ArrayList;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.java.paramclasses.CharCoordinates;
import com.java.paramclasses.StarBars;
import com.java.paramclasses.TextLine;
import com.java.tabinput.InputParser;

/**
 * TabUtilitiesBundle --- This class performs output operations to the pdf document associated with it. 
 */
public class TabUtilitiesBundle{
	PdfContentByte cB;	//Helps output characters to the pdf document that it belongs to.
	BaseFont bF;		//The font that is being used in the pdf document.
	String fontName;  	//The name of the font being used in the pdf document.
	float fontSize;		//The size of the font being used in the pdf document.
	float spacing;		//The spacing value.
	Rectangle pageSize;	//The size of the pdf document.

	//For drawing tilted shapes.
	private class TiltedShape{
		
		//variables related to the PdfTemplate.
		float tiltAngle = (float)(-45 * (Math.PI / 180));
		float scaleX = (float)(Math.cos(tiltAngle)); 		
		float scaleY = (float)(Math.cos(tiltAngle)); 
		float tiltX  = (float)(-Math.sin(tiltAngle)); 
		float tiltY  = (float)(Math.sin(tiltAngle)); 
	}
	    
	PdfTemplate angularRect = null;		//The tilted square would be drawn onto the following PdfTemplate first.
	PdfTemplate slideLine = null;
	    
	/**
	 * 	The TabUtilitiesBundle constructor.
	 * @param cB The PdfContentByte attached to the pdf document that would be written to.
	 * @param fontName The font-name that would be used.
	 * @param fontSize The font-size that would be used.
	 * @param spacing The spacing value.
	 * @param pageSize The page-size.
	 */
	public TabUtilitiesBundle(PdfContentByte cB, String fontName, float fontSize, float spacing, Rectangle pageSize){
		this.cB = cB;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.spacing = spacing;
		this.pageSize = pageSize;
			
		//Instantiating the font object.
		try{
			bF = BaseFont.createFont(fontName,
					BaseFont.WINANSI, BaseFont.EMBEDDED);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
			
		//Instantiating the PdfTemplate and drawing the tilted square on it.
		angularRect = cB.createTemplate(fontSize/2f, fontSize/2f);                
		angularRect.rectangle(0.5f, 0.5f, fontSize/3f, fontSize/3f);
		angularRect.stroke();
		
		slideLine = cB.createTemplate(fontSize/2.0f, fontSize/2.0f);
		slideLine.moveTo(0f, fontSize/4.0f);
		slideLine.lineTo(fontSize/2.0f, fontSize/4.0f);
		slideLine.stroke();
			
	}

	/**
	 * Sets the font-size to the value contained in variable fontSize.
	 * @param fontSize Value that the size of the font would be set to.
	 */
	public void setFontSize(float fontSize){
		
		//Calling the setFontAndSize method of class PdfContentByte.
		cB.setFontAndSize(bF, fontSize);
	}

	/**
	 * Sets the width of the lines.
	 * @param lineWidth Value that the width of lines would be set to.
	 */
	public void setLineWidth(float lineWidth){
		cB.setLineWidth(lineWidth);
	}

	/**
	 * Writes the header in the pdf document.
	 * @param in The inputParser object responsible for the input.
	 * @param docWidth The width of the pdf document.
	 * @param yPos The vertical position with respect to which the header and sub-header would be written.
	 */
	public void processHeader(InputParser in, float docWidth, float yPos){
		setFontSize(20f);
		cB.beginText();
		
		//Writing the header.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, in.getTitle(), docWidth/2, yPos, 0);	
		cB.setFontAndSize(bF, 10f);
		
		//Writing the sub-header.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, in.getSubtitle(), docWidth/2, yPos - 15, 0);
		cB.endText();

		//Setting the font-size back to the user's preference.
		setFontSize(fontSize);
	}

	/**
	 * Processes input within triangular-brackets and displays it in the proper format on the pdf document.
	 * @param number The number that appears within triangular brackets.
	 * @param xPos The horizontal position where the output shall be written.  
	 * @param yPos The vertical position where the output shall be written.
	 */
	public void processInputWithinTriangle(String numericVal, float xPos, float yPos){
		cB.beginText();
		//Writing the number.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, numericVal, xPos, yPos, 0);	
		cB.endText();

		//The object that stores the data about how to draw the PdfTemplate named angularRect.
		TiltedShape tS = new TiltedShape();
		
		//Drawing the Pdftemplate named angularRect on the pdf document.
		cB.addTemplate(angularRect, tS.scaleX, tS.tiltX, tS.scaleY, tS.tiltY, xPos + bF.getWidthPoint(numericVal, fontSize), yPos + fontSize/2.5f);
		
		//Giving a slight touch-up to the look of the document by drawing a short line. 
		cB.moveTo(xPos + bF.getWidthPoint(numericVal, fontSize) + fontSize/2f, yPos+fontSize/2.5f);
		cB.lineTo(xPos + ((numericVal.length()+2)*spacing), yPos+fontSize/2.5f);
		cB.stroke();
	}

	/**
	 * Draws single bars at the beginning of a unit if the parent block was split in the middle.
	 * @param numLines The number of lines.
	 * @param xPos The horizontal position where the output shall be written.
	 * @param yPos The vertical position where the output shall be written.
	 */
	public void processSingleBars(int numLines, float xPos, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 5){
				cB.moveTo(xPos, yPos-fontSize/1.5f);
				cB.lineTo(xPos, yPos+fontSize/2.5f);
				
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
				
				yPos -= fontSize;
			}
		}
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Draws a line instead of a dash on the pdf document.
	 * @param xPos The horizontal position where the line shall be drawn.
	 * @param yPos The vertical position where the line shall be drawn.
	 */
	public void processDashes(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
		cB.stroke();
	}

	/**
	 * Processes a hammer-on that appears in the ascii file.
	 * @param xPos The horizontal position where the symbol shall be drawn.
	 * @param yPos The vertical position where the symbol shall be drawn. 
	 */
	public void processH(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Processes a pull-off that appears in the ascii file.
	 * @param xPos The horizontal position where the pull-off symbol shall be written.
	 * @param yPos The vertical position where the pull-off symbol shall be written.
	 */
	public void processP(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}
	
	public void processUnknown(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Writes a digit to the pdf file
	 * @param number The digit to be written to the pdf file.
	 * @param lineNum The lineNum from the unit that the input-digit came from.
	 * @param line The actual line from the unit that the input-digit came from. 
	 * @param charNum The character-number from the line that the input-digit came from.
	 * @param xPos The horizontal position where the output shall be written.
	 * @param yPos THe vertical position where the output shall be written.
	 * @param lastNumPos An ArrayList that stores the horizontal position of the last digit on the same line.
	 */
	public void processDigit(String numericVal, TextLine textLine, CharCoordinates coordinates){
		
		if(textLine.line.charAt(textLine.charNum-(numericVal.length()+1)) == 'p'){
			
			//If the pull-off can be processed on the same line
			if(coordinates.yPos == coordinates.lastNumYPos[textLine.lineNum]){
				cB.arc(coordinates.lastNumXPos[textLine.lineNum], coordinates.yPos+fontSize/2, coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize, 25, 130);
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p",(coordinates.lastNumXPos[textLine.lineNum] + coordinates.xPos + (numericVal.length()*spacing)/2)/2, coordinates.yPos+fontSize+(fontSize/10), 0);	
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
			}
			
			//Else if the digits associated with the pull-off are on two different lines.
			else{
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", coordinates.lastNumXPos[textLine.lineNum] + 10, coordinates.lastNumYPos[textLine.lineNum]+fontSize+(fontSize/10), 0);
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", coordinates.xPos - 10, coordinates.yPos + fontSize+(fontSize/10), 0); 
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
				
				cB.arc(coordinates.lastNumXPos[textLine.lineNum], coordinates.lastNumYPos[textLine.lineNum]+fontSize/2, coordinates.lastNumXPos[textLine.lineNum] + 30, coordinates.lastNumYPos[textLine.lineNum]+fontSize, 90, 90);
				cB.arc(coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize/2, coordinates.xPos - 30, coordinates.yPos+fontSize, 0, 90); 
				
			}
		}
		
		else if(textLine.line.charAt(textLine.charNum-(numericVal.length()+1)) == 'h'){
			
			//If the pull-off can be processed on the same line
			if(coordinates.yPos == coordinates.lastNumYPos[textLine.lineNum]){
				cB.arc(coordinates.lastNumXPos[textLine.lineNum], coordinates.yPos+fontSize/2, coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize, 25, 130);
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h",(coordinates.lastNumXPos[textLine.lineNum] + coordinates.xPos + (numericVal.length()*spacing)/2)/2, coordinates.yPos+fontSize+(fontSize/10), 0);	
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
			}
			
			//Else if the digits associated with the pull-off are on two different lines.
			else{
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", coordinates.lastNumXPos[textLine.lineNum] + 10, coordinates.lastNumYPos[textLine.lineNum]+fontSize+(fontSize/10), 0);
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", coordinates.xPos - 10, coordinates.yPos + fontSize+(fontSize/10), 0); 
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
				
				cB.arc(coordinates.lastNumXPos[textLine.lineNum], coordinates.lastNumYPos[textLine.lineNum]+fontSize/2, coordinates.lastNumXPos[textLine.lineNum] + 30, coordinates.lastNumYPos[textLine.lineNum]+fontSize, 90, 90);
				cB.arc(coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize/2, coordinates.xPos - 30, coordinates.yPos+fontSize, 0, 90); 
				
			}
		}


		//Writing the digit.
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, numericVal, coordinates.xPos, coordinates.yPos, 0);	
		cB.endText();

		//Saving the horizontal and vertical positions of the current digit for any later pull-offs that may appear.
		coordinates.lastNumXPos[textLine.lineNum] = coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2;
		coordinates.lastNumYPos[textLine.lineNum] = coordinates.yPos;

		//if a very short line is to be inserted between numbers with no gaps in between then 
		//the if statement needs to be removed
		//if(line.charAt(charNum) != ' '){
		cB.moveTo(coordinates.xPos + bF.getWidthPoint(numericVal, fontSize), coordinates.yPos+fontSize/2.5f);
		cB.lineTo(coordinates.xPos + ((numericVal.length())*spacing), coordinates.yPos+fontSize/2.5f);
		cB.stroke();
		//}

	}

	/**
	 * Processes a slide-up
	 * @param xPos The horizontal position where the slide-up character shall be drawn.
	 * @param yPos The vertical position where the slide-up character shall be drawn.
	 */
	public void processSlideUp(float xPos, float yPos){
		//cB.beginText();
		//cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "/", xPos+1, yPos+fontSize/6f, 0);	
		//cB.endText();
		TiltedShape tS = new TiltedShape();
		cB.addTemplate(slideLine, tS.scaleX, tS.tiltX, tS.scaleY, tS.tiltY, xPos, yPos+fontSize/2.5f);
		
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
		
	}

	/**
	 * To process the star character in the ascii file.
	 * @param xPos The horizontal position where the representation of the star character (a filled circle) shall be drawn.
	 * @param yPos The vertical position where the representation of the star character (a filled circle) shall be drawn.
	 */
	public void processStar(float xPos, float yPos, boolean nextCharIsBar){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
		cB.stroke();
		
		if(nextCharIsBar){
			cB.circle(xPos + (spacing - 5), yPos + fontSize/2.5f, fontSize/5f);
		}
		else{
			cB.circle(xPos - (spacing - 5), yPos + fontSize/2.5f, fontSize/5f);
		}
		cB.fillStroke();
		
	}

	/**
	 * Draws the horizontal lines before the block.
	 * @param numLines Number of lines in the block
	 * @param leftMargin The left margin
	 * @param yPos The vertical position with respect to which the lines would be drawn.
	 */
	public void processBeginningHorizontalLines(int numLines, float leftMargin, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 6){
				cB.moveTo(0, yPos+fontSize/2.5f);
				cB.lineTo(leftMargin, yPos+fontSize/2.5f);
				yPos -= fontSize;
			}
		}
	}

	/**
	 * Draws the horizontal lines after the block
	 * @param xPos The horizontal position that marks the beginning of the lines
	 * @param yPos The vertical position with respect to which the lines would be drawn.
	 */
	public void processEndingHorizontalLines(float xPos, float yPos){
		for(int lineNum = 0; lineNum < 6; lineNum++){
			if(lineNum < 6){
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(pageSize.getWidth(), yPos+fontSize/2.5f);
				yPos -= fontSize;
			}
		}
		cB.stroke();
	}

	/**
	 * Processes the bars that appear within a block. The last set of bars within a block are not processed by this method.
	 * @param curDIndex The index used to mark the current unit within the parent block.
	 * @param lineNum The current line-number.
	 * @param charNum The current character-number.
	 * @param barFreq The number of bars within a set of bars.
	 * @param repIndex The index of the repeat-information, if available.
	 * @param repNum The number of repeats, if available.
	 * @param xPos The horizontal position at which the first of the bars would be drawn. 
	 * @param yPos The vertical position where the bars would be drawn.
	 */
	public float processCurrentBars(TextLine textLine, StarBars starBars, float xPos, float yPos){
		int barNum = 0;
		
		//If the repeat message has to be printed
		if(textLine.lineNum == 0 && starBars.barFreq == 2 && starBars.repIndex > -1){
			String message = "Repeat " + starBars.repNum + " times";
			cB.beginText();
			cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, xPos-bF.getWidthPoint(message, fontSize), yPos+fontSize, 0);
			cB.endText();
		}

		for(; barNum < starBars.barFreq;){
			if(textLine.lineNum < 5){
				if(barNum == 0 && starBars.barFreq == 2 && starBars.starIndexes.size() > 0 && starBars.starIndexes.get(0) == textLine.charNum + 2){
					cB.stroke();
					cB.setLineWidth(2.0f);
					if(textLine.lineNum >= 4){
						starBars.starIndexes.remove(0).toString();
					}
				}
				else if(barNum ==1 && starBars.barFreq == 2 && starBars.starIndexes.size() > 0 && starBars.starIndexes.get(0) == textLine.charNum - 2){
					cB.stroke();
					cB.setLineWidth(2.0f);
					if(textLine.lineNum >= 4){
						starBars.starIndexes.remove(0).toString();
					}
				}

				cB.moveTo(xPos, yPos-fontSize/1.5f);
				cB.lineTo(xPos, yPos+fontSize/2.5f);
				cB.stroke();
			}
			barNum++;
			cB.setLineWidth(0.5f);


			//horizontal lines
			if(barNum < starBars.barFreq){
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(xPos + 5, yPos+fontSize/2.5f);
				xPos += 5;
			}
			else{
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
				xPos += spacing;
			}


			textLine.charNum++;
		}
		
		return xPos;
	}

	/**
	 * Draws the ending set of bars.
	 * @param curDIndex curDIndex The index used to mark the current unit within the parent block.
	 * @param numLines The number of lines in the block.
	 * @param charNum The current character-number.
	 * @param barFreq barFreq The number of bars within a set of bars.
	 * @param repIndex The index of the repeat-information, if available.
	 * @param repNum The number of repeats, if available.
	 * @param margin The position at which the first bar from the set would be drawn.
	 * @param xPos The horizontal position at which the first of the bars would be drawn. 
	 * @param yPos The vertical position where the bars would be drawn.
	 * @return
	 */
	public float processTrailingBars(ArrayList<Integer> starIndexes, int numLines, int charNum, int barFreq, int repIndex, String repNum, float margin, float xPos, float yPos){
		int numOfBars = barFreq;
		boolean removeElement = false;
		int chNum = charNum;
		//System.out.println(starIndexes.get(0) + " " + charNum);
		
		for(int lineNum = 0; lineNum < numLines; lineNum++){

			//Writing the repeat message on the Pdf document.
			if(lineNum == 0 && barFreq == 2 && repIndex > -1){
				String message = "Repeat " + repNum + " times";

				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, xPos-bF.getWidthPoint(message, fontSize), yPos+fontSize, 0);
				cB.endText();

			}

			for(int barNum = 0; barNum < numOfBars;){
				if(lineNum < 5){

				/*	if(barNum == 0 && barFreq == 2 && starIndexes.get(0) == charNum + 2){
						System.out.println("bar0");
						cB.stroke();
						cB.setLineWidth(2.0f);
						removeElement = true;
						//if(lineNum >= 6){
							//starIndexes.remove(0);
						//}
						
					}*/
					if(barNum ==1 && barFreq == 2 && starIndexes.size() > 0 && starIndexes.get(0) == chNum - 2){
						//System.out.println(starIndexes.get(0) + " " + chNum);
						cB.stroke();
						cB.setLineWidth(2.0f);
						removeElement = true;
						//if(lineNum >= 6){
						//	starIndexes.remove(0);
						//}
					}

					cB.moveTo(xPos, yPos-fontSize/1.5f);
					cB.lineTo(xPos, yPos+fontSize/2.5f);
					cB.stroke();
				}
				
				barNum++;
				chNum++;
				
				cB.setLineWidth(0.5f);
				//horizontal lines;
				if(barNum < barFreq){
					cB.moveTo(xPos, yPos+fontSize/2.5f);
					cB.lineTo(xPos + 5, yPos+fontSize/2.5f);
					xPos += 5;
				}
				//else{
				//	cB.moveTo(xPos, yPos+fontSize/2.5f);
				//	cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
				//	xPos += spacing;
				//}
			}

			cB.stroke();
			
			if(lineNum < 5){
				xPos = margin;
				yPos -= fontSize;
			}
			
			chNum = charNum;
		}
		
		if(removeElement){
			//System.out.println("removed");
			starIndexes.remove(0);
		}

		return xPos;
	}

	/**
	 * Processes input with chord-id.
	 * @param line The actual line from the unit that the input-digit came from.
	 * @param charNum The current character-number.
	 * @param xPos The horizontal position with respect to which the output shall be written.
	 * @param yPos The vertical position with respect to which the output shall be written.
	 */
	public void processChordID(String line, int charNum, float xPos, float yPos){
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(line.charAt(charNum)), xPos+1, yPos+fontSize/6f, 0);	
		cB.endText();
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}
}