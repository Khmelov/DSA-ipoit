package by.it.group410972.popova.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;   // индекс первого элемента
    private int tail;   // индекс после последнего элемента
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16]; // начальная ёмкость
        head = 0;
        tail = 0;
        size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            @SuppressWarnings("unchecked")
            E[] newArr = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            head = 0;
            tail = size;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
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
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E val = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return val;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E val = elements[tail];
        elements[tail] = null;
        size--;
        return val;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (elements[idx] == null ? o == null : elements[idx].equals(o)) {
                // сдвигаем элементы влево
                for (int j = idx; j != tail; j = (j + 1) % elements.length) {
                    int next = (j + 1) % elements.length;
                    elements[j] = elements[next];
                }
                tail = (tail - 1 + elements.length) % elements.length;
                elements[tail] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (elements[idx] == null ? o == null : elements[idx].equals(o)) {
                // сдвигаем элементы вправо
                for (int j = idx; j != tail; j = (j + 1) % elements.length) {
                    int next = (j + 1) % elements.length;
                    elements[j] = elements[next];
                }
                tail = (tail - 1 + elements.length) % elements.length;
                elements[tail] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public E peek() { return size == 0 ? null : getFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size == 0 ? null : getLast(); }
    @Override public boolean remove(Object o) { return removeFirstOccurrence(o); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { while (pollFirst() != null); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
