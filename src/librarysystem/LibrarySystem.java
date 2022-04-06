package librarysystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import business.ControllerInterface;
import business.SystemController;
import ui.AddBookPanel;
import ui.AddMemberPanel;
import ui.AllBookPanel;
import ui.AllCheckoutPanel;
import ui.LoginPanel;
import ui.AllMemberPanel;
import ui.AllOverduePanel;
import ui.BookCopyPanel;
import ui.BookStatusPanel;
import ui.CheckoutBookPanel;
import ui.SearchMemberPanel;
import utils.GuiControl;
import utils.Util;

public class LibrarySystem extends JFrame implements LibWindow {
	ControllerInterface ci = new SystemController();
	public final static LibrarySystem INSTANCE = new LibrarySystem();
	// New
	String[] links;
	JList<ListItem> linkList;
	JPanel cards;
	public static JTextArea statusBar = new JTextArea("Welcome to the library system!");

	LoginPanel login;
	AddBookPanel addBookPanel;
	CheckoutBookPanel checkoutBookPanel;
	BookCopyPanel bookCopyPanel;
	BookStatusPanel bookStatusPanel;
	AddMemberPanel addMemberPanel;
	SearchMemberPanel searchMemberPanel;
	AllCheckoutPanel allCheckoutPanel;
	AllOverduePanel allOverduePanel;
	AllMemberPanel allMemberIdsPanel;
	AllBookPanel allBookPanel;
	// boolean startup = true;

	// list items
	ListItem loginItem = new ListItem("Login/Logout", true);
	ListItem addMember = new ListItem("Add Member", false);
	ListItem searchMember = new ListItem("Search Member", false);
	ListItem addBook = new ListItem("Add Book", false);
	ListItem checkoutBook = new ListItem("Checkout Book", false);
	ListItem bookCopy = new ListItem("Book Copy", false);
	ListItem bookStatus = new ListItem("Book Status", false);
	ListItem allCheckouts = new ListItem("All Checkout", false);
	ListItem allOverdue = new ListItem("All Overdue", false);
	ListItem allMembers = new ListItem("All Member", false);
	ListItem allBooks = new ListItem("All Book", false);

	ListItem[] logoutItems = { loginItem };
	ListItem[] librarianItems = { loginItem, searchMember, bookStatus,checkoutBook, allMembers, allBooks, allCheckouts, allOverdue};
	ListItem[] adminItems = { loginItem, addMember, searchMember, addBook, checkoutBook, bookCopy, bookStatus, allCheckouts,allOverdue, allMembers, allBooks };
	ListItem[] bothItems = { loginItem, addMember, searchMember, addBook, checkoutBook, bookCopy, bookStatus, allCheckouts, allOverdue,allMembers, allBooks };

	public ListItem[] getLogoutItems() {
		return logoutItems;
	}
	public ListItem[] getLibrarianItems() {
		return librarianItems;
	}
	
	public ListItem[] getBothItems() {
		return bothItems;
	}

	public ListItem[] getAdminItems() {
		return adminItems;
	}

	public JList<ListItem> getLinkList() {
		return linkList;
	}

	private boolean isInitialized = false;

	private static LibWindow[] allWindows = { LibrarySystem.INSTANCE };

	public static void hideAllWindows() {

		for (LibWindow frame : allWindows) {
			frame.setVisible(false);
		}
	}

	private LibrarySystem() {
		
	}

