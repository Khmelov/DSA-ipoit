package by.it.group410971.kazakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements = new Object[10];
    private int size;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                out.append(", ");
            }
            out.append(elements[i]);
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int idx = findIndex(e);
        if (idx >= 0) {
            return false;
        }
        int insertAt = -idx - 1;
        ensureCapacity(size + 1);
        int moved = size - insertAt;
        if (moved > 0) {
            System.arraycopy(elements, insertAt, elements, insertAt + 1, moved);
        }
        elements[insertAt] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = findIndex(o);
        if (idx < 0) {
            return false;
        }
        int moved = size - idx - 1;
        if (moved > 0) {
            System.arraycopy(elements, idx + 1, elements, idx, moved);
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return findIndex(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                @SuppressWarnings("unchecked")
                E value = (E) elements[index++];
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[size];
        System.arraycopy(elements, 0, out, 0, size);
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
            return result;
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int newCapacity = Math.max(minCapacity, elements.length * 2);
        Object[] next = new Object[newCapacity];
        System.arraycopy(elements, 0, next, 0, size);
        elements = next;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<? super E>) a).compareTo((E) b);
    }

    private int findIndex(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], o);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
