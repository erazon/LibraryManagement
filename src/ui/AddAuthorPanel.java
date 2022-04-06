package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Address;
import business.Author;
import business.LibraryMember;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import ui.EditMemberPanel.CloseListener;
import ui.EditMemberPanel.UpdateListener;
import utils.AuthorListener;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class AddAuthorPanel extends JFrame {
	
	DataAccessFacade daf = new DataAccessFacade();
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField titleField;
	private JTextField streetField;	
	private JTextField cityField;	
	private JTextField stateField;	
	private JTextField zipField;	
	private JTextField phoneField;
	private JTextField bioField;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	private AuthorListener authorListener;
	
	public AddAuthorPanel(AuthorListener authorListener) {
		this.authorListener = authorListener;
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
			
	}
	
	
	private void initializeWindow() {
		
		setSize(Math.round(GuiControl.SCREEN_WIDTH),
				Math.round(GuiControl.SCREEN_HEIGHT));
		GuiControl.centerFrameOnDesktop(this);
		
	}
	
	private void defineMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		defineUpperPanel();
		defineMiddlePanel();
		defineLowerPanel();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
		mainPanel.add(lower,BorderLayout.SOUTH);
			
	}
	//label
	public void defineUpperPanel(){
		upper = new JPanel();
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel mainLabel = new JLabel(DefaultData.ADD_AUTHOR);
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
		GridLayout gl = new GridLayout(9,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);

		String[] fldPerson = DefaultData.FIELD_PERSON;
		String[] fldAddress = DefaultData.FIELD_ADDRESS;
		String[] fldAuthor = DefaultData.FIELD_AUTHOR;
		
		String labelName = fldPerson[DefaultData.PERSON_FIRST_NAME];
		
		labelName = fldPerson[DefaultData.PERSON_FIRST_NAME];
		makeLabel(gridPanel,labelName);
		firstNameField = new JTextField(12);
		gridPanel.add(firstNameField);		
		
		labelName = fldPerson[DefaultData.PERSON_LAST_NAME];
		makeLabel(gridPanel,labelName);
		lastNameField = new JTextField(12);
		gridPanel.add(lastNameField);
		
		labelName = fldAuthor[DefaultData.AUTHOR_TITLE];
		makeLabel(gridPanel,labelName);
		titleField = new JTextField(12);
		gridPanel.add(titleField);
		
		labelName = fldAddress[DefaultData.ADDRESS_STREET];
		makeLabel(gridPanel,labelName);
		streetField = new JTextField(12);
		gridPanel.add(streetField);
		
		labelName = fldAddress[DefaultData.ADDRESS_CITY];
		makeLabel(gridPanel,labelName);
		cityField = new JTextField(12);
		gridPanel.add(cityField);
		
		labelName = fldAddress[DefaultData.ADDRESS_STATE];
		makeLabel(gridPanel,labelName);
		stateField = new JTextField(12);
		gridPanel.add(stateField);
		
		labelName = fldAddress[DefaultData.ADDRESS_ZIP];
		makeLabel(gridPanel,labelName);
		zipField = new JTextField(12);
		gridPanel.add(zipField);
						
		labelName = fldPerson[DefaultData.PERSON_PHONE];
		makeLabel(gridPanel,labelName);
		phoneField = new JTextField(12);
		gridPanel.add(phoneField);
		
		labelName = fldAuthor[DefaultData.AUTHOR_BIO];
		makeLabel(gridPanel,labelName);
		bioField = new JTextField(12);
		gridPanel.add(bioField);
		

	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton addButton = new JButton(DefaultData.ADD_BUTN);
		addButton.addActionListener(new AddListener());
		
		
		//back to cart button
		JButton closeButton = new JButton(DefaultData.CLOSE_BUTN);
		closeButton.addActionListener(new CloseListener());
		

		
		//create lower panel
		JButton [] buttons = {addButton, closeButton};
		lower = GuiControl.createStandardButtonPanel(buttons);
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
						
	class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
		
			String firstName = firstNameField.getText().toString();
			String lastName = lastNameField.getText().toString();
			String title = titleField.getText().toString();
			String street = streetField.getText().toString();
			String city = cityField.getText().toString();
			String state = stateField.getText().toString();
			String zip = zipField.getText().toString();
			String cell = phoneField.getText().toString();
			String bio = bioField.getText().toString();
			
			Address address = new Address(street, city, state, zip);
			Author author = new Author(firstName, lastName, title, address, bio);
			authorListener.addAuthor(author);
			dispose();
			
        }
	}
	
	class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	dispose();
        }
	}
	
	private boolean isIdValid(String id) {
		if (id.equals("")) {
			showError("ID cannot be empty!");
			return false;
		}
		return true;
	}
	
	private void showInfo(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	private void showError(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	private void clearAll() {
		firstNameField.setText("");
		lastNameField.setText("");
		titleField.setText("");
		streetField.setText("");
		cityField.setText("");
		zipField.setText("");
		phoneField.setText("");
		bioField.setText("");
	}	
	private static final long serialVersionUID = 1L;	
}
