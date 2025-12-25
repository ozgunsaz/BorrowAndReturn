import java.util.Scanner;
import java.util.List;

public class LibrarianUI {
    private final Catalog catalog;
    private final Scanner scanner;
    private final Librarian librarian;
    private final UserUI userAccess;

    public LibrarianUI(Catalog catalog, Scanner scanner, Librarian librarian) {
        this.catalog = catalog;
        this.scanner = scanner;
        this.librarian = librarian;
        this.userAccess = new UserUI(catalog, scanner, null);
    }

    public void startSession() {
        boolean running = true;
        while (running) {
            System.out.println("\n---- Librarian Menu ----");
            System.out.println("1. View All Books");
            System.out.println("2. Search for Book");
            System.out.println("3. Manage Book Catalog");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    userAccess.displayCatalog(catalog.getAllBooks());
                    break;
                case "2":
                    userAccess.searchBookPrompt();
                    break;
                case "3":
                    manageCatalogMenu();
                    break;
                case "4":
                    running = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageCatalogMenu() {
        boolean managing = true;
        while(managing) {
            System.out.println("\n---- Manage Catalog Menu ----");
            System.out.println("1. Add New Book");
            System.out.println("2. Remove Existing Book");
            System.out.println("3. Back to Main Librarian Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addBookPrompt();
                    break;
                case "2":
                    removeBookPrompt();
                    break;
                case "3":
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void addBookPrompt() {
        System.out.println("\n---- Add New Book ----");
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter Quantity to add: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Aborting.");
            return;
        }

        Book newBook = new Book(isbn, title, author, genre, quantity);
        catalog.addBook(newBook);
        System.out.println("Book '" + title + "' added to catalog successfully.");
    }

    private void removeBookPrompt() {
        System.out.println("\n---- Remove Book ----");
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        if (catalog.removeBook(isbn)) {
            System.out.println("Book with ISBN " + isbn + " successfully removed.");
        }
    }
}