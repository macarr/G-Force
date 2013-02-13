//package com.java.pdfmaker;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

import javax.swing.AbstractAction;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jpedal.*;
import org.jpedal.fonts.FontMappings;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class GUI extends JFrame implements ActionListener{
  private Dimension screenSize;
private JPanel centerPane;
private JPanel rightPane;
private JPanel buttonPane;
private JPanel controlPane;
private JButton open;
private JButton savePDF;
private JButton save;
private JButton close;
private JComboBox<String> fontNames;
private JComboBox<String> spacing;
private JComboBox<String> fontSizes;
private JInternalFrame pane;
private JTextArea ASCIIDisplayArea;
private JLabel statusLabel;

private ArrayList<ArrayList<String>> contents = null;
private ArrayList <String> header = null;

private String outputPath = "";
private String inputPath = "";
private String [] fonts = {BaseFont.HELVETICA, BaseFont.HELVETICA_OBLIQUE, BaseFont.HELVETICA_BOLD, BaseFont.HELVETICA_BOLDOBLIQUE,
BaseFont.TIMES_ROMAN, BaseFont.TIMES_ITALIC, BaseFont.TIMES_BOLD, BaseFont.TIMES_BOLDITALIC };

private String [] s = {"1", "2", "3", "4", "5", "6", "7", "8"};
private int widthFactor = 7;

File source = null;
File destination = null;

//The class that helps bind the F1 key to the help-screen.
private class HelpAction extends AbstractAction{
public void actionPerformed( ActionEvent tf ){
new HelpWindow();
}
}

//To show the help-screen.
private class HelpWindow extends JFrame{
public HelpWindow(){
JTextPane helpPane = new JTextPane();
helpPane.setText("To save a file as PDF:\n1) First open an Ascii file by clicking on 'Open Ascii File'.\n" +
"2) Select your desired font, font-size, and spacing.\n3) Click on 'Save as PDF & Preview'." +
"\n\nTo make a change to the font settings or spacing:\n1) Choose the desired settings from the drop down menus.\n" +
"2) Click on 'Save as PDF & Preview'.");
helpPane.setEditable(false);
this.getContentPane().setLayout(new BorderLayout());
this.getContentPane().add(helpPane);
this.setSize(400, 500);
this.setVisible(true);
}
}

public GUI(){
setTitle("G-Force's Ascii to Pdf Guitar Tab Converter");

screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//buttonPane holds the 'open' button and the controlPane (which holds the JComboBoxes and the 'savePDF'
//button
buttonPane = new JPanel(new BorderLayout());
open = new JButton("Open Ascii File");
open.addActionListener(this);
open.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
open.getActionMap().put("showHelp", new HelpAction());
open.setPreferredSize(new Dimension(screenSize.width/widthFactor, screenSize.height/17));

savePDF = new JButton("Save as PDF & Preview");
savePDF.setEnabled(false);
savePDF.addActionListener(this);
savePDF.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
savePDF.getActionMap().put("showHelp", new HelpAction());

savePDF.setPreferredSize(new Dimension(screenSize.width/widthFactor, screenSize.height/17));


//Holds the JComboBoxes
controlPane = new JPanel(new GridLayout(3, 1));
//controlPane.setPreferredSize(new Dimension(screenSize.width/widthFactor, screenSize.height/3));

//fontName is a JComboBox
fontNames = new JComboBox<String>(fonts);
fontNames.setEnabled(false);
fontNames.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
fontNames.getActionMap().put("showHelp", new HelpAction());

fontNames.setSelectedIndex(0);
fontNames.setBorder(BorderFactory.createTitledBorder("Select Font:"));

//fontSize is a JComboBox
fontSizes = new JComboBox<String>(s);
fontSizes.setEnabled(false);
fontSizes.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
fontSizes.getActionMap().put("showHelp", new HelpAction());
fontSizes.setSelectedIndex(fontSizes.getItemCount()-1);
fontSizes.setBorder(BorderFactory.createTitledBorder("Select Font Size:"));

//spacing is a JComboBox
spacing = new JComboBox<String>(s);
spacing.setEnabled(false);
spacing.getInputMap().put(KeyStroke.getKeyStroke("F1"), "showHelp");
spacing.getActionMap().put("showHelp", new HelpAction());
spacing.setSelectedIndex(4);
spacing.setBorder(BorderFactory.createTitledBorder("Select Spacing:"));

//Adding the JComboBoxes and the 'savePDF' button to controlPane
controlPane.add(new JScrollPane(fontNames));
controlPane.add(new JScrollPane(fontSizes));
controlPane.add(new JScrollPane(spacing));
controlPane.setBorder(BorderFactory.createLineBorder(Color.black));

//littleTopPane holds the help-tip message and the open button.
JPanel littleTopPane = new JPanel();
littleTopPane.setLayout(new BorderLayout());
JLabel helpTip = new JLabel("Press F1 on keyboard for help.");
helpTip.setForeground(new Color(182, 33, 45));
littleTopPane.add(helpTip, BorderLayout.NORTH);
littleTopPane.add(open, BorderLayout.SOUTH);

//Adding the controls to buttonPane
buttonPane.add(littleTopPane, BorderLayout.NORTH);
buttonPane.add(controlPane, BorderLayout.CENTER);
buttonPane.add(savePDF, BorderLayout.SOUTH);
buttonPane.setBorder(BorderFactory.createLineBorder(Color.black));
buttonPane.setPreferredSize(new Dimension(screenSize.width/widthFactor, screenSize.height/3));

//rightPane holds the buttonPane and controlPane
rightPane = new JPanel(new BorderLayout());
rightPane.add(buttonPane, BorderLayout.NORTH);
//rightPane.add(instructions, BorderLayout.CENTER);
rightPane.setBorder(BorderFactory.createLineBorder(Color.black));

//centerPane is where the preview section would be displayed
centerPane = new JPanel();
centerPane.setBackground(Color.GRAY);
centerPane.setLayout(new BorderLayout());

//textArea is going to show the initial ASCII file that the user opens
ASCIIDisplayArea = new JTextArea();

Container con = getContentPane();

Dimension d = new Dimension(con.getWidth(), 30);
statusLabel = new JLabel(" ");
statusLabel.setPreferredSize(d);
statusLabel.setForeground(Color.BLUE);

con.setLayout(new BorderLayout());
con.add(statusLabel, BorderLayout.NORTH);
con.add(rightPane, BorderLayout.EAST);
con.add(centerPane, BorderLayout.CENTER);

setSize(screenSize.width, screenSize.height - 40);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);
}

