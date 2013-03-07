package com.java.pdfmaker;

import java.util.ArrayList;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;

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

	private class TiltedSquare{
		
		//variables related to the PdfTemplate.
		float tiltAngle = (float)(-45 * (Math.PI / 180));
		float scaleX = (float)(Math.cos(tiltAngle)); 		
		float scaleY = (float)(Math.cos(tiltAngle)); 
		float tiltX  = (float)(-Math.sin(tiltAngle)); 
		float tiltY  = (float)(Math.sin(tiltAngle)); 
	}
	    
	PdfTemplate angularRect = null;		//The tilted square would be drawn onto the following PdfTemplate first.
	    
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
		angularRect = cB.createTemplate(fontSize/4f, fontSize/4f);                
		angularRect.rectangle(0f, 0f, fontSize/4f, fontSize/4f);
		angularRect.stroke();
			
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
	public void processHeader(inputParser in, float docWidth, float yPos){
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
	public void processInputWithinTriangle(String number, float xPos, float yPos){
		cB.beginText();
		//Writing the number.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);	
		cB.endText();

		//The object that stores the data about how to draw the PdfTemplate named angularRect.
		TiltedSquare tS = new TiltedSquare();
		
		//Drawing the Pdftemplate named angularRect on the pdf document.
		cB.addTemplate(angularRect, tS.scaleX, tS.tiltX, tS.scaleY, tS.tiltY, xPos + bF.getWidthPoint(number, fontSize), yPos + fontSize/2);
		
		//Giving a slight touch-up to the look of the document by drawing a short line. 
		cB.moveTo(xPos + bF.getWidthPoint(number, fontSize) + fontSize/3f, yPos+fontSize/2f);
		cB.lineTo(xPos + ((number.length()+2)*spacing), yPos+fontSize/2f);
		cB.stroke();

	}

	/**
	 * Draws single bars at the beginning of a unit if the parent block was split in the middle.
	 * @param numLines The number of lines.
	 * @param xPos The horizontal position where the output shall be written.
	 * @param yPos The vertical position where the output shall be written.
	 */
	public void drawSingleBars(int numLines, float xPos, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 5){
				cB.moveTo(xPos, yPos-fontSize/2f);
				cB.lineTo(xPos, yPos+fontSize/2f);
				yPos -= fontSize;
			}
		}
	}

	/**
	 * Draws a line instead of a dash on the pdf document.
	 * @param xPos The horizontal position where the line shall be drawn.
	 * @param yPos The vertical position where the line shall be drawn.
	 */
	public void processDashes(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		cB.stroke();
	}

	/**
	 * Processes a hammer-on that appears in the ascii file.
	 * @param xPos The horizontal position where the symbol shall be drawn.
	 * @param yPos The vertical position where the symbol shall be drawn. 
	 */
	public void processH(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		
		//Drawing the arc symbolizing the hammer-on.
		cB.arc(xPos, yPos+fontSize/2, xPos + spacing, yPos+fontSize, 25, 130);
		cB.stroke();
		cB.setFontAndSize(bF, fontSize/2);
		cB.beginText();
		
		//Writing the 'h' character on the arc.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", xPos+(spacing/3f), yPos+fontSize+(fontSize/10), 0);	
		cB.endText();
		cB.setFontAndSize(bF, fontSize);
	}

	/**
	 * Processes a pull-off that appears in the ascii file.
	 * @param xPos The horizontal position where the pull-off symbol shall be written.
	 * @param yPos The vertical position where the pull-off symbol shall be written.
	 */
	public void processP(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
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
	public void processDigit(String number, int lineNum, String line, int charNum, float xPos, float yPos, float [] lastNumXPos, float [] lastNumYPos, float rightMargin){
		if(line.charAt(charNum-(number.length()+1)) == 'p'){
			
			//If the pull-off can be processed on the same line
			if(yPos == lastNumYPos[lineNum]){
				cB.arc(lastNumXPos[lineNum], yPos+fontSize/2, xPos + (number.length()*spacing)/2, yPos+fontSize, 25, 130);
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p",(lastNumXPos[lineNum] + xPos + (number.length()*spacing)/2)/2, yPos+fontSize+(fontSize/10), 0);	
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
			}
			
			//Else if the digits associated with the pull-off are on two different lines.
			else{
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", lastNumXPos[lineNum] + 10, lastNumYPos[lineNum]+fontSize+(fontSize/10), 0);
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", xPos - 10, yPos + fontSize+(fontSize/10), 0); 
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
				
				cB.arc(lastNumXPos[lineNum], lastNumYPos[lineNum]+fontSize/2, lastNumXPos[lineNum] + 30, lastNumYPos[lineNum]+fontSize, 90, 90);
				cB.arc(xPos + (number.length()*spacing)/2, yPos+fontSize/2, xPos - 30, yPos+fontSize, 0, 90); 
				
			}
		}

		//Writing the digit.
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);	
		cB.endText();

		//Saving the horizontal and vertical positions of the current digit for any later pull-offs that may appear.
		lastNumXPos[lineNum] = xPos + (number.length()*spacing)/2;
		lastNumYPos[lineNum] = yPos;

		//if a very short line is to be inserted between numbers with no gaps in between then 
		//the if statement needs to be removed
		//if(line.charAt(charNum) != ' '){
		cB.moveTo(xPos + bF.getWidthPoint(number, fontSize), yPos+fontSize/2f);
		cB.lineTo(xPos + ((number.length())*spacing), yPos+fontSize/2f);
		cB.stroke();
		//}

	}

	/**
	 * Processes a slide-up
	 * @param xPos The horizontal position where the slide-up character shall be drawn.
	 * @param yPos The vertical position where the slide-up character shall be drawn.
	 */
	public void slideUp(float xPos, float yPos){
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "/", xPos+1, yPos+fontSize/6f, 0);	
		cB.endText();
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
	}

	/**
	 * To process the star character in the ascii file.
	 * @param xPos The horizontal position where the representation of the star character (a filled circle) shall be drawn.
	 * @param yPos The vertical position where the representation of the star character (a filled circle) shall be drawn.
	 */
	public void processStar(float xPos, float yPos){
		cB.circle(xPos, yPos + fontSize/2f, fontSize/5f);
		cB.fillStroke();
		cB.moveTo(xPos + fontSize/5f, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		cB.stroke();
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
				cB.moveTo(0, yPos+fontSize/2);
				cB.lineTo(leftMargin, yPos+fontSize/2);
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
				cB.moveTo(xPos, yPos+fontSize/2);
				cB.lineTo(pageSize.getWidth(), yPos+fontSize/2);
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
	public void processCurrentBars(int curDIndex, int lineNum, int charNum, int barFreq, int repIndex, char repNum, float xPos, float yPos){
		int barNum = 0;
		//If the repeat message has to be printed
		if(lineNum == 0 && barFreq == 2 && repIndex > -1){
			String message = "Repeat " + repNum + " times";
			cB.beginText();
			cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, xPos-bF.getWidthPoint(message, fontSize), yPos+fontSize, 0);
			cB.endText();
		}

		for(; barNum < barFreq;){
			if(lineNum < 5){
				if(barNum == 0 && curDIndex == 0 && barFreq == 2){
					cB.stroke();
					cB.setLineWidth(2.0f);
				}
				else if(barNum ==1 && curDIndex > 0 && barFreq == 2){
					cB.stroke();
					cB.setLineWidth(2.0f);
				}

				cB.moveTo(xPos, yPos-fontSize/2f);
				cB.lineTo(xPos, yPos+fontSize/2f);
				cB.stroke();
			}
			barNum++;
			cB.setLineWidth(0.5f);


			//horizontal lines
			cB.moveTo(xPos, yPos+fontSize/2);
			cB.lineTo(xPos + spacing, yPos+fontSize/2);
			xPos += spacing;


			charNum++;
		}
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
	public float processTrailingBars(int curDIndex, int numLines, int charNum, int barFreq, int repIndex, char repNum, float margin, float xPos, float yPos){
		int numOfBars = barFreq;

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

					if(barNum == 0 && curDIndex == 0 && barFreq == 2){
						cB.stroke();
						cB.setLineWidth(2.0f);
					}
					else if(barNum ==1 && curDIndex > 0 && barFreq == 2){
						cB.stroke();
						cB.setLineWidth(2.0f);
					}

					cB.moveTo(xPos, yPos-fontSize/2);
					cB.lineTo(xPos, yPos+fontSize/2);
					cB.stroke();
				}
				barNum++;
				cB.setLineWidth(0.5f);
				//horizontal lines;
				if(barNum < barFreq){
					cB.moveTo(xPos, yPos+fontSize/2);
					cB.lineTo(xPos + spacing, yPos+fontSize/2);
					xPos += spacing;
				}

				//charNum++;
			}

			cB.stroke();
			if(lineNum < 5){
				xPos = margin;
				yPos -= fontSize;
			}
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
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
	}
}