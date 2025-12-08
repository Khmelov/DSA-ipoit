package by.it.group410971.galdytskaya.lesson10;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] heap;
    private int size = 0;

    public MyPriorityQueue() {
        this.heap = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    public MyPriorityQueue(int capacity) {
        this.heap = (E[]) new Comparable[capacity];
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Comparable[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }

    private void siftUp(int idx) {
        E value = heap[idx];
        while (idx > 0) {
            int parent = (idx - 1) >>> 1;
            if (heap[parent].compareTo(value) <= 0) break;
            heap[idx] = heap[parent];
            idx = parent;
        }
        heap[idx] = value;
    }

    private void siftDown(int idx) {
        E value = heap[idx];
        int half = size >>> 1;
        while (idx < half) {
            int leftChild = (idx << 1) + 1;
            int rightChild = leftChild + 1;
            int smallestChild = leftChild;

            if (rightChild < size && heap[rightChild].compareTo(heap[leftChild]) < 0) {
                smallestChild = rightChild;
            }

            if (value.compareTo(heap[smallestChild]) <= 0) break;

            heap[idx] = heap[smallestChild];
            idx = smallestChild;
        }
        heap[idx] = value;
    }

    @Override
    public boolean add(E e) {
        if (offer(e)) {
            return true;
        }
        throw new IllegalStateException("Queue full");
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        E last = heap[--size];
        heap[size] = null;
        if (size > 0) {
            heap[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) throw new NoSuchElementException();
        return result;
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (offer(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        boolean changed = size != newSize;
        size = newSize;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }


    @Override
    public boolean remove(Object o) {
        // Remove one occurrence of o
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                E last = heap[size - 1];
                heap[size - 1] = null;
                size--;
                if (i < size) {
                    heap[i] = last;
                    siftDown(i);
                    siftUp(i);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return heap[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(heap, 0, arr, 0, size);
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(heap, size, a.getClass());
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        boolean changed = size != newSize;
        size = newSize;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

}
