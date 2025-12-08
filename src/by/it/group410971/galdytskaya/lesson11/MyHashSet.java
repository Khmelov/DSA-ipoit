package by.it.group410971.galdytskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    private int hash(Object o) {
        if (o == null) return 0;
        int h = o.hashCode();
        return (h) ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        if (size >= threshold) {
            resize();
        }
        int idx = indexFor(hash(e), table.length);
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = indexFor(hash(node.value), newCapacity);
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(hash(o), table.length);
        Node<E> node = table[idx];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(hash(o), table.length);
        Node<E> prev = null;
        Node<E> node = table[idx];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
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
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int bucketIndex = 0;
            Node<E> current = null;
            Node<E> lastReturned = null;

            private void advance() {
                while (current == null && bucketIndex < table.length) {
                    current = table[bucketIndex++];
                }
            }

            @Override
            public boolean hasNext() {
                advance();
                return current != null;
            }

            @Override
            public E next() {
                advance();
                if (current == null) throw new NoSuchElementException();
                lastReturned = current;
                current = current.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null)
                    throw new IllegalStateException();
                MyHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> node = table[i]; node != null; node = node.next) {
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }
                sb.append(node.value);
            }
        }
        sb.append("]");
        return sb.toString();
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
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int pos = 0;
        for (E e : this) {
            array[pos++] = e;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int s = size();
        if (a.length < s) {
            a = java.util.Arrays.copyOf(a, s);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > s) {
            a[s] = null;
        }
        return a;
    }
}
