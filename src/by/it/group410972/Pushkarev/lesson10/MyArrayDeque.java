package by.it.group410972.Pushkarev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int INITIAL_CAPACITY = 16;

    private E[] data;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        data = (E[]) new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void increaseSize() {
        if (size == data.length) {
            int newCapacity = data.length * 2;
            E[] newData = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = data[(head + i) % data.length];
            }
            data = newData;
            head = 0;
            tail = size;
        }
    }

    private void checkNotEmpty() {
        if (size == 0) throw new NoSuchElementException();
    }

    private int dec(int index) {
        return (index - 1 + data.length) % data.length;
    }

    private int inc(int index) {
        return (index + 1) % data.length;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % data.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void addFirst(E element) {
        increaseSize();
        head = dec(head);
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E e) {
        increaseSize();
        data[tail] = e;
        tail = inc(tail);
        size++;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E val = data[head];
        data[head] = null;
        head = inc(head);
        size--;
        return val;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        E val = data[tail];
        data[tail] = null;
        size--;
        return val;

    }

    @Override
    public E getFirst() {
        checkNotEmpty();
        return data[head];
    }

    @Override
    public E getLast() {
        checkNotEmpty();
        return data[dec(tail)];
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}
