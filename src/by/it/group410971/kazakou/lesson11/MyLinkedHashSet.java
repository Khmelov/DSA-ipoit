package by.it.group410971.kazakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E key;
        Node<E> nextBucket;
        Node<E> prevOrder;
        Node<E> nextOrder;

        Node(E key) {
            this.key = key;
        }
    }

    private Node<E>[] table = new Node[16];
    private int size;
    private Node<E> head;
    private Node<E> tail;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder out = new StringBuilder();
        out.append('[');
        Node<E> current = head;
        while (current != null) {
            if (current != head) {
                out.append(", ");
            }
            out.append(current.key);
            current = current.nextOrder;
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = new Node[16];
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        ensureCapacity(size + 1);
        int index = indexFor(e, table.length);
        Node<E> node = new Node<>(e);
        node.nextBucket = table[index];
        table[index] = node;
        if (tail == null) {
            head = node;
        } else {
            tail.nextOrder = node;
            node.prevOrder = tail;
        }
        tail = node;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if (equalsKey(current.key, o)) {
                if (prev == null) {
                    table[index] = current.nextBucket;
                } else {
                    prev.nextBucket = current.nextBucket;
                }
                unlinkOrder(current);
                size--;
                return true;
            }
            prev = current;
            current = current.nextBucket;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = indexFor(o, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (equalsKey(current.key, o)) {
                return true;
            }
            current = current.nextBucket;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.key;
                current = current.nextOrder;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[size];
        int i = 0;
        for (E e : this) {
            out[i++] = e;
        }
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] out = toArray();
        if (a.length < out.length) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.util.Arrays.copyOf(out, out.length, a.getClass());
            return result;
        }
        System.arraycopy(out, 0, a, 0, out.length);
        if (a.length > out.length) {
            a[out.length] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            while (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextOrder;
            if (!c.contains(current.key)) {
                remove(current.key);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    private void ensureCapacity(int minSize) {
        if (minSize <= table.length * 0.75) {
            return;
        }
        Node<E>[] old = table;
        table = new Node[old.length * 2];
        Node<E> current = head;
        while (current != null) {
            current.nextBucket = null;
            int index = indexFor(current.key, table.length);
            current.nextBucket = table[index];
            table[index] = current;
            current = current.nextOrder;
        }
    }

    private int indexFor(Object key, int length) {
        int hash = key == null ? 0 : key.hashCode();
        return (hash & 0x7fffffff) % length;
    }

    private void unlinkOrder(Node<E> node) {
        Node<E> prev = node.prevOrder;
        Node<E> next = node.nextOrder;
        if (prev != null) {
            prev.nextOrder = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prevOrder = prev;
        } else {
            tail = prev;
        }
        node.prevOrder = null;
        node.nextOrder = null;
    }

    private boolean equalsKey(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
