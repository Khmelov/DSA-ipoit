package by.it.group510971.rudko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //Реализация: односвязный список с головой и хвостом, узлы - собственный класс Node.

    private class Node {
        E value;
        Node next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        appendNode(e);
        return true;
    }

    @Override
    public E remove(int index) {
        Node prev = (index == 0) ? null : nodeAt(index - 1);
        Node cur = (prev == null) ? nodeAt(0) : prev.next;
        unlink(prev, cur);
        return cur.value;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index: " + index);
        if (index == size) {
            appendNode(element);
            return;
        }
        Node n = new Node(element);
        n.next = nodeAt(index);
        if (index == 0) {
            head = n;
        } else {
            nodeAt(index - 1).next = n;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node prev = null;
        for (Node cur = head; cur != null; cur = cur.next) {
            if (equal(o, cur.value)) {
                unlink(prev, cur);
                return true;
            }
            prev = cur;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node n = nodeAt(index);
        E old = n.value;
        n.value = element;
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
        for (Node cur = head; cur != null; cur = cur.next, i++) {
            if (equal(o, cur.value)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return nodeAt(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int last = -1;
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next, i++) {
            if (equal(o, cur.value)) last = i;
        }
        return last;
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
            appendNode(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index: " + index);
        int i = index;
        for (E e : c) {
            add(i++, e);
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

    //удаляет узлы, значение которых содержится в c (inCollection = true) или не содержится (false)
    private boolean removeIf(Collection<?> c, boolean inCollection) {
        boolean changed = false;
        Node prev = null;
        Node cur = head;
        while (cur != null) {
            Node next = cur.next;
            if (c.contains(cur.value) == inCollection) {
                unlink(prev, cur);
                changed = true;
            } else {
                prev = cur;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            result[i++] = cur.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size
                ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            result[i++] = (T) cur.value;
        }
        if (result.length > size) result[size] = null;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node cur = head;

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
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList не реализован");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator не реализован");
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("listIterator не реализован");
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    private void appendNode(E e) {
        Node n = new Node(e);
        if (tail == null) {
            head = n;
        } else {
            tail.next = n;
        }
        tail = n;
        size++;
    }

    //узел с номером index (0 - первый); проверяет границы
    private Node nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    //исключает узел cur, у которого предыдущий узел prev (null, если cur - голова)
    private void unlink(Node prev, Node cur) {
        if (prev == null) {
            head = cur.next;
        } else {
            prev.next = cur.next;
        }
        if (cur == tail) {
            tail = prev;
        }
        size--;
    }

    private static boolean equal(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
