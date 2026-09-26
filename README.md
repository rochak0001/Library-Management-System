# Library Management System (Core Java + Custom DSA)

A console-based library management system written in plain Java, with **every core
data structure implemented from scratch** (no `java.util.LinkedList`, `Stack`, `ArrayDeque`,
or `TreeMap`) to demonstrate the underlying data structures directly rather than
relying on the standard library to hide them.

## What it demonstrates

| Feature | Data structure used | Why |
|---|---|---|
| Exact ISBN lookup | `HashMap<String, Book>` | O(1) average lookup |
| Title search & sorted catalog listing | `BookCatalogBST` (hand-built BST) | O(log n) avg search, free in-order sort |
| Master record of all books | `CustomLinkedList<Book>` | O(1) append, safe removal-by-match |
| Reservation waitlist per book | `CustomQueue<Member>` | FIFO — first person to reserve is first served |
| Undo last issue/return | `CustomStack<Action>` | LIFO — most recent action reverts first |
| Catalog persistence | `java.io.BufferedReader` / `BufferedWriter` | Plain-text save/load between runs |
| Domain errors | Custom checked exceptions (`BookNotFoundException`, `BookUnavailableException`) | Clear, typed failure handling instead of generic exceptions |

## Project structure

```
src/library/
  Book.java              - book record (isbn, title, author, copies)
  Member.java             - library member
  Action.java             - a reversible action, for the undo stack
  LibraryExceptions.java  - BookNotFoundException, BookUnavailableException
  CustomLinkedList.java   - hand-built singly linked list
  CustomStack.java        - hand-built LIFO stack
  CustomQueue.java        - hand-built FIFO queue
  BookCatalogBST.java     - hand-built binary search tree (keyed by title)
  Library.java            - core engine wiring the structures together
  Main.java                - console menu (entry point)
```

## Run it

Requires a JDK (17+ recommended).

```bash
cd src
javac -d ../bin library/*.java
cd ..
java -cp bin library.Main
```

On first run it seeds a small demo catalog. Every run saves to `library_data.txt`
in the working directory, which is loaded automatically next time.

## Example session

```
1. Add book        2. Remove book
3. Find by ISBN     4. Find by title
5. List sorted      6. Issue book
7. Return book      8. Undo last action
9. Save             0. Save and exit
```

Try issuing the same title to more members than it has copies — the extra
member is placed on the reservation queue, and returning a copy automatically
hands it to whoever's been waiting longest.
