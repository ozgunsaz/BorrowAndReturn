import java.util.Scanner;

public class AuthenticationUI {
    private final Catalog catalog;
    private final Scanner scanner;

    public AuthenticationUI(Catalog catalog, Scanner scanner) {
        this.catalog = catalog;
        this.scanner = scanner;
    }


    public void start() {
        boolean applicationRunning = true;
        while (applicationRunning) {
            System.out.println("\n---- Welcome to Borrow and Return (B&R) ----");
            System.out.println("1. Login");
            System.out.println("2. Register New User");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    attemptLogin();
                    break;
                case "2":
                    registerUserPrompt();
                    break;
                case "3":
                    applicationRunning = false;
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private boolean attemptLogin() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        Account loggedInAccount = catalog.getAccount(username);

        if (loggedInAccount != null && loggedInAccount.login(username, password)) {
            System.out.println("\nLogin Successful! Welcome, " + loggedInAccount.getUsername() + " (" + loggedInAccount.getRole() + ")");

            if (loggedInAccount instanceof User) {
                new UserUI(catalog, scanner, (User)loggedInAccount).startSession();
            } else if (loggedInAccount instanceof Librarian) {
                new LibrarianUI(catalog, scanner, (Librarian)loggedInAccount).startSession();
            }
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }

    private void registerUserPrompt() {
        System.out.println("\n---- New User Registration ----");
        String username;
        do {
            System.out.print("Enter desired Username: ");
            username = scanner.nextLine();
            if (catalog.isUsernameTaken(username)) {
                System.out.println("Username already exists. Please choose a different one.");
            }
        } while (catalog.isUsernameTaken(username));

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String newUserID = catalog.getNextUserID();

        User newUser = new User(newUserID, username, password);
        catalog.addAccount(newUser);

        System.out.println("\nRegistration successful!");
        System.out.println("Account created for " + username + " with ID: " + newUserID);
    }
}