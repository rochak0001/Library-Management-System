package library;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core library engine. Deliberately built on hand-written data structures
 * (CustomLinkedList, CustomStack, CustomQueue, BookCatalogBST) instead of
 * java.util equivalents, to demonstrate the underlying DSA rather than hide it.
 *
 * - HashMap<isbn, Book>        -> O(1) exact-ISBN lookup
 * - BookCatalogBST             -> O(log n) avg lookup by title + sorted listing
 * - CustomLinkedList<Book>     -> insertion-ordered master record of all books
 * - CustomQueue<Member> (x N)  -> FIFO reservation waitlist per out-of-stock book
 * - CustomStack<Action>        -> undo history for issue/return actions
 */
public class Library {

    private final Map<String, Book> catalogByIsbn = new HashMap<>();
    private final BookCatalogBST catalogByTitle = new BookCatalogBST();
    private final CustomLinkedList<Book> allBooks = new CustomLinkedList<>();
    private final Map<String, CustomQueue<Member>> waitlists = new HashMap<>();
    private final CustomStack<Action> history = new CustomStack<>();

    public void addBook(String isbn, String title, String author, int copies) {
        Book book = new Book(isbn, title, author, copies);
        catalogByIsbn.put(isbn, book);
        catalogByTitle.insert(book);
        allBooks.add(book);
    }

    public void removeBook(String isbn) throws BookNotFoundException {
        Book book = catalogByIsbn.get(isbn);
        if (book == null) throw new BookNotFoundException(isbn);
        catalogByIsbn.remove(isbn);
        catalogByTitle.delete(book.getTitle());
        allBooks.removeIf(b -> b.getIsbn().equals(isbn));
        waitlists.remove(isbn);
    }

    public Book findByIsbn(String isbn) throws BookNotFoundException {
        Book book = catalogByIsbn.get(isbn);
        if (book == null) throw new BookNotFoundException(isbn);
        return book;
    }

    public Book findByTitle(String title) throws BookNotFoundException {
        Book book = catalogByTitle.searchByTitle(title);
        if (book == null) throw new BookNotFoundException(title);
        return book;
    }

    public List<Book> listAllSortedByTitle() {
        return catalogByTitle.listSortedByTitle();
    }

    /**
     * Issues a book to a member. If no copies remain, the member is placed on
     * the FIFO reservation queue for that title instead of failing silently.
     */
    public String issueBook(String isbn, String memberName) throws BookNotFoundException {
        Book book = findByIsbn(isbn);
        if (book.getAvailableCopies() <= 0) {
            waitlists.computeIfAbsent(isbn, k -> new CustomQueue<>()).enqueue(new Member(memberName));
            return memberName + " added to the reservation queue for \"" + book.getTitle() + "\" (no copies free).";
        }
        book.decrementAvailable();
        history.push(new Action(Action.Type.ISSUE, isbn, memberName));
        return "Issued \"" + book.getTitle() + "\" to " + memberName + ".";
    }

    /**
     * Returns a book. If a reservation queue exists for it, the book is
     * immediately handed to the next member in line (FIFO) instead of
     * sitting idle on the shelf.
     */
    public String returnBook(String isbn) throws BookNotFoundException {
        Book book = findByIsbn(isbn);
        book.incrementAvailable();
        history.push(new Action(Action.Type.RETURN, isbn, null));

        CustomQueue<Member> queue = waitlists.get(isbn);
        if (queue != null && !queue.isEmpty()) {
            Member next = queue.dequeue();
            book.decrementAvailable();
            history.push(new Action(Action.Type.ISSUE, isbn, next.getName()));
            return "Returned \"" + book.getTitle() + "\", then auto-issued to next in queue: " + next.getName() + ".";
        }
        return "Returned \"" + book.getTitle() + "\".";
    }

    /** Reverts the most recent issue/return action. Powered by the undo stack. */
    public String undoLastAction() throws BookNotFoundException {
        if (history.isEmpty()) return "Nothing to undo.";
        Action last = history.pop();
        Book book = findByIsbn(last.isbn);
        if (last.type == Action.Type.ISSUE) {
            book.incrementAvailable();
            return "Undid issue of \"" + book.getTitle() + "\" to " + last.memberName + ".";
        } else {
            book.decrementAvailable();
            return "Undid return of \"" + book.getTitle() + "\".";
        }
    }

    public int totalTitles() { return allBooks.size(); }

    // ---- File persistence (java.io) ----

    public void saveToFile(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Book b : allBooks) {
                writer.write(String.join("|", b.getIsbn(), b.getTitle(), b.getAuthor(),
                        String.valueOf(b.getTotalCopies()), String.valueOf(b.getAvailableCopies())));
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue;
                addBook(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                Book b = catalogByIsbn.get(parts[0]);
                int available = Integer.parseInt(parts[4]);
                while (b.getAvailableCopies() > available) b.decrementAvailable();
            }
        }
    }
}
