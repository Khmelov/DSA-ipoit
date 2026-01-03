package by.it.group410971.usovskiy.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    // массив-куча и текущий размер
    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[16];
    }

    // ================== вспомогательные методы кучи ==================

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCap) {
        if (heap.length >= minCap) return;
        int newCap = heap.length * 2;
        if (newCap < minCap) newCap = minCap;
        E[] n = (E[]) new Object[newCap];
        System.arraycopy(heap, 0, n, 0, size);
        heap = n;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];
            if (key.compareTo(e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;          // первый индекс листа
        while (k < half) {
            int child = (k << 1) + 1;   // левый сын
            E c = heap[child];
            int right = child + 1;
            if (right < size &&
                    ((Comparable<? super E>) c).compareTo(heap[right]) > 0) {
                c = heap[child = right];
            }
            if (key.compareTo(c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    // построить кучу из heap[0..size)
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            E x = heap[i];
            siftDown(i, x);
        }
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) return i;
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        if (i < 0 || i >= size) return;
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    // ================== реализация Queue ==================

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        if (size == 0) heap[0] = e;
        else siftUp(size, e);
        size++;
        return true;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = heap[0];
        E x = heap[s];
        heap[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
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
        return indexOf(o) != -1;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    // ================== Iterator ==================

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) throw new NoSuchElementException();
            lastRet = cursor;
            return heap[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            MyPriorityQueue.this.removeAt(lastRet);
            cursor = lastRet; // на этом индексе теперь другой элемент
            lastRet = -1;
        }
    }

    // ================== toArray ==================

    @Override
    public Object[] toArray() {
        Object[] r = new Object[size];
        System.arraycopy(heap, 0, r, 0, size);
        return r;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] r = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
            System.arraycopy(heap, 0, r, 0, size);
            return r;
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    // ================== bulk-операции ==================

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E e = heap[i];
            if (c.contains(e)) {
                changed = true;
            } else {
                heap[newSize++] = e;
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        if (changed) heapify();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E e = heap[i];
            if (c.contains(e)) {
                heap[newSize++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        if (changed) heapify();
        return changed;
    }

    // ================== toString ==================

    @Override
    public String toString() {
        // приводим к куче «как у PriorityQueue»
        heapify();
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(',').append(' ');
            sb.append(heap[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
