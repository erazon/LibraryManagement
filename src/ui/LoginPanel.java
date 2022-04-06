package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dataaccess.Auth;
import dataaccess.TestData;
import dataaccess.User;
import librarysystem.LibrarySystem;
import librarysystem.ListItem;
import utils.DefaultData;
import utils.GuiControl;
import utils.Util;



public class LoginPanel  {
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	private JPanel mainPanel;
	private JPanel upperHalf;
	private JPanel middleHalf;
	private JPanel container;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel lowerPanel;
	private JPanel leftTextPanel;
	private JPanel rightTextPanel;
	
	private JTextField idField;
	private JTextField pwdField;
	private JLabel label;
	private JButton loginButton;
	private JButton logoutButton;
	public LoginPanel() {
		
		mainPanel = new JPanel();
		
		defineUpperHalf();
		defineMiddleHalf();
		//defineLowerHalf();
		BorderLayout bl = new BorderLayout();
		bl.setVgap(30);
		mainPanel.setLayout(bl);
					
		mainPanel.add(upperHalf, BorderLayout.NORTH);
		mainPanel.add(middleHalf, BorderLayout.CENTER);
		//mainPanel.add(lowerHalf, BorderLayout.SOUTH);

	}
	private void defineUpperHalf() {
		
		upperHalf = new JPanel();
		upperHalf.setLayout(new BorderLayout());
		defineTopPanel();
		defineMiddlePanel();
		defineLowerPanel();
		upperHalf.add(topPanel, BorderLayout.NORTH);
		upperHalf.add(middlePanel, BorderLayout.CENTER);
		upperHalf.add(lowerPanel, BorderLayout.SOUTH);
		
	}
	private void defineMiddleHalf() {
		middleHalf = new JPanel();
		middleHalf.setLayout(new BorderLayout());
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		//middleHalf.add(Box.createRigidArea(new Dimension(0,50)));
		middleHalf.add(s, BorderLayout.SOUTH);
		
	}

	private void defineTopPanel() {
		topPanel = new JPanel();
		
		JLabel loginLabel = new JLabel("Login");
		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);
		
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(loginLabel);
		
	}
	
	
	
	private void defineMiddlePanel() {
		middlePanel=new JPanel();
		middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		defineLeftTextPanel();
		defineRightTextPanel();
		middlePanel.add(leftTextPanel);
		middlePanel.add(rightTextPanel);
	}
	private void defineLowerPanel() {
		lowerPanel = new JPanel();
		loginButton = new JButton("Login");
		logoutButton = new JButton("Logout");
		addLoginButtonListener(loginButton);
		addLogoutButtonListener(logoutButton);
		
		
		JButton [] buttons = {loginButton,logoutButton};
		lowerPanel = GuiControl.createStandardButtonPanel(buttons);
	}

	private void defineLeftTextPanel() {
		
		JPanel topText = new JPanel();
		JPanel bottomText = new JPanel();
		topText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));		
		
		idField = new JTextField(10);
		label = new JLabel(DefaultData.USER_ID);
		label.setFont(Util.makeSmallFont(label.getFont()));
		topText.add(idField);
		bottomText.add(label);
		
		leftTextPanel = new JPanel();
		leftTextPanel.setLayout(new BorderLayout());
		leftTextPanel.add(topText,BorderLayout.NORTH);
		leftTextPanel.add(bottomText,BorderLayout.CENTER);
	}
	private void defineRightTextPanel() {
		
		JPanel topText = new JPanel();
		JPanel bottomText = new JPanel();
		topText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));		
		
		pwdField = new JPasswordField(10);
		label = new JLabel(DefaultData.PASSWORD);
		label.setFont(Util.makeSmallFont(label.getFont()));
		topText.add(pwdField);
		bottomText.add(label);
		
		rightTextPanel = new JPanel();
		rightTextPanel.setLayout(new BorderLayout());
		rightTextPanel.add(topText,BorderLayout.NORTH);
		rightTextPanel.add(bottomText,BorderLayout.CENTER);
		
	}
	
	private void addLoginButtonListener(JButton butn) {
		butn.addActionListener(evt -> {
			
			String userId = idField.getText().trim();
			String pwd = pwdField.getText().trim();
			if(userId.length() == 0 || pwd.length() == 0) {
				
			}
			else { 
				User login = new User(userId, pwd, null);
				List<User> list = TestData.allUsers;
				User u = Util.findUser(list, login);
				if(u != null) {
					showInfo(DefaultData.LOGIN_SUCCESS_MSG);
					updateLeftPanel(u.getAuthorization());
					clearAll();
					LibrarySystem.INSTANCE.statusBar.repaint();
				}else {
					showError(DefaultData.LOGIN_ERROR_MSG);
				}
				
			}	
			

		});
	}
	private void addLogoutButtonListener(JButton butn) {
		butn.addActionListener(evt -> {
			//LibrarySystem.INSTANCE.hideAllWindows();
			logoutItems();
		});
	}
	private void updateLeftPanel(Auth auth) {
		if(auth == Auth.LIBRARIAN) librarianItems();
		else if(auth == Auth.ADMIN) adminItems();
		else if(auth == Auth.BOTH) bothItems();
		
	}
	
	private void librarianItems() {
		ListItem[] librarianItems = LibrarySystem.INSTANCE.getLibrarianItems();
		updateList(librarianItems);
	}
	
	private void adminItems() {
		ListItem[] adminItems = LibrarySystem.INSTANCE.getAdminItems();
		updateList(adminItems);
	}
	
	private void bothItems() {
		ListItem[] bothItems = LibrarySystem.INSTANCE.getBothItems();
		updateList(bothItems);
	}
	
	private void logoutItems() {
		ListItem[] logoutItems = LibrarySystem.INSTANCE.getLogoutItems();
		updateList(logoutItems);
	}
	
	
	@SuppressWarnings("unchecked")
	private void updateList(ListItem[] items) {
		JList<ListItem> linkList = LibrarySystem.INSTANCE.getLinkList();
		DefaultListModel<ListItem> model = (DefaultListModel)linkList.getModel();
		int size = model.getSize();
		if(items != null) {	
			java.util.List<Integer> indices = new ArrayList<>();
			for(ListItem item : items) {
				int index = model.indexOf(item);
				indices.add(index);
				ListItem next = (ListItem)model.get(index);
				next.setHighlight(true);
			}
			
			for(int i = 0; i <size; ++i) {
				if(!indices.contains(i)) {
					ListItem next = (ListItem)model.get(i);
					next.setHighlight(false);
				}
			}
		} else {
			for(int i = 0; i <size; ++i) {
				ListItem next = (ListItem)model.get(i);
				next.setHighlight(true);	
			}
			
		}
	}
	
	public void clearAll() {
		idField.setText("");
		pwdField.setText("");
	}
	
	private void showInfo(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	private void showError(String msg) {
		LibrarySystem.INSTANCE.statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		LibrarySystem.INSTANCE.statusBar.setText(msg);
	}
	
	
	private static final long serialVersionUID = 3618976789175941432L;
	
	
}


