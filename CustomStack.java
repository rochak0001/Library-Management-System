package library;

import java.util.EmptyStackException;

/**
 * A hand-built LIFO stack backed by singly linked nodes.
 * Used to power the application's "undo last action" feature.
 */
public class CustomStack<T> {

    private class Node {
        T data;
        Node next;
        Node(T data, Node next) { this.data = data; this.next = next; }
    }

    private Node top;
    private int size;

    public void push(T value) {
        top = new Node(value, top);
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int size() { return size; }
}
