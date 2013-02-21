package com.tab2pdf.cse2311;

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
import javax.swing.BoxLayout;
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

public class GUI extends JFrame{
	public GUI(){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		UIView subject = new UIView();
		UIModel data = new UIModel(subject);
		UIController ui = new UIController(new Dimension(size.width/7, size.height/3), data);
		//JPanel p = new JPanel(new BorderLayout());
		//p.add(ui, BorderLayout.NORTH);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(subject, BorderLayout.CENTER);
		c.add(ui, BorderLayout.EAST);

		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setVisible(true);
	}
}