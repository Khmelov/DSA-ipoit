package by.it.group410971.tishuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент
    private int size = 0;

    // Внутренний класс Node
    private static class Node<E> {
        E value;
        Node<E> next; // для коллизий
        Node<E> prevOrder; // предыдущий по добавлению
        Node<E> nextOrder; // следующий по добавлению

        Node(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private int index(Object obj) {
        return (obj == null) ? 0 : Math.abs(obj.hashCode()) % table.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object obj) {
        int idx = index(obj);
        Node<E> current = table[idx];
        while (current != null) {
            if ((obj == null && current.value == null) ||
                    (obj != null && obj.equals(current.value))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        int idx = index(e);
        Node<E> newNode = new Node<>(e);

        // добавляем в цепочку коллизий
        newNode.next = table[idx];
        table[idx] = newNode;

        // добавляем в порядок добавления
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.nextOrder = newNode;
            newNode.prevOrder = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        int idx = index(obj);
        Node<E> current = table[idx];
        Node<E> prev = null;

        while (current != null) {
            if ((obj == null && current.value == null) ||
                    (obj != null && obj.equals(current.value))) {

                // удаляем из цепочки коллизий
                if (prev == null) {
                    table[idx] = current.next;
                } else {
                    prev.next = current.next;
                }

                // удаляем из порядка добавления
                if (current.prevOrder != null) {
                    current.prevOrder.nextOrder = current.nextOrder;
                } else {
                    head = current.nextOrder;
                }

                if (current.nextOrder != null) {
                    current.nextOrder.prevOrder = current.prevOrder;
                } else {
                    tail = current.prevOrder;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.nextOrder;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E value = current.value;
                current = current.nextOrder;
                return value;
            }
        };
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
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.nextOrder;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = nextNode;
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) {
            arr[i++] = e;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        return a;
    }
}
