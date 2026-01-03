package by.it.group410971.usovskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // ===== внутренний узел связанного списка =====
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E value, Node<E> next, Node<E> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    // быстрый доступ к узлу по индексу
    private Node<E> node(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<E> x;
        if (index < (size >> 1)) {
            x = head;
            for (int i = 0; i < index; i++) x = x.next;
        } else {
            x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
        }
        return x;
    }

    // =====================================================
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ
    // =====================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = head;
        int i = 0;
        while (x != null) {
            if (i++ > 0) sb.append(", ");
            sb.append(x.value);
            x = x.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e, null, tail);
        if (tail == null) {          // список был пуст
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        Node<E> x = node(index);
        E old = x.value;

        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null) head = next;
        else prev.next = next;

        if (next == null) tail = prev;
        else next.prev = prev;

        size--;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (index == size) { // добавление в конец
            add(element);
            return;
        }

        Node<E> succ = node(index);
        Node<E> pred = succ.prev;
        Node<E> newNode = new Node<>(element, succ, pred);

        succ.prev = newNode;
        if (pred == null) head = newNode;
        else pred.next = newNode;

        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> x = head;
        while (x != null) {
            if (o == null ? x.value == null : o.equals(x.value)) {
                Node<E> prev = x.prev;
                Node<E> next = x.next;

                if (prev == null) head = next;
                else prev.next = next;

                if (next == null) tail = prev;
                else next.prev = prev;

                size--;
                return true;
            }
            x = x.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node<E> x = node(index);
        E old = x.value;
        x.value = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int idx = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.value == null : o.equals(x.value))
                return idx;
            idx++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return node(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int idx = size - 1;
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (o == null ? x.value == null : o.equals(x.value))
                return idx;
            idx--;
        }
        return -1;
    }

    // ====== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ======

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for (E e : c) add(e);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        int i = index;
        for (E e : c) {
            add(i++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object x : c) {
            while (remove(x)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> x = head;
        int idx = 0;
        while (x != null) {
            Node<E> next = x.next;
            if (!c.contains(x.value)) {
                remove(idx);
                modified = true;
            } else {
                idx++;
            }
            x = next;
        }
        return modified;
    }

    // =====================================================
    // ОПЦИОНАЛЬНЫЕ
    // =====================================================

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            res[i++] = x.value;
        }
        return res;
    }

    // Итератор
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E v = current.value;
                current = current.next;
                return v;
            }
        };
    }
}
