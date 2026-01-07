package by.it.group410972.mezenova.lesson11;

import java.util.*;

class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class LinkedHashNode<E> {
        final int hash;
        final E key;
        LinkedHashNode<E> next;
        LinkedHashNode<E> before, after;

        LinkedHashNode(int hash, E key, LinkedHashNode<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private LinkedHashNode<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    private LinkedHashNode<E> head;
    private LinkedHashNode<E> tail;

    public MyLinkedHashSet() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        @SuppressWarnings("unchecked")
        LinkedHashNode<E>[] newTable = (LinkedHashNode<E>[]) new LinkedHashNode[DEFAULT_INITIAL_CAPACITY];
        this.table = newTable;
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.threshold = (int)(initialCapacity * loadFactor);
        @SuppressWarnings("unchecked")
        LinkedHashNode<E>[] newTable = (LinkedHashNode<E>[]) new LinkedHashNode[initialCapacity];
        this.table = newTable;
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public MyLinkedHashSet(int initialCapacity) {
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
        int newCapacity = oldCapacity << 1;
        threshold = (int)(newCapacity * loadFactor);

        @SuppressWarnings("unchecked")
        LinkedHashNode<E>[] newTable = (LinkedHashNode<E>[]) new LinkedHashNode[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            LinkedHashNode<E> e = table[i];
            while (e != null) {
                LinkedHashNode<E> next = e.next;
                int newIndex = indexFor(e.hash, newCapacity);
                e.next = newTable[newIndex];
                newTable[newIndex] = e;
                e = next;
            }
            table[i] = null;
        }

        table = newTable;
    }

    private void linkNodeLast(LinkedHashNode<E> node) {
        LinkedHashNode<E> last = tail;
        tail = node;
        if (last == null) {
            head = node;
        } else {
            node.before = last;
            last.after = node;
        }
    }

    private void unlinkNode(LinkedHashNode<E> node) {
        LinkedHashNode<E> before = node.before;
        LinkedHashNode<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
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
        if (o == null) {
            for (LinkedHashNode<E> e = table[0]; e != null; e = e.next) {
                if (e.key == null) {
                    return true;
                }
            }
            return false;
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        for (LinkedHashNode<E> e = table[index]; e != null; e = e.next) {
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

        LinkedHashNode<E> first = table[index];
        for (LinkedHashNode<E> node = first; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(e, node.key)) {
                return false;
            }
        }

        LinkedHashNode<E> newNode = new LinkedHashNode<>(hash, e, first);
        table[index] = newNode;
        linkNodeLast(newNode);
        size++;
        return true;
    }

    private boolean addNull() {
        int index = 0;
        LinkedHashNode<E> first = table[index];

        for (LinkedHashNode<E> node = first; node != null; node = node.next) {
            if (node.key == null) {
                return false;
            }
        }

        LinkedHashNode<E> newNode = new LinkedHashNode<>(0, null, first);
        table[index] = newNode;
        linkNodeLast(newNode);
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

        LinkedHashNode<E> prev = null;
        LinkedHashNode<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && Objects.equals(o, current.key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                unlinkNode(current);
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
        LinkedHashNode<E> prev = null;
        LinkedHashNode<E> current = table[index];

        while (current != null) {
            if (current.key == null) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                unlinkNode(current);
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
            head = tail = null;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        LinkedHashNode<E> current = head;
        while (current != null) {
            sb.append(current.key);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private LinkedHashNode<E> current = head;
            private LinkedHashNode<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                E element = current.key;
                current = current.after;
                return element;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.key);
                lastReturned = null;
            }
        };
    }

    // Остальные методы интерфейса Set (не требуются по заданию)

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (LinkedHashNode<E> node = head; node != null; node = node.after) {
            result[i++] = node.key;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
