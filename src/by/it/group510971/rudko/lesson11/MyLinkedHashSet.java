package by.it.group510971.rudko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

//Множество на основе массива с адресацией по хеш-коду (коллизии - односвязные списки)
//плюс двусвязный список порядка добавления: toString() и итератор выдают элементы по порядку добавления.
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<T> {
        final T value;
        final int hash;
        Node<T> next;          //следующий в той же корзине хеш-таблицы (коллизии)
        Node<T> before, after; //соседи в порядке добавления

        Node(T value, int hash, Node<T> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head;   //первый добавленный
    private Node<E> tail;   //последний добавленный
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> n = head; n != null; n = n.after) {
            sb.append(n.value);
            if (n.after != null) sb.append(", ");
        }
        return sb.append("]").toString();
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int idx = index(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && equal(e, n.value)) return false;
        }
        Node<E> node = new Node<>(e, h, table[idx]);
        table[idx] = node;
        //добавляем в конец списка порядка
        node.before = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;
        size++;
        if (size > table.length * LOAD_FACTOR) resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = index(h, table.length);
        Node<E> prev = null;
        for (Node<E> n = table[idx]; n != null; prev = n, n = n.next) {
            if (n.hash == h && equal(o, n.value)) {
                if (prev == null) {
                    table[idx] = n.next;
                } else {
                    prev.next = n.next;
                }
                unlinkOrder(n);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        for (Node<E> n = table[index(h, table.length)]; n != null; n = n.next) {
            if (n.hash == h && equal(o, n.value)) return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
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
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : toArray()) {
            if (!c.contains(o)) {
                remove(o);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int k = 0;
        for (Node<E> n = head; n != null; n = n.after) {
            result[k++] = n.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] items = toArray();
        T[] result = a.length >= size
                ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) items[i];
        }
        if (result.length > size) result[size] = null;
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        Object[] items = toArray();
        return new Iterator<E>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < items.length;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (pos >= items.length) throw new NoSuchElementException();
                return (E) items[pos++];
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    private void unlinkOrder(Node<E> n) {
        if (n.before == null) {
            head = n.after;
        } else {
            n.before.after = n.after;
        }
        if (n.after == null) {
            tail = n.before;
        } else {
            n.after.before = n.before;
        }
    }

    private static int hash(Object o) {
        return (o == null) ? 0 : (o.hashCode() & 0x7fffffff);
    }

    private static int index(int hash, int length) {
        return hash % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = table;
        table = (Node<E>[]) new Node[old.length * 2];
        for (Node<E> bucket : old) {
            Node<E> n = bucket;
            while (n != null) {
                Node<E> next = n.next;
                int idx = index(n.hash, table.length);
                n.next = table[idx];
                table[idx] = n;
                n = next;
            }
        }
    }

    private static boolean equal(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
