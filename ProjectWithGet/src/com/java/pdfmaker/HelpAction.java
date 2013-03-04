package com.java.pdfmaker;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

//The class that helps bind the F1 key to the help-screen.
public class HelpAction extends AbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6891593475959299916L;

	public void actionPerformed( ActionEvent tf ){
		new HelpView();
	}
}