package by.it.group510972.bahutskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Очередь с двух концов на кольцевом массиве.
public class TaskA<E> implements Deque<E> {

    private Object[] data = new Object[8];
    private int head;
    private int size;

    private void grow() {
        Object[] next = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            next[i] = data[index(i)];
        }
        data = next;
        head = 0;
    }

    private int index(int offset) {
        return (head + offset) % data.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(data[index(i)]);
        }
        sb.append(']');
        return sb.toString();
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
        if (size == data.length) {
            grow();
        }
        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == data.length) {
            grow();
        }
        data[index(size)] = element;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E element() {
        return getFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) data[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) data[index(size - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = (E) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int last = index(size - 1);
        E value = (E) data[last];
        data[last] = null;
        size--;
        return value;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null && size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null && size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peekFirst() {
        return size == 0 ? null : (E) data[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peekLast() {
        return size == 0 ? null : (E) data[index(size - 1)];
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
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            Object value = data[index(i)];
            if (o == null ? value == null : o.equals(value)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int offset) {
        for (int i = offset; i < size - 1; i++) {
            data[index(i)] = data[index(i + 1)];
        }
        data[index(size - 1)] = null;
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            Object value = data[index(i)];
            if (o == null ? value == null : o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            Object value = data[index(i)];
            if (o == null ? value == null : o.equals(value)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[index(i)] = null;
        }
        head = 0;
        size = 0;
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
        boolean changed = false;
        for (E item : c) {
            addLast(item);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (c.contains(data[index(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(data[index(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) data[index(cursor++)];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            private int cursor = size - 1;

            @Override
            public boolean hasNext() {
                return cursor >= 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) data[index(cursor--)];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        for (int i = 0; i < size; i++) {
            copy[i] = data[index(i)];
        }
        return copy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) data[index(i)];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
