import java.io.Serializable;
import java.time.LocalDate;

public class TransactionRecord implements Serializable {
    private String transactionID;
    private String bookISBN;
    private String userID;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public TransactionRecord(String transactionID, String bookISBN, String userID, LocalDate loanDate, LocalDate dueDate) {
        this.transactionID = transactionID;
        this.bookISBN = bookISBN;
        this.userID = userID;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }


    public boolean isLate() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        }
        return false;
    }

    public String getBookISBN() { return bookISBN; }
    public String getUserID() { return userID; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return String.format("Transaction ID: %s | Book ISBN: %s | User: %s | Loan: %s | Due: %s | Returned: %s",
                transactionID, bookISBN, userID, loanDate, dueDate,
                returnDate == null ? "N/A" : returnDate);
    }
}