package utils;

import java.util.*;

public class DefaultData {

	public static DefaultData instance;

	private DefaultData() {
		// private constructor; must be accessed with getInstance method
	}

	/**
	 * This method provides the only avenue for obtaining an instance of this
	 * singleton class.
	 * 
	 * @return DefaultData
	 */
	public static DefaultData getInstance() {
		if (instance == null) {
			instance = new DefaultData();
		}
		return instance;
	}

	// User ID Password
	public final static String USER_ID = "User ID";
	public final static String PASSWORD = "Password";
	public final static String AUTHOR = "Author";

	// Page Title
	public final static String ADD_NEW_MEMBER = "Add New Member";
	public final static String UPDATE_MEMBER = "Update";
	public final static String ADD_AUTHOR = "Add Author";
	public final static String EDIT_MEMBER = "Edit Member";
	public final static String ADD_NEW_BOOK = "Add New Book";
	public final static String CHECKOUT_BOOK = "Checkout Book";
	public final static String SEARCH_BY_MEMBER_ID = "Member Search By ID";
	public final static String MEMBER_LIST = "Member List";
	public final static String BOOK_LIST = "Book List";
	public final static String CHECKOUT_LIST = "Checkout List";
	public final static String OVERDUE_LIST = "Overdue List";
	public final static String BOOK_COPY_STATUS = "Book Copy Status";
	public final static String SEARCH_BY_ID = "Search By ID";

	// Title and Bio
	public static final String[] FIELD_AUTHOR = { "Title", "Bio" };
	public static final int AUTHOR_TITLE = 0;
	public static final int AUTHOR_BIO = 1;

	// Person Default Data
	public static final String[] FIELD_PERSON = { "ID", "First Name", "Last Name", "Phone" };
	public static final int PERSON_ID = 0;
	public static final int PERSON_FIRST_NAME = 1;
	public static final int PERSON_LAST_NAME = 2;
	public static final int PERSON_PHONE = 3;

	// Address Default Data
	public static final String[] FIELD_ADDRESS = { "Street", "City", "State", "Zip" };
	public static final int ADDRESS_STREET = 0;
	public static final int ADDRESS_CITY = 1;
	public static final int ADDRESS_STATE = 2;
	public static final int ADDRESS_ZIP = 3;

	// Book Default Data
	public static final String[] FIELD_BOOK = { "ISBN", "Title", "Checkout Length", "Member ID"};
	public static final int BOOK_ISBN = 0;
	public static final int BOOK_TITLE = 1;
	public static final int BOOK_CHECKOUT_LENGTH = 2;
	public static final int MEMBER_ID = 3;

	// Success/Error Message
	public final static String LOGIN_SUCCESS_MSG = "Login successful!";
	public final static String LOGIN_ERROR_MSG = "Login failed!";

	// All Button
	public static final String ADD_NEW_MEMBER_BUTN = "Create New Member";
	public static final String UPDATE_MEMBER_BUTN = "Update";
	public static final String ADD_BOOK_BUTN = "Add Book";
	public static final String ADD_AUTHOR_BUTN = "Add Author";
	public static final String CHECKOUT_BUTN = "Checkout";
	public static final String SEARCH_BUTN = "Search";
	public static final String ADD_BUTN = "Add";
	public static final String CLEAR_BUTN = "Clear";
	public static final String CLOSE_BUTN = "Close";

}