package by.it.group410971.usovskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел: один next для цепочки в бакете и prev/next для порядка вставки
    private static class Node<E> {
        E value;
        Node<E> bucketNext; // цепочка коллизий
        Node<E> prev;       // предыдущий в порядке вставки
        Node<E> next;       // следующий в порядке вставки

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;   // массив бакетов
    private int size;          // кол-во элементов

    private Node<E> head;      // первый по порядку вставки
    private Node<E> tail;      // последний по порядку вставки

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16]; // фиксированный размер достаточно для задачи
    }

    // ======================= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =======================

    private int index(Object o) {
        if (o == null) return 0;
        int h = o.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % table.length;
    }

    private boolean equalsElem(E a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }

    private void linkInsertion(Node<E> n) {
        if (head == null) {
            head = tail = n;
        } else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
    }

    private void unlinkInsertion(Node<E> n) {
        Node<E> p = n.prev;
        Node<E> q = n.next;
        if (p == null) head = q;
        else           p.next = q;

        if (q == null) tail = p;
        else           q.prev = p;

        n.prev = n.next = null;
    }

    // ======================= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =========================

    @Override
    public int size() {
        return size;
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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> cur = table[idx];

        // проверка на существование
        while (cur != null) {
            if (equalsElem(cur.value, e)) return false;
            cur = cur.bucketNext;
        }

        // вставка в бакет
        Node<E> n = new Node<>(e);
        n.bucketNext = table[idx];
        table[idx] = n;

        // вставка в цепочку вставки
        linkInsertion(n);

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        Node<E> prev = null;

        while (cur != null) {
            if (equalsElem(cur.value, o)) {
                // вырезаем из бакета
                if (prev == null) table[idx] = cur.bucketNext;
                else              prev.bucketNext = cur.bucketNext;

                // вырезаем из цепочки вставки
                unlinkInsertion(cur);

                size--;
                return true;
            }
            prev = cur;
            cur = cur.bucketNext;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (equalsElem(cur.value, o)) return true;
            cur = cur.bucketNext;
        }
        return false;
    }

    // toString() — в порядке добавления (по цепочке head->tail)
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext()) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Set)" : e);
            if (!it.hasNext()) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(',').append(' ');
        }
    }

    // ======================= bulk-операции =======================

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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (c.contains(e)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    // ======================= остальная часть Set =======================

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head;
            Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                lastReturned = cur;
                cur = cur.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
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
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > size) a[size] = null;
        return a;
    }

}
