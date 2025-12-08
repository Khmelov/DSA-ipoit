package by.it.group410971.galdytskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] array;
    private int size;

    public MyTreeSet() {
        array = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    private int binarySearch(E key) {
        int low = 0, high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = array[mid].compareTo(key);
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

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > array.length) {
            int newCapacity = array.length * 2;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            E[] newArray = (E[]) new Comparable[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int idx = binarySearch(e);
        if (idx >= 0) return false;
        int insertPos = -idx - 1;
        ensureCapacity(size + 1);
        System.arraycopy(array, insertPos, array, insertPos + 1, size - insertPos);
        array[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            return binarySearch(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            int idx = binarySearch(e);
            if (idx < 0) return false;
            int numMoved = size - idx - 1;
            if (numMoved > 0) {
                System.arraycopy(array, idx + 1, array, idx, numMoved);
            }
            array[--size] = null;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
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
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastRet = cursor++;
                return array[lastRet];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                MyTreeSet.this.remove(array[lastRet]);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(array, 0, arr, 0, size);
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = java.util.Arrays.copyOf(a, size);
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }
}

