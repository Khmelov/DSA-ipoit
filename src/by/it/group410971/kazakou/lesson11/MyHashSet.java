package by.it.group410971.kazakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

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

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder out = new StringBuilder();
        out.append('[');
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    out.append(", ");
                }
                out.append(node.key);
                first = false;
                node = node.next;
            }
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);
        Node<E> head = table[index];
        if (head == null) {
            table[index] = new Node<>(hash, e, null);
        } else {
            Node<E> current = head;
            Node<E> last = null;
            while (current != null) {
                if (equalsKey(current.key, e)) {
                    return false;
                }
                last = current;
                current = current.next;
            }
            last.next = new Node<>(hash, e, null);
        }
        size++;
        if (size > threshold) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if (current.hash == hash && equalsKey(current.key, o)) {
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
    public boolean contains(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && equalsKey(current.key, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int bucketIndex;
            private Node<E> current;

            @Override
            public boolean hasNext() {
                while (current == null && bucketIndex < table.length) {
                    current = table[bucketIndex++];
                }
                return current != null;
            }

            @Override
            public E next() {
                E value = current.key;
                current = current.next;
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
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            Node<E> prev = null;
            while (current != null) {
                if (!c.contains(current.key)) {
                    if (prev == null) {
                        table[i] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    modified = true;
                    current = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = current;
                    current = current.next;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            Node<E> prev = null;
            while (current != null) {
                if (c.contains(current.key)) {
                    if (prev == null) {
                        table[i] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    modified = true;
                    current = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = current;
                    current = current.next;
                }
            }
        }
        return modified;
    }

    private void resize() {
        Node<E>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = oldCap << 1;
        Node<E>[] newTab = new Node[newCap];
        for (int i = 0; i < oldCap; i++) {
            Node<E> e = oldTab[i];
            if (e == null) {
                continue;
            }
            Node<E> loHead = null;
            Node<E> loTail = null;
            Node<E> hiHead = null;
            Node<E> hiTail = null;
            while (e != null) {
                Node<E> next = e.next;
                if ((e.hash & oldCap) == 0) {
                    if (loTail == null) {
                        loHead = e;
                    } else {
                        loTail.next = e;
                    }
                    loTail = e;
                } else {
                    if (hiTail == null) {
                        hiHead = e;
                    } else {
                        hiTail.next = e;
                    }
                    hiTail = e;
                }
                e = next;
            }
            if (loTail != null) {
                loTail.next = null;
                newTab[i] = loHead;
            }
            if (hiTail != null) {
                hiTail.next = null;
                newTab[i + oldCap] = hiHead;
            }
        }
        table = newTab;
        threshold = (int) (newCap * LOAD_FACTOR);
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private boolean equalsKey(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
