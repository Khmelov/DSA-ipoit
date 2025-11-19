package by.it.group410972.kazyukevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    private Node<E> head; // начало списка по порядку вставки
    private Node<E> tail; // конец списка по порядку вставки

    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next; // в бакете
        Node<E> prevOrder, nextOrder; // для LinkedHashSet

        Node(int hash, E key) {
            this.hash = hash;
            this.key = key;
        }
    }

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        head = null;
        tail = null;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() & 0x7fffffff;
    }

    private void resizeIfNeeded() {
        if (size < threshold) return;
        int newCap = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];
        Node<E> node = head;
        while (node != null) {
            int idx = node.hash % newCap;
            Node<E> next = node.next;
            node.next = newTable[idx];
            newTable[idx] = node;
            node = node.nextOrder;
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
            if (node.hash == h && (node.key == e || (node.key != null && node.key.equals(e)))) {
                return false; // уже есть
            }
            node = node.next;
        }
        Node<E> newNode = new Node<>(h, e);
        newNode.next = table[idx];
        table[idx] = newNode;

        // добавляем в конец списка по порядку вставки
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.nextOrder = newNode;
            newNode.prevOrder = tail;
            tail = newNode;
        }

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
                if (prev == null) table[idx] = node.next;
                else prev.next = node.next;

                // удаляем из списка порядка вставки
                if (node.prevOrder != null) node.prevOrder.nextOrder = node.nextOrder;
                else head = node.nextOrder;
                if (node.nextOrder != null) node.nextOrder.prevOrder = node.prevOrder;
                else tail = node.prevOrder;

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
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E res = current.key;
                current = current.nextOrder;
                return res;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> node = head;
        boolean first = true;
        while (node != null) {
            if (!first) sb.append(", ");
            sb.append(node.key);
            first = false;
            node = node.nextOrder;
        }
        sb.append(']');
        return sb.toString();
    }

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
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.nextOrder;
            if (!c.contains(node.key)) {
                remove(node.key);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

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

    @Override public int hashCode() { int h = 0; for (E e : this) if (e != null) h += e.hashCode(); return h; }
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Set)) return false; Set<?> s = (Set<?>) o; if (s.size() != size) return false; return containsAll(s); }
}
