import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;


public class FileGrabber extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;
	
	public FileGrabber() {
		super(new BorderLayout());
		
		//Create the log first, because the action listeners need to refer to it.
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
		
		//create a file chooser
		fc = new JFileChooser();
		
		//create the open button
		openButton = new JButton("Open a File...", createImageIcon("Images/Open16.gif"));
		openButton.addActionListener(this);
		
		//create the save button
		saveButton = new JButton("Save a File...", createImageIcon("images/Save16.gif"));
		saveButton.addActionListener(this);
		
		//for layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);
		
		//add the buttons and the log to this panel
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//Handle open button action
		if(e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(FileGrabber.this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//this is where a real application would open the file
				log.append("Opening: " + file.getName() + "." + newline);
			} else {
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
			
		//handle save button action
		} else if (e.getSource() == saveButton) {
			int returnVal = fc.showSaveDialog(FileGrabber.this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//this is where a real application would save the file.
				log.append("Saving: " + file.getName() + "." + newline);
			} else {
				log.append("Save command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}
	
	//returns an ImageIcon, or null if the path was invalid
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FileGrabber.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	//create the GUI and show it. For thread safety, this method should be invoked from the event dispatch thread.
	private static void createAndShowGUI() {
		
		//create and set up the window
		JFrame frame = new JFrame("FileGrabber");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add content to the window
		frame.add(new FileGrabber());
		
		//display the window
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		//schedule a job for the event dispatch thread: creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal",  Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

}
