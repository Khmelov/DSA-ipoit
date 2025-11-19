package by.it.group410972.kazyukevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int size;
    private int head; // индекс первого элемента
    private int tail; // индекс следующей позиции после последнего элемента

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        size = 0;
        head = 0;
        tail = 0;
    }

    //=================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ===================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    //--- Добавление элементов ---

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    //--- Получение элементов (без удаления) ---

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new IllegalStateException("Deque is empty");
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new IllegalStateException("Deque is empty");
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }

    //--- Извлечение элементов (с удалением) ---

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    //=================== Вспомогательные ===================

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) return;

        E[] newArr = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr;
        head = 0;
        tail = size;
    }

    //=================== Пустые-заглушки для интерфейса ===================

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peek() { return size == 0 ? null : getFirst(); }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override public E peekFirst() { return size == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size == 0 ? null : getLast(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return pollFirst(); }
    @Override public Iterator<E> iterator() { return null; }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean remove(Object o) { return false; }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { size = 0; head = 0; tail = 0; }

}
