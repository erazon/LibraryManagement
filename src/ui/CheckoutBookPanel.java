package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Author;
import business.Book;
import business.BookCopy;
import business.Checkout;
import business.LibraryMember;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class CheckoutBookPanel extends JFrame{
	
	DataAccessFacade dataAccessFacade = new DataAccessFacade();
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	private JTextField isbnField;
	private JTextField memberIdField;
	private JButton checkoutButton;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle;
	
	public CheckoutBookPanel() {
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
		
		JLabel mainLabel = new JLabel(DefaultData.CHECKOUT_BOOK);
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
		
		labelName = fldBook[DefaultData.MEMBER_ID];
		makeLabel(gridPanel,labelName);
		memberIdField = new JTextField(12);
		gridPanel.add(memberIdField);		
		
		makeLabel(gridPanel,"");
		checkoutButton = new JButton(DefaultData.CHECKOUT_BUTN);
		checkoutButton.addActionListener(new CheckoutListener());
		gridPanel.add(checkoutButton);
		
		
		

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
	
	class CheckoutListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	String isbn = isbnField.getText().toString();
			String memberId = memberIdField.getText().toString();
			
			if(!isISBNValid(isbn)) return;
			if(!isMemberIDValid(memberId)) return;
			
			Book book = dataAccessFacade.readBookByISBN(isbn);
			LibraryMember member = dataAccessFacade.readMemberById(memberId);
			
			if (!isBookNullCheck(book)) return;
			if (!isMemberNullCheck(member)) return;
			
			if (book.isAvailable()) {
			    BookCopy copy = book.getNextAvailableCopy();
			    Checkout checkout = new Checkout(copy, 
			    		LocalDate.now(), 
			    		LocalDate.now().plusDays(book.getMaxCheckoutLength()), 
			    		memberId);
			    dataAccessFacade.saveNewBook(book);
			    dataAccessFacade.saveNewCheckout(checkout);
			    showInfo("Checkout successful!");
			}else {
				showError("Sorry, Book copy not available!");
			}
			
        }
	}
					
	
	private static final long serialVersionUID = 1L;

	
	private boolean isISBNValid(String isbn) {
		if (isbn.equals("")) {
			showError("ISBN cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isMemberIDValid(String title) {
		if (title.equals("")) {
			showError("Member ID cannot be empty!");
			return false;
		}
		return true;
	}
	
	private boolean isMemberNullCheck(LibraryMember libMem) {
		if (libMem == null) {
			showError("Member not found!");
			return false;
		}
		return true;
	}
	
	private boolean isBookNullCheck(Book book) {
		if (book == null) {
			showError("Book not found!");
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
	
}
