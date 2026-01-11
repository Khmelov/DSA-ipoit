package by.it.group410972.stankevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 16;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[INITIAL_CAPACITY];
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }


    private void siftUp(int idx) {
        E value = heap[idx];
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (heap[parent].compareTo(value) <= 0) break;
            heap[idx] = heap[parent];
            idx = parent;
        }
        heap[idx] = value;
    }

    private void siftDown(int idx) {
        E value = heap[idx];
        int half = size / 2;
        while (idx < half) {
            int left = 2 * idx + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) {
                smallest = right;
            }
            if (heap[smallest].compareTo(value) >= 0) break;
            heap[idx] = heap[smallest];
            idx = smallest;
        }
        heap[idx] = value;
    }

    private E removeAt(int idx) {
        if (idx >= size) return null;
        E removed = heap[idx];
        size--;
        if (idx != size) {
            heap[idx] = heap[size];
            siftDown(idx);
            siftUp(idx);
        }
        heap[size] = null;
        return removed;
    }

    private boolean containsInternal(E element) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(element)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        return (size == 0) ? null : removeAt(0);
    }

    @Override
    public E remove() {
        if (size == 0) throw new IllegalStateException("Queue is empty");
        return removeAt(0);
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new IllegalStateException("Queue is empty");
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return containsInternal((E) o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
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

        for (int i = (size / 2) - 1; i >= 0; i--) {
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

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }


    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}
