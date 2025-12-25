import java.io.Serializable;

public abstract class Account implements Serializable {
    protected String userID;
    protected String username;
    private String password;

    public Account(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public abstract String getRole();

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
}