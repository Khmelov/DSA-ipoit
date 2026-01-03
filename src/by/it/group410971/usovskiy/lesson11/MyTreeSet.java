package by.it.group410971.usovskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Отсортированный массив элементов
    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Comparable[16];
        size = 0;
    }

    // ====================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ======================

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (elements.length >= minCapacity) return;
        int newCap = elements.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        E[] newArr = (E[]) new Comparable[newCap];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    /** Бинарный поиск. Если элемент найден — индекс [0..size-1].
     *  Если нет — отрицательная позиция для вставки: -(pos + 1). */
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = elements[mid];
            int cmp = key.compareTo(midVal);
            if (cmp > 0) {
                low = mid + 1;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    private void removeAt(int index) {
        if (index < 0 || index >= size) return;
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    // ====================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ======================

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // add(Object)
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int idx = binarySearch(e);
        if (idx >= 0) return false;           // уже есть
        int ins = -idx - 1;                   // место вставки
        ensureCapacity(size + 1);
        for (int i = size; i > ins; i--) {    // сдвиг вправо
            elements[i] = elements[i - 1];
        }
        elements[ins] = e;
        size++;
        return true;
    }

    // remove(Object)
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) return false;
        E e;
        try {
            e = (E) o;
        } catch (ClassCastException ex) {
            return false;
        }
        int idx = binarySearch(e);
        if (idx < 0) return false;
        removeAt(idx);
        return true;
    }

    // contains(Object)
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (o == null) return false;
        E e;
        try {
            e = (E) o;
        } catch (ClassCastException ex) {
            return false;
        }
        return binarySearch(e) >= 0;
    }

    // ====================== bulk-операции ======================

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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (c.contains(e)) {
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
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    // ====================== Iterator и прочее Set ======================

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                MyTreeSet.this.removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        for (int i = 0; i < size; i++) {
            a[i] = elements[i];
        }
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Class<?> comp = a.getClass().getComponentType();
            T[] newArr = (T[]) java.lang.reflect.Array
                    .newInstance(comp, size);
            for (int i = 0; i < size; i++) {
                newArr[i] = (T) elements[i];
            }
            return newArr;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) a[size] = null;
        return a;
    }

}
