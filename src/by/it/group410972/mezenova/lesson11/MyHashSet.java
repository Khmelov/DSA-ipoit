package by.it.group410972.mezenova.lesson11;
import java.util.*;

class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashSet() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.table = newTable;
        this.size = 0;
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.threshold = (int)(initialCapacity * loadFactor);
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[initialCapacity];
        this.table = newTable;
        this.size = 0;
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void ensureCapacity() {
        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1; // удваиваем
        threshold = (int)(newCapacity * loadFactor);

        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            Node<E> e = table[i];
            while (e != null) {
                Node<E> next = e.next;
                int newIndex = indexFor(e.hash, newCapacity);
                e.next = newTable[newIndex];
                newTable[newIndex] = e;
                e = next;
            }
            table[i] = null;
        }

        table = newTable;
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
        if (o == null) {
            for (Node<E> e = table[0]; e != null; e = e.next) {
                if (e.key == null) {
                    return true;
                }
            }
            return false;
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        for (Node<E> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(o, e.key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();

        if (e == null) {
            return addNull();
        }

        int hash = hash(e);
        int index = indexFor(hash, table.length);

        Node<E> first = table[index];
        for (Node<E> node = first; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(e, node.key)) {
                return false;
            }
        }

        table[index] = new Node<>(hash, e, first);
        size++;
        return true;
    }

    private boolean addNull() {
        int index = 0;
        Node<E> first = table[index];

        for (Node<E> node = first; node != null; node = node.next) {
            if (node.key == null) {
                return false; // null уже есть
            }
        }

        table[index] = new Node<>(0, null, first);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return removeNull();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && Objects.equals(o, current.key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    private boolean removeNull() {
        int index = 0;
        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.key == null) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
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
    public void clear() {
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean firstElement = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!firstElement) {
                    sb.append(", ");
                }
                sb.append(node.key);
                firstElement = false;
                node = node.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Остальные методы интерфейса Set (не требуются по заданию)

    @Override
    public Iterator<E> iterator() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}