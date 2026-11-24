package by.it.group510971.rudko.lesson10;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//Двусторонняя очередь на основе двунаправленного связного списка.
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyLinkedList<E> implements Deque<E> {

    private class Node {
        E value;
        Node prev;
        Node next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node first;
    private Node last;
    private int size;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node cur = first; cur != null; cur = cur.next) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        Node n = nodeAt(index);
        unlink(n);
        return n.value;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        checkNotNull(e);
        Node n = new Node(e);
        n.next = first;
        if (first == null) {
            last = n;
        } else {
            first.prev = n;
        }
        first = n;
        size++;
    }

    @Override
    public void addLast(E e) {
        checkNotNull(e);
        Node n = new Node(e);
        n.prev = last;
        if (last == null) {
            first = n;
        } else {
            last.next = n;
        }
        last = n;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.value;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        E e = first.value;
        unlink(first);
        return e;
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        E e = last.value;
        unlink(last);
        return e;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     остальные методы Deque                ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.value;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.value;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node cur = first; cur != null; cur = cur.next) {
            if (equal(o, cur.value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node cur = last; cur != null; cur = cur.prev) {
            if (equal(o, cur.value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean contains(Object o) {
        for (Node cur = first; cur != null; cur = cur.next) {
            if (equal(o, cur.value)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node cur = first;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E value = cur.value;
                cur = cur.next;
                return value;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node cur = last;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E value = cur.value;
                cur = cur.prev;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node cur = first; cur != null; cur = cur.next) {
            result[i++] = cur.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node cur = first; cur != null; cur = cur.next) {
            result[i++] = (T) cur.value;
        }
        if (result.length > size) result[size] = null;
        return result;
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
        for (E e : c) {
            addLast(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeIf(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeIf(c, false);
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    private Node nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
        Node cur;
        if (index < size / 2) {                       //идем от головы
            cur = first;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {                                      //идем от хвоста
            cur = last;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    //исключает узел из списка и связывает соседей
    private void unlink(Node n) {
        if (n.prev == null) {
            first = n.next;
        } else {
            n.prev.next = n.next;
        }
        if (n.next == null) {
            last = n.prev;
        } else {
            n.next.prev = n.prev;
        }
        size--;
    }

    private boolean removeIf(Collection<?> c, boolean inCollection) {
        boolean changed = false;
        Node cur = first;
        while (cur != null) {
            Node next = cur.next;
            if (c.contains(cur.value) == inCollection) {
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    private static void checkNotNull(Object e) {
        if (e == null) throw new NullPointerException();
    }

    private static boolean equal(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
