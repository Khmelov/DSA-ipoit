package by.it.group410972.sheshtakauskas.lesson01.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// ///               Обязательные к реализации методы             ///////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E> head;
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
        if (head == null) {
            head = new Node<>(e, null);
        } else {
            Node<E> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = new Node<>(e, null);
        }
        size++;
        return true;
    }

    private Node<E> getNode(int index) {
        Node<E> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        if (index == 0) {
            E val = head.value;
            head = head.next;
            size--;
            return val;
        }

        Node<E> prev = getNode(index - 1);
        E val = prev.next.value;
        prev.next = prev.next.next;
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (index == 0) {
            head = new Node<>(element, head);
        } else {
            Node<E> prev = getNode(index - 1);
            prev.next = new Node<>(element, prev.next);
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (head == null) return false;

        if (Objects.equals(head.value, o)) {
            head = head.next;
            size--;
            return true;
        }

        Node<E> cur = head;
        while (cur.next != null) {
            if (Objects.equals(cur.next.value, o)) {
                cur.next = cur.next.next;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
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
        checkIndex(index);
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


    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// ///               Опциональные к реализации методы             ///////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////


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
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        int i = index;
        for (E e : c) {
            add(i++, e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> prev = null;
        Node<E> cur = head;

        while (cur != null) {
            if (c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                changed = true;
                cur = (prev == null) ? head : prev.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> prev = null;
        Node<E> cur = head;

        while (cur != null) {
            if (!c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                changed = true;
                cur = (prev == null) ? head : prev.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }

        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();

        ListB<E> sub = new ListB<>();
        Node<E> cur = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.value);
            cur = cur.next;
        }
        return sub;
    }


    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<>() {
            Node<E> nextNode = (index == 0) ? head : getNode(index);
            Node<E> lastReturned = null;
            int cursor = index;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = nextNode.next;
                cursor++;
                return lastReturned.value;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                throw new UnsupportedOperationException("previous() not implemented");
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(E e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException();
            }
        };
    }


    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            a[i++] = (T) cur.value;
        }

        if (a.length > size) a[size] = null;

        return a;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// /////        Эти методы имплементировать необязательно    ////////////
    /// /////        но они будут нужны для корректной отладки    ////////////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
