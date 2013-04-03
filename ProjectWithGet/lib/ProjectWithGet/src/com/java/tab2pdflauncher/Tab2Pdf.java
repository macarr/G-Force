package com.java.tab2pdflauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import com.java.tabui.TabFileManager;
import com.java.tabui.TabUIMainWindow;

public class Tab2Pdf {
	public static void main(String args[]){
		String errorLog = ""+TabFileManager.getTempDir()+"T2PDFErr.txt";
		File errorFile = new File(errorLog);
		try {
			if(!(errorFile.exists()))
				errorFile.createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(errorLog, true));
			Date date = new Date();
			out.println("["+date+"]");
			System.setErr(out);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		new TabUIMainWindow();
	}
}
