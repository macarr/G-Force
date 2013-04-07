package com.java.tabpdf;

import java.util.ArrayList;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.java.paramclasses.CharCoordinates;
import com.java.paramclasses.ReturnValues;
import com.java.paramclasses.StarBars;
import com.java.paramclasses.TextLine;
import com.java.paramclasses.TrailingCoordinates;
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
	    
	PdfTemplate angularRect = null;
	PdfTemplate slideLine = null;
	    
	/**
	 * 	The TabUtilitiesBundle constructor.
	 */
	public TabUtilitiesBundle(PdfContentByte cB, String fontName, float fontSize, float spacing, Rectangle pageSize){
		//Initializing some of the member objects.
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
			
		//Instantiating the PdfTemplate for a tilted square.
		angularRect = cB.createTemplate(fontSize/2f, fontSize/2f);                
		angularRect.rectangle(0.5f, 0.5f, fontSize/3f, fontSize/3f);
		angularRect.stroke();
		
		//Instantiating the PdfTemplate for a line representing a slide.
		slideLine = cB.createTemplate(fontSize/2.0f, fontSize/2.0f);
		slideLine.moveTo(0f, fontSize/4.0f);
		slideLine.lineTo(fontSize/2.0f, fontSize/4.0f);
		slideLine.stroke();
			
	}

	/**
	 * Sets the font-size to the value contained in variable fontSize.
	 */
	public void setFontSize(float fontSize){
		
		//Calling the setFontAndSize method of class PdfContentByte.
		cB.setFontAndSize(bF, fontSize);
	}

	/**
	 * Sets the width of the lines.
	 */
	public void setLineWidth(float lineWidth){
		cB.setLineWidth(lineWidth);
	}

	/**
	 * Writes the header in the pdf document.
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
	 */
	public void processSingleBars(int numLines, float xPos, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 5){
				//Drawing the vertical bars.
				cB.moveTo(xPos, yPos-fontSize/1.5f);
				cB.lineTo(xPos, yPos+fontSize/2.5f);
				
				//Drawing the horizontal bars.
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
				
				yPos -= fontSize;
			}
		}
		
		//Drawing one more horizontal bar for proper alignment.
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Draws a line instead of a dash on the pdf document.
	 */
	public void processDashes(float xPos, float yPos){
		//Drawing a line representing a dash
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
		cB.stroke();
	}

	/**
	 * Processes a hammer-on that appears in the ascii file.
	 */
	public void processH(float xPos, float yPos){
		//Drawing a line. The actual hammer-on is processed within processDigit.
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Processes a pull-off that appears in the ascii file.
	 */
	public void processP(float xPos, float yPos){
		//Drawing a line. The actual pull-off is processed within processDigit.
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}
	
	/**
	 * Writes a straight line in place of a character that is not known.
	 */
	public void processUnknown(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Writes a number.
	 */
	public ReturnValues processDigit(String numericVal, TextLine textLine, CharCoordinates coordinates){
		
		if(textLine.line.charAt(textLine.charNum-(numericVal.length()+1)) == 'p'){
			
			//If the pull-off can be processed on the same line
			if(coordinates.yPos == coordinates.lastNumYPos){
				cB.arc(coordinates.lastNumXPos, coordinates.yPos+fontSize/2, coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize, 25, 130);
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p",(coordinates.lastNumXPos + (coordinates.xPos - coordinates.lastNumXPos)/2f), coordinates.yPos+fontSize+(fontSize/10), 0);	
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
			}
			
			//Else if the numbers associated with the pull-off are on two different lines.
			else{
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", coordinates.lastNumXPos + 10, coordinates.lastNumYPos+fontSize+(fontSize/10), 0);
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p", coordinates.xPos - 10, coordinates.yPos + fontSize+(fontSize/10), 0); 
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
				
				cB.arc(coordinates.lastNumXPos, coordinates.lastNumYPos+fontSize/2, coordinates.lastNumXPos + 30, coordinates.lastNumYPos+fontSize, 90, 90);
				cB.arc(coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize/2, coordinates.xPos - 30, coordinates.yPos+fontSize, 0, 90); 
				
			}
		}
		
		
		else if(textLine.line.charAt(textLine.charNum-(numericVal.length()+1)) == 'h'){
			
			//If the hammer-on can be processed on the same line
			if(coordinates.yPos == coordinates.lastNumYPos){
				cB.arc(coordinates.lastNumXPos, coordinates.yPos+fontSize/2, coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize, 25, 130);
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h",(coordinates.lastNumXPos + (coordinates.xPos - coordinates.lastNumXPos)/2f), coordinates.yPos+fontSize+(fontSize/10), 0);	
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
			}
			
			//Else if the numbers associated with the hammer-on are on two different lines.
			else{
				cB.setFontAndSize(bF, fontSize/2);
				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", coordinates.lastNumXPos + 10, coordinates.lastNumYPos+fontSize+(fontSize/10), 0);
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", coordinates.xPos - 10, coordinates.yPos + fontSize+(fontSize/10), 0); 
				cB.endText();
				cB.setFontAndSize(bF, fontSize);
				
				cB.arc(coordinates.lastNumXPos, coordinates.lastNumYPos+fontSize/2, coordinates.lastNumXPos + 30, coordinates.lastNumYPos+fontSize, 90, 90);
				cB.arc(coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos+fontSize/2, coordinates.xPos - 30, coordinates.yPos+fontSize, 0, 90); 
				
			}
		}

		//Writing the number.
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, numericVal, coordinates.xPos, coordinates.yPos, 0);	
		cB.endText();

		//Saving the horizontal and vertical positions of the current number for any later pull-offs that may appear.
		ReturnValues returnValues = new ReturnValues(coordinates.xPos + (bF.getWidthPoint(numericVal, fontSize))/2, coordinates.yPos);
		
		//if a very short line is to be inserted between numbers with no gaps in between then 
		//the if statement needs to be removed
		cB.moveTo(coordinates.xPos + bF.getWidthPoint(numericVal, fontSize), coordinates.yPos+fontSize/2.5f);
		cB.lineTo(coordinates.xPos + ((numericVal.length())*spacing), coordinates.yPos+fontSize/2.5f);
		cB.stroke();
		
		return returnValues;
	}

	/**
	 * Processes a slide-up
	 */
	public void processSlideUp(float xPos, float yPos){
		TiltedShape tS = new TiltedShape();
		
		//Drawing the PdfContentByte to the Pdf.
		cB.addTemplate(slideLine, tS.scaleX, tS.tiltX, tS.scaleY, tS.tiltY, xPos, yPos+fontSize/2.5f);
		
		//Drawing a horizontal line.
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
	}

	/**
	 * Processes the star character in the ascii file.
	 */
	public void processStar(float xPos, float yPos, boolean nextCharIsBar){
		//Horizontal lines
		cB.moveTo(xPos, yPos+fontSize/2.5f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2.5f);
		cB.stroke();
		
		//Makes sure that the gap between the filled-circle and the next '|' is constant regardless of the spacing value. 
		if(nextCharIsBar){
			cB.circle(xPos + (spacing - 5), yPos + fontSize/2.5f, fontSize/5f);
		}
		
		//Makes sure that the gap between the filled-circle and the previous '|' is constant regardless of the spacing value.
		else{
			cB.circle(xPos - (spacing - 5), yPos + fontSize/2.5f, fontSize/5f);
		}
		cB.fillStroke();
	}

	/**
	 * Draws the horizontal lines before the block.
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
	 * Process the bars within blocks.
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
				//In case of double bars, making the first one a bit thicker if the associated star character is yet to follow.
				if(barNum == 0 && starBars.barFreq == 2 && starBars.starIndexes.size() > 0 && starBars.starIndexes.get(0) == textLine.charNum + 2){
					cB.stroke();
					cB.setLineWidth(3.0f);
					if(textLine.lineNum >= 4){
						starBars.starIndexes.remove(0).toString();
					}
				}
				
				//In case of double bars, making the second one a bit thicker if the associated star character has already been seen.
				else if(barNum ==1 && starBars.barFreq == 2 && starBars.starIndexes.size() > 0 && starBars.starIndexes.get(0) == textLine.charNum - 2){
					cB.stroke();
					cB.setLineWidth(3.0f);
					if(textLine.lineNum >= 4){
						starBars.starIndexes.remove(0).toString();
					}
				}

				//Vertical lines.
				cB.moveTo(xPos, yPos-fontSize/1.5f);
				cB.lineTo(xPos, yPos+fontSize/2.5f);
				cB.stroke();
			}
			barNum++;
			cB.setLineWidth(0.5f);


			//Horizontal lines
			if(barNum < starBars.barFreq){
				cB.moveTo(xPos, yPos+fontSize/2.5f);
				cB.lineTo(xPos + 5, yPos+fontSize/2.5f);
				xPos += 5;
			}
			
			//Horizontal lines
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
	 * Processes the last set of bars in a block.
	 */
	public float processTrailingBars(StarBars starBars, int numLines, int charNum, TrailingCoordinates trailCdrs){
		int numOfBars = starBars.barFreq;
		boolean removeElement = false;
		int chNum = charNum;
		
		for(int lineNum = 0; lineNum < numLines; lineNum++){

			//Writing the repeat message on the Pdf document.
			if(lineNum == 0 && starBars.barFreq == 2 && starBars.repIndex > -1){
				String message = "Repeat " + starBars.repNum + " times";

				cB.beginText();
				cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, trailCdrs.xPos-bF.getWidthPoint(message, fontSize), trailCdrs.yPos+fontSize, 0);
				cB.endText();
			}

			for(int barNum = 0; barNum < numOfBars;){
				if(lineNum < 5){
					//In case of double bars, making the second one a bit thicker if the associated star character has already been seen.
					if(barNum ==1 && starBars.barFreq == 2 && starBars.starIndexes.size() > 0 && starBars.starIndexes.get(0) == chNum - 2){
						cB.stroke();
						cB.setLineWidth(3.0f);
						removeElement = true;
					}

					cB.moveTo(trailCdrs.xPos, trailCdrs.yPos-fontSize/1.5f);
					cB.lineTo(trailCdrs.xPos, trailCdrs.yPos+fontSize/2.5f);
					cB.stroke();
				}
				
				barNum++;
				chNum++;
				
				cB.setLineWidth(0.5f);
				
				//Horizontal lines;
				if(barNum < starBars.barFreq){
					cB.moveTo(trailCdrs.xPos, trailCdrs.yPos+fontSize/2.5f);
					cB.lineTo(trailCdrs.xPos + 5, trailCdrs.yPos+fontSize/2.5f);
					trailCdrs.xPos += 5;
				}
			}

			cB.stroke();
			
			//Reinitialing the horizontal position.
			if(lineNum < 5){
				trailCdrs.xPos = trailCdrs.margin;
				trailCdrs.yPos -= fontSize;
			}
			
			chNum = charNum;
		}
		
		//If the assotiated '*' has already been printed, then its index should be removed.
		if(removeElement){
			starBars.starIndexes.remove(0);
		}

		return trailCdrs.xPos;
	}
	
	public void processChordID(int lineNum, String line, int charNum, float xPos, float yPos){
		//cB.beginText();
		//cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(line.charAt(charNum)), xPos, yPos+fontSize/6f, 0);	
		//cB.endText();
		if(lineNum < 5){
			cB.moveTo(xPos, yPos-fontSize/1.5f);
			cB.lineTo(xPos, yPos+fontSize/2.5f);
		}
	}
}