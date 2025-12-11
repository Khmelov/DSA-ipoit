package by.it.group410971.nesteruk.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> orderNext;
        Node<E> orderPrev;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.orderNext;
        }
        sb.append("]");
        return sb.toString();
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
            return false;
        }
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if (o.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (contains(e)) {
            return false;
        }

        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        // Добавляем в хеш-таблицу
        newNode.next = table[index];
        table[index] = newNode;

        // Добавляем в порядок добавления
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.orderNext = newNode;
            newNode.orderPrev = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o.equals(current.data)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из порядка добавления
                if (current.orderPrev != null) {
                    current.orderPrev.orderNext = current.orderNext;
                } else {
                    head = current.orderNext;
                }
                if (current.orderNext != null) {
                    current.orderNext.orderPrev = current.orderPrev;
                } else {
                    tail = current.orderPrev;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
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
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
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
            Node<E> next = current.orderNext;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    private int getIndex(Object o) {
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        // Сохраняем старый порядок и перестраиваем хеш-таблицу
        Node<E> oldHead = head;
        Node<E> oldTail = tail;
        head = tail = null;
        size = 0;

        Node<E> current = oldHead;
        while (current != null) {
            // Создаем копию данных, но не узла
            E data = current.data;
            int index = getIndex(data);
            Node<E> newNode = new Node<>(data);

            // Добавляем в хеш-таблицу
            newNode.next = table[index];
            table[index] = newNode;

            // Добавляем в порядок добавления
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.orderNext = newNode;
                newNode.orderPrev = tail;
                tail = newNode;
            }

            size++;
            current = current.orderNext;
        }
    }

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
}