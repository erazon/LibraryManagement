package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;

import business.Author;
import business.Book;
import dataaccess.DataAccessFacade;
import librarysystem.LibrarySystem;
import utils.CustomTableModel;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;

public class BookCopyPanel extends JWindow implements ParentWindow {
	private DataAccessFacade dataAccessFacade = new DataAccessFacade();
	private Window parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;

	// JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;
	List<String[]> booksData = dataAccessFacade.readAllBook();

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private final String ISBN = "ISBN";
	private final String TITLE = "Title";
	private final String CHECKOUT_LENGTH = "Checkout Length";
	
	private final String COPY_BUTN = "Copy";

	// table config
	private final String[] DEFAULT_COLUMN_HEADERS = { ISBN, TITLE, CHECKOUT_LENGTH };
	private final int TABLE_WIDTH = GuiControl.SCREEN_WIDTH;
	private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f * GuiControl.SCREEN_HEIGHT);

	// these numbers specify relative widths of the columns -- they must add up to 1
	private final float[] COL_WIDTH_PROPORTIONS = { 0.1f, 0.2f, 0.2f };

	public BookCopyPanel() {
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);

	}

	private void initializeWindow() {

		setSize(GuiControl.SCREEN_WIDTH, GuiControl.SCREEN_HEIGHT);
		Util.centerFrameOnDesktop(this);

	}

	private void defineMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(GuiControl.FILLER_COLOR);
		defineUpperPanel();
		defineMiddlePanel();
		defineLowerPanel();
		mainPanel.add(upper, BorderLayout.NORTH);
		mainPanel.add(middle, BorderLayout.CENTER);
		mainPanel.add(lower, BorderLayout.SOUTH);

	}

	// label
	public void defineUpperPanel() {
		upper = new JPanel();
		upper.setBackground(GuiControl.FILLER_COLOR);
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel mainLabel = new JLabel(DefaultData.BOOK_LIST);
		Font f = GuiControl.makeVeryLargeFont(mainLabel.getFont());
		f = GuiControl.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);
	}

	// middle -- table and combo box
	public void defineMiddlePanel() {

		middle = new JPanel();
		middle.setLayout(new BorderLayout());

		// table
		createTableAndTablePane();
		GuiControl.createCustomColumns(table, TABLE_WIDTH, COL_WIDTH_PROPORTIONS, DEFAULT_COLUMN_HEADERS);

		middle.add(GuiControl.createStandardTablePanePanel(table, tablePane), BorderLayout.CENTER);

	}

	// buttons
	public void defineLowerPanel() {

		// edit button
		JButton editButton = new JButton(COPY_BUTN);
		editButton.addActionListener(new CopyButtonListener());

		// create lower panel
		JButton[] buttons = { editButton };
		lower = GuiControl.createStandardButtonPanel(buttons);
	}

	private void createTableAndTablePane() {
		updateModel(booksData);
		table = new JTable(model);
		tablePane = new JScrollPane();
		tablePane.setPreferredSize(new Dimension(TABLE_WIDTH, DEFAULT_TABLE_HEIGHT));
		tablePane.getViewport().add(table);

	}

	public void updateModel(List<String[]> list) {
		model = new CustomTableModel();
		model.setTableValues(list);
	}

	private void updateTable() {

		table.setModel(model);
		table.updateUI();
		repaint();

	}

	public void setParentWindow(Window parentWindow) {
		parent = parentWindow;
	}

	public Window getParentWindow() {
		return parent;
	}

	class CopyButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String isbn = (String) model.getValueAt(selectedRow, 0);
				if (isbn.isEmpty()) {
					showError("Book not found!");
				} else {
					//copy book
					Book book = dataAccessFacade.readBookByISBN(isbn);
					if (book == null) {
						showError("Book not found!");
						return;
					}
					book.addCopy();
					dataAccessFacade.saveNewBook(book);
					showInfo("Book successfully copied!");
				}
			} else {
				showError("Please select a row.");
			}
		}
	}

	private void showInfo(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}

	private void showError(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}

	private static final long serialVersionUID = 3257569511937880631L;

}
