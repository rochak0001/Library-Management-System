package library;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** Menu-driven console entry point for the Library Management System. */
public class Main {

    private static final String DATA_FILE = "library_data.txt";

    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        try {
            library.loadFromFile(DATA_FILE);
            System.out.println("Loaded existing catalog from " + DATA_FILE + " (" + library.totalTitles() + " titles).");
        } catch (IOException e) {
            System.out.println("Could not load existing data: " + e.getMessage());
        }

        if (library.totalTitles() == 0) {
            seedDemoData(library);
            System.out.println("No saved data found - seeded a demo catalog instead.");
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("ISBN: ");
                        String isbn = sc.nextLine().trim();
                        System.out.print("Title: ");
                        String title = sc.nextLine().trim();
                        System.out.print("Author: ");
                        String author = sc.nextLine().trim();
                        System.out.print("Copies: ");
                        int copies = Integer.parseInt(sc.nextLine().trim());
                        library.addBook(isbn, title, author, copies);
                        System.out.println("Added.");
                    }
                    case "2" -> {
                        System.out.print("ISBN to remove: ");
                        library.removeBook(sc.nextLine().trim());
                        System.out.println("Removed.");
                    }
                    case "3" -> {
                        System.out.print("ISBN: ");
                        System.out.println(library.findByIsbn(sc.nextLine().trim()));
                    }
                    case "4" -> {
                        System.out.print("Title: ");
                        System.out.println(library.findByTitle(sc.nextLine().trim()));
                    }
                    case "5" -> {
                        List<Book> books = library.listAllSortedByTitle();
                        if (books.isEmpty()) System.out.println("Catalog is empty.");
                        books.forEach(System.out::println);
                    }
                    case "6" -> {
                        System.out.print("ISBN: ");
                        String isbn = sc.nextLine().trim();
                        System.out.print("Member name: ");
                        String member = sc.nextLine().trim();
                        System.out.println(library.issueBook(isbn, member));
                    }
                    case "7" -> {
                        System.out.print("ISBN: ");
                        System.out.println(library.returnBook(sc.nextLine().trim()));
                    }
                    case "8" -> System.out.println(library.undoLastAction());
                    case "9" -> {
                        library.saveToFile(DATA_FILE);
                        System.out.println("Saved to " + DATA_FILE + ".");
                    }
                    case "0" -> {
                        library.saveToFile(DATA_FILE);
                        System.out.println("Saved. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Not a valid option - pick a number from the menu.");
                }
            } catch (BookNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number for copies.");
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("""

                ===== Library Management System =====
                1. Add book
                2. Remove book
                3. Find book by ISBN (HashMap, O(1))
                4. Find book by title (BST, O(log n) avg)
                5. List all books sorted by title (BST in-order)
                6. Issue book to member
                7. Return book
                8. Undo last issue/return (Stack)
                9. Save catalog to file
                0. Save and exit
                Choose: """);
    }

    private static void seedDemoData(Library library) {
        library.addBook("978-0132350884", "Clean Code", "Robert C. Martin", 2);
        library.addBook("978-0201633610", "Design Patterns", "Gamma, Helm, Johnson, Vlissides", 1);
        library.addBook("978-0262033848", "Introduction to Algorithms", "Cormen, Leiserson, Rivest, Stein", 1);
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 2);
    }
}
