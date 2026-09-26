package library;

import java.util.NoSuchElementException;

/**
 * A hand-built FIFO queue backed by singly linked nodes.
 * Used to hold the reservation waitlist for a book that is out of copies.
 */
public class CustomQueue<T> {

    private class Node {
        T data;
        Node next;
        Node(T data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    public void enqueue(T value) {
        Node node = new Node(value);
        if (tail == null) { head = node; tail = node; }
        else { tail.next = node; tail = node; }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return data;
    }

    public boolean isEmpty() { return head == null; }
    public int size() { return size; }
}
