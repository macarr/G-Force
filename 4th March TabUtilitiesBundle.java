package com.java.pdfmaker;

import java.util.ArrayList;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;

public class TabUtilitiesBundle{
  
	//The PdfContentByte helps us to write to the document that it is attached to.
	PdfContentByte cB;
	
	//The font that is being used to write to the Pdf document.
	BaseFont bF;
	
	//The font-name being used to write to the Pdf document.
	String fontName;
	
	//The font-size being used to write to the Pdf document.
	float fontSize;
	
	//The spacing value
	float spacing;
	
	//The page-size of the Pdf document.
	Rectangle pageSize;
	
	//The variables needed to draw a tilted square to the Pdf file.
	float tiltAngle = (float)(-45 * (Math.PI / 180)); 
    float scaleX = (float)(Math.cos(tiltAngle)); 
    float scaleY = (float)(Math.cos(tiltAngle)); 
    float tiltX  = (float)(-Math.sin(tiltAngle)); 
    float tiltY  = (float)(Math.sin(tiltAngle)); 
    
    //The tilted square would be drawn onto the following PdfTemplate first.
    PdfTemplate angularRect = null;
    
	//The constructor for TabUtilitiesBundle.
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
	
	//Sets the font-size. A call is made to the setFontAndSize of the PDfContentByte class.
    public void setFontSize(float fontSize){
		cB.setFontAndSize(bF, fontSize);
	}
	
	//Sets the line-width.
    public void setLineWidth(float lineWidth){
		cB.setLineWidth(lineWidth);
	}
	
	//Writes the header of the Pdf document.
    public void processHeader(ArrayList<String> header, float docWidth, float yPos){
		
    	//First setting the font to 20 points.
    	setFontSize(20f);
		cB.beginText();
		
		//Writing the header.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, header.get(0), docWidth/2, yPos, 0);	
		cB.setFontAndSize(bF, 10f);
		
		//Writing the sub-header.
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, header.get(1), docWidth/2, yPos - 15, 0);
		cB.endText();
		
		//Setting the font-size back to what the user selected.
		setFontSize(fontSize);
	}
	
	public void processInputWithinTriangle(String line, String number, float xPos, float yPos){
		
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);	
		cB.endText();
		
		cB.addTemplate(angularRect, scaleX, tiltX, scaleY, tiltY, xPos + bF.getWidthPoint(number, fontSize), yPos + fontSize/2);
		cB.moveTo(xPos + bF.getWidthPoint(number, fontSize) + fontSize/3f, yPos+fontSize/2f);
		cB.lineTo(xPos + ((number.length()+2)*spacing), yPos+fontSize/2f);
		cB.stroke();
		
	}
	
	public void drawSingleBars(int numLines, float xPos, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 5){
				cB.moveTo(xPos, yPos-fontSize/2f);
				cB.lineTo(xPos, yPos+fontSize/2f);
				yPos -= fontSize;
			}
		}
	}
	
	public void processDashes(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		cB.stroke();
	}
	
	public void processH(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		cB.arc(xPos, yPos+fontSize/2, xPos + spacing, yPos+fontSize, 25, 130);
		cB.stroke();
		cB.setFontAndSize(bF, fontSize/2);
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "h", xPos+(spacing/3f), yPos+fontSize+(fontSize/10), 0);	
		cB.endText();
		cB.setFontAndSize(bF, fontSize);
	}
	
	public void processP(float xPos, float yPos){
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
	}
	
	public void processDigit(String number, int lineNum, String line, int charNum, float xPos, float yPos, float [] lastNumPos){
		
		
		if(line.charAt(charNum-(number.length()+1)) == 'p'){
			cB.arc(lastNumPos[lineNum], yPos+fontSize/2, xPos + (number.length()*spacing)/2, yPos+fontSize, 25, 130);
			cB.setFontAndSize(bF, fontSize/2);
			cB.beginText();
			cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "p",(lastNumPos[lineNum] + xPos + (number.length()*spacing)/2)/2, yPos+fontSize+(fontSize/10), 0);	
			cB.endText();
			cB.setFontAndSize(bF, fontSize);
		}
		
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);	
		cB.endText();
		
		lastNumPos[lineNum] = xPos + (number.length()*spacing)/2;
		
		//if a very short line is to be inserted between numbers with no gaps in between then 
		//the if statement needs to be removed
		//if(line.charAt(charNum) != ' '){
		cB.moveTo(xPos + bF.getWidthPoint(number, fontSize), yPos+fontSize/2f);
		cB.lineTo(xPos + ((number.length())*spacing), yPos+fontSize/2f);
		cB.stroke();
		//}
		
	}
	
	public void slideUp(float xPos, float yPos){
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "/", xPos+1, yPos+fontSize/6f, 0);	
		cB.endText();
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
	}
	
	public void processRepeat(float xPos, float yPos){
		cB.circle(xPos, yPos + fontSize/2f, fontSize/5f);
		cB.fillStroke();
		cB.moveTo(xPos + fontSize/5f, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
		cB.stroke();
		
	}
	
	//draws the horizontal lines before the block
	public void processBeginningHorizontalLines(int numLines, float leftMargin, float yPos){
		for(int lineNum = 0; lineNum < numLines; lineNum++){
			if(lineNum < 6){
				cB.moveTo(0, yPos+fontSize/2);
				//or document.left()
				cB.lineTo(leftMargin, yPos+fontSize/2);
				yPos -= fontSize;
			}
		}
	}
	
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
	
	public void processChordID(String line, int charNum, float xPos, float yPos){
		cB.beginText();
		cB.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(line.charAt(charNum)), xPos+1, yPos+fontSize/6f, 0);	
		cB.endText();
		cB.moveTo(xPos, yPos+fontSize/2f);
		cB.lineTo(xPos + spacing, yPos+fontSize/2f);
	}
}
