import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalog implements Serializable {
    private List<Book> bookList;
    private List<TransactionRecord> transactionRecords;
    private List<Account> accountList;
    private int nextTransactionID = 1000;
    private int nextUserID = 1001;

    public Catalog() {
        this.bookList = new ArrayList<>();
        this.transactionRecords = new ArrayList<>();
        this.accountList = new ArrayList<>();
    }


    public void addBook(Book book) {
        if (bookList.stream().noneMatch(b -> b.getISBN().equals(book.getISBN()))) {
            bookList.add(book);
        } else {
            System.out.println("Error: Book with ISBN " + book.getISBN() + " already exists.");
        }
    }

    public boolean removeBook(String ISBN) {
        Book bookToRemove = bookList.stream()
                .filter(b -> b.getISBN().equals(ISBN))
                .findFirst()
                .orElse(null);

        if (bookToRemove == null) {
            System.out.println("Error: Book not found.");
            return false;
        }
        if (bookToRemove.getQuantityAvailable() != bookToRemove.getQuantityAvailable()) {
            System.out.println("Error: Cannot remove. Some copies are currently on loan.");
            return false;
        }

        return bookList.remove(bookToRemove);
    }

    public List<Book> searchBooks(String query, String searchType) {
        return bookList.stream()
                .filter(b -> {
                    if (searchType.equalsIgnoreCase("ISBN")) {
                        return b.getISBN().contains(query);
                    } else if (searchType.equalsIgnoreCase("Title")) {
                        return b.getTitle().toLowerCase().contains(query.toLowerCase());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return bookList;
    }

    public int getActiveLoansCount(String userID) {
        return (int) transactionRecords.stream()
                .filter(tr -> tr.getUserID().equals(userID) && tr.getReturnDate() == null)
                .count();
    }

    public String borrowBook(User user, String ISBN) {
        Book book = bookList.stream().filter(b -> b.getISBN().equals(ISBN)).findFirst().orElse(null);

        if (book == null || book.getQuantityAvailable() == 0) {
            return "Error: Book is not available or does not exist.";
        }

        if (!user.canBorrow(getActiveLoansCount(user.getUserID()))) {
            if (user.isBanned()) {
                return "Error: Borrowing temporarily restricted due to late return. Ban expires on " + user.getBanExpiryDate() + ".";
            }
            return "Error: Borrowing failed. You have reached your maximum borrowing limit (" + user.getBorrowingLimit() + ").";
        }

        if (book.reduceQuantity()) {
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusWeeks(3);
            TransactionRecord newRecord = new TransactionRecord(
                    String.valueOf(nextTransactionID++), ISBN, user.getUserID(), loanDate, dueDate
            );
            transactionRecords.add(newRecord);
            return "Success: Book borrowed! Due Date: " + dueDate;
        }
        return "Error: Unknown borrowing error.";
    }

    public String returnBook(User user, String ISBN) {
        TransactionRecord activeRecord = transactionRecords.stream()
                .filter(tr -> tr.getUserID().equals(user.getUserID()) && tr.getBookISBN().equals(ISBN) && tr.getReturnDate() == null)
                .findFirst()
                .orElse(null);

        if (activeRecord == null) {
            return "Error: No active loan found for this book and user (Invalid ISBN).";
        }

        Book book = bookList.stream().filter(b -> b.getISBN().equals(ISBN)).findFirst().orElse(null);
        if (book == null) {
            return "Error: Book not found in catalog. Check required.";
        }

        LocalDate returnDate = LocalDate.now();
        activeRecord.setReturnDate(returnDate);
        book.increaseQuantity();

        String message = "Success: Book returned on " + returnDate + ".";

        if (activeRecord.isLate()) {
            user.applyBan();
            message += " WARNING: Return was late. A one-month borrowing ban has been applied. Ban expires on " + user.getBanExpiryDate() + ".";
        }

        return message;
    }

    public String getNextUserID() {
        String newID = "U" + nextUserID;
        nextUserID++;
        return newID;
    }

    public Account getAccount(String username) {
        return accountList.stream()
                .filter(a -> a.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }


    public boolean isUsernameTaken(String username) {
        return accountList.stream()
                .anyMatch(a -> a.getUsername().equalsIgnoreCase(username));
    }

    public void addAccount(Account account) {
        this.accountList.add(account);
    }

    public List<TransactionRecord> getUserTransactions(String userID) {
        return transactionRecords.stream()
                .filter(tr -> tr.getUserID().equals(userID))
                .collect(Collectors.toList());
    }
}