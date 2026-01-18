package by.it.group410972.ivanovich.lesson10;

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

    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        this.heap = new Object[initialCapacity];
        this.size = 0;
        this.comparator = null;
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<? super E>) e1).compareTo(e2);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
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
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (heap[i] == null) {
                    return true;
                }
            } else {
                if (o.equals(heap[i])) {
                    return true;
                }
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

        @SuppressWarnings("unchecked")
        E result = (E) heap[0];

        size--;
        heap[0] = heap[size];
        heap[size] = null;

        if (size > 0) {
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
        E result = (E) heap[0];
        return result;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E result = (E) heap[0];
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
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

        boolean modified = false;
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Создаем новую кучу и добавляем только те элементы, которых нет в c
        Object[] newHeap = new Object[heap.length];
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

        // Восстанавливаем свойства кучи
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем новую кучу и добавляем только те элементы, которые есть в c
        Object[] newHeap = new Object[heap.length];
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

        // Восстанавливаем свойства кучи
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        // Находим индекс элемента
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (heap[i] == null) {
                    index = i;
                    break;
                }
            } else {
                if (o.equals(heap[i])) {
                    index = i;
                    break;
                }
            }
        }

        if (index == -1) {
            return false;
        }

        // Удаляем элемент
        size--;
        heap[index] = heap[size];
        heap[size] = null;

        if (index < size) {
            // Восстанавливаем свойства кучи
            if (index > 0 && compareElement(index, (index - 1) / 2) < 0) {
                siftUp(index);
            } else {
                siftDown(index);
            }
        }

        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////      Эти методы имплементировать необязательно            ///////
    //////        но они будут нужны для корректной отладки          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return (E) heap[currentIndex++];
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////      Методы которые не реализуем       /////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////
    //////////////////////// Вспомогательные методы /////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = heap.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            heap = java.util.Arrays.copyOf(heap, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private int compareElement(int i, int j) {
        return compare((E) heap[i], (E) heap[j]);
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compareElement(index, parent) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && compareElement(leftChild, smallest) < 0) {
                smallest = leftChild;
            }
            if (rightChild < size && compareElement(rightChild, smallest) < 0) {
                smallest = rightChild;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}