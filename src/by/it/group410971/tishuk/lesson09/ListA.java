package by.it.group410971.tishuk.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    private E[] elements;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public ListA() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
    }

    // --- Метод для увеличения массива ---
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
    }

    // --- Обязательные методы ---
    @Override
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E removed = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

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

    // --- Опциональные методы можно оставить не реализованными ---
    @Override
    public void add(int index, E element) { throw new UnsupportedOperationException(); }
    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public E set(int index, E element) { throw new UnsupportedOperationException(); }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public void clear() { for (int i = 0; i < size; i++) elements[i] = null; size = 0; }
    @Override
    public int indexOf(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public E get(int index) { throw new UnsupportedOperationException(); }
    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public int lastIndexOf(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(int index, Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
    @Override
    public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }
    @Override
    public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}
