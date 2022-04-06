package dataaccess;

import java.util.HashMap;

import business.Book;
import business.Checkout;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public void saveNewMember(LibraryMember member); 
	public HashMap<String, Checkout> readCheckoutMap();
	public void saveNewCheckout(Checkout checkout); 
}
