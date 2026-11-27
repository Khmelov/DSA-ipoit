package by.it.group510972.bahutskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Множество, элементы всегда по возрастанию.
public class TaskC<E> implements Set<E> {

    private Object[] data = new Object[8];
    private int size;

    @SuppressWarnings("unchecked")
    private int compare(Object left, Object right) {
        return ((Comparable<Object>) left).compareTo(right);
    }

    private int binarySearch(Object value) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = compare(data[mid], value);
            if (cmp == 0) {
                return mid;
            }
            if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -(left + 1);
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
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int index = binarySearch(element);
        if (index >= 0) {
            return false;
        }
        index = -index - 1;
        if (size == data.length) {
            data = java.util.Arrays.copyOf(data, data.length * 2);
        }
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = binarySearch(element);
        if (index < 0) {
            return false;
        }
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        return binarySearch(element) >= 0;
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
            changed |= add(item);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            changed |= remove(item);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int write = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[write++] = data[i];
            } else {
                changed = true;
            }
        }
        for (int i = write; i < size; i++) {
            data[i] = null;
        }
        size = write;
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
                return (E) data[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(data, size);
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
}
