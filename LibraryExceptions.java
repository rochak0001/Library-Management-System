package library;

/** Thrown when an ISBN has no matching book in the catalog. */
class BookNotFoundException extends Exception {
    public BookNotFoundException(String isbn) {
        super("No book found with ISBN: " + isbn);
    }
}

/** Thrown when a book exists but has zero copies available to issue right now. */
class BookUnavailableException extends Exception {
    public BookUnavailableException(String isbn) {
        super("Book with ISBN " + isbn + " has no available copies");
    }
}
