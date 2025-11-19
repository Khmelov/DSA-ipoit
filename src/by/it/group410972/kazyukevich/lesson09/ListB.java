package by.it.group410972.kazyukevich.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ListB() {
        elements = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        E[] newArr = (E[]) new Object[size + 1];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        newArr[size] = e;
        elements = newArr;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) return;
        E[] newArr = (E[]) new Object[size + 1];
        for (int i = 0; i < index; i++) {
            newArr[i] = elements[i];
        }
        newArr[index] = element;
        for (int i = index; i < size; i++) {
            newArr[i + 1] = elements[i];
        }
        elements = newArr;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) return null;
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) return null;
        E removed = elements[index];
        E[] newArr = (E[]) new Object[size - 1];
        for (int i = 0; i < index; i++) {
            newArr[i] = elements[i];
        }
        for (int i = index + 1; i < size; i++) {
            newArr[i - 1] = elements[i];
        }
        elements = newArr;
        size--;
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public void clear() {
        elements = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    // ---- Остальные методы (необязательные, но должны быть) ----
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
}
