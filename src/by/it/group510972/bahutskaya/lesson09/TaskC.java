package by.it.group510972.bahutskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Список, который умеет работать сразу с пачкой элементов.
public class TaskC<E> implements List<E> {

    private Object[] data = new Object[8];
    private int size;

    private void grow() {
        Object[] next = new Object[data.length * 2];
        System.arraycopy(data, 0, next, 0, size);
        data = next;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(data[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == data.length) {
            grow();
        }
        data[size++] = e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) data[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        if (size == data.length) {
            grow();
        }
        int moved = size - index;
        if (moved > 0) {
            System.arraycopy(data, index, data, index + 1, moved);
        }
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }
        return -1;
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
            add(item);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        if (c.isEmpty()) {
            return false;
        }
        Object[] extra = c.toArray();
        while (size + extra.length > data.length) {
            grow();
        }
        int moved = size - index;
        if (moved > 0) {
            System.arraycopy(data, index, data, index + extra.length, moved);
        }
        System.arraycopy(extra, 0, data, index, extra.length);
        size += extra.length;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int write = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                data[write++] = data[i];
            }
        }
        boolean changed = write != size;
        for (int i = write; i < size; i++) {
            data[i] = null;
        }
        size = write;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int write = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[write++] = data[i];
            }
        }
        boolean changed = write != size;
        for (int i = write; i < size; i++) {
            data[i] = null;
        }
        size = write;
        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(data, size, a.getClass());
        }
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(data, size);
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
                return (E) data[cursor++];
            }
        };
    }
}
