import java.io.Serializable;

public class Book implements Serializable {
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private int quantityAvailable;

    public Book(String ISBN, String title, String author, String genre, int quantity) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantityAvailable = quantity;
    }

    public boolean reduceQuantity() {
        if (quantityAvailable > 0) {
            quantityAvailable--;
            return true;
        }
        return false;
    }

    public void increaseQuantity() {
        quantityAvailable++;
    }


    public String getISBN() { return ISBN; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getQuantityAvailable() { return quantityAvailable; }

    @Override
    public String toString() {
        return String.format("%-15s | %-30s | %-20s | %-20s | %-10s",
                ISBN, title, author, genre, "Quantity: " + quantityAvailable);
    }
}