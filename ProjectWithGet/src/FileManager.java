import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class FileManager extends JPanel {
	
	private JFileChooser fc;
	
	/*
	 * Launches JFileChooser dialog and returns the chosen File.
	 * 
	 * @return file chosen by JFileChooser
	 */
	
	public FileManager() {
		
	}
	
	public File loadFile() {
		
		int returnVal = fc.showOpenDialog(FileManager.this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		}
		
		return null;
		
	}
	
	/*
	 * Launches JFileChooser dialog and writes the input file to the chosen output.
	 * 
	 * @param file The File to be saved
	 * 
	 * @throws IOException if Input or Output Streams cannot read or write
	 * 
	 */
	
	public void saveFile(File file) throws IOException {
		
		InputStream in = null;
		OutputStream out = null;
		
		int returnVal = fc.showSaveDialog(FileManager.this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File target = fc.getSelectedFile();
		
			try {
				
				in = new FileInputStream(file);
				out = new FileOutputStream(target);
				
				byte[] buffer = new byte[1024];
				int length;
				
				while((length = in.read(buffer)) > 0) { //file copy loop
					
					out.write(buffer, 0, length);
					
				} //file copy loop
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
				
			} finally {
				
				if(out != null)
					out.close();
				
				if(in != null)					
					in.close();				
			}
		}
	}

}
