package by.it.group410971.galdytskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        Node<E> nextInBucket;       // next node in the same hash bucket chain
        Node<E> prevInsertion;      // previous node in insertion order
        Node<E> nextInsertion;      // next node in insertion order

        Node(E value) {
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    // Head and tail of doubly linked list maintaining insertion order
    private Node<E> headInsertion;
    private Node<E> tailInsertion;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
        headInsertion = null;
        tailInsertion = null;
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
        Node<E> newNode = new Node<>(e);
        // Insert at bucket head
        newNode.nextInBucket = table[idx];
        table[idx] = newNode;

        // Append to insertion order list
        if (tailInsertion == null) {
            headInsertion = tailInsertion = newNode;
        } else {
            tailInsertion.nextInsertion = newNode;
            newNode.prevInsertion = tailInsertion;
            tailInsertion = newNode;
        }
        size++;
        return true;
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        Node<E> current = headInsertion;
        while (current != null) {
            int idx = indexFor(hash(current.value), newCapacity);
            current.nextInBucket = newTable[idx];
            newTable[idx] = current;
            current = current.nextInsertion;
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
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(hash(o), table.length);
        Node<E> prevBucket = null;
        Node<E> node = table[idx];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                // Remove from bucket chain
                if (prevBucket == null) {
                    table[idx] = node.nextInBucket;
                } else {
                    prevBucket.nextInBucket = node.nextInBucket;
                }

                // Remove from insertion order doubly linked list
                if (node.prevInsertion != null) {
                    node.prevInsertion.nextInsertion = node.nextInsertion;
                } else {
                    headInsertion = node.nextInsertion;
                }
                if (node.nextInsertion != null) {
                    node.nextInsertion.prevInsertion = node.prevInsertion;
                } else {
                    tailInsertion = node.prevInsertion;
                }

                size--;
                return true;
            }
            prevBucket = node;
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        headInsertion = tailInsertion = null;
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
        return new Iterator<E>() {
            Node<E> current = headInsertion;
            Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null)
                    throw new NoSuchElementException();
                lastReturned = current;
                current = current.nextInsertion;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null)
                    throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = headInsertion;
        boolean first = true;
        while (node != null) {
            if (!first) sb.append(", ");
            else first = false;
            sb.append(node.value);
            node = node.nextInsertion;
        }
        sb.append("]");
        return sb.toString();
    }

    // Additional bulk methods for level B

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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E val = it.next();
            if (!c.contains(val)) {
                it.remove();
                changed = true;
            }
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

