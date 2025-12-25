import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserUI {
    private final Catalog catalog;
    private final Scanner scanner;
    private final User user;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserUI(Catalog catalog, Scanner scanner, User user) {
        this.catalog = catalog;
        this.scanner = scanner;
        this.user = user;
    }

    public void startSession() {
        boolean running = true;
        while (running) {
            System.out.println("\n---- User Menu ----");
            System.out.println("1. View All Books");
            System.out.println("2. Search for Book");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. View My Transactions");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayCatalog(catalog.getAllBooks());
                    break;
                case "2":
                    searchBookPrompt();
                    break;
                case "3":
                    borrowBookPrompt();
                    break;
                case "4":
                    returnBookPrompt();
                    break;
                case "5":
                    displayUserTransactions();
                    break;
                case "6":
                    running = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public void displayCatalog(List<Book> books) {
        System.out.println("\n---- CATALOG ----");
        System.out.println(String.format("%-15s | %-30s | %-20s | %-20s | %-10s", "ISBN", "Title", "Author", "Genre", "Availability"));
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        if (books.isEmpty()) {
            System.out.println("The catalog is empty.");
            return;
        }
        books.forEach(System.out::println);
    }

    public void searchBookPrompt() {
        System.out.println("\n---- Search Book ----");
        String typeChoice;
        String searchType = "";
        boolean validChoice = false;

        do {
            System.out.print("Search by (1) ISBN or (2) Title: ");
            typeChoice = scanner.nextLine();

            if (typeChoice.equals("1")) {
                searchType = "ISBN";
                validChoice = true;
            } else if (typeChoice.equals("2")) {
                searchType = "Title";
                validChoice = true;
            } else {
                System.out.println("Invalid choice. Please enter '1' for ISBN or '2' for Title.");
            }
        } while (!validChoice);

        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<Book> results = catalog.searchBooks(query, searchType);
        if (results.isEmpty()) {
            System.out.println("No books found matching your query.");
        } else {
            System.out.println("\n---- Search Results ----");
            displayCatalog(results);
        }
    }

    private void borrowBookPrompt() {
        System.out.println("\n---- Borrow Book ----");
        if (user.isBanned()) {
            System.out.println("!! WARNING !! You are currently banned from borrowing until: " + user.getBanExpiryDate().format(DATE_FORMAT));
        }
        System.out.println("Your current loan count: " + catalog.getActiveLoansCount(user.getUserID()) + "/" + user.getBorrowingLimit()); // FR6 Display
        System.out.print("Enter ISBN of the book you wish to borrow: ");
        String ISBN = scanner.nextLine();

        String result = catalog.borrowBook(user, ISBN);
        System.out.println(result);
    }

    private void returnBookPrompt() {
        System.out.println("\n---- Return Book ----");
        System.out.print("Enter ISBN of the book you wish to return: ");
        String ISBN = scanner.nextLine();

        String result = catalog.returnBook(user, ISBN); // Call to Catalog
        System.out.println(result);
    }

    private void displayUserTransactions() {
        System.out.println("\n---- YOUR TRANSACTION HISTORY ----");
        List<TransactionRecord> records = catalog.getUserTransactions(user.getUserID());
        if (records.isEmpty()) {
            System.out.println("No transaction history found.");
            return;
        }
        records.forEach(System.out::println);
    }
}