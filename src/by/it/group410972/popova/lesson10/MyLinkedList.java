package by.it.group410972.popova.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class MyLinkedList<E> implements Deque<E> {

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;
        Node(E value) { this.value = value; }
    }

    private Node<E> head; // первый элемент
    private Node<E> tail; // последний элемент
    private int size;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        Node<E> node = new Node<>(e);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> node = new Node<>(e);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev != null) prev.next = next;
        else head = next;
        if (next != null) next.prev = prev;
        else tail = prev;
        size--;
    }

    private Node<E> getNode(int index) {
        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E val = head.value;
        unlink(head);
        return val;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E val = tail.value;
        unlink(tail);
        return val;
    }

    // Дополнительные методы интерфейса Deque
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public E peek() { return head == null ? null : head.value; }
    @Override public E peekFirst() { return head == null ? null : head.value; }
    @Override public E peekLast() { return tail == null ? null : tail.value; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) {
        Node<E> cur = tail;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.prev;
        }
        return false;
    }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { head = tail = null; size = 0; }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
