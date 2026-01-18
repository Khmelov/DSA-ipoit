package by.it.group410972.ivanovich.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class LinkedNode<E> {
        final E data;
        final int hash;
        LinkedNode<E> next; // для коллизий в корзине
        LinkedNode<E> before, after; // для поддержания порядка добавления

        LinkedNode(E data, int hash, LinkedNode<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
            this.before = null;
            this.after = null;
        }
    }

    private LinkedNode<E>[] table;
    private LinkedNode<E> head; // первый добавленный элемент
    private LinkedNode<E> tail; // последний добавленный элемент
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        this.table = (LinkedNode<E>[]) new LinkedNode[initialCapacity];
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        LinkedNode<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (size > 0) {
            Arrays.fill(table, null);
            head = null;
            tail = null;
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

        LinkedNode<E> prev = null;
        LinkedNode<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (obj == current.data || obj.equals(current.data))) {
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка порядка добавления
                removeFromOrder(current);
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
        LinkedNode<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (obj == current.data || obj.equals(current.data))) {
                return true;
            }
            current = current.next;
        }
        return false;
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
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Проходим по списку порядка добавления
        LinkedNode<E> current = head;
        while (current != null) {
            LinkedNode<E> next = current.after;
            if (c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Проходим по списку порядка добавления
        LinkedNode<E> current = head;
        while (current != null) {
            LinkedNode<E> next = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private LinkedNode<E> current = head;
            private LinkedNode<E> lastReturned = null;

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
                E result = current.data;
                current = current.after;
                return result;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.data);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        LinkedNode<E> current = head;
        int index = 0;

        while (current != null) {
            result[index++] = current.data;
            current = current.after;
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
        LinkedNode<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (element == current.data || element.equals(current.data))) {
                return false;
            }
            current = current.next;
        }

        // Создаем новый узел
        LinkedNode<E> newNode = new LinkedNode<>(element, hash, table[index]);
        table[index] = newNode;

        // Добавляем в список порядка добавления
        addToOrder(newNode);
        size++;

        if (size > threshold) {
            resize();
        }

        return true;
    }

    private void addToOrder(LinkedNode<E> node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void removeFromOrder(LinkedNode<E> node) {
        if (node.before != null) {
            node.before.after = node.after;
        } else {
            head = node.after;
        }

        if (node.after != null) {
            node.after.before = node.before;
        } else {
            tail = node.before;
        }

        node.before = null;
        node.after = null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        LinkedNode<E>[] newTable = (LinkedNode<E>[]) new LinkedNode[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        // Перераспределяем элементы по новым корзинам
        LinkedNode<E> current = head;
        while (current != null) {
            LinkedNode<E> nextInOrder = current.after;
            int newIndex = (newCapacity - 1) & current.hash;

            // Сохраняем ссылку на следующий узел в оригинальной цепочке
            LinkedNode<E> nextInBucket = current.next;

            // Вставляем в начало цепочки новой корзины
            current.next = newTable[newIndex];
            newTable[newIndex] = current;

            current = nextInOrder;
        }

        table = newTable;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }
}