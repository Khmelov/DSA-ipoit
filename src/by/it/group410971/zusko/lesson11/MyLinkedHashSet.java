package by.it.group410971.zusko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Entry<E> {
        E data;
        Entry<E> nextHash;
        Entry<E> prevOrder;
        Entry<E> nextOrder;

        Entry(E data) {
            this.data = data;
        }
    }

    private Entry<E>[] table;
    private Entry<E> head;
    private Entry<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Entry<E>[]) new Entry[INITIAL_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & (table.length - 1);
    }

    private void ensureCapacity() {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<E>[] oldTable = table;
        table = (Entry<E>[]) new Entry[oldTable.length * 2];
        head = null;
        tail = null;
        size = 0;

        for (Entry<E> headEntry : oldTable) {
            Entry<E> entry = headEntry;
            while (entry != null) {
                add(entry.data);
                entry = entry.nextHash;
            }
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
        int index = hash(o);
        Entry<E> entry = table[index];
        while (entry != null) {
            if (o == null ? entry.data == null : o.equals(entry.data)) {
                return true;
            }
            entry = entry.nextHash;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        int index = hash(e);

        // Check if already exists
        Entry<E> entry = table[index];
        while (entry != null) {
            if (e == null ? entry.data == null : e.equals(entry.data)) {
                return false;
            }
            entry = entry.nextHash;
        }

        // Create new entry
        Entry<E> newEntry = new Entry<>(e);

        // Add to hash table
        newEntry.nextHash = table[index];
        table[index] = newEntry;

        // Add to order (end of linked list)
        if (head == null) {
            head = newEntry;
            tail = newEntry;
        } else {
            tail.nextOrder = newEntry;
            newEntry.prevOrder = tail;
            tail = newEntry;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Entry<E> entry = table[index];
        Entry<E> prev = null;

        while (entry != null) {
            if (o == null ? entry.data == null : o.equals(entry.data)) {
                // Remove from hash table
                if (prev == null) {
                    table[index] = entry.nextHash;
                } else {
                    prev.nextHash = entry.nextHash;
                }

                // Remove from order
                if (entry.prevOrder != null) {
                    entry.prevOrder.nextOrder = entry.nextOrder;
                } else {
                    head = entry.nextOrder;
                }

                if (entry.nextOrder != null) {
                    entry.nextOrder.prevOrder = entry.prevOrder;
                } else {
                    tail = entry.prevOrder;
                }

                size--;
                return true;
            }
            prev = entry;
            entry = entry.nextHash;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Entry<E> current = head;
        while (current != null) {
            Entry<E> next = current.nextOrder;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Entry<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.nextOrder;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}