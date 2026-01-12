package by.it.group410971.tishuk.lesson10;

import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    // ================= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =================

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        for (int i = size; i > 0; i--) {
            elements[i] = elements[i - 1];
        }
        elements[0] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[size++] = element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E getLast() {
        return size == 0 ? null : elements[size - 1];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;

        E value = elements[0];
        for (int i = 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        elements[--size] = null;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;

        E value = elements[--size];
        elements[size] = null;
        return value;
    }

    // ================= ВСПОМОГАТЕЛЬНОЕ =================

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArray = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }

    // ================= НЕОБЯЗАТЕЛЬНЫЕ (пустые) =================

    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean offer(E e) { return false; }
    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public void push(E e) {}
    @Override public E pop() { return null; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(java.util.Collection<?> c) { return false; }
    @Override public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override public void clear() {}
    @Override public E peek() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
}
