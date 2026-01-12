package by.it.group410972.Pushkarev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before, after;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        size = 0;
        head = tail = null;
    }

    private int index(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % table.length;
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        Node<E> cur = head;
        while (cur != null) {
            int idx = index(cur.value);
            cur.next = table[idx];
            table[idx] = cur;
            cur = cur.after;
        }
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
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) return true;
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
    public boolean add(E e) {
        if (size + 1 > table.length * LOAD_FACTOR) resize();
        int idx = index(e);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((e == null && cur.value == null) || (e != null && e.equals(cur.value))) return false;
            cur = cur.next;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[idx];
        table[idx] = newNode;

        // добавляем в связный список
        if (head == null) head = tail = newNode;
        else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

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

                if (cur.before != null) cur.before.after = cur.after;
                else head = cur.after;
                if (cur.after != null) cur.after.before = cur.before;
                else tail = cur.before;

                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.after;
        }
        sb.append("]");
        return sb.toString();
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
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        head = tail = null;
        size = 0;
    }
}
