import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
	float fontSize = 8f;
	float spacing;

	String s = 
			"|-------------------------|-------------------------|\n" +
			"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
			"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
			"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n" +
			"|-0-----------------------|-------------------------|\n" +
			"|-------------------------|-3-----------------------|";
	
	String s1 = "|-------------------------|-------------------------|\n" +
				"|-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n" +
				"|---2-----2-----3-----3---|---1-----2-----2-----1---|\n" +
				"|-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n" +
				"|-------------5-----------|-------------------------|\n" +
				"|-1-----------------------|-0-----------0-----------|";
	
	String s2 = "|-------------------0----0|-0-----------------0----0|\n" +
				"|-----------1-----1-----1-|-----3-----3-----3-----3-|\n" +
				"|-2---2---2-----2-----2---|---4-----4-----4-----4---|\n" +
				"|-2-2---2-----2-----2-----|-------2-----2-----2-----|\n" +
				"|-0-----------------------|-------------------------|\n" +
				"|-------------------------|-0-----------------------|";
	
	String s3 = "|-------------------0----0|-0-----------------0----0|\n" +
				"|-----------1-----1-----1-|-----3-----3-----3-----3-|\n" +
				"|-3---6---9-----3-----3---|---4-----4-----4-----4---|\n" +
				"|-2-3---1-----1-----4-----|-------2-----2-----2-----|\n" +
				"|-1-----------------------|-------------------------|\n" +
				"|-------------------------|-0-----------------------|";
	
	String s4 = "|------- ---- --------------|------------------ ------ -|\n" +
				"|-1---1- ---1 1-----1-----1-|------------------ ------ -|\n" +
				"|---0--- --0- ----0-----0---|-----5-----5-----5 8----5 8|\n" +
				"|-2----2 2--- --2-----2-----|---5-----5-----5-- --5--- -|\n" +
				"|-3----- ---- --------------|-6-----6-----6---- 6----- -|\n" +
				"|------- ---- ---3----------|-4---------------- 4----- -|";
	
	String s5 = "|-------------------0----0|-0-----------------0----0|\n" +
				"|-----------1-----1-----1-|-----4-----7-----2-----3-|\n" +
				"|-8---8---9-----8-----0---|---4-----4-----4-----4---|\n" +
				"|-2-3---1-----1-----4-----|-------2-----2-----2-----|\n" +
				"|-1-----------------------|-------------------------|\n" +
				"|-------------------------|-0-----------------------|";
	
	
	
	ArrayList<String> contents = new ArrayList<String>();
	
	//public static void main(String[] args) throws DocumentException, IOException{
		//FirstPDF pdf = new FirstPDF();
		//pdf.createPDF();
//	}
	
	//public PdfMaker(String filePath, ArrayList<String> contents, String fontName, float fontSize, float spacing) throws DocumentException, IOException {
	public PdfMaker(String filePath, String fontName, float fontSize, float spacing) throws DocumentException, IOException {

		this.filePath = filePath;
		this.fontName = fontName;
		this.spacing = spacing;
		this.fontSize = fontSize;
		this.contents = contents;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//width = screenSize.getWidth();
		//height = screenSize.getHeight();
		
		document = new Document();
		writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();
		
		contents.add(s);
		contents.add(s1);
		contents.add(s2);
		contents.add(s3);
		contents.add(s4);
		contents.add(s5);
	}
	
	public void createPDF() throws DocumentException, IOException {
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
		cb.stroke();
		cb.setFontAndSize(bF, fontSize);
		
		if(bF.getWidthPoint('3', fontSize) > spacing) {
			spacing = bF.getWidthPoint('3', fontSize);
		}
		
		float yIncrement = 40f;
		float yPos;
		
		
		float xPos = document.left();
		float margin = document.left();
		yPos = document.top() - yIncrement;
		
		for(int no = 0; no < contents.size();) {
			for(int i = 0, line = 1; i < contents.get(no).length(); i++) {
				if(contents.get(no).charAt(i) == '-'){
					
					cb.moveTo(xPos, yPos);
			
					float newXPos = xPos + spacing;
					cb.lineTo(newXPos, yPos);
					
					xPos = newXPos;
				}
				
				else if(Character.isDigit(contents.get(no).charAt(i))){
					cb.beginText();
					cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(contents.get(no).charAt(i)), xPos, yPos- fontSize/2f, 0);	
					cb.endText();
					xPos += spacing;
				}
			
				else if(contents.get(no).charAt(i) == '|'){
					if(line < 6) {
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos, yPos - fontSize);
					}
				}
				
				cb.stroke();
			
				if(contents.get(no).charAt(i) == '\n'){
					xPos = margin;
					yPos -= fontSize;
					line++;
				}	
			}
			
			no++;
			
			if(no >= contents.size()) {
				break;
			}
			
			if(xPos + ((contents.get(no).substring(0, contents.get(no).indexOf('\n') - 1).length()) * spacing) < document.right()) {
				//float fl = ((contents.get(no + 1).substring(0, contents.get(no + 1).indexOf('\n') - 1).length()) * spacing) < document.right();
				//System.out.println("fl: " + fl + ", " + "left: ")
				margin = xPos;
				yPos = document.top() - yIncrement;
			}
			else {
				xPos = document.left();
				yIncrement += (fontSize * 8);
				yPos = document.top() - yIncrement;
				margin = document.left();
			}
				

		
			/*for(int i = 0, line = 1; i < contents.get(no).length(); i++) {
			
				if(contents.get(no).charAt(i) == '-'){
					
					cb.moveTo(xPos, yPos);
			
					float newXPos = xPos + spacing;
					cb.lineTo(newXPos, yPos);
					
					xPos = newXPos;
				}
				
				else if(Character.isDigit(contents.get(no).charAt(i))){
					cb.beginText();
					cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(contents.get(no).charAt(i)), xPos, yPos- fontSize/2f, 0);	
					cb.endText();
					xPos += spacing;
				}
			
				else if(contents.get(no).charAt(i) == '|'){
					if(line < 6) {
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos, yPos - fontSize);
					}
				}
				
				cb.stroke();
			
				if(contents.get(no).charAt(i) == '\n'){
					xPos = document.left();
					yPos -= fontSize;
					line++;
				}	
			}
		
			no++;

			if(no >= contents.size()) {
				break;
			}
			float newLeft = xPos;
			yPos = document.top() - yIncrement;
			
			for(int i = 0, line = 1; i < contents.get(no).length(); i++) {
			
				if(contents.get(no).charAt(i) == '-'){
					cb.moveTo(xPos, yPos);
					float newXPos = xPos + spacing;
					cb.lineTo(newXPos, yPos);
					xPos = newXPos;
				}
				
				else if(Character.isDigit(contents.get(no).charAt(i))){
					cb.beginText();
					cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(contents.get(no).charAt(i)), xPos, yPos - fontSize/2f, 0);	
					cb.endText();
					xPos += spacing;
				}
			
				else if(contents.get(no).charAt(i) == '|'){
					if(line < 6) {
						cb.moveTo(xPos, yPos);
						cb.lineTo(xPos, yPos - fontSize);
					}
				}
				
				cb.stroke();
			
				if(contents.get(no).charAt(i) == '\n'){
				
					xPos = newLeft;
					yPos -= fontSize;
					line++;
				}	
			}
			
			yIncrement += (fontSize * 8);*/
		}
		
		document.close();
	}

}
