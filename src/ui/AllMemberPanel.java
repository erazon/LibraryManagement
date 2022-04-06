package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import dataaccess.DataAccessFacade;
import librarysystem.ChangeListener;
import librarysystem.LibrarySystem;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;
import utils.CustomTableModel;

public class AllMemberPanel extends JWindow implements ParentWindow, ChangeListener {
	private DataAccessFacade dataAccessFacade = new DataAccessFacade();
	private Window parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;

	// JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private final String ID = "ID";
	private final String NAME = "Name";
	private final String PHONE = "Phone";

	// widget labels
	private final String EDIT_BUTN = "Edit";

	// table config
	private final String[] DEFAULT_COLUMN_HEADERS = { ID, NAME, PHONE };
	private final int TABLE_WIDTH = GuiControl.SCREEN_WIDTH;
	private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f * GuiControl.SCREEN_HEIGHT);

	// these numbers specify relative widths of the columns -- they must add up to 1
	private final float[] COL_WIDTH_PROPORTIONS = { 0.2f, 0.2f, 0.2f };

	public AllMemberPanel() {
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);

	}

	private void initializeWindow() {

		setSize(GuiControl.SCREEN_WIDTH, GuiControl.SCREEN_HEIGHT);
		GuiControl.centerFrameOnDesktop(this);

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

		JLabel mainLabel = new JLabel(DefaultData.MEMBER_LIST);
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
		JButton editButton = new JButton(EDIT_BUTN);
		editButton.addActionListener(new EditButtonListener());

		// create lower panel
		JButton[] buttons = { editButton };
		lower = GuiControl.createStandardButtonPanel(buttons);
	}

	private void createTableAndTablePane() {
		updateModel(dataAccessFacade.readAllMember());
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

	class EditButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String memberId = (String) model.getValueAt(selectedRow, 0);
				if (memberId.isEmpty()) {
					showError("ID not found!");
				} else {
					EditMemberPanel editMem = new EditMemberPanel(memberId, AllMemberPanel.this);
					editMem.setVisible(true);
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

	@Override
	public void onChanged() {
		updateModel(dataAccessFacade.readAllMember());
		updateTable();
	}

}
