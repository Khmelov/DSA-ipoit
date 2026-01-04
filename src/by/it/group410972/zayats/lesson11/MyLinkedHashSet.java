package by.it.group410972.zayats.lesson11;



import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;

    // Головной и хвостовой узлы для порядка вставки
    private Node<E> head;
    private Node<E> tail;

    // Узел для хранения элементов
    private static class Node<E> {
        E value;
        Node<E> next;      // для хеш-таблицы (коллизии)
        Node<E> before;    // для связного списка порядка добавления
        Node<E> after;

        Node(E value) {
            this.value = value;
        }
    }

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = tail = null;
    }

    private int getIndex(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        // Проверяем, есть ли уже элемент
        while (current != null) {
            if ((current.value == null && e == null) || (current.value != null && current.value.equals(e))) {
                return false;
            }
            current = current.next;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<>(e);

        // Добавляем в хеш-таблицу (в начало списка)
        newNode.next = table[index];
        table[index] = newNode;

        // Добавляем в конец списка порядка вставки
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if ((current.value == null && o == null) || (current.value != null && current.value.equals(o))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if ((current.value == null && o == null) || (current.value != null && current.value.equals(o))) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из списка порядка вставки
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }
                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // --- Методы работы с коллекциями ---
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
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    // --- Не реализуемые методы для итератора и массивов ---
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}

