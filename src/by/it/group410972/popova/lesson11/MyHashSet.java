package by.it.group410972.popova.lesson11;

import java.util.Set;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<E>[] buckets;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int index(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % buckets.length;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> current = buckets[idx];
        while (current != null) {
            if (e == null ? current.value == null : e.equals(current.value)) return false;
            current = current.next;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = buckets[idx];
        buckets[idx] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> current = buckets[idx];
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> current = buckets[idx];
        Node<E> prev = null;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                if (prev == null) buckets[idx] = current.next;
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
    public void clear() {
        for (int i = 0; i < buckets.length; i++) buckets[i] = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
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

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
}
