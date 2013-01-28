import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.itextpdf.awt.geom.Dimension;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;


public class FirstPDF {

	String fileName
    = "C:/CSE2311/output.pdf";
	Document document;
	PdfWriter writer;
	Dimension screenSize;
	double width;
	double height;
	float fontSize = 8f;
	String line[] = new String[4]; //For holding the initial 3 lines of txt file
	String s = "";
	String s1 = "";
	
	
	public static void main(String[] args) throws DocumentException, IOException{
		FirstPDF pdf = new FirstPDF();
		pdf.createPDF();
	}
	
	public FirstPDF() throws DocumentException, IOException {
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		document = new Document();
		writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		document.open();
	}
	
	public void createPDF() throws DocumentException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader("C:/CSE2311/input.txt"));
		
		//Read in the first 4 lines of the txt file.
		for(int i = 0 ; i < 4 ; i++)
			line[i] = in.readLine();

		String title = line[0].substring(6);
		String subtitle = line[1].substring(9);
		int spacing = Integer.parseInt(line[2].substring(8));
		
		PdfContentByte cb = writer.getDirectContent();
		BaseFont bF = BaseFont.createFont();
		cb.setFontAndSize(bF, 20f);
		cb.setLineWidth(0.5f);
		
		cb.beginText();
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, title, 310, document.top(), 0);	
		cb.setFontAndSize(bF, 10f);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, subtitle, 315, document.top() - 15, 0);
		cb.endText();
		
		cb.stroke();
		
		cb.setFontAndSize(bF, fontSize);
		
		float xPos = document.left();
		float yPos = document.top() - 40;
		
		//Store the input 6 lines at a time
		for(int i = 1; i <= 6; i++)
		{
			if(i != 6)
				s += in.readLine() + "\n";
			else 
				s += in.readLine();
		}
		//Throw away empty line
		in.readLine();
		
		for(int i = 0, line = 1; i < s.length(); i++) {
			float f = 4.4480004f;
			System.out.print(s.charAt(i));
			if(s.charAt(i) == '-'){
				cb.moveTo(xPos, yPos+3);
				//float newXPos = xPos + f + 4.0f;
				float newXPos = xPos + f;
				cb.lineTo(newXPos, yPos+3);
				xPos = newXPos;
			}
				
			else if(s.charAt(i) != '|'){
				//xPos += 4.0f;
				cb.beginText();
				cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(s.charAt(i)), xPos, yPos, 0);	
				cb.endText();
				xPos += f;
			}
			
			else if(s.charAt(i) == '|'){
				if(line < 6) {
					cb.moveTo(xPos, yPos + 3);
					cb.lineTo(xPos, yPos - 5);
				}
			}
			cb.stroke();
			
			if(s.charAt(i) == '\n'){
				
				xPos = (int) document.left();
				yPos -= fontSize;
				//i++;
				line++;
			}	
		}
		

		float newLeft = xPos;
		yPos = document.top() - 40;
		
		for(int i = 1; i <= 6; i++)
		{
			if(i != 6)
				s1 += in.readLine() + "\n";
			else 
				s1 += in.readLine();
		}
		//Throw away empty line
		in.readLine();
		
		for(int i = 0, line = 1; i < s1.length(); i++) {
			float f = 4.4480004f;
			System.out.print(s1.charAt(i));
			if(s1.charAt(i) == '-'){
				cb.moveTo(xPos, yPos+3);
				//float newXPos = xPos + f + 4.0f;
				float newXPos = xPos + f;
				cb.lineTo(newXPos, yPos+3);
				xPos = newXPos;
			}
				
			else if(s1.charAt(i) != '|'){
				//xPos += 4.0f;
				cb.beginText();
				cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(s1.charAt(i)), xPos, yPos, 0);	
				cb.endText();
				xPos += f;
			}
			
			else if(s1.charAt(i) == '|'){
				if(line < 6) {
					cb.moveTo(xPos, yPos + 3);
					cb.lineTo(xPos, yPos - 5);
				}
			}
			cb.stroke();
			
			if(s1.charAt(i) == '\n'){
				
				xPos = newLeft;
				yPos -= fontSize;
				//i++;
				line++;
			}	
		}
		
		//
		document.close();
		System.exit(0);
	}

}