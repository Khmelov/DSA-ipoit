package by.it.group410971.tishuk.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private E[] elements;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
    }

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
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
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
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
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

    // --- Опциональные методы ---

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean modified = false;
        int i = index;
        for (E e : c) {
            add(i++, e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListC<E> sub = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(elements[i]);
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        return new ListIteratorImpl(index);
    }

    private class ListIteratorImpl implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;

        public ListIteratorImpl(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = cursor;
            return elements[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            lastRet = --cursor;
            return elements[cursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(cursor++, e);
            lastRet = -1;
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }
}
