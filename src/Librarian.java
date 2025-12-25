import java.io.Serializable;

public class Librarian extends Account implements Serializable {

    public Librarian(String userID, String username, String password) {
        super(userID, username, password);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

}