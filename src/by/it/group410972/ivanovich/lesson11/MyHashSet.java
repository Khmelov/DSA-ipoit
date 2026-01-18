package by.it.group410972.ivanovich.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        final E data;
        final int hash;
        Node<E> next;

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.size = 0;
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (size > 0) {
            Arrays.fill(table, null);
            size = 0;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object obj) {
        return addElement((E) obj);
    }

    @Override
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }

        int hash = hash(obj);
        int index = (table.length - 1) & hash;

        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (obj == current.data || obj.equals(current.data))) {
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
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }

        int hash = hash(obj);
        int index = (table.length - 1) & hash;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (obj == current.data || obj.equals(current.data))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> current = null;
            private Node<E> next = null;
            private int count = 0;

            {
                findNext();
            }

            private void findNext() {
                if (next != null && next.next != null) {
                    next = next.next;
                    return;
                }

                while (bucketIndex < table.length) {
                    if (table[bucketIndex] != null) {
                        next = table[bucketIndex++];
                        return;
                    }
                    bucketIndex++;
                }
                next = null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                current = next;
                E result = current.data;
                findNext();
                count++;
                return result;
            }

            @Override
            public void remove() {
                if (current == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(current.data);
                current = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;

        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            while (current != null) {
                result[index++] = current.data;
                current = current.next;
            }
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
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////////////////////// Вспомогательные методы /////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private boolean addElement(E element) {
        if (element == null) {
            return false;
        }

        int hash = hash(element);
        int index = (table.length - 1) & hash;

        // Проверяем, нет ли уже такого элемента
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (element == current.data || element.equals(current.data))) {
                return false;
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки
        table[index] = new Node<>(element, hash, table[index]);
        size++;

        if (size > threshold) {
            resize();
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = (newCapacity - 1) & current.hash;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16); // Spread bits to reduce collisions
    }
}