package by.it.group410972.Pushkarev.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e, tail, null);
        if (tail == null) head = newNode;
        else tail.next = newNode;
        tail = newNode;
        size++;
        return true;
    }

    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            Node<E> cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    @Override
    public E remove(int index) {
        Node<E> node = getNode(index);
        E val = node.value;

        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;

        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    // ================= ДОПОЛНИТЕЛЬНЫЕ =================

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        if (index == size) { // добавить в конец
            add(element);
            return;
        }

        Node<E> nextNode = getNode(index);
        Node<E> prevNode = nextNode.prev;
        Node<E> newNode = new Node<>(element, prevNode, nextNode);

        if (prevNode != null) prevNode.next = newNode;
        else head = newNode;

        nextNode.prev = newNode;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
                if (cur.prev != null) cur.prev.next = cur.next;
                else head = cur.next;
                if (cur.next != null) cur.next.prev = cur.prev;
                else tail = cur.prev;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = getNode(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next, i++) {
            if (Objects.equals(cur.value, o)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return getNode(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int idx = -1;
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next, i++) {
            if (Objects.equals(cur.value, o)) idx = i;
        }
        return idx;
    }

    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) { add(e); changed = true; }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int i = index;
        for (E e : c) { add(i++, e); }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListC<E> sub = new ListC<>();
        Node<E> cur = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.value);
            cur = cur.next;
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator() { return listIterator(0); }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<>() {
            Node<E> nextNode = (index == 0) ? head : getNode(index);
            Node<E> lastReturned = null;
            int cursor = index;

            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = nextNode.next;
                cursor++;
                return lastReturned.value;
            }

            @Override public boolean hasPrevious() { return cursor > 0; }
            @Override public E previous() { throw new UnsupportedOperationException(); }

            @Override public int nextIndex() { return cursor; }
            @Override public int previousIndex() { return cursor - 1; }

            @Override public void remove() { throw new UnsupportedOperationException(); }
            @Override public void set(E e) { throw new UnsupportedOperationException(); }
            @Override public void add(E e) { throw new UnsupportedOperationException(); }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) a[i++] = (T) cur.value;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) arr[i++] = cur.value;
        return arr;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
