package by.it.group410972.mezenova.lesson10;

import java.util.*;

class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }


    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            Object[] newElements = new Object[newCapacity];

            if (head < tail) {

                System.arraycopy(elements, head, newElements, 0, size);
            } else if (size > 0) {

                int firstPart = elements.length - head;
                System.arraycopy(elements, head, newElements, 0, firstPart);
                System.arraycopy(elements, 0, newElements, firstPart, tail);
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    // Вспомогательный метод для работы с циклическим массивом


    private int wrapIndex(int index) {
        return (index + elements.length) % elements.length;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        int index = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
            index = wrapIndex(index + 1);
        }

        sb.append("]");
        return sb.toString();
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
        ensureCapacity(size + 1);
        head = wrapIndex(head - 1);
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity(size + 1);
        elements[tail] = e;
        tail = wrapIndex(tail + 1);
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E element = (E) elements[head];
        return element;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int lastIndex = wrapIndex(tail - 1);
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
        elements[head] = null; //
        head = wrapIndex(head + 1);
        size--;

        // сбрасывае индексы
        if (size == 0) {
            head = 0;
            tail = 0;
        }

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = wrapIndex(tail - 1);
        @SuppressWarnings("unchecked")
        E element = (E) elements[tail];
        elements[tail] = null;
        size--;

        // сбрасываем индексы
        if (size == 0) {
            head = 0;
            tail = 0;
        }

        return element;
    }

    // Остальные методы интерфейса Deque

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return getLast();
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = head;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = (E) elements[currentIndex];
                currentIndex = wrapIndex(currentIndex + 1);
                count++;
                return element;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = head;
        for (int i = 0; i < size; i++) {
            result[i] = elements[index];
            index = wrapIndex(index + 1);
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {

        int index = head;
        for (int i = 0; i < size; i++) {
            elements[index] = null;
            index = wrapIndex(index + 1);
        }
        head = 0;
        tail = 0;
        size = 0;
    }
}