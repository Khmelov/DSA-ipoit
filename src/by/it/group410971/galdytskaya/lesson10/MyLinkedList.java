package by.it.group410971.galdytskaya.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private int size = 0;
    private Node<E> head;
    private Node<E> tail;

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while(current != null) {
            sb.append(current.item);
            current = current.next;
            if(current != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // добавить в конец
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        checkIndex(index);
        Node<E> node = node(index);
        return unlink(node);
    }

    // удалить первый найденный элемент
    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        if(o == null) {
            while(current != null) {
                if(current.item == null) {
                    unlink(current);
                    return true;
                }
                current = current.next;
            }
        } else {
            while(current != null) {
                if(o.equals(current.item)) {
                    unlink(current);
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(null, element, head);
        if(head == null) {
            tail = newNode;
        } else {
            head.prev = newNode;
        }
        head = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(tail, element, null);
        if(tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    // вернуть первый элемент
    @Override
    public E element() {
        return getFirst();
    }
    @Override
    public E getFirst() {
        if(head == null) throw new NoSuchElementException();
        return head.item;
    }

    // вернуть последний элемент
    @Override
    public E getLast() {
        if(tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    // удалить и вернуть первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if(head == null) return null;
        return unlink(head);
    }

    // удалить и вернуть последний элемент
    @Override
    public E pollLast() {
        if(tail == null) return null;
        return unlink(tail);
    }

    private E unlink(Node<E> node) {
        E element = node.item;
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        if(prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if(next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return element;
    }

    private Node<E> node(int index) {
        if(index < (size >> 1)) {
            Node<E> x = head;
            for(int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for(int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private void checkIndex(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
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
    public E peek() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekFirst() {
        return peek();
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if(head == null) throw new NoSuchElementException();
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if(tail == null) throw new NoSuchElementException();
        return unlink(tail);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
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
    @Override
    public boolean contains(Object o) {
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
    public void clear() {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }
    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }
}
