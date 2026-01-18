package by.it.group410972.ivanovich.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

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

    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
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
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        if (size == 0) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if (o == null) {
                if (elements[index] == null) {
                    removeAt(index, i);
                    return true;
                }
            } else {
                if (o.equals(elements[index])) {
                    removeAt(index, i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (size == 0) {
            return false;
        }

        for (int i = size - 1; i >= 0; i--) {
            int index = (head + i) % elements.length;
            if (o == null) {
                if (elements[index] == null) {
                    removeAt(index, i);
                    return true;
                }
            } else {
                if (o.equals(elements[index])) {
                    removeAt(index, i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    /////////////////////////////////////////////////////////////////////////
    //////      Эти методы имплементировать необязательно            ///////
    //////        но они будут нужны для корректной отладки          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if (o == null) {
                if (elements[index] == null) {
                    return true;
                }
            } else {
                if (o.equals(elements[index])) {
                    return true;
                }
            }
        }
        return false;
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

    /////////////////////////////////////////////////////////////////////////
    ////////////////      Методы которые не реализуем       /////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public java.util.Iterator<E> iterator() {
        return null;
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
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
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                int index = (head + i) % elements.length;
                newElements[i] = elements[index];
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    private void removeAt(int index, int position) {
        // Если удаляемый элемент ближе к началу
        if (position < size / 2) {
            // Сдвигаем элементы слева от удаляемого вправо
            for (int i = position; i > 0; i--) {
                int currIndex = (head + i) % elements.length;
                int prevIndex = (head + i - 1) % elements.length;
                elements[currIndex] = elements[prevIndex];
            }
            elements[head] = null;
            head = (head + 1) % elements.length;
        } else {
            // Сдвигаем элементы справа от удаляемого влево
            for (int i = position; i < size - 1; i++) {
                int currIndex = (head + i) % elements.length;
                int nextIndex = (head + i + 1) % elements.length;
                elements[currIndex] = elements[nextIndex];
            }
            tail = (tail - 1 + elements.length) % elements.length;
            elements[tail] = null;
        }
        size--;
    }
}