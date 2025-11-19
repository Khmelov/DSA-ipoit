package by.it.group410972.kazyukevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

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

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() & 0x7fffffff;
    }

    private void resizeIfNeeded() {
        if (size < threshold) return;
        int newCap = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = (node.hash) % newCap;
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCap * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int idx = h % table.length;
        Node<E> node = table[idx];
        while (node != null) {
            if (node.hash == h) {
                if (node.key == e || (node.key != null && node.key.equals(e))) {
                    return false; // уже есть
                }
            }
            node = node.next;
        }
        // вставляем в голову списка
        Node<E> newNode = new Node<>(h, e, table[idx]);
        table[idx] = newNode;
        size++;
        resizeIfNeeded();
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int idx = h % table.length;
        Node<E> node = table[idx];
        while (node != null) {
            if (node.hash == h && (node.key == o || (node.key != null && node.key.equals(o)))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = h % table.length;
        Node<E> node = table[idx];
        Node<E> prev = null;
        while (node != null) {
            if (node.hash == h && (node.key == o || (node.key != null && node.key.equals(o)))) {
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
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ============ вспомогательное: итератор ============

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> current = null;

            {
                advanceToNext();
            }

            private void advanceToNext() {
                if (current != null && current.next != null) {
                    current = current.next;
                    return;
                }
                current = null;
                while (bucket < table.length) {
                    if (table[bucket] != null) {
                        current = table[bucket++];
                        return;
                    }
                    bucket++;
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E res = current.key;
                advanceToNext();
                return res;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // toString — формат как у стандартных коллекций: [a, b, c]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.key);
                first = false;
                node = node.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // ====== простые реализации для коллекционных операций ======

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
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
            while (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> node = table[i];
            while (node != null) {
                if (!c.contains(node.key)) {
                    // удалить node
                    if (prev == null) table[i] = node.next;
                    else prev.next = node.next;
                    size--;
                    changed = true;
                    node = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = node;
                    node = node.next;
                }
            }
        }
        return changed;
    }

    // ====== остальные методы интерфейса Set — реализованы как заглушки/примитивы ======

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int k = 0;
        for (E e : this) arr[k++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int idx = 0;
        T[] arr = a.length >= size ? a : (T[]) new Object[size];
        for (E e : this) arr[idx++] = (T) e;
        if (arr.length > size) arr[size] = null;
        return arr;
    }

    // Methods below are optional; kept simple / not optimized
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Set)) return false; Set<?> s = (Set<?>) o; if (s.size() != size) return false; return containsAll(s); }
    @Override public int hashCode() { int h = 0; for (E e : this) if (e != null) h += e.hashCode(); return h; }

    // -------- Unused optional mutating operations may be left unsupported:
    // (we implemented core ones above)
}
