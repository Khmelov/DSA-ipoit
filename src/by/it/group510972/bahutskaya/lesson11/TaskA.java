package by.it.group510972.bahutskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Множество без повторов. Ищем элемент по хешу.
public class TaskA<E> implements Set<E> {

    private static final class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] buckets = new Node[16];
    private int size;

    private int index(Object value) {
        int hash = value == null ? 0 : value.hashCode();
        hash ^= hash >>> 16;
        return (hash & 0x7fffffff) % buckets.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = new Node[16];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) {
            return false;
        }
        if (size > buckets.length * 0.75) {
            resize();
        }
        int index = index(element);
        buckets[index] = new Node<>(element, buckets[index]);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = buckets;
        buckets = new Node[old.length * 2];
        size = 0;
        for (Node<E> node : old) {
            while (node != null) {
                add(node.value);
                node = node.next;
            }
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
        Node<E> current = buckets[index(element)];
        while (current != null) {
            if (element == null ? current.value == null : element.equals(current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : buckets) {
            for (Node<E> node = bucket; node != null; node = node.next) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(node.value);
            }
        }
        sb.append(']');
        return sb.toString();
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
        for (Object item : c) {
            changed |= remove(item);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Node<E> bucket : java.util.Arrays.copyOf(buckets, buckets.length)) {
            for (Node<E> node = bucket; node != null; node = node.next) {
                if (!c.contains(node.value)) {
                    remove(node.value);
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int bucket;
            private Node<E> current = advance();

            private Node<E> advance() {
                while (bucket < buckets.length) {
                    if (buckets[bucket] != null) {
                        return buckets[bucket++];
                    }
                    bucket++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.next != null ? current.next : advance();
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        int i = 0;
        for (E item : this) {
            copy[i++] = item;
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
        for (E item : this) {
            a[i++] = (T) item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
