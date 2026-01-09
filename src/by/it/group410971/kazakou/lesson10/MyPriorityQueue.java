package by.it.group410971.kazakou.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] heap = new Object[8];
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
            out.append(heap[i]);
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
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (offer(element)) {
            return true;
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
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
    public E poll() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) heap[0];
        int lastIndex = --size;
        Object last = heap[lastIndex];
        heap[lastIndex] = null;
        if (lastIndex > 0) {
            heap[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E value = (E) heap[0];
        return value;
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
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx < 0) {
            return false;
        }
        removeAt(idx);
        return true;
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
            offer(item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
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
                E value = (E) heap[index++];
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[size];
        System.arraycopy(heap, 0, out, 0, size);
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.util.Arrays.copyOf(heap, size, a.getClass());
            return result;
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= heap.length) {
            return;
        }
        int newCapacity = Math.max(minCapacity, heap.length * 2);
        Object[] next = new Object[newCapacity];
        System.arraycopy(heap, 0, next, 0, size);
        heap = next;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<? super E>) a).compareTo((E) b);
    }

    private void siftUp(int index) {
        Object target = heap[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            Object parentVal = heap[parent];
            if (compare(target, parentVal) >= 0) {
                break;
            }
            heap[index] = parentVal;
            index = parent;
        }
        heap[index] = target;
    }

    private void siftDown(int index) {
        Object target = heap[index];
        int half = size >>> 1;
        while (index < half) {
            int left = (index << 1) + 1;
            int right = left + 1;
            int smallest = left;
            Object child = heap[left];
            if (right < size && compare(heap[right], child) < 0) {
                smallest = right;
                child = heap[right];
            }
            if (compare(target, child) <= 0) {
                break;
            }
            heap[index] = child;
            index = smallest;
        }
        heap[index] = target;
    }

    private int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return i;
            }
        }
        return -1;
    }

    private void removeAt(int index) {
        int lastIndex = --size;
        if (lastIndex == index) {
            heap[index] = null;
            return;
        }
        Object moved = heap[lastIndex];
        heap[lastIndex] = null;
        heap[index] = moved;
        siftDown(index);
        if (heap[index] == moved) {
            siftUp(index);
        }
    }

    private boolean bulkRemove(Collection<?> c, boolean remove) {
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object value = heap[i];
            boolean contains = c.contains(value);
            if (contains != remove) {
                heap[newSize++] = value;
            } else {
                modified = true;
            }
        }
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        if (modified) {
            size = newSize;
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }
        return modified;
    }
}
