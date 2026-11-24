package by.it.group510971.rudko.lesson10;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

//Очередь с приоритетами (min-куча с естественным порядком) на приватном массиве E[].
//Структура кучи и порядок элементов повторяют java.util.PriorityQueue,
//чтобы toString() совпадал с образцом.
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int INITIAL_CAPACITY = 11;

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[INITIAL_CAPACITY];
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
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
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        int i = size;
        size = i + 1;
        siftUp(i, e);
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        int n = --size;
        E x = heap[n];
        heap[n] = null;
        if (n > 0) siftDown(0, x);
        return result;
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            offer(e);
        }
        return !c.isEmpty();
    }

    //удаление всех элементов из c за O(n): фильтруем массив, затем восстанавливаем кучу
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
        return new Iterator<E>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public E next() {
                if (pos >= size) throw new NoSuchElementException();
                return heap[pos++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) heap[i];
        }
        if (result.length > size) result[size] = null;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return i;
        }
        return -1;
    }

    private void ensureCapacity() {
        if (size < heap.length) return;
        @SuppressWarnings("unchecked")
        E[] bigger = (E[]) new Object[heap.length * 2];
        System.arraycopy(heap, 0, bigger, 0, size);
        heap = bigger;
    }

    //просеивание вверх: элемент x ставим на позицию k, поднимая его, пока он меньше родителя
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];
            if (compare(x, e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    //просеивание вниз: элемент x ставим на позицию k, опуская его за меньшим потомком
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && compare(c, heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (compare(x, c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    //удаление элемента на позиции i
    private void removeAt(int i) {
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

    //оставляет (inCollection = false) или удаляет (true) элементы из c; порядок сохраняется, затем heapify
    private boolean bulkRemove(Collection<?> c, boolean inCollection) {
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(heap[r]) != inCollection) {
                heap[w++] = heap[r];
            }
        }
        boolean changed = w != size;
        for (int k = w; k < size; k++) {
            heap[k] = null;
        }
        size = w;
        if (changed) heapify();
        return changed;
    }

    //восстановление свойства кучи за O(n)
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }
}
