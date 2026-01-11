package by.it.group410972.stankevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int INITIAL_CAPACITY = 16;
    private Object[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        if (a instanceof Comparable) {
            return ((Comparable<E>) a).compareTo(b);
        }
        return a.toString().compareTo(b.toString());
    }

    private int findIndex(E e) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            @SuppressWarnings("unchecked")
            E midElement = (E) elements[mid];
            int cmp = compare(e, midElement);

            if (cmp < 0) {
                right = mid - 1;
            } else if (cmp > 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -(left + 1);
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
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
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            E e = (E) o;
            return findIndex(e) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        int index = findIndex(e);
        if (index >= 0) {
            return false;
        }

        int insertPos = -index - 1;
        ensureCapacity();


        for (int i = size; i > insertPos; i--) {
            elements[i] = elements[i - 1];
        }

        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        try {
            E e = (E) o;
            int index = findIndex(e);
            if (index < 0) {
                return false;
            }


            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }

            elements[--size] = null;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            if (c.contains(element)) {
                newElements[newSize++] = element;
            } else {
                modified = true;
            }
        }

        elements = newElements;
        size = newSize;
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
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
}
