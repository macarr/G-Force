package com.tab2pdf.cse2311;

import javax.swing.JFileChooser;

public class FileManager {
	
	public JFileChooser fc;
	
	public FileManager() {
		
		fc = new JFileChooser();
		
	}
	
	public void loadFile(InputConverter in) {
		
		int returnVal = fc.showOpenDialog(in.subject);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			in.inputPath = fC.getSelectedFile().toString();
			in.contents = inputConverter(in.inputPath);
			in.subject.showAsciiFile(in.contents);
			return in;
		}
		
		return null;
		
	}

}
