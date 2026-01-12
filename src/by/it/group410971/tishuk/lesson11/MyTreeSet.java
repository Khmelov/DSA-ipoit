package by.it.group410971.tishuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private static final int INITIAL_CAPACITY = 16;
    private E[] elements;
    private int size = 0;

    public MyTreeSet() {
        elements = (E[]) new Comparable[INITIAL_CAPACITY];
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newArray = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }

    private int findIndex(E e) {
        // бинарный поиск: возвращает индекс элемента или -1 если нет
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -1; // не найден
    }

    private int findInsertIndex(E e) {
        // возвращает индекс, куда вставлять, чтобы сохранить сортировку
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(elements[mid]);
            if (cmp == 0) return mid; // элемент уже есть
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return left;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (contains(e)) return false;

        ensureCapacity();
        int idx = findInsertIndex(e);

        // сдвигаем элементы вправо
        for (int i = size; i > idx; i--) {
            elements[i] = elements[i - 1];
        }
        elements[idx] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Comparable)) return false;
        E e = (E) o;
        int idx = findIndex(e);
        if (idx == -1) return false;

        // сдвигаем элементы влево
        for (int i = idx; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) return false;
        E e = (E) o;
        return findIndex(e) != -1;
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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
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

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[index++];
            }
        };
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

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        return a;
    }
}
