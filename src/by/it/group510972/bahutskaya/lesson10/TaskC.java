package by.it.group510972.bahutskaya.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

// Очередь, которая отдаёт наименьшее. Внутри куча.
public class TaskC<E> implements Queue<E> {

    private Object[] queue = new Object[16];
    private int size;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(queue[i]);
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
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E value = poll();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean remove(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? queue[i] == null : element.equals(queue[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void removeAt(int index) {
        int last = --size;
        E moved = (E) queue[last];
        queue[last] = null;
        if (index == last) {
            return;
        }
        siftDown(index, moved);
        if (queue[index] == moved) {
            siftUp(index, moved);
        }
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? queue[i] == null : element.equals(queue[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == queue.length) {
            Object[] next = new Object[queue.length * 2];
            System.arraycopy(queue, 0, next, 0, size);
            queue = next;
        }
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = (E) queue[0];
        int last = --size;
        E moved = (E) queue[last];
        queue[last] = null;
        if (last != 0) {
            siftDown(0, moved);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return size == 0 ? null : (E) queue[0];
    }

    @Override
    public E element() {
        E value = peek();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
            offer(item);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    private boolean bulkRemove(Collection<?> c, boolean retain) {
        int first = 0;
        while (first < size) {
            boolean hit = c.contains(queue[first]);
            if (retain ? !hit : hit) {
                break;
            }
            first++;
        }
        if (first == size) {
            return false;
        }
        int write = first;
        for (int i = first + 1; i < size; i++) {
            boolean hit = c.contains(queue[i]);
            if (retain ? hit : !hit) {
                queue[write++] = queue[i];
            }
        }
        for (int i = write; i < size; i++) {
            queue[i] = null;
        }
        size = write;
        heapify();
        return true;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E value) {
        Comparable<? super E> key = (Comparable<? super E>) value;
        // Поднимаем элемент, пока родитель больше
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object parentValue = queue[parent];
            if (key.compareTo((E) parentValue) >= 0) {
                break;
            }
            queue[k] = parentValue;
            k = parent;
        }
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E value) {
        Comparable<? super E> key = (Comparable<? super E>) value;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object childValue = queue[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) childValue).compareTo((E) queue[right]) > 0) {
                childValue = queue[child = right];
            }
            if (key.compareTo((E) childValue) <= 0) {
                break;
            }
            queue[k] = childValue;
            k = child;
        }
        queue[k] = key;
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
                return (E) queue[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(queue, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(queue, size, a.getClass());
        }
        System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
