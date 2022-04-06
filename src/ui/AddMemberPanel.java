package ui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Address;
import business.LibraryMember;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class AddMemberPanel extends JFrame {
	
	DataAccessFacade daf = new DataAccessFacade();
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;	
	private JTextField streetField;	
	private JTextField cityField;	
	private JTextField stateField;	
	private JTextField zipField;	
	private JTextField phoneField;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	public AddMemberPanel() {
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
		defineLowerPanel();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
		mainPanel.add(lower,BorderLayout.SOUTH);
			
	}
	//label
	public void defineUpperPanel(){
		upper = new JPanel();
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel mainLabel = new JLabel(DefaultData.ADD_NEW_MEMBER);
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
		GridLayout gl = new GridLayout(8,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);

		String[] fldPerson = DefaultData.FIELD_PERSON;
		String[] fldAddress = DefaultData.FIELD_ADDRESS;
		
		String labelName = fldPerson[DefaultData.PERSON_ID];
		makeLabel(gridPanel,labelName);
		idField = new JTextField(12);
		gridPanel.add(idField);
		
		labelName = fldPerson[DefaultData.PERSON_FIRST_NAME];
		makeLabel(gridPanel,labelName);
		firstNameField = new JTextField(12);
		gridPanel.add(firstNameField);		
		
		labelName = fldPerson[DefaultData.PERSON_LAST_NAME];
		makeLabel(gridPanel,labelName);
		lastNameField = new JTextField(12);
		gridPanel.add(lastNameField);
		
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
		

	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton saveButton = new JButton(DefaultData.ADD_NEW_MEMBER_BUTN);
		saveButton.addActionListener(new SaveListener());
		
		
		//back to cart button
		JButton clearButton = new JButton(DefaultData.CLEAR_BUTN);
		clearButton.addActionListener(new ClearListener());
		

		
		//create lower panel
		JButton [] buttons = {saveButton,clearButton};
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
						
	class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
		
        	String id = idField.getText().toString();
			String firstName = firstNameField.getText().toString();
			String lastName = lastNameField.getText().toString();
			String street = streetField.getText().toString();
			String city = cityField.getText().toString();
			String state = stateField.getText().toString();
			String zip = zipField.getText().toString();
			String cell = phoneField.getText().toString();
			
			if(!isIdValid(id)) return;
			
			if(!isFirstNameValid(firstName)) return;
			
			if(!isLastNameValid(lastName)) return;
			
			if(!isStreetValid(street)) return;
			
			if(!isCityValid(city)) return;
			
			if(!isStateValid(state)) return;

			
			Address address = new Address(street, city, state, zip);
			LibraryMember lm = new LibraryMember(id, firstName, lastName, cell, address);
			daf.saveNewMember(lm);
			clearAll();
			showInfo("Member successfully added!");
        }
	}
	
	class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
		
        	clearAll();
        }
	}
	
	private boolean isIdValid(String id) {
		if (id.equals("")) {
			showError("ID cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isFirstNameValid(String firstName) {
		if (firstName.equals("")) {
			showError("First name cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isLastNameValid(String lastName) {
		if (lastName.equals("")) {
			showError("Last name cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isStreetValid(String street) {
		if (street.equals("")) {
			showError("Street cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isCityValid(String city) {
		if (city.equals("")) {
			showError("City cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isStateValid(String state) {
		if (state.equals("")) {
			showError("State cannot be empty!");
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
		idField.setText("");
		firstNameField.setText("");
		lastNameField.setText("");
		streetField.setText("");
		cityField.setText("");
		stateField.setText("");
		zipField.setText("");
		phoneField.setText("");
	}	
	private static final long serialVersionUID = 1L;	
}
