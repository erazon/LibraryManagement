package dataaccess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.Checkout;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public class DataAccessFacade implements DataAccess {

	enum StorageType {
		BOOKS, MEMBERS, USERS, CHECKOUT;
	}

	public static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/dataaccess/storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";

	// implement: other save operations
	public void saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		mems.put(memberId, member);
		saveToStorage(StorageType.MEMBERS, mems);
	}

	// implement: other save operations
	public void saveNewBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		String bookIsbn = book.getIsbn();
		books.put(bookIsbn, book);
		saveToStorage(StorageType.BOOKS, books);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Checkout> readCheckoutMap() {
		// Returns a Map with name/value pairs being
		// checkoutId -> Checkout
		return (HashMap<String, Checkout>) readFromStorage(StorageType.CHECKOUT);
	}

	public void saveNewCheckout(Checkout checkout) {
		HashMap<String, Checkout> check = readCheckoutMap();
		String checkoutId = checkout.getCheckoutId();
		check.put(checkoutId, checkout);
		saveToStorage(StorageType.CHECKOUT, check);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Book> readBooksMap() {
		// Returns a Map with name/value pairs being
		// isbn -> Book
		return (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		// Returns a Map with name/value pairs being
		// memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
	}

	@SuppressWarnings("unchecked")
	public LibraryMember readMemberById(String ID) {
		HashMap<String, LibraryMember> map = (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
		if (map.containsKey(ID)) {
			return map.get(ID);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Book readBookByISBN(String isbn) {
		HashMap<String, Book> map = (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
		if (map.containsKey(isbn)) {
			return map.get(isbn);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String[]> readAllMember() {
		HashMap<String, LibraryMember> map = (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
		List<String[]> list = new ArrayList<String[]>();
		map.forEach((k, v) -> {
			System.out.println(v.getFirstName());
			String[] s = new String[3];
			s[0] = v.getMemberId();
			s[1] = v.getFirstName() + " " + v.getLastName();
			s[2] = v.getTelephone();
			list.add(s);
		});
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<String[]> readAllCheckout() {
		HashMap<String, Checkout> map = readCheckoutMap();
		List<String[]> list = new ArrayList<String[]>();
		map.forEach((k, v) -> {
			String[] s = new String[3];
			s[0] = v.getCopy().getBook().getIsbn();
			s[1] = v.getMemberId();
			s[2] = v.getDueDate();
			list.add(s);
		});
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<String[]> readOverdueCheckout() {
		HashMap<String, Checkout> map = readCheckoutMap();
		List<String[]> list = new ArrayList<String[]>();
		map.forEach((k, v) -> {
			LocalDate dueDate =  LocalDate.parse(v.getDueDate(), DateTimeFormatter.ofPattern(DataAccessFacade.DATE_PATTERN));
			if(dueDate.compareTo(LocalDate.now()) < 0) {
				String[] s = new String[3];
				s[0] = v.getCopy().getBook().getIsbn();
				s[1] = v.getMemberId();
				s[2] = v.getDueDate();
				list.add(s);
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String[]> readAllBook() {
		HashMap<String, Book> map = (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
		List<String[]> list = new ArrayList<String[]>();
		map.forEach((k, v) -> {
			String[] s = new String[3];
			s[0] = v.getIsbn();
			s[1] = v.getTitle();
			s[2] = v.getMaxCheckoutLength() + "";
			list.add(s);
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String[]> readAllBookStatus() {
		HashMap<String, Book> map = (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
		List<String[]> list = new ArrayList<String[]>();
		map.forEach((k, v) -> {
			String[] s = new String[3];
			s[0] = v.getIsbn();
			s[1] = String.valueOf(v.getMaxCheckoutLength());
			s[2] = String.valueOf(v.getNumCopies());
			list.add(s);
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, User> readUserMap() {
		// Returns a Map with name/value pairs being
		// userId -> User
		return (HashMap<String, User>) readFromStorage(StorageType.USERS);
	}

	///// load methods - these place test data into the storage area
	///// - used just once at startup

	static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}

	static void loadUserMap(List<User> userList) {
		HashMap<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}

	static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}

	static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
			System.out.println(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return retVal;
	}

	final static class Pair<S, T> implements Serializable {

		S first;
		T second;

		Pair(S s, T t) {
			first = s;
			second = t;
		}

		@Override
		public boolean equals(Object ob) {
			if (ob == null)
				return false;
			if (this == ob)
				return true;
			if (ob.getClass() != getClass())
				return false;
			@SuppressWarnings("unchecked")
			Pair<S, T> p = (Pair<S, T>) ob;
			return p.first.equals(first) && p.second.equals(second);
		}

		@Override
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}

		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}

		private static final long serialVersionUID = 5399827794066637059L;
	}

}
