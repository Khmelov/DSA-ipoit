package by.it.group410971.usovskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // ===================== ВНУТРЕННЕЕ СОСТОЯНИЕ =====================

    private E[] data;
    private int head; // индекс первого элемента
    private int tail; // индекс позиции СРАЗУ после последнего элемента
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // ===================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====================

    private void ensureCapacity(int needed) {
        if (data.length >= needed) return;
        int newCap = Math.max(needed, data.length * 2);
        @SuppressWarnings("unchecked")
        E[] newData = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % data.length];
        }
        data = newData;
        head = 0;
        tail = size;
    }

    private int inc(int i) {
        return (i + 1) % data.length;
    }

    private int dec(int i) {
        return (i - 1 + data.length) % data.length;
    }

    // логический индекс 0..size-1 -> физический индекс в массиве
    private int toArrayIndex(int logicalIndex) {
        return (head + logicalIndex) % data.length;
    }

    // удалить элемент с логическим индексом logicalIndex (0..size-1)
    private void removeAtLogicalIndex(int logicalIndex) {
        if (logicalIndex < 0 || logicalIndex >= size) return;
        int idx = toArrayIndex(logicalIndex);

        // Выбираем более короткую сторону для сдвига
        if (logicalIndex < size / 2) {
            // ближе к head — сдвигаем в сторону tail
            int cur = idx;
            while (cur != head) {
                int prev = dec(cur);
                data[cur] = data[prev];
                cur = prev;
            }
            data[head] = null;
            head = inc(head);
        } else {
            // ближе к tail — сдвигаем в сторону head
            int cur = idx;
            int last = dec(tail);
            while (cur != last) {
                int next = inc(cur);
                data[cur] = data[next];
                cur = next;
            }
            tail = dec(tail);
            data[tail] = null;
        }
        size--;
    }

    private int indexOfInternal(Object o) {
        if (size == 0) return -1;
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[toArrayIndex(i)] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[toArrayIndex(i)])) return i;
            }
        }
        return -1;
    }

    // ===================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[toArrayIndex(i)]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    // --------- добавление ---------

    @Override
    public void addFirst(E e) {
        offerFirst(e);
    }

    @Override
    public void addLast(E e) {
        offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        ensureCapacity(size + 1);
        head = dec(head);
        data[head] = e;
        size++;
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        ensureCapacity(size + 1);
        data[tail] = e;
        tail = inc(tail);
        size++;
        return true;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    // --------- удаление ---------

    @Override
    public E removeFirst() {
        E res = pollFirst();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E removeLast() {
        E res = pollLast();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E res = data[head];
        data[head] = null;
        head = inc(head);
        size--;
        return res;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        E res = data[tail];
        data[tail] = null;
        size--;
        return res;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    // --------- просмотр (без удаления) ---------

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[dec(tail)];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : data[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : data[dec(tail)];
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    // --------- стековые операции ---------

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // ===================== МЕТОДЫ КОЛЛЕКЦИИ =====================

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOfInternal(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new DeqIterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = data[toArrayIndex(i)];
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) data[toArrayIndex(i)];
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOfInternal(o);
        if (idx == -1) return false;
        removeAtLogicalIndex(idx);
        return true;
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
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[toArrayIndex(i)] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    // ===================== ОСОБЫЕ МЕТОДЫ DEQUE =====================

    @Override
    public boolean removeFirstOccurrence(Object o) {
        int idx = indexOfInternal(o);
        if (idx == -1) return false;
        removeAtLogicalIndex(idx);
        return true;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (size == 0) return false;
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (data[toArrayIndex(i)] == null) {
                    removeAtLogicalIndex(i);
                    return true;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(data[toArrayIndex(i)])) {
                    removeAtLogicalIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    // ===================== ИТЕРАТОРЫ =====================

    private class DeqIterator implements Iterator<E> {
        int cursor = 0;     // логический индекс следующего элемента
        int lastRet = -1;   // логический индекс последнего возвращённого

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E res = data[toArrayIndex(cursor)];
            lastRet = cursor;
            cursor++;
            return res;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            MyArrayDeque.this.removeAtLogicalIndex(lastRet);
            cursor = lastRet;      // после удаления текущий сдвинулся
            lastRet = -1;
        }
    }

    private class DescIterator implements Iterator<E> {
        int cursor = size - 1; // логический индекс следующего (с конца)
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor >= 0;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E res = data[toArrayIndex(cursor)];
            lastRet = cursor;
            cursor--;
            return res;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            MyArrayDeque.this.removeAtLogicalIndex(lastRet);
            cursor = lastRet - 1;
            lastRet = -1;
        }
    }
}