	public void init() {
		Util.adjustLabelFont(statusBar, Util.DARK_BLUE, true);
		setSize(660, 500);
		isInitialized = true;

		createLinkLabels();
		createMainPanels();
		CardLayout cl = (CardLayout) (cards.getLayout());
		linkList.addListSelectionListener(event -> {
			String value = linkList.getSelectedValue().getItemName();
			boolean allowed = linkList.getSelectedValue().highlight();
			System.out.println(value + " " + allowed);
			
			statusBar.setText("");
			if (!allowed) {
				value = loginItem.getItemName();
				linkList.setSelectedIndex(0);
			}
			
			if (value.equals("Login")) new LoginPanel();
			if (value.equals("Add Member")) new AddMemberPanel();
			if (value.equals("Search Member")) new SearchMemberPanel();
			if (value.equals("Add Book")) new AddBookPanel();
			if (value.equals("Checkout Book")) new CheckoutBookPanel();
			if (value.equals("Book Copy")) new BookCopyPanel();
			if (value.equals("Book Status")) new BookStatusPanel();
			if (value.equals("All Checkout")) new AllCheckoutPanel();
			if (value.equals("All Overdue")) new AllOverduePanel();
			if (value.equals("All Member IDs")) new AllMemberPanel();
			if (value.equals("All Book IDs")) new AllBookPanel();
			cl.show(cards, value);

		});

		// set up split panes
		JSplitPane innerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, linkList, cards);
		innerPane.setDividerLocation(180);
		JSplitPane outerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, innerPane, statusBar);
		outerPane.setDividerLocation(400);
		add(outerPane, BorderLayout.CENTER);
	}

	public JTextArea getStatusBar() {
		return statusBar;
	}

	public void createLinkLabels() {
		DefaultListModel<ListItem> model = new DefaultListModel<>();
		model.addElement(loginItem);
		model.addElement(addMember);
		model.addElement(searchMember);
		model.addElement(addBook);
		model.addElement(checkoutBook);
		model.addElement(bookCopy);
		model.addElement(bookStatus);
		model.addElement(allCheckouts);
		model.addElement(allOverdue);
		model.addElement(allMembers);
		model.addElement(allBooks);

		linkList = new JList<ListItem>(model);
		linkList.setCellRenderer(new DefaultListCellRenderer() {

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ListItem) {
					ListItem nextItem = (ListItem) value;
					setText(nextItem.getItemName());
					if (nextItem.highlight()) {
						setForeground(Util.LINK_AVAILABLE);
					} else {
						setForeground(Util.LINK_NOT_AVAILABLE);
					}
					if (isSelected) {
						setForeground(Color.BLACK);
						setBackground(new Color(236, 243, 245));
						// setBackground(Color.WHITE);
					}
				}
				return c;
			}

		});
	}


	public void createMainPanels() {
		// login
		login = new LoginPanel();
		addMemberPanel = new AddMemberPanel();
		searchMemberPanel = new SearchMemberPanel();
		addBookPanel = new AddBookPanel();
		checkoutBookPanel = new CheckoutBookPanel();
		bookCopyPanel = new BookCopyPanel();
		bookStatusPanel = new BookStatusPanel();
		allCheckoutPanel = new AllCheckoutPanel();
		allOverduePanel = new AllOverduePanel();
		allMemberIdsPanel = new AllMemberPanel();
		allBookPanel = new AllBookPanel();

		ImageIcon image = new ImageIcon(GuiControl.SPLASH_IMAGE);
		JPanel splash = new JPanel();
		splash.add(new JLabel(image), BorderLayout.CENTER);

		cards = new JPanel(new CardLayout());
		cards.add(splash, "splash");
		cards.add(login.getMainPanel(), loginItem.getItemName());
		cards.add(addMemberPanel.getMainPanel(), addMember.getItemName());
		cards.add(searchMemberPanel.getMainPanel(), searchMember.getItemName());
		cards.add(addBookPanel.getMainPanel(), addBook.getItemName());
		cards.add(checkoutBookPanel.getMainPanel(), checkoutBook.getItemName());
		cards.add(bookCopyPanel.getMainPanel(), bookCopy.getItemName());
		cards.add(bookStatusPanel.getMainPanel(), bookStatus.getItemName());
		cards.add(allCheckoutPanel.getMainPanel(), allCheckouts.getItemName());
		cards.add(allOverduePanel.getMainPanel(), allOverdue.getItemName());
		cards.add(allMemberIdsPanel.getMainPanel(), allMembers.getItemName());
		cards.add(allBookPanel.getMainPanel(), allBooks.getItemName());

	}

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}

	@Override
	public void isInitialized(boolean val) {
		isInitialized = val;

	}

}
