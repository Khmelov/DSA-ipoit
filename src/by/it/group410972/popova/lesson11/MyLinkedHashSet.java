package by.it.group410972.popova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    // Узел для хранения элемента в бакете
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;
        Node(E value) { this.value = value; }
    }

    private Node<E>[] buckets;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    // Дополнительные ссылки для порядка добавления
    private Node<E> head;
    private Node<E> tail;

    public MyLinkedHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = tail = null;
    }

    private int index(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % buckets.length;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> current = buckets[idx];
        while (current != null) {
            if (e == null ? current.value == null : e.equals(current.value)) {
                return false; // элемент уже есть
            }
            current = current.next;
        }
        Node<E> newNode = new Node<>(e);
        // вставка в бакет
        newNode.next = buckets[idx];
        buckets[idx] = newNode;
        // вставка в список порядка
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }
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
                // удаление из бакета
                if (prev == null) buckets[idx] = current.next;
                else prev.next = current.next;
                // удаление из списка порядка
                if (current.before != null) current.before.after = current.after;
                else head = current.after;
                if (current.after != null) current.after.before = current.before;
                else tail = current.before;
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
        head = tail = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // Методы работы с коллекциями
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) if (remove(o)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
