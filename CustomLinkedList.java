package library;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A hand-built singly linked list (no java.util.LinkedList used).
 * Supports append, remove-by-match, and forward iteration in O(1) space per step.
 */
public class CustomLinkedList<T> implements Iterable<T> {

    private class Node {
        T data;
        Node next;
        Node(T data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    public void add(T value) {
        Node node = new Node(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /** Removes the first element matching the predicate; returns true if something was removed. */
    public boolean removeIf(java.util.function.Predicate<T> predicate) {
        Node prev = null, curr = head;
        while (curr != null) {
            if (predicate.test(curr.data)) {
                if (prev == null) head = curr.next;
                else prev.next = curr.next;
                if (curr == tail) tail = prev;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = head;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
