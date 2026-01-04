package by.it.group410972.zayats.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {

    // Начальный размер массива бакетов
    private static final int DEFAULT_CAPACITY = 16;

    // Массив бакетов
    private Node<E>[] table;

    // Количество элементов
    private int size;

    // Узел для односвязного списка в бакете
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Получаем индекс бакета по хеш-коду
    private int getIndex(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if ((current.value == null && e == null) || (current.value != null && current.value.equals(e))) {
                return false; // элемент уже есть
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(e, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if ((current.value == null && o == null) || (current.value != null && current.value.equals(o))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if ((current.value == null && o == null) || (current.value != null && current.value.equals(o))) {
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Простейший toString() в стиле стандартных коллекций
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // --- Методы из Set<E> для компиляции (не реализуем, просто throw) ---
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
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
