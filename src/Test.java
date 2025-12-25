import java.time.LocalDate;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        testBorrowingAndLateReturnLogic();
    }

    public static void testBorrowingAndLateReturnLogic() {
        System.out.println("----- Testing Catalog Business Logic (UC1, UC2, FR5, FR6, FR8) -----");

        Catalog testCatalog = new Catalog();
        User testUser = new User("T001", "tester", "testpass");
        testCatalog.addAccount(testUser);
        Book bookA = new Book("100-A", "Test Book A", "Author X", "RandomTest", 1);
        testCatalog.addBook(bookA);

        // --- Test 1: Successful Borrowing ---
        System.out.println("\n[Test 1] Successful Borrow (FR5):");
        String result1 = testCatalog.borrowBook(testUser, "100-A");
        System.out.println("Result: " + result1);

        if (result1.startsWith("Success") && bookA.getQuantityAvailable() == 0 && testCatalog.getActiveLoansCount("T001") == 1) {
            System.out.println("  PASS: Borrowing successful and quantity updated.");
        } else {
            System.out.println("  FAIL: Borrowing failed or quantity/count incorrect.");
        }


        // --- Test 2: Borrowing Unavailable Book ---
        System.out.println("\n[Test 2] Borrow Unavailable Book (FR7 Check):");
        String result2 = testCatalog.borrowBook(testUser, "100-A");
        System.out.println("Result: " + result2);
        if (result2.contains("not available")) {
            System.out.println("  PASS: Correctly blocked borrowing when quantity is 0.");
        } else {
            System.out.println("  FAIL: Did not block borrowing when unavailable.");
        }


        // --- Test 3: Late Return and Ban (UC2/FR8) ---
        System.out.println("\n[Test 3] Late Return and Ban (FR8):");

        List<TransactionRecord> transactions = testCatalog.getUserTransactions("T001");
        if (!transactions.isEmpty()) {
            TransactionRecord loanRecord = transactions.get(0);

            loanRecord.setDueDate(LocalDate.now().minusDays(5));

            String result3 = testCatalog.returnBook(testUser, "100-A");
            System.out.println("Result: " + result3);

            if (result3.contains("WARNING") && testUser.isBanned()) {
                System.out.println("  PASS: Late return resulted in a borrowing ban.");
            } else {
                System.out.println("  FAIL: Ban was not correctly applied after late return.");
            }
        } else {
            System.out.println("  FAIL: Could not retrieve loan record for testing.");
        }


        // --- Test 4: Borrowing while Banned (Alternate Path UC1) ---
        System.out.println("\n[Test 4] Borrowing while Banned (FR8 Check):");
        Book bookB = new Book("100-B", "Test Book B", "Author Y", "RandomTest2", 1);
        testCatalog.addBook(bookB);
        String result4 = testCatalog.borrowBook(testUser, "100-B");
        System.out.println("Result: " + result4);

        if (result4.contains("temporarily restricted")) {
            System.out.println("  PASS: Correctly blocked borrowing while banned.");
        } else {
            System.out.println("  FAIL: Allowed borrowing while banned.");
        }

        System.out.println("\n----- Catalog Test Complete -----");
    }
}