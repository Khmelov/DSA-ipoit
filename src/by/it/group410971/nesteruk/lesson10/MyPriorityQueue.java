package by.it.group410971.nesteruk.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;
    private Comparator<? super E> comparator;

    public MyPriorityQueue() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this();
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
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
        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elementData(0);
        removeAt(0);
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : elementData(0);
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return elementData(0);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E element : c) {
            offer(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            heap = newHeap;
            size = newSize;
            // Восстанавливаем структуру кучи
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            heap = newHeap;
            size = newSize;
            // Восстанавливаем структуру кучи
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) {
        return (E) heap[index];
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int index) {
        E element = elementData(index);
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E parentElement = elementData(parent);
            if (compare(element, parentElement) >= 0) {
                break;
            }
            heap[index] = parentElement;
            index = parent;
        }
        heap[index] = element;
    }

    private void siftDown(int index) {
        E element = elementData(index);
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E childElement = elementData(child);
            int right = child + 1;
            if (right < size && compare(childElement, elementData(right)) > 0) {
                child = right;
                childElement = elementData(right);
            }
            if (compare(element, childElement) <= 0) {
                break;
            }
            heap[index] = childElement;
            index = child;
        }
        heap[index] = element;
    }

    private void removeAt(int index) {
        size--;
        if (size == index) {
            heap[index] = null;
        } else {
            E moved = elementData(size);
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = heap.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newHeap = new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
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