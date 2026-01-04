package by.it.group410972.zayats.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    // ======== Вспомогательные методы кучи ========
    private void ensureCapacity() {
        if (size >= heap.length) {
            @SuppressWarnings("unchecked")
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void beautifyUp() {
        int index = size - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) < 0) {
                E temp = heap[index];
                heap[index] = heap[parent];
                heap[parent] = temp;
                index = parent;
            } else break;
        }
    }

    private void beautifyDown() {
        beautifyDownFrom(0);
    }

    private void beautifyDownFrom(int index) {
        while (index < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) smallest = left;
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) smallest = right;

            if (smallest != index) {
                E temp = heap[index];
                heap[index] = heap[smallest];
                heap[smallest] = temp;
                index = smallest;
            } else break;
        }
    }

    private void removeElement(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                heap[i] = heap[size - 1];
                heap[size - 1] = null;
                size--;
                beautifyDownFrom(i);
                break;
            }
        }
    }

    // ======== Методы Queue ========
    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        heap[size++] = element;
        beautifyUp();
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException();
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        beautifyDown();
        return result;
    }

    @Override
    public E poll() {
        return isEmpty() ? null : remove();
    }

    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public E peek() {
        return isEmpty() ? null : heap[0];
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
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) if (heap[i].equals(o)) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ======== Методы с коллекциями ========
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else modified = true;
        }

        // очистка хвостовой части массива
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }

        size = writeIndex;

        // восстановление свойств кучи
        for (int i = size / 2 - 1; i >= 0; i--) {
            beautifyDownFrom(i);
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else modified = true;
        }

        // очистка хвостовой части массива
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }

        size = writeIndex;

        // восстановление свойств кучи
        for (int i = size / 2 - 1; i >= 0; i--) {
            beautifyDownFrom(i);
        }

        return modified;
    }

    // ======== Итератор ========
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

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

    // ======== Остальные методы Collection ========
    @Override
    public boolean remove(Object o) {
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