//showASCIIFile shows the input file in its original ASCII format
public void showASCIIFile(){
ASCIIDisplayArea.setText("");
ASCIIDisplayArea.setEditable(false);

try{
BufferedReader in = new BufferedReader(new FileReader(inputPath));
String line = in.readLine();
while(line != null){
ASCIIDisplayArea.append(line + "\n");
line = in.readLine();
}
ASCIIDisplayArea.setSize(centerPane.getSize());
centerPane.removeAll();
//centerPane.setLayout(new BorderLayout());
centerPane.add(new JScrollPane(ASCIIDisplayArea),BorderLayout.CENTER);
centerPane.revalidate();
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

public File saveFile() {
JFileChooser fc = new JFileChooser();
File file = null;
int returnVal = fc.showSaveDialog(centerPane);

if(returnVal == JFileChooser.APPROVE_OPTION) {
file = fc.getSelectedFile();
}

return file;

}

public void actionPerformed(ActionEvent e){
if(e.getSource() == open){
source = loadFile();

if(source != null){
fontNames.setEnabled(true);
fontSizes.setEnabled(true);
spacing.setEnabled(true);
savePDF.setEnabled(true);
destination = null;
inputPath = source.toString();
//Once we have the input file, it is time to show the ASCII file in a JTextArea
showASCIIFile();
}
}
else if(e.getSource() == savePDF){
if(destination == null){
destination = saveFile();
}

if(destination != null){
if(!destination.exists()){
try{
destination.createNewFile();
}catch(Exception ex){
ex.printStackTrace();
}
}

outputPath = destination.toString();
centerPane.removeAll();
//centerPane.setLayout(new BorderLayout());
JProgressBar pG = new JProgressBar();
pG.setIndeterminate(true);
centerPane.add(pG, BorderLayout.CENTER);
centerPane.revalidate();
centerPane.repaint();
statusLabel.setText("Saving File...");
Thread pdfThread = new Thread(){
public void run(){
try{
contents = inputConverter(inputPath);
//long timeStart = System.currentTimeMillis();
new PdfMaker(header, contents, outputPath, fontNames.getSelectedItem().toString(), Float.parseFloat(fontSizes.getSelectedItem().toString()), Float.parseFloat(spacing.getSelectedItem().toString())).createPDF();
//long timeEnd = System.currentTimeMillis();
//System.out.println((timeEnd - timeStart)/1000.0);
//timeStart = System.currentTimeMillis();
statusLabel.setText("Generating Preview...");
PdfRenderer();
//timeEnd = System.currentTimeMillis();
//System.out.println((timeEnd - timeStart)/1000.0);
}
catch(Exception e){
e.printStackTrace();
}
}
};
pdfThread.start();
}
}
}
public void PdfRenderer() {
PdfDecoder pdf = new PdfDecoder();

try {
pdf.openPdfFile(outputPath);
//long timeStart = System.currentTimeMillis();
pdf.decodePage(1);
//long timeEnd = System.currentTimeMillis();
//System.out.println((timeEnd - timeStart)/1000.0);
//pdf.setPrintAutoRotateAndCenter(true);
pdf.setPageParameters(2, 1);
}
catch (Exception e) {
}

//first we clear up the centerPane
centerPane.removeAll();
//centerPane.setLayout(new BorderLayout());
//then we add the pdf onto the centerPane and display it
centerPane.add(new JScrollPane(pdf, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

centerPane.revalidate();
centerPane.repaint();
statusLabel.setText(" ");
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

header = block;

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