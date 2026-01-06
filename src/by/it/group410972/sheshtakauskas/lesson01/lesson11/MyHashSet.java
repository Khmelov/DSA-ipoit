package by.it.group410972.sheshtakauskas.lesson01.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private int index(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % table.length;
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
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            Node<E> cur = bucket;
            while (cur != null) {
                if (!first) sb.append(", ");
                sb.append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((e == null && cur.value == null) || (e != null && e.equals(cur.value))) {
                return false;
            }
            cur = cur.next;
        }
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }
}
