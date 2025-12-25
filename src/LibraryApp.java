import java.util.Scanner;

public class LibraryApp {
    private Catalog catalog;
    private Scanner scanner;

    public LibraryApp() {
        this.catalog = new Catalog();
        this.scanner = new Scanner(System.in);
        initializeData();
    }

    // --- Initialization (Data Setup for presentation) ---
    private void initializeData() {
        // Default Accounts (FR1)
        catalog.addAccount(new User("U001", "ece", "ece123"));
        catalog.addAccount(new User("U002", "anil", "anil123"));
        catalog.addAccount(new User("U003", "omerlevent", "omerlevent123"));

        // Default Librarians
        catalog.addAccount(new Librarian("L001", "fatma", "fatma123"));
        catalog.addAccount(new Librarian("L002", "ozgun", "ozgun123"));
        catalog.addAccount(new Librarian("L003", "berkekaan", "berkekaan123"));

        // Default Books
        catalog.addBook(new Book("001", "The Java Handbook", "J. Gosling", "Tech", 3));
        catalog.addBook(new Book("002", "Design Patterns Refactored", "E. Gamma", "Tech", 1));
        catalog.addBook(new Book("003", "Ah Su Otizm", "Barış Korkmaz", "Science", 5));
        catalog.addBook(new Book("004", "Kucuk Kara Balik", "Samed Behrengi", "Fiction", 2));

        System.out.println("---- Borrow and Return (B&R) Initializing... ----");
    }


    public void start() {
        new AuthenticationUI(this.catalog, this.scanner).start();
    }

    public static void main(String[] args) {
        LibraryApp app = new LibraryApp();
        app.start();
    }
}