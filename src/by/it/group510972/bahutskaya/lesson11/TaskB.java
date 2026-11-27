package by.it.group510972.bahutskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Множество, которое помнит порядок добавления.
public class TaskB<E> implements Set<E> {

    private static final class Node<E> {
        E value;
        Node<E> next;
        Node<E> after;
        Node<E> before;

        Node(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] buckets = new Node[16];
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private int index(Object value) {
        int hash = value == null ? 0 : value.hashCode();
        hash ^= hash >>> 16;
        return (hash & 0x7fffffff) % buckets.length;
    }

    private Node<E> find(Object value) {
        for (Node<E> node = buckets[index(value)]; node != null; node = node.next) {
            if (value == null ? node.value == null : value.equals(node.value)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = head; node != null; node = node.after) {
            if (node != head) {
                sb.append(", ");
            }
            sb.append(node.value);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = new Node[16];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (find(element) != null) {
            return false;
        }
        if (size > buckets.length * 0.75) {
            resize();
        }
        Node<E> node = new Node<>(element);
        int index = index(element);
        node.next = buckets[index];
        buckets[index] = node;
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E> oldHead = head;
        buckets = new Node[buckets.length * 2];
        head = null;
        tail = null;
        size = 0;
        for (Node<E> node = oldHead; node != null; node = node.after) {
            add(node.value);
        }
    }

    @Override
    public boolean remove(Object element) {
        int index = index(element);
        Node<E> current = buckets[index];
        Node<E> prev = null;
        while (current != null) {
            if (element == null ? current.value == null : element.equals(current.value)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                if (current.before == null) {
                    head = current.after;
                } else {
                    current.before.after = current.after;
                }
                if (current.after == null) {
                    tail = current.before;
                } else {
                    current.after.before = current.before;
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
    public boolean contains(Object element) {
        return find(element) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            changed |= add(item);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.after;
            if (c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.after;
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.after;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            copy[i++] = node.value;
        }
        return copy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            a[i++] = (T) node.value;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
