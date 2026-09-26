package library;

import java.util.ArrayList;
import java.util.List;

/**
 * A hand-built Binary Search Tree keyed by book title (case-insensitive).
 * Gives average O(log n) search by title and an O(n) alphabetically-sorted
 * listing via in-order traversal, without relying on java.util.TreeMap.
 */
public class BookCatalogBST {

    private class Node {
        Book book;
        Node left, right;
        Node(Book book) { this.book = book; }
    }

    private Node root;

    public void insert(Book book) {
        root = insertRec(root, book);
    }

    private Node insertRec(Node node, Book book) {
        if (node == null) return new Node(book);
        int cmp = book.getTitle().compareToIgnoreCase(node.book.getTitle());
        if (cmp < 0) node.left = insertRec(node.left, book);
        else if (cmp > 0) node.right = insertRec(node.right, book);
        else node.book = book; // same title: overwrite
        return node;
    }

    public Book searchByTitle(String title) {
        Node curr = root;
        while (curr != null) {
            int cmp = title.compareToIgnoreCase(curr.book.getTitle());
            if (cmp == 0) return curr.book;
            curr = cmp < 0 ? curr.left : curr.right;
        }
        return null;
    }

    public void delete(String title) {
        root = deleteRec(root, title);
    }

    private Node deleteRec(Node node, String title) {
        if (node == null) return null;
        int cmp = title.compareToIgnoreCase(node.book.getTitle());
        if (cmp < 0) node.left = deleteRec(node.left, title);
        else if (cmp > 0) node.right = deleteRec(node.right, title);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.book = successor.book;
            node.right = deleteRec(node.right, successor.book.getTitle());
        }
        return node;
    }

    /** Alphabetical listing via in-order traversal. */
    public List<Book> listSortedByTitle() {
        List<Book> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node node, List<Book> acc) {
        if (node == null) return;
        inOrder(node.left, acc);
        acc.add(node.book);
        inOrder(node.right, acc);
    }
}
