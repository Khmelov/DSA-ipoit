package by.it.group410971.zusko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Увеличиваем массив при необходимости
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];

            // Копируем элементы начиная с head
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) elements[head];
        return result;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) elements[(tail - 1 + elements.length) % elements.length];
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        @SuppressWarnings("unchecked")
        E result = (E) elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы (заглушки)                 ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E e) { return false; }

    @Override
    public boolean offerLast(E e) { return false; }

    @Override
    public E removeFirst() { return null; }

    @Override
    public E removeLast() { return null; }

    @Override
    public E peekFirst() { return null; }

    @Override
    public E peekLast() { return null; }

    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }

    @Override
    public boolean removeLastOccurrence(Object o) { return false; }

    @Override
    public boolean offer(E e) { return false; }

    @Override
    public E remove() { return null; }

    @Override
    public E peek() { return null; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public void clear() { }

    @Override
    public void push(E e) { }

    @Override
    public E pop() { return null; }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<E> iterator() { return null; }

    @Override
    public Iterator<E> descendingIterator() { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }
}