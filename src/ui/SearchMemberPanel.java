package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Book;
import business.BookCopy;
import business.Checkout;
import business.LibraryMember;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class SearchMemberPanel extends JFrame{
	
	DataAccessFacade dataAccessFacade = new DataAccessFacade();
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	private JTextField searchField;
	private JButton searchButton;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle;
	
	public SearchMemberPanel() {
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
	}
	
	
	private void initializeWindow() {
		
		setSize(Math.round(.7f*GuiControl.SCREEN_WIDTH),
				Math.round(.7f*GuiControl.SCREEN_HEIGHT));
		GuiControl.centerFrameOnDesktop(this);
		
	}
	
	private void defineMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		defineUpperPanel();
		defineMiddlePanel();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
			
	}
	//label
	public void defineUpperPanel(){
		upper = new JPanel();
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel mainLabel = new JLabel(DefaultData.SEARCH_BY_MEMBER_ID);
		Font f = GuiControl.makeVeryLargeFont(mainLabel.getFont());
		f = GuiControl.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}

	//table
	public void defineMiddlePanel(){
		middle = new JPanel();
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel gridPanel = new JPanel();
		middle.add(gridPanel);
		GridLayout gl = new GridLayout(2,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);
		
		String labelName = DefaultData.SEARCH_BY_ID;
		makeLabel(gridPanel,labelName);
		searchField = new JTextField(12);
		gridPanel.add(searchField);	
		
		makeLabel(gridPanel,"");
		searchButton = new JButton(DefaultData.SEARCH_BUTN);
		searchButton.addActionListener(new SearchListener());
		gridPanel.add(searchButton);
		
		
		

	}
	
	private void makeLabel(JPanel p, String s) {
		JLabel l = new JLabel(s);
		p.add(leftPaddedPanel(l));
	}
	private JPanel leftPaddedPanel(JLabel label) {
		JPanel paddedPanel = new JPanel();
		paddedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		paddedPanel.add(GuiControl.createHBrick(1));
		paddedPanel.add(label);
		return paddedPanel;		
	}
	
	class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	String s = searchField.getText().toString();
        	LibraryMember member = dataAccessFacade.readMemberById(s);
        	if (member != null) {
				showInfo(String.format("ID: %s, Name: %s %s, Phone: %s", 
						member.getMemberId(), member.getFirstName(), 
						member.getLastName(), member.getTelephone()));
			}else {
				showError("Sorry, Member not found!");
			}
        }
	}
					
	
	private static final long serialVersionUID = 1L;
	
	private void showInfo(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	private void showError(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
}
