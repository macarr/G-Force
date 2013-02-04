package com.java.pdfmaker;

import java.awt.Dimension;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class PdfMaker {
  String filePath = null;
	Document document;
	PdfWriter writer;
	PdfContentByte cb;
	BaseFont bF;
	Dimension screenSize;
	double width;
	double height;
	String fontName = "";
	float fontSize;
	float spacing;
	ArrayList<ArrayList<String>> contents;

	
	
	public PdfMaker(ArrayList<ArrayList<String>> contents, String filePath, String fontName, float fontSize, float spacing) throws DocumentException, IOException {
		this.contents = contents;
		this.filePath = filePath;
		this.fontName = fontName;
		this.spacing = spacing;
		this.fontSize = fontSize;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//width = screenSize.getWidth();
		//height = screenSize.getHeight();
		
		document = new Document();
		writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();
		
	}
	
	public void createPDF() throws DocumentException, IOException {
		try{
		cb = writer.getDirectContent();
		bF = BaseFont.createFont(fontName,
		          BaseFont.WINANSI, BaseFont.EMBEDDED);
		cb.setFontAndSize(bF, 20f);
		cb.setLineWidth(0.5f);
	    cb.beginText();
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Moonlight Sonata", 310, document.top(), 0);	
		cb.setFontAndSize(bF, 10f);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Ludwig van Beethoven", 315, document.top() - 15, 0);
		cb.endText();
		//cb.stroke();
		cb.setFontAndSize(bF, fontSize);
		
		if(bF.getWidthPoint('3', fontSize) > spacing) {
			spacing = bF.getWidthPoint('3', fontSize);
		}
		
		float angle   = (float)(-45 * (Math.PI / 180)); 
        float fxScale = (float)(Math.cos(angle)); 
        float fyScale = (float)(Math.cos(angle)); 
        float fxRote  = (float)(-Math.sin(angle)); 
        float fyRote  = (float)(Math.sin(angle)); 

		PdfTemplate template = cb.createTemplate(fontSize/3f, fontSize/3f);                
        template.rectangle(0f, -fontSize/3f, fontSize/3f, fontSize/3f);
        template.stroke();
		
		float yIncrement = 40f;
		boolean distanceTillEnd = false;
		float margin = document.left();
		float yPos = document.top() - yIncrement;
		int i;
		float len;
		int split;
		int bIndex = 2;
		int sIndex = 0;
		boolean flag = true;
		boolean lineSplit = false;
		
		for(int no = 0; no < contents.size();){
			//System.out.println(no + " " + bIndex);
			float xPos = margin;	
			//len = (contents.get(no).get(0).length())*spacing;
			split = contents.get(no).get(0).indexOf('|', bIndex);
			for(i = 0; i < contents.get(no).size(); i++) {
				//if(no == 14){
					//System.out.println(no);
				//}
				for(int j = sIndex; j <= split; j++) {
					if(no == 14){
						//System.out.println(no);
					}
					if(contents.get(no).get(i).charAt(j) != '|') {
						flag = true;
					}
					if(contents.get(no).get(i).charAt(j) == '-'){
						cb.moveTo(xPos, yPos);
						float newXPos = xPos + spacing;
						cb.lineTo(newXPos, yPos);
						xPos = newXPos;
					}
					else if(contents.get(no).get(i).charAt(j) == '<'){
						String number = "";
						j++;
						for(; contents.get(no).get(i).charAt(j) != '>'; j++) {
							number += contents.get(no).get(i).charAt(j);
						}
						cb.beginText();
						cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos- fontSize/2f, 0);	
						xPos += (number.length()) * spacing;
						cb.endText();
						cb.addTemplate(template, fxScale, fxRote, fyRote, fyScale, xPos, yPos);
						
						xPos += fontSize/3f;
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos + (spacing - fontSize/3f), yPos);
						xPos += spacing - fontSize/3f;
						
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos + spacing, yPos);
						xPos += spacing;
					}	
					//if the character is not s
					else if(Character.isLetterOrDigit(contents.get(no).get(i).charAt(j)) && contents.get(no).get(i).charAt(j) != 's'){
						/*if(Character.isDigit(contents.get(no).get(i).charAt(j)) && contents.get(no).get(i).charAt(j - 1) == '|' && contents.get(no).get(i + 1).charAt(j) == '|') {
							cb.beginText();
							cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "Repeat " + contents.get(no).get(i).charAt(j) + " times", xPos, yPos+ fontSize/2f, 0);	
							cb.endText();
						}*/
						//else{
							cb.beginText();
							cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(contents.get(no).get(i).charAt(j)), xPos, yPos- fontSize/2f, 0);	
							cb.endText();
						//}
						xPos += spacing;
					}
					
					//if the character is s
					else if(contents.get(no).get(i).charAt(j) == 's') {
						cb.beginText();
						cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "/", xPos, yPos- fontSize/2f, 0);	
						cb.endText();
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos + spacing, yPos);
						xPos += spacing;
					}
					else if(contents.get(no).get(i).charAt(j) == '|'){
						if(i < 5) {
							//for any first double bar
							if(contents.get(no).get(i).length() > 1 && j == 0 && contents.get(no).get(i).charAt(j + 1) == '|' && flag){
								cb.stroke();
								cb.setLineWidth(2f);
								flag = false;
							//	cb.rectangle(xPos,  yPos, 5, 5);
								//cb.stroke();
							}
							
							//for any second double bar
							else if(j > 0 && contents.get(no).get(i).charAt(j - 1) == '|' && flag){
								cb.stroke();
								cb.setLineWidth(2f);
							}
							
							//if there is a number instead if a second |
							else if(j > 0 && j < contents.get(no).get(i).length() - 1) {
								if(Character.isDigit(contents.get(no).get(i).charAt(j + 1))){
									cb.beginText();
									cb.endText();
									//System.out.println(contents.get(no).get(i));
									contents.get(no).set(i, contents.get(no).get(i).substring(0, j + 1) + "|" + contents.get(no).get(i).substring(j+2, contents.get(no).get(i).length()));
									//System.out.println(contents.get(no).get(i));
								}
							}
							cb.moveTo(xPos, yPos);								
							cb.lineTo(xPos, yPos - fontSize);
							cb.stroke();
							cb.setLineWidth(0.5f);
						}
						
						distanceTillEnd = xPos + ((contents.get(no).get(i).indexOf('|', bIndex + 1) - bIndex) * spacing) < document.right();
						//System.out.println(no + " " + distanceTillEnd);
						//If block has been split because of insufficient space, then no horizontal line should be drawn
						//&& distanceTillEnd < document.right()
						
						if(j < contents.get(no).get(i).length() - 1) {
							cb.moveTo(xPos, yPos);								
							cb.lineTo(xPos + spacing, yPos);
							xPos += spacing;
							//lineSplit = !lineSplit;
						}
					}
					
					else if(contents.get(no).get(i).charAt(j) == '*') {
						cb.circle(xPos, yPos, fontSize/5);
						cb.fillStroke();
						xPos += spacing;
					}
				}
				if(i < 5){
					xPos = margin;
					yPos -= fontSize;
				}
				
			}
			
			i = 0;
			
			//if there are still characters to be output from the current string
			if(split < contents.get(no).get(i).length()-1){
				//cb.moveTo(xPos, yPos);								
				//cb.lineTo(xPos, yPos - fontSize);
				//cb.stroke();
				//System.out.println(split + " " + (contents.get(no).get(i).length()-1));
				bIndex = split + 1;
				sIndex = split + 1;
				if(bIndex < contents.get(no).get(i).length()) {
					//distance from the current | to the next |
					if(xPos + ((contents.get(no).get(i).indexOf('|', bIndex + 1) - bIndex) * spacing) < document.right()) {
						margin = xPos;
						yPos = document.top() - yIncrement;						
					}
					else {
						xPos = document.left();
						yIncrement += (fontSize * 8);						
						yPos = document.top() - yIncrement;
						margin = document.left();
						
						//cb.rectangle(xPos, yPos, 3, 3);
						//draw the left | of there was a block split
						float temp = yPos;
						for(int k = 0; k < 5; k++, temp -= fontSize) {
							cb.moveTo(xPos, temp);								
							cb.lineTo(xPos, temp - fontSize);
							cb.stroke();
							//cb.setLineWidth(0.5f);
						}
					}
				}
			}
			
			//if no more character remains from the current string
			else{
				bIndex = 2;
				sIndex = 0;
				i = 0;
				yPos = document.top() - yIncrement;
				no++;
				if(no >= contents.size()) {
					break;
				}
				
				
				
			//	len = (contents.get(no).get(0).length())*spacing;
				
				if(bIndex < contents.get(no).get(i).length()) {
					if(xPos + ((contents.get(no).get(i).indexOf('|', bIndex + 1) - bIndex) * spacing) < document.right()) {
						margin = xPos;
						yPos = document.top() - yIncrement;						
					}
					else {
						xPos = document.left();
						yIncrement += (fontSize * 8);						
						yPos = document.top() - yIncrement;
						margin = document.left();
						
						//draw the left | of there was a block split
						float temp = yPos;
						for(int k = 0; k < 5; k++, temp -= fontSize) {
							cb.moveTo(xPos, temp);								
							cb.lineTo(xPos, temp - fontSize);
							cb.stroke();
							//cb.setLineWidth(0.5f);
						}
					}
				}
			}
		}
		document.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	} 
}
