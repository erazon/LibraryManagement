package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Author;
import business.Book;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import utils.AuthorListener;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class AddBookPanel extends JFrame implements AuthorListener{
	
	DataAccessFacade daf = new DataAccessFacade();
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField checkoutLengthField;
	private JButton addAuthorButton;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	private List<Author> authors = new ArrayList<Author>();
	
	public AddBookPanel() {
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
		
		JLabel mainLabel = new JLabel(DefaultData.ADD_NEW_BOOK);
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
		GridLayout gl = new GridLayout(4,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);

		String[] fldBook = DefaultData.FIELD_BOOK;
		
		String labelName = fldBook[DefaultData.BOOK_ISBN];
		makeLabel(gridPanel,labelName);
		isbnField = new JTextField(12);
		gridPanel.add(isbnField);
		
		labelName = fldBook[DefaultData.BOOK_TITLE];
		makeLabel(gridPanel,labelName);
		titleField = new JTextField(12);
		gridPanel.add(titleField);		
		
		labelName = fldBook[DefaultData.BOOK_CHECKOUT_LENGTH];
		makeLabel(gridPanel,labelName);
		checkoutLengthField = new JTextField(12);
		gridPanel.add(checkoutLengthField);
		
		labelName = DefaultData.AUTHOR;
		makeLabel(gridPanel,labelName);
		addAuthorButton = new JButton(DefaultData.ADD_AUTHOR_BUTN);
		addAuthorButton.addActionListener(new AuthorListener());
		gridPanel.add(addAuthorButton);
		
		
		

	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton saveButton = new JButton(DefaultData.ADD_BOOK_BUTN);
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
	
	class AuthorListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	AddAuthorPanel addAuth = new AddAuthorPanel(AddBookPanel.this);
        	addAuth.setVisible(true);
        }
	}
						
	class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	String isbn = isbnField.getText().toString();
        	String title = titleField.getText().toString();
        	String chLength = checkoutLengthField.getText().toString();
		
        	if(!isISBNValid(isbn)) return;
        	if(!isTitleValid(title)) return;
        	if(!isLengthValid(chLength)) return;
        	if(!isAuthorAdded()) return;
        	
        	Book book = new Book(isbn, title, Integer.parseInt(chLength), authors);
        	daf.saveNewBook(book);
        	clearAll();
			showInfo("Book successfully added!");
        }
	}
	
	class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
		
        }
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	public void addAuthor(Author author) {
		authors.add(author);
	}
	
	private boolean isISBNValid(String isbn) {
		if (isbn.equals("")) {
			showError("ISBN cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isTitleValid(String title) {
		if (title.equals("")) {
			showError("Title cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isLengthValid(String length) {
		if (length.equals("")) {
			showError("Length cannot be empty!");
			return false;
		}else if (Integer.parseInt(length) != 7 && Integer.parseInt(length) != 21) {
			showError("Book length should be either 7 or 21");
			return false;
		}
		return true;
	}
	
	private boolean isAuthorAdded() {
		if (authors.size() == 0) {
			showError("Author cannot be empty!");
			return false;
		}
		return true;
	}
	
	private void clearAll() {
		isbnField.setText("");
		titleField.setText("");
		checkoutLengthField.setText("");
	}
	
	private void showInfo(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	private void showError(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
}
