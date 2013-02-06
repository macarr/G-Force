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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jpedal.*;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;



public class GUI extends JFrame implements ActionListener {
	
	private JPanel rightPane;
	private JPanel buttonPane;
	private JPanel sliderPane;
	private JButton open;
	private JButton save;
	private JButton convertPDF;
	private JButton convertASCII;
	private JList<String> fontNames;
	private JSlider spacing;
	private JSlider font;
	private JInternalFrame pane;
	private FileManager fm;
	private File readFile;
	private File toSave;

	
	/*
	 * Matt comment: these hard coded paths need to be replaced at some point by FileManager.loadFile()
	 * and FileManager.saveFile(). However, at this point I'm not sure how to untangle them in such a
	 * way that I don't totally bone the program. It LOOKS like all we need to do is just make it so 
	 * that the PDF Renderer/creator do not just start up when the GUI starts up, and instead wait for
	 * a Load/Save event (at which point we run the PDF renderer/creator respectively with the returned
	 * FileManager values as arguments). Unfortunately, I'm not sure where to start with that without
	 * destroying what makes everything work.
	 * 
	 * Please feel free to tell me I'm silly and do things your own way.
	 */
	String inputPath = null;
	String filePath = "files/output.pdf"; //Hard coded output path
	ArrayList<String> contents = new ArrayList<String>();
	
	//Array of fonts
	String [] fonts = { BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
						BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC }; 
	
	public GUI() throws DocumentException, IOException{
		
		fm = new FileManager();
		inputPath = fm.loadFile().getAbsolutePath();
		
		//Top-right button pane
		buttonPane = new JPanel(new GridLayout(7, 1));
		open = new JButton("Open File");
		open.addActionListener(this);
		save = new JButton("Save File");
		save.addActionListener(this);
		convertPDF = new JButton("Convert to PDF");
		convertASCII = new JButton("Convert to ASCII");
		contents = inputConverter(inputPath);
		
		buttonPane.add(open);
		buttonPane.add(new JLabel());
		buttonPane.add(save);
		buttonPane.add(new JLabel());
		buttonPane.add(convertPDF);
		buttonPane.add(new JLabel());
		buttonPane.add(convertASCII);
		buttonPane.setBorder(BorderFactory.createLineBorder(Color.black));

		
		//Middle-right font list
		sliderPane = new JPanel(new GridLayout(3, 1, 0, 40));

		fontNames = new JList<String>(fonts);
		fontNames.setBorder(BorderFactory.createTitledBorder("Select Font:"));
		

		//List listener
		fontNames.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refresh();
            }
		});

		//Middle-right font size slider
		font = new JSlider(1, 10);
		font.setBorder(BorderFactory.createTitledBorder("Font Size:"));
		font.setMajorTickSpacing(5);
		font.setMinorTickSpacing(1);
		font.setPaintTicks(true);
		
		//Font size slider listener
		font.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				refresh();
			}
		});

		//Bottom-right spacing slider
		spacing = new JSlider(1, 10);
		spacing.setBorder(BorderFactory.createTitledBorder("Spacing:"));
		spacing.setMajorTickSpacing(5);
		spacing.setMinorTickSpacing(1);
		spacing.setPaintTicks(true);
		
		//Spacing slider listener
		spacing.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				refresh();
			}
		});

		//Add the elements to the GUI
		sliderPane.add(font);
		sliderPane.add(spacing);
		sliderPane.setBorder(BorderFactory.createLineBorder(Color.black));
		rightPane = new JPanel(new GridLayout(3, 1));
		rightPane.add(buttonPane);
		rightPane.add(new JScrollPane(fontNames), BorderLayout.CENTER);
		rightPane.add(sliderPane);
		rightPane.setBorder(BorderFactory.createLineBorder(Color.black));

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(rightPane, BorderLayout.EAST);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/*
	 * Need to find some way to re-load the input file and re-create the output when the open button is fired
	 * 
	 */
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(open)) {
			
			readFile = fm.loadFile();
			inputPath = readFile.getAbsolutePath();
			refresh();
			
		}
		if(e.getSource().equals(save)) {
			
			toSave = new File(filePath);
			try {
			fm.saveFile(toSave);
			} catch(IOException z) {
				JOptionPane.showMessageDialog(this, "IOException (fix me later)", "IOException", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	private void refresh() {
		Thread t = new Thread() {
			public void run() {

				try{
					if(fontNames.getSelectedValue() != null)
						new PdfMaker(contents, filePath, fontNames.getSelectedValue().toString(), font.getValue(), spacing.getValue()).createPDF();
					else
						new PdfMaker(contents, filePath, BaseFont.TIMES_ROMAN, font.getValue(), spacing.getValue()).createPDF();

					PdfRenderer();
				}
				catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	public ArrayList<String> inputConverter(String inputPath) throws DocumentException, IOException {
		

		String s = "";
		String cur = "";
		ArrayList<String> contents = new ArrayList<String>();
		
		//Input file
		BufferedReader in = new BufferedReader(new FileReader(inputPath));

		contents.add((in.readLine()).substring(6));
		contents.add((in.readLine()).substring(9));
		contents.add((in.readLine()).substring(8));
		
		cur = in.readLine();
		while(cur != null)
		{
			for(int i = 1; i <= 6; i++)
			{
				if(i != 6)
				{
					s += in.readLine() + "\n";
				}
				else 
					s += in.readLine();
			}

			contents.add(s);
			s = "";
			cur = in.readLine();
		}
		in.close();
		return contents;
		
	}

	public void PdfRenderer() {
		
		/*try{
			PdfMaker pdf = new PdfMaker(filePath, fontNames.getSelectedItem().toString(), font.getValue(), spacing.getValue());
			//System.out.println(spacing.getValue());
			pdf.createPDF();
		}
		catch(Exception e){
			e.printStackTrace();
		}*/

		PdfDecoder pdfView = new PdfDecoder();

		try {
			pdfView.openPdfFile(filePath);
			pdfView.decodePage(1);
			pdfView.setPageParameters(2, 1);
		} 
		catch (Exception e) {
			//e.printStackTrace();
		}
		getContentPane().add(pdfView, BorderLayout.CENTER);
		validate();
	}

	public static void main (String[] args) {
		GUI gui = null;
		try {
			gui = new GUI();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.PdfRenderer();
	}
}