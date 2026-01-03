package by.it.group410971.usovskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // ===== узел двусвязного списка =====
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    // ===== вспомогательные методы =====

    private void linkFirst(E e) {
        Node<E> n = new Node<>(e);
        n.next = head;
        if (head != null) head.prev = n;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    private void linkLast(E e) {
        Node<E> n = new Node<>(e);
        n.prev = tail;
        if (tail != null) tail.next = n;
        tail = n;
        if (head == null) head = n;
        size++;
    }

    private E unlinkFirst() {
        if (head == null) return null;
        Node<E> n = head;
        Node<E> next = n.next;
        head = next;
        if (next != null) next.prev = null;
        else tail = null;
        size--;
        return n.item;
    }

    private E unlinkLast() {
        if (tail == null) return null;
        Node<E> n = tail;
        Node<E> prev = n.prev;
        tail = prev;
        if (prev != null) prev.next = null;
        else head = null;
        size--;
        return n.item;
    }

    private E unlink(Node<E> n) {
        if (n == null) return null;

        if (n.prev != null) n.prev.next = n.next;
        else head = n.next;

        if (n.next != null) n.next.prev = n.prev;
        else tail = n.prev;

        size--;
        return n.item;
    }

    private Node<E> node(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur;
    }

    // ===== обязательные методы задания =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append(']');
        return sb.toString();
    }

    // add(E element)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // remove(int index)
    public E remove(int index) {
        return unlink(node(index));
    }

    // remove(E element) – через remove(Object o)
    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        if (o == null) {
            while (cur != null) {
                if (cur.item == null) {
                    unlink(cur);
                    return true;
                }
                cur = cur.next;
            }
        } else {
            while (cur != null) {
                if (o.equals(cur.item)) {
                    unlink(cur);
                    return true;
                }
                cur = cur.next;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    // addFirst(E element) / addLast(E element)

    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    // element(), getFirst(), getLast()

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    // poll(), pollFirst(), pollLast()

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return unlinkFirst();
    }

    @Override
    public E pollLast() {
        return unlinkLast();
    }

    // ===== остальные методы Deque / Collection (минимальные реализации) =====

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
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E removeFirst() {
        E v = pollFirst();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E removeLast() {
        E v = pollLast();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
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
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> cur = tail;
        if (o == null) {
            while (cur != null) {
                if (cur.item == null) {
                    unlink(cur);
                    return true;
                }
                cur = cur.prev;
            }
        } else {
            while (cur != null) {
                if (o.equals(cur.item)) {
                    unlink(cur);
                    return true;
                }
                cur = cur.prev;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> cur = head;
        if (o == null) {
            while (cur != null) {
                if (cur.item == null) return true;
                cur = cur.next;
            }
        } else {
            while (cur != null) {
                if (o.equals(cur.item)) return true;
                cur = cur.next;
            }
        }
        return false;
    }

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
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> cur = tail;
            Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                lastReturned = cur;
                cur = cur.prev;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            a[i++] = cur.item;
        }
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Class<?> comp = a.getClass().getComponentType();
            a = (T[]) java.lang.reflect.Array.newInstance(comp, size);
        }
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            a[i++] = (T) cur.item;
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
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
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
            if (!c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }
}
