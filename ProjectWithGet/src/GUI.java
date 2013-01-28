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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
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

import com.itextpdf.text.pdf.BaseFont;



public class GUI extends JFrame {
	private JPanel rightPane;
	private JPanel buttonPane;
	private JPanel sliderPane;
	private JButton open;
	private JButton convertPDF;
	private JButton convertASCII;
	private JList<String> fontNames;
	private JSlider spacing;
	private JSlider font;
	private JInternalFrame pane;
	
	String filePath = "C:/CSE2311/output.pdf";
	String [] fonts = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
			BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC}; 
	
	public GUI(){
		buttonPane = new JPanel(new GridLayout(7, 1));
		open = new JButton("Open File");
		convertPDF = new JButton("Convert to PDF");
		convertASCII = new JButton("Convert to ASCII");
		
		buttonPane.add(new JLabel());
		buttonPane.add(open);
		buttonPane.add(new JLabel());
		buttonPane.add(convertPDF);
		buttonPane.add(new JLabel());
		buttonPane.add(convertASCII);
		buttonPane.add(new JLabel());
		buttonPane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		sliderPane = new JPanel(new GridLayout(3, 1, 0, 40));
		
		fontNames = new JList<String>(fonts);
		//fontNames.setSelectedIndex(0);
		fontNames.setBorder(BorderFactory.createTitledBorder("Select Font:"));
		fontNames.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Thread t = new Thread() {
					public void run() {
						try{
							if(fontNames.getSelectedValue() != null)
								new PdfMaker(filePath, fontNames.getSelectedValue().toString(), font.getValue(), spacing.getValue()).createPDF();
							else
								new PdfMaker(filePath, BaseFont.TIMES_ROMAN, font.getValue(), spacing.getValue()).createPDF();
					
							PdfRenderer();
						}
						catch(Exception ex) {
							//ex.printStackTrace();
						}
					}
				};
				t.start();
            }

			
		});
		
		font = new JSlider(1, 10);
		font.setBorder(BorderFactory.createTitledBorder("Font Size:"));
		font.setMajorTickSpacing(5);
		font.setMinorTickSpacing(1);
		font.setPaintTicks(true);
		font.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				Thread t = new Thread() {
					public void run() {
				
						try{
							if(fontNames.getSelectedValue() != null)
								new PdfMaker(filePath, fontNames.getSelectedValue().toString(), font.getValue(), spacing.getValue()).createPDF();
							else
								new PdfMaker(filePath, BaseFont.TIMES_ROMAN, font.getValue(), spacing.getValue()).createPDF();
							PdfRenderer();
						}
						catch(Exception ex) {
							//ex.printStackTrace();
						}
					}
				};
				t.start();
				
			}
		});
		
		spacing = new JSlider(1, 10);
		spacing.setBorder(BorderFactory.createTitledBorder("Spacing:"));
		spacing.setMajorTickSpacing(5);
		spacing.setMinorTickSpacing(1);
		spacing.setPaintTicks(true);
		spacing.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				Thread t = new Thread() {
					public void run() {
				
						try{
							if(fontNames.getSelectedValue() != null)
								new PdfMaker(filePath, fontNames.getSelectedValue().toString(), font.getValue(), spacing.getValue()).createPDF();
							else
								new PdfMaker(filePath, BaseFont.TIMES_ROMAN, font.getValue(), spacing.getValue()).createPDF();
					
							PdfRenderer();
						}
						catch(Exception ex) {
							//ex.printStackTrace();
						}
					}
				};
				t.start();
				
			}
		});
		
		
		//sliderPane.add(new JLabel());
		sliderPane.add(font);
		//sliderPane.add(new JLabel());
		sliderPane.add(spacing);
		
		//sliderPane.add(new JLabel());
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
	
	public static void main (String args[]) {
		GUI gui = new GUI();
		gui.PdfRenderer();
	}
}
