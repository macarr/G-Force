//package com.java.pdfmaker;

import java.awt.Dimension;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class PdfMaker {
  String filePath = null;
Document document;
PdfWriter writer;
PdfContentByte cb;
BaseFont bF;
Dimension screenSize;
double width;
double height;
String fontName = "";
float fontSize;
float spacing;
ArrayList<String> header;
ArrayList<ArrayList<String>> contents;

float angle = (float)(-45 * (Math.PI / 180));
    float fxScale = (float)(Math.cos(angle));
    float fyScale = (float)(Math.cos(angle));
    float fxRote = (float)(-Math.sin(angle));
    float fyRote = (float)(Math.sin(angle));
    PdfTemplate angularRect = null;
    
    float yIncrement = 40f;
float margin;
float xPos;
float yPos;
ArrayList<DIndexFreq> dIndexes;
int curDIndex = 0;
int charNum = 0;
String line = "";

int skip = 0;
int blockNum;
int lineNum;


public PdfMaker(ArrayList<String> header, ArrayList<ArrayList<String>> contents, String filePath, String fontName, float fontSize, float spacing) throws DocumentException, IOException {
try{
this.header = header;
this.contents = contents;
this.filePath = filePath;
this.fontName = fontName;
this.spacing = spacing;
this.fontSize = fontSize;


document = new Document();
writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
document.open();
cb = writer.getDirectContent();
bF = BaseFont.createFont(fontName,
BaseFont.WINANSI, BaseFont.EMBEDDED);
cb.setFontAndSize(bF, 20f);
cb.setLineWidth(0.5f);
cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, header.get(0), 310, document.top(), 0);
cb.setFontAndSize(bF, 10f);
cb.showTextAlignedKerned(PdfContentByte.ALIGN_CENTER, header.get(1), 315, document.top() - 15, 0);
cb.endText();
cb.setFontAndSize(bF, fontSize);

dIndexes = getDIndexes(contents.get(0).get(1));
margin = document.left();
xPos = margin;
yPos = document.top() - yIncrement;
}
catch(Exception e){
e.printStackTrace();
}
}

private void drawSingleBars(){
for(int lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
if(lineNum < 5){
cb.moveTo(xPos, yPos-fontSize/2f);
cb.lineTo(xPos, yPos+fontSize/2f);
yPos -= fontSize;
}
}
}

private void processInputWithinTriangle(){
String number = "";
charNum++;
for(; line.charAt(charNum) != '>'; charNum++) {
number += line.charAt(charNum);
}

//need to advance charNum one more time to point right after the '>'
charNum++;
cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);
cb.endText();

cb.addTemplate(angularRect, fxScale, fxRote, fyScale, fyRote, xPos + bF.getWidthPoint(number, fontSize), yPos + fontSize/2);
cb.moveTo(xPos + bF.getWidthPoint(number, fontSize) + fontSize/3f, yPos+fontSize/2f);
cb.lineTo(xPos + ((number.length()+2)*spacing), yPos+fontSize/2f);
cb.stroke();
xPos += (number.length()+2)*spacing;
}

private void processDashes(){
cb.moveTo(xPos, yPos+fontSize/2f);
cb.lineTo(xPos + spacing, yPos+fontSize/2f);
charNum++;
xPos += spacing;
cb.stroke();
}

private void processHOrP(){
cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, Character.toString(line.charAt(charNum)), xPos, yPos, 0);
cb.endText();
charNum++;
xPos += spacing;
}

private void processDigit(){
String number = "";
//charNum++;
for(; Character.isDigit(line.charAt(charNum)) ; charNum++) {
number += line.charAt(charNum);
}


cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, number, xPos, yPos, 0);
cb.endText();

//if a very short line is to be inserted between numbers with no gaps in between then
//the if statement needs to be removed
//if(line.charAt(charNum) != ' '){
cb.moveTo(xPos + bF.getWidthPoint(number, fontSize), yPos+fontSize/2f);
cb.lineTo(xPos + ((number.length())*spacing), yPos+fontSize/2f);
cb.stroke();
//}
xPos += (number.length())*spacing;
}

private void shiftUp(){
cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, "/", xPos+1, yPos+fontSize/6f, 0);
cb.endText();
cb.moveTo(xPos, yPos+fontSize/2f);
cb.lineTo(xPos + spacing, yPos+fontSize/2f);
xPos += spacing;
charNum++;
}

private void processRepeat(){
cb.circle(xPos, yPos + fontSize/2f, fontSize/5f);
cb.fillStroke();
cb.moveTo(xPos + fontSize/5f, yPos+fontSize/2f);
cb.lineTo(xPos + spacing, yPos+fontSize/2f);
cb.stroke();
xPos += spacing;
charNum++;
}

