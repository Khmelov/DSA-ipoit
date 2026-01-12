package by.it.group410971.tishuk.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private int size = 0;

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private int index(Object obj) {
        return (obj == null) ? 0 : Math.abs(obj.hashCode()) % table.length;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean contains(Object obj) {
        int idx = index(obj);
        Node<E> current = table[idx];
        while (current != null) {
            if ((obj == null && current.value == null) || (obj != null && obj.equals(current.value))) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        int idx = index(e);
        Node<E> newNode = new Node<>(e);
        newNode.next = table[idx];
        table[idx] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        int idx = index(obj);
        Node<E> current = table[idx], prev = null;
        while (current != null) {
            if ((obj == null && current.value == null) || (obj != null && obj.equals(current.value))) {
                if (prev == null) table[idx] = current.next;
                else prev.next = current.next;
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
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (!first) sb.append(", ");
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;
            private Node<E> currentNode = null;
            private Node<E> nextNode = findNext();

            private Node<E> findNext() {
                while (index < table.length) {
                    if (table[index] != null) {
                        Node<E> node = table[index++];
                        return node;
                    }
                    index++;
                }
                return null;
            }

            @Override
            public boolean hasNext() { return nextNode != null; }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                currentNode = nextNode;
                nextNode = currentNode.next != null ? currentNode.next : findNext();
                return currentNode.value;
            }
        };
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (Node<E> node : table) {
            Node<E> prev = null, current = node;
            while (current != null) {
                if (!c.contains(current.value)) {
                    if (prev == null) node = current.next;
                    else prev.next = current.next;
                    size--;
                    changed = true;
                } else prev = current;
                current = current.next;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int i = 0;
        for (E e : this) a[i++] = (T) e;
        return a;
    }
}
