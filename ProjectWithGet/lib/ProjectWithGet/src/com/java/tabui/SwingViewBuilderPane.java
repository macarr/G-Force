/**
 * This class extends the class SingViewBuilder from icepdf, and overrides certain methods to remove the functionalities that
 * are not needed. 
 */
package com.java.tabui;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class SwingViewBuilderPane extends SwingViewBuilder{
	/**
	 * SwingViewBuilderPane constructor.
	 */
	public SwingViewBuilderPane(SwingController controller){
		super(controller);
	}
	
	/**
	 * All the following methods disable certain functionalities by returning a null value. 
	 */
	
	public JToggleButton buildSelectToolButton(){
		return null;
	}
	
	public JToggleButton buildTextSelectToolButton(){
		return null;
	}
	
	public JToggleButton buildLinkAnnotationToolButton(){
		return null;
	}
	
	public JButton buildShowHideUtilityPaneButton(){
		return null;
	}
	
	public JToggleButton buildPanToolButton(){
		return null;
	}
	
	public JButton buildSaveAsFileButton(){
		return null;
	}
}
