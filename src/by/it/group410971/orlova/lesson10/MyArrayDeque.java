package by.it.a_khmelev.lesson10;

import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
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
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(size + 1);

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(size + 1);

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        @SuppressWarnings("unchecked")
        E element = (E) elements[head];
        return element;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        int lastIndex = (tail - 1 + elements.length) % elements.length;
        @SuppressWarnings("unchecked")
        E element = (E) elements[lastIndex];
        return element;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E element = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        tail = (tail - 1 + elements.length) % elements.length;
        @SuppressWarnings("unchecked")
        E element = (E) elements[tail];
        elements[tail] = null;
        size--;

        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // Увеличиваем на 50%

        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }

        if (newCapacity > MAX_ARRAY_SIZE) {
            newCapacity = hugeCapacity(minCapacity);
        }

        Object[] newElements = new Object[newCapacity];

        // Копируем элементы в новый массив
        if (head < tail) {
            // Элементы расположены непрерывно
            System.arraycopy(elements, head, newElements, 0, size);
        } else {
            // Элементы "разорваны" (голова после хвоста)
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, newElements, 0, firstPart);
            System.arraycopy(elements, 0, newElements, firstPart, tail);
        }

        elements = newElements;
        head = 0;
        tail = size;
    }

    private int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Методы, которые можно не имплементировать        ////////
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
        E element = pollFirst();
        if (element == null) {
            throw new java.util.NoSuchElementException();
        }
        return element;
    }

    @Override
    public E removeLast() {
        E element = pollLast();
        if (element == null) {
            throw new java.util.NoSuchElementException();
        }
        return element;
    }

    @Override
    public E peekFirst() {
        return pollFirst();
    }

    @Override
    public E peekLast() {
        return pollLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
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
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                int index = (head + cursor) % elements.length;
                cursor++;
                return (E) elements[index];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            result[i] = elements[index];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            elements[index] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }
}