package by.it.group410972.kazyukevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    //=================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ===================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
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
        Node<E> newNode = new Node<>(element, null, head);
        if (head != null) head.prev = newNode;
        head = newNode;
        if (tail == null) tail = head;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail != null) tail.next = newNode;
        tail = newNode;
        if (head == null) head = tail;
        size++;
    }

    //--- Получение элементов ---

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new IllegalStateException("List is empty");
        return tail.value;
    }

    //--- Удаление по индексу ---

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        E value = current.value;
        unlink(current);
        return value;
    }

    //--- Удаление по элементу ---

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    //--- Удаление первого и последнего ---

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E value = head.value;
        unlink(head);
        return value;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E value = tail.value;
        unlink(tail);
        return value;
    }

    //=================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===================

    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev != null) prev.next = next;
        else head = next;

        if (next != null) next.prev = prev;
        else tail = prev;

        node.prev = node.next = null;
        size--;
    }

    //=================== ПРОЧЕЕ (заглушки) ===================

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peek() { return head == null ? null : head.value; }

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

    @Override public E peekFirst() { return peek(); }
    @Override public E peekLast() { return tail == null ? null : tail.value; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { return remove(o); }
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
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { head = tail = null; size = 0; }
}
