package by.it.group410971.kazakou.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder out = new StringBuilder();
        out.append('[');
        Node<E> current = head;
        while (current != null) {
            if (current != head) {
                out.append(", ");
            }
            out.append(current.item);
            current = current.next;
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        checkIndex(index);
        Node<E> target = nodeAt(index);
        return unlink(target);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        Node<E> node = new Node<>(e);
        node.next = head;
        if (head != null) {
            head.prev = node;
        } else {
            tail = node;
        }
        head = node;
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> node = new Node<>(e);
        node.prev = tail;
        if (tail != null) {
            tail.next = node;
        } else {
            head = node;
        }
        tail = node;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
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
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
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
    public boolean removeFirstOccurrence(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.item;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            private Node<E> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.item;
                current = current.prev;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[size];
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            out[i++] = current.item;
            current = current.next;
        }
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
            return result;
        }
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            @SuppressWarnings("unchecked")
            T value = (T) current.item;
            a[i++] = value;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            addLast(item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.item)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.item)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int indexOf(Object o) {
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return index;
            }
            index++;
            current = current.next;
        }
        return -1;
    }

    private Node<E> nodeAt(int index) {
        if (index < (size >> 1)) {
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        Node<E> current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private E unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
        size--;
        return node.item;
    }
}
