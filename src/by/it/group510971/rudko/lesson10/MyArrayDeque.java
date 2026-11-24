package by.it.group510971.rudko.lesson10;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//Двусторонняя очередь на основе кольцевого приватного массива E[].
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head;   //индекс первого элемента в массиве data
    private int size;   //число элементов

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[8];
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[index(i)]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        checkNotNull(e);
        ensureCapacity();
        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        checkNotNull(e);
        ensureCapacity();
        data[index(size)] = e;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[index(size - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        int last = index(size - 1);
        E e = data[last];
        data[last] = null;
        size--;
        return e;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     остальные методы Deque                ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : data[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : data[index(size - 1)];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            if (equal(o, data[index(i)])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (equal(o, data[index(i)])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (equal(o, data[index(i)])) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
                return data[index(pos++)];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int pos = size - 1;

            @Override
            public boolean hasNext() {
                return pos >= 0;
            }

            @Override
            public E next() {
                if (pos < 0) throw new NoSuchElementException();
                return data[index(pos--)];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[index(i)];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) data[index(i)];
        }
        if (result.length > size) result[size] = null;
        return result;
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
        for (E e : c) {
            addLast(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeIf(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeIf(c, false);
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        head = 0;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    //физический индекс логической позиции i (0 - первый элемент)
    private int index(int i) {
        return (head + i) % data.length;
    }

    private void ensureCapacity() {
        if (size < data.length) return;
        @SuppressWarnings("unchecked")
        E[] bigger = (E[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[index(i)];
        }
        data = bigger;
        head = 0;
    }

    //удаляет элемент с логической позицией i, сдвигая хвост влево
    private void removeAt(int i) {
        for (int k = i; k < size - 1; k++) {
            data[index(k)] = data[index(k + 1)];
        }
        data[index(size - 1)] = null;
        size--;
    }

    //удаляет элементы, которые содержатся (inCollection = true) или не содержатся (false) в c; за O(n)
    private boolean removeIf(Collection<?> c, boolean inCollection) {
        int w = 0;
        for (int r = 0; r < size; r++) {
            E e = data[index(r)];
            if (c.contains(e) != inCollection) {
                data[index(w++)] = e;
            }
        }
        boolean changed = w != size;
        for (int k = w; k < size; k++) {
            data[index(k)] = null;
        }
        size = w;
        return changed;
    }

    private static void checkNotNull(Object e) {
        if (e == null) throw new NullPointerException();
    }

    private static boolean equal(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
