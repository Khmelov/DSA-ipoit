package by.it.group410972.yarmak.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private void ensureCapacity(int capacity) {
        if (capacity <= elements.length) return;
        int newCap = Math.max(capacity, elements.length * 2 + 1);
        E[] newArr = (E[]) new Object[newCap];
        System.arraycopy(elements, 0, newArr, 0, size);
        elements = newArr;
    }

    private void ensureCapacity() {
        ensureCapacity(size + 1);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
    }

    /* ======================= BASIC ======================== */

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

    @Override
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elements[index];
        int moved = size - index - 1;
        if (moved > 0)
            System.arraycopy(elements, index + 1, elements, index, moved);
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    /* ======================= TASK B ======================== */

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elements[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    /* ======================= TASK C ======================== */

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureCapacity(size + c.size());
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        Object[] arr = c.toArray();
        int addSize = arr.length;
        if (addSize == 0) return false;

        ensureCapacity(size + addSize);

        System.arraycopy(elements, index, elements, index + addSize, size - index);

        for (int i = 0; i < addSize; i++)
            elements[index + i] = (E) arr[i];

        size += addSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    /* ================== НЕ ОБЯЗАТЕЛЬНЫ =================== */

    @Override public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
    @Override public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }
    @Override public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() {
        Object[] res = new Object[size];
        System.arraycopy(elements, 0, res, 0, size);
        return res;
    }
}