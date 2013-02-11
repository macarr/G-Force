import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jpedal.*;
import org.jpedal.fonts.FontMappings;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class GUI extends JFrame implements ActionListener{
	private JPanel centerPane;
	private JPanel rightPane;
	private JPanel buttonPane;
	private JPanel controlPane;
	private JButton open;
	private JButton convertPDF;
	private JButton save;
	private JButton close;
	private JComboBox<String> fontNames;
	private JComboBox<String> spacing;
	private JComboBox<String> fontSizes;
	private JInternalFrame pane;
	private JTextArea ASCIIDisplayArea;
	private ArrayList<ArrayList<String>> contents = null;

	String outputPath = "files/output.pdf";
	//String inputPath = "C:/CSE2311/input1.txt";
	String inputPath = "";
	String [] fonts = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC }; 

	String [] s = {"1", "2", "3", "4", "5", "6", "7", "8"};
	public GUI(){
		//buttonPane holds the buttons
		buttonPane = new JPanel(new GridLayout(4, 1, 0, 10));
		open = new JButton("Open File");
		open.addActionListener(this);
		convertPDF = new JButton("Convert to PDF");
		convertPDF.addActionListener(this);
		save = new JButton("Save File");
		close = new JButton("Close File");

		//Adding the buttons to buttonPane
		buttonPane.add(open);
		buttonPane.add(convertPDF);
		buttonPane.add(save);
		buttonPane.add(close);
		buttonPane.setBorder(BorderFactory.createLineBorder(Color.black));

		//controlPane holds the JComboBoxes
		controlPane = new JPanel(new GridLayout(3, 1));

		//fontName is a JComboBox
		fontNames = new JComboBox<String>(fonts);
		fontNames.setSelectedIndex(fontNames.getItemCount() - 1);
		fontNames.setBorder(BorderFactory.createTitledBorder("Select Font:"));
		
		//fontSize is a JComboBox
		fontSizes = new JComboBox<String>(s);
		fontSizes.setSelectedIndex(7);
		fontSizes.setBorder(BorderFactory.createTitledBorder("Font Size:"));

		//spacing is a JComboBox
		spacing = new JComboBox<String>(s);
		spacing.setSelectedIndex(4);
		spacing.setBorder(BorderFactory.createTitledBorder("Spacing:"));
		
		//Adding the JComboBoxes to controlPane
		controlPane.add(new JScrollPane(fontNames));
		controlPane.add(new JScrollPane(fontSizes));
		controlPane.add(new JScrollPane(spacing));
		controlPane.setBorder(BorderFactory.createLineBorder(Color.black));

		//rightPane holds the buttonPane and controlPane
		rightPane = new JPanel(new GridLayout(4, 1, 0, 20));
		rightPane.add(buttonPane);
		rightPane.add(controlPane);
		rightPane.setBorder(BorderFactory.createLineBorder(Color.black));

		//centerPane is where the preview section would be displayed
		centerPane = new JPanel();
		centerPane.setBackground(Color.GRAY);
		centerPane.setLayout(new BorderLayout());
		
		//textArea is going to show the initial ASCII file that the user opens
		ASCIIDisplayArea = new JTextArea(); 
		
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(rightPane, BorderLayout.EAST);
		con.add(centerPane, BorderLayout.CENTER);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 30);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//showASCIIFile shows the input file in its original ASCII format
	public void showASCIIFile(){
		try{
			BufferedReader in = new BufferedReader(new FileReader(inputPath));
			String line = in.readLine();
			while(line != null){
				ASCIIDisplayArea.append(line + "\n");
				line = in.readLine();
			}
			ASCIIDisplayArea.setSize(centerPane.getSize());
			centerPane.add(ASCIIDisplayArea);
			centerPane.validate();
			centerPane.repaint();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//loadFile gets called when the user presses the open-file button
	public File loadFile() {
		JFileChooser fc = new JFileChooser();
		File file = null;
		int returnVal = fc.showOpenDialog(centerPane);

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
		}

		return file;

	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == open){
			inputPath = loadFile().toString();
			//Once we have the input file, it is time to show the ASCII file in a JTextArea
			centerPane.removeAll();
			centerPane.revalidate();
			showASCIIFile();
		}
		else if(e.getSource() == convertPDF){
			try{
				contents = inputConverter(inputPath);
				//System.out.println(contents);
				new PdfMaker(contents, outputPath, fontNames.getSelectedItem().toString(), Float.parseFloat(fontSizes.getSelectedItem().toString()), Float.parseFloat(spacing.getSelectedItem().toString())).createPDF();
				
				centerPane.removeAll();
				centerPane.revalidate();
				//centerPane.repaint();
				PdfRenderer();
			}
			catch (Exception ex) {
			}	
		}
	}

	public void PdfRenderer() {
		PdfDecoder pdfView = new PdfDecoder(true);

		try {
			pdfView.openPdfFile(outputPath);
		    FontMappings.setFontReplacements();
		    pdfView.decodePage(1);
		    pdfView.setPageParameters(2, 1);
		}
		catch (Exception e) {
		}
		//getContentPane().remove(centerPane);
		//getContentPane().validate();
		centerPane.add(new JScrollPane(pdfView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
		centerPane.revalidate();
		centerPane.repaint();
	}
	
	public ArrayList<ArrayList<String>> inputConverter(String inputPath) throws DocumentException, IOException {
		ArrayList<ArrayList<String>> cont;

		ArrayList<String> block = new ArrayList<String>();
		String s = "";
		String cur = "";
		cont = new ArrayList<ArrayList<String>>();

		try{
		//Input file
		BufferedReader in = new BufferedReader(new FileReader(inputPath));

		block.add(in.readLine().substring(6));
		block.add(in.readLine().substring(9));
		block.add(in.readLine().substring(8));
		
		//cont.add(block);
		block = new ArrayList<String>();
		in.readLine();

		int line = 1;
		s = "";
	 	
		
		while((cur = in.readLine()) != null) {
	 		
			if(line <= 6) {
	 			block.add(new String(cur));
	 			line++;
			}
			else {
				cont.add(block);
				block = new ArrayList<String>();
	 			line = 1;
			}
		}
		
		//System.out.println(cont.size());
		in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return cont;
	}

	public static void main (String args[]) {
		GUI gui = new GUI();
		//gui.PdfRenderer();
	}
}