package by.it.group410971.usovskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // ===== узел односвязного списка в бакете =====
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;  // массив бакетов
    private int size;         // количество элементов

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        // фиксированный размер таблицы
        table = (Node<E>[]) new Node[16];
    }

    // ===== вспомогательные методы =====

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

    // ===== обязательные методы =====

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
        int idx = index(e);
        Node<E> head = table[idx];
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            if (equalsElem(cur.value, e)) {
                return false; // уже есть
            }
        }
        table[idx] = new Node<>(e, head); // вставляем в начало списка
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
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (equalsElem(cur.value, o)) return true;
        }
        return false;
    }

    // ===== toString в формате обычных коллекций =====

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

    // ===== остальная обязательная часть интерфейса Set/Collection =====

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucketIndex = 0;
            Node<E> current = null;
            E lastReturned = null;
            boolean canRemove = false;

            private void advance() {
                while (current == null && bucketIndex < table.length) {
                    current = table[bucketIndex++];
                }
            }

            @Override
            public boolean hasNext() {
                advance();
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = current.value;
                current = current.next;
                canRemove = true;
                return lastReturned;
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();
                MyHashSet.this.remove(lastReturned);
                canRemove = false;
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

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

}
