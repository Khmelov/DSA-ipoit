package by.it.group410972.Pushkarev.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] heap;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    private void increaseSize() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        E value = heap[index];
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[parent].compareTo(value) <= 0) break;
            heap[index] = heap[parent];
            index = parent;
        }
        heap[index] = value;
    }

    private void siftDown(int index) {
        E value = heap[index];
        int half = size / 2;
        while (index < half) {
            int left = 2 * index + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) {
                smallest = right;
            }
            if (heap[smallest].compareTo(value) >= 0) break;
            heap[index] = heap[smallest];
            index = smallest;
        }
        heap[index] = value;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean contains(E element) {
        if (element == null) return false;
        for (int i = 0; i < size; i++) {
            if (heap[i].compareTo(element) == 0) return true;
        }
        return false;
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
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        offer(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
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
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        E[] newHeap = (E[]) new Comparable[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        heap = newHeap;
        size = newSize;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        E[] newHeap = (E[]) new Comparable[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        heap = newHeap;
        size = newSize;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean offer(E e) {
        increaseSize();
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E val = poll();
        if (val == null) throw new NoSuchElementException();
        return val;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E element() {
        E val = peek();
        if (val == null) throw new NoSuchElementException();
        return val;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return heap[0];
    }
}
