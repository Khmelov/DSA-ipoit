package by.it.group410972.zayats.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private static final int INITIAL_CAPACITY = 16;

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    // Бинарный поиск. Если элемент найден, возвращает его индекс >= 0
    // Если нет, возвращает -(вставка) - 1
    private int binarySearch(E e) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -left - 1; // позиция вставки
    }

    @Override
    public boolean add(E e) {
        int idx = binarySearch(e);
        if (idx >= 0) return false; // элемент уже есть

        ensureCapacity();
        int insertPos = -idx - 1;

        // сдвигаем элементы вправо
        for (int i = size; i > insertPos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            return binarySearch(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            int idx = binarySearch(e);
            if (idx < 0) return false;

            // сдвигаем элементы влево
            for (int i = idx; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[size - 1] = null;
            size--;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
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
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
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
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            }
        }
        return changed;
    }

    // --- Не реализуемые методы для итератора и массивов ---
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}

