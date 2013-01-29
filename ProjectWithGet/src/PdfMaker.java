import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

public class PdfMaker {
	String filePath = null;
	String inputPath = null;
	String title = null;
	String subtitle = null;
	String s = "";
	String cur = "";
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
	ArrayList<String> contents = new ArrayList<String>();

	public PdfMaker(ArrayList<String> contents, String filePath, String fontName, float fontSize, float spacing) throws DocumentException, IOException {

		this.contents = contents;
		this.filePath = filePath;
		this.fontName = fontName;
		this.spacing = spacing;
		this.fontSize = fontSize;
		
		title = contents.get(0);
		subtitle = contents.get(1);
		spacing = Float.parseFloat((contents.get(2)));
		
		document = new Document();
		writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();


	}

	public void createPDF() throws DocumentException, IOException {
		
		cb = writer.getDirectContent();
		bF = BaseFont.createFont(fontName, BaseFont.WINANSI, BaseFont.EMBEDDED);
		cb.setFontAndSize(bF, 20f);
		cb.setLineWidth(0.5f);
	    cb.beginText();
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, title, 310, document.top(), 0);	
		cb.setFontAndSize(bF, 10f);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, subtitle, 315, document.top() - 15, 0);
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

		for(int no = 3; no < contents.size();) {
			
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
				margin = xPos;
				yPos = document.top() - yIncrement;
			}
			else {
				xPos = document.left();
				yIncrement += (fontSize * 8);
				yPos = document.top() - yIncrement;
				margin = document.left();
			}
		}
		document.close();
	}
}