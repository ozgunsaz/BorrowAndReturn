import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class User extends Account implements Serializable {
    private final int borrowingLimit = 3;
    private LocalDate banExpiryDate;

    public User(String userID, String username, String password) {
        super(userID, username, password);
        this.banExpiryDate = null;
    }

    @Override
    public String getRole() {
        return "User";
    }

    public boolean canBorrow(int currentLoans) {
        if (banExpiryDate != null && banExpiryDate.isAfter(LocalDate.now())) {
            return false;
        }
        return currentLoans < borrowingLimit;
    }

    public void applyBan() {
        this.banExpiryDate = LocalDate.now().plusMonths(1);
    }

    public int getBorrowingLimit() { return borrowingLimit; }
    public LocalDate getBanExpiryDate() { return banExpiryDate; }

    public boolean isBanned() {
        return banExpiryDate != null && banExpiryDate.isAfter(LocalDate.now());
    }
}