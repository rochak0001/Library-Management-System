package library;

/** Simple immutable-ish record of a book title held by the library. */
public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    void incrementAvailable() { availableCopies++; }
    void decrementAvailable() { availableCopies--; }

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" by %s - %d/%d available",
                isbn, title, author, availableCopies, totalCopies);
    }
}
