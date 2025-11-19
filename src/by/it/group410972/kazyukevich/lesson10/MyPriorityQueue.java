package by.it.group410972.kazyukevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[16];
        size = 0;
    }

    //=================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ===================

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        heap = (E[]) new Comparable[16];
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
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
    public E remove() {
        E result = poll();
        if (result == null) throw new IllegalStateException("Queue is empty");
        return result;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new IllegalStateException("Queue is empty");
        return heap[0];
    }

    // --- Проверка на пустоту ---
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // --- Проверка наличия элемента ---
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i]))
                return true;
        }
        return false;
    }

    //=================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===================

    private void ensureCapacity() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parent = (index - 1) / 2;
            E parentVal = heap[parent];
            if (parentVal == null || element.compareTo(parentVal) >= 0)
                break;
            heap[index] = parentVal;
            index = parent;
        }
        heap[index] = element;
    }


    private boolean siftDown(int index) {
        boolean moved = false;
        int half = size / 2;
        E element = heap[index];
        while (index < half) {
            int left = 2 * index + 1;
            int right = left + 1;
            int smaller = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0)
                smaller = right;
            if (element.compareTo(heap[smaller]) <= 0)
                break;
            heap[index] = heap[smaller];
            index = smaller;
            moved = true;
        }
        heap[index] = element;
        return moved;
    }


    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    //=================== КОЛЛЕКЦИОННЫЕ МЕТОДЫ ===================

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        E[] newHeap = (E[]) new Comparable[heap.length];

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        heap = newHeap;
        size = newSize;

        heapify();

        return modified;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        E[] newHeap = (E[]) new Comparable[heap.length];

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        heap = newHeap;
        size = newSize;

        heapify();

        return modified;
    }

    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    private void removeElementAt(int index) {
        int lastIndex = size - 1;
        E moved = heap[lastIndex];
        heap[lastIndex] = null;
        size--;

        if (index == lastIndex) return;

        heap[index] = moved;

        // если перемещённый элемент не null, восстанавливаем кучу
        if (moved != null) {
            if (!siftDown(index)) {
                siftUp(index);
            }
        }
    }


    //=================== ПРОЧЕЕ (заглушки для Queue) ===================

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
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                removeElementAt(i);
                return true;
            }
        }
        return false;
    }
}