private void processCurrentBars(){
if(lineNum == 0 && dIndexes.get(curDIndex).freq == 2 && dIndexes.get(curDIndex).repIndex > -1){
String message = "Repeat " + dIndexes.get(curDIndex).repNum + " times";
cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, xPos-bF.getWidthPoint(message, fontSize), yPos+fontSize, 0);
cb.endText();
}
for(int barNum = 0; barNum < dIndexes.get(curDIndex).freq;){
if(lineNum < 5){
if(barNum == 0 && curDIndex == 0 && dIndexes.get(curDIndex).freq == 2){
cb.stroke();
cb.setLineWidth(2.0f);
}
else if(barNum ==1 && curDIndex > 0 && dIndexes.get(curDIndex).freq == 2){
cb.stroke();
cb.setLineWidth(2.0f);
}

cb.moveTo(xPos, yPos-fontSize/2f);
cb.lineTo(xPos, yPos+fontSize/2f);
cb.stroke();
}
barNum++;
cb.setLineWidth(0.5f);


//horizontal lines;
cb.moveTo(xPos, yPos+fontSize/2);
cb.lineTo(xPos + spacing, yPos+fontSize/2);
xPos += spacing;

charNum++;
}
}

private void processTrailingBars(){

for(int lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
if(lineNum == 0 && dIndexes.get(curDIndex).freq == 2 && dIndexes.get(curDIndex).repIndex > -1){
String message = "Repeat " + dIndexes.get(curDIndex).repNum + " times";

cb.beginText();
cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, message, xPos-bF.getWidthPoint(message, fontSize), yPos+fontSize, 0);
cb.endText();

}
for(int barNum = 0; barNum < dIndexes.get(curDIndex).freq;){
if(lineNum < 5){

if(barNum == 0 && curDIndex == 0 && dIndexes.get(curDIndex).freq == 2){
cb.stroke();
cb.setLineWidth(2.0f);
}
else if(barNum ==1 && curDIndex > 0 && dIndexes.get(curDIndex).freq == 2){
cb.stroke();
cb.setLineWidth(2.0f);
}

cb.moveTo(xPos, yPos-fontSize/2);
cb.lineTo(xPos, yPos+fontSize/2);
cb.stroke();
}
barNum++;
cb.setLineWidth(0.5f);
//horizontal lines;
if(barNum < dIndexes.get(curDIndex).freq){
cb.moveTo(xPos, yPos+fontSize/2);
cb.lineTo(xPos + spacing, yPos+fontSize/2);
xPos += spacing;
}

charNum++;
}

cb.stroke();
if(lineNum < 5){
xPos = margin;
yPos -= fontSize;
}
}
yPos = document.top()-yIncrement;
}

//draws the horizontal lines before the block
private void processBeginningHorizontalLines(){
for(int lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){
if(lineNum < 6){
cb.moveTo(20, yPos+fontSize/2);
cb.lineTo(document.left(), yPos+fontSize/2);
yPos -= fontSize;
}
}
yPos = document.top()-yIncrement;
}

private void processEndingHorizontalLines(){
for(int lineNum = 0; lineNum < 6; lineNum++){
if(lineNum < 6){
cb.moveTo(xPos, yPos+fontSize/2);
cb.lineTo(document.right(), yPos+fontSize/2);
yPos -= fontSize;
}
}
cb.stroke();
yPos = document.top()-yIncrement;
}

