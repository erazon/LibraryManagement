package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import dataaccess.DataAccessFacade;

final public class Checkout implements Serializable {

	private static final long serialVersionUID = 6110690276685962830L;
	private String checkoutId;
	private BookCopy copy;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private String memberId;

	public Checkout(BookCopy copy, LocalDate checkoutDate, LocalDate dueDate, String memberId) {
		this.checkoutId = UUID.randomUUID().toString();
		this.copy = copy;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
		this.memberId = memberId;
	}

	public String getCheckoutId() {
		return checkoutId;
	}

	public BookCopy getCopy() {
		return copy;
	}

	public String getCheckoutDate() {
		return checkoutDate.format(DateTimeFormatter.ofPattern(DataAccessFacade.DATE_PATTERN)).toString();
	}

	public String getDueDate() {
		return dueDate.format(DateTimeFormatter.ofPattern(DataAccessFacade.DATE_PATTERN)).toString();
	}

	public String getMemberId() {
		return memberId;
	}
	
	@Override
	public String toString() {
		return "ISBN: "+copy.getBook().getIsbn()+", Member Id: "+memberId;
	}
}
