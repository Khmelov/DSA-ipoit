package by.it.group410971.nesteruk.lesson10;

import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> current = first; current != null; current = current.next) {
                if (current.data == null) {
                    unlink(current);
                    return true;
                }
            }
        } else {
            for (Node<E> current = first; current != null; current = current.next) {
                if (o.equals(current.data)) {
                    unlink(current);
                    return true;
                }
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
        if (element == null) {
            throw new NullPointerException();
        }
        Node<E> newNode = new Node<>(element);
        if (first == null) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        Node<E> newNode = new Node<>(element);
        if (last == null) {
            first = last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new java.util.NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) {
            return null;
        }
        E data = first.data;
        unlink(first);
        return data;
    }

    @Override
    public E pollLast() {
        if (last == null) {
            return null;
        }
        E data = last.data;
        unlink(last);
        return data;
    }

    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
        return data;
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
    public E removeFirst() {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (last == null) {
            throw new java.util.NoSuchElementException();
        }
        return pollLast();
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
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> current = last; current != null; current = current.prev) {
                if (current.data == null) {
                    unlink(current);
                    return true;
                }
            }
        } else {
            for (Node<E> current = last; current != null; current = current.prev) {
                if (o.equals(current.data)) {
                    unlink(current);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E peek() {
        return pollFirst();
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
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}