public void createPDF() throws DocumentException, IOException {
try{
angularRect = cb.createTemplate(fontSize/4f, fontSize/4f);
angularRect.rectangle(0f, 0f, fontSize/4f, fontSize/4f);
angularRect.stroke();
        
//looping through all the blocks in order to extract their contents and create the pdf
//file using the right format.
for(blockNum = 0; blockNum < contents.size();){
//at the beginning of each new block the Y-Position and margin needs to be set
yPos = document.top() - yIncrement;
xPos = margin;

//calling this method to draw the beginning 6 horizontal lines
processBeginningHorizontalLines();

//skip > 0 means that the block was split and the trailing '|'s were printed before the split,
//and suppressed on the current position. Instead, the single '|' would be printed, which is done
//through maintaining the skip variable and a call to drawSingleBars.
if(skip > 0){
drawSingleBars();
}

yPos = document.top() - yIncrement;

//at the beginning of each block the xPos needs to be set to the margin.
for(lineNum = 0; lineNum < contents.get(blockNum).size(); lineNum++){

line = contents.get(blockNum).get(lineNum);
xPos = margin;

//the idea is to absorb the characters starting with '|' or '||' and ending right before
//the next '|' or '||'s
for(charNum = dIndexes.get(curDIndex).index + skip; curDIndex + 1 < dIndexes.size() && charNum < (dIndexes.get(curDIndex+1).index);){

if(line.charAt(charNum) == '|'){
processCurrentBars();
}
else if(line.charAt(charNum) == '<'){
processInputWithinTriangle();
}

//if character is '-', then a line with width the value of 'spacing' would be drawn onto the
//pdf file
else if(line.charAt(charNum) == '-'){
processDashes();
}
//if the character is letter or digit it would be printed onto the pdf file
else if(Character.isDigit(line.charAt(charNum))){
processDigit();
}
else if(line.charAt(charNum) == 's') {
shiftUp();
}
else if(line.charAt(charNum) == '*') {
processRepeat();
}
else if(line.charAt(charNum) == ' ') {
charNum++;
}
//write code for the cases that have not been handled
else if(line.charAt(charNum) == 'h' || line.charAt(charNum) == 'p'){
processHOrP();
}
}

//after printing the parts of each line, we need to move to the next
yPos -= fontSize;
}
curDIndex++;
skip = 0;

//if current character index is less than the index of the first of the last set of '|'s
if(curDIndex < dIndexes.size()-1){
margin = xPos;
yPos = document.top() - yIncrement;

//checking if the next block can be fit next to the current. If not, we need to print the
//trailing bars for the current block before the actual split that is coming up.
if(xPos + dIndexes.get(curDIndex).spaceNeeds > document.right()) {

//if a block was split from the middle, the trailing '|'s should be printed before the split.
//processTrailingBars does that. Also the ending 6 horizontal lines are printed

processTrailingBars();
//if a block was split from the middle, the trailing '|'s should be printed before the split.
//and the same '|'s should be suppressed before the continuation on the next position, which is
//achieved through setting the skip variable.
skip = dIndexes.get(curDIndex).freq;
}
}
//otherwise we need to print the last sets of '|'s for the current block and increase the
//blockNum variable.
else{
margin = xPos;
yPos = document.top() - yIncrement;

processTrailingBars();
//margin and yPosition needs to be reset after processTrailingBars returns, so that the next
//line that would be output is done at the proper location.
margin = xPos;
yPos = document.top() - yIncrement;

blockNum++;
curDIndex = 0;
}
if(blockNum < contents.size())
dIndexes = getDIndexes(contents.get(blockNum).get(1));

//if there is no more space left to join the next block right after the current block, set the margin
//to the left end of the document, and yIncrement is increased to set the gap between the current block
//and the next.
//Please Note: On bigger spacing if the requirement is to keep the double bar part on the current lines,
//then we have to change the following code a little bit.
if(xPos + dIndexes.get(curDIndex).spaceNeeds > document.right()) {
//first draw the 6 horizontal lines at the end of the current block
processEndingHorizontalLines();
margin = document.left();
xPos = margin;
yIncrement += fontSize * 8;
}
}

document.close();
}
catch(Exception e) {
e.printStackTrace();
}
}

public ArrayList<DIndexFreq> getDIndexes(String source) {
ArrayList<DIndexFreq> dIndexes = new ArrayList<DIndexFreq>();
for(int ind = 0; ind < source.length(); ind++) {
if(source.charAt(ind) == '|'){
int numRep = -1;
char repVal = 0;
if(Character.isDigit(contents.get(blockNum).get(0).charAt(ind))){
//System.out.println(blockNum + " " + contents.get(blockNum).get(0).charAt(ind));
numRep = ind;
repVal = contents.get(blockNum).get(0).charAt(ind);
}

int freq = 1;
int temp = ind;
ind++;

//calculating frequency of each occurrence of '|', as well as number of repeats
for(; ind < source.length() && source.charAt(ind) == '|'; ind++){
if(Character.isDigit(contents.get(blockNum).get(0).charAt(ind))){
numRep = ind;
repVal = contents.get(blockNum).get(0).charAt(ind);
}
freq++;
}
DIndexFreq data = new DIndexFreq();
data.index = temp;
data.freq = freq;
data.repIndex = numRep;
data.repNum = repVal;
dIndexes.add(data);
}
}

for(int ind = 0; ind < dIndexes.size()-1; ind++){
dIndexes.get(ind).spaceNeeds = ((dIndexes.get(ind + 1).index)-(dIndexes.get(ind).index))*spacing;
}
return dIndexes;
}

class DIndexFreq{
int index;
int freq;
int repIndex;
char repNum;
float spaceNeeds;


}
}