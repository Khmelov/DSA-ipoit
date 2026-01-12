package by.it.group410972.Pushkarev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyTreeSet() {
        elements = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private void increaseSize() {
        if (size >= elements.length) {
            E[] newElements = (E[]) new Comparable[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private int findIndex(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            else if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -left - 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            return findIndex(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
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
    public boolean add(E e) {
        int idx = findIndex(e);
        if (idx >= 0) return false;
        idx = -idx - 1;
        increaseSize();
        System.arraycopy(elements, idx, elements, idx + 1, size - idx);
        elements[idx] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            int idx = findIndex(e);
            if (idx < 0) return false;
            // сдвигаем элементы влево
            System.arraycopy(elements, idx + 1, elements, idx, size - idx - 1);
            elements[size - 1] = null;
            size--;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            } else i++;
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }
}
