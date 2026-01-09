package by.it.group410971.kazakou.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private Object[] elements = new Object[8];
    private int head;
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
            out.append(elements[index(i)]);
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = dec(head);
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[index(size)] = element;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[head];
        return value;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[index(size - 1)];
        return value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[head];
        elements[head] = null;
        head = inc(head);
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int lastIndex = index(size - 1);
        @SuppressWarnings("unchecked")
        E value = (E) elements[lastIndex];
        elements[lastIndex] = null;
        size--;
        return value;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[head];
        return value;
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[index(size - 1)];
        return value;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        int pos = indexOf(o, false);
        if (pos < 0) {
            return false;
        }
        removeAt(pos);
        return true;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        int pos = indexOf(o, true);
        if (pos < 0) {
            return false;
        }
        removeAt(pos);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o, false) >= 0;
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
                E value = (E) elements[index(index++)];
                return value;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                @SuppressWarnings("unchecked")
                E value = (E) elements[index(index--)];
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[size];
        for (int i = 0; i < size; i++) {
            out[i] = elements[index(i)];
        }
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] copy = (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
            return copy;
        }
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T value = (T) elements[index(i)];
            a[i] = value;
        }
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
            addLast(item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            while (removeFirstOccurrence(item)) {
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
            Object value = elements[index(i)];
            if (!c.contains(value)) {
                removeAt(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        head = 0;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }
        Object[] next = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            next[i] = elements[index(i)];
        }
        elements = next;
        head = 0;
    }

    private int index(int offset) {
        int idx = head + offset;
        int mod = elements.length;
        if (idx >= mod) {
            idx -= mod;
        }
        return idx;
    }

    private int inc(int value) {
        int next = value + 1;
        if (next == elements.length) {
            next = 0;
        }
        return next;
    }

    private int dec(int value) {
        int next = value - 1;
        if (next < 0) {
            next = elements.length - 1;
        }
        return next;
    }

    private int indexOf(Object o, boolean fromEnd) {
        if (!fromEnd) {
            for (int i = 0; i < size; i++) {
                Object value = elements[index(i)];
                if (o == null ? value == null : o.equals(value)) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                Object value = elements[index(i)];
                if (o == null ? value == null : o.equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void removeAt(int pos) {
        for (int i = pos; i < size - 1; i++) {
            elements[index(i)] = elements[index(i + 1)];
        }
        elements[index(size - 1)] = null;
        size--;
    }
}
