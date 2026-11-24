package by.it.group510971.rudko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

//Множество на основе отсортированного массива (элементы по возрастанию, поиск бинарный).
//Элементы должны быть Comparable.
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
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
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int pos = lowerBound(e);
        if (pos < size && compare(data[pos], e) == 0) return false;
        ensureCapacity();
        System.arraycopy(data, pos, data, pos + 1, size - pos);
        data[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int pos = findPos(o);
        if (pos < 0) return false;
        System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        data[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return o != null && findPos(o) >= 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
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
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size
                ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) data[i];
        }
        if (result.length > size) result[size] = null;
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public E next() {
                if (pos >= size) throw new NoSuchElementException();
                return data[pos++];
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    //первая позиция, на которой элемент >= e
    private int lowerBound(Object e) {
        int lo = 0, hi = size;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (compare(data[mid], e) < 0) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    //позиция элемента o или -1
    private int findPos(Object o) {
        int pos = lowerBound(o);
        return (pos < size && compare(data[pos], o) == 0) ? pos : -1;
    }

    //удаляет элементы, которые есть в c (inCollection = true) или которых нет в c (false), за O(n)
    private boolean bulkRemove(Collection<?> c, boolean inCollection) {
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(data[r]) != inCollection) {
                data[w++] = data[r];
            }
        }
        boolean changed = w != size;
        for (int k = w; k < size; k++) {
            data[k] = null;
        }
        size = w;
        return changed;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < data.length) return;
        E[] bigger = (E[]) new Object[data.length * 2];
        System.arraycopy(data, 0, bigger, 0, size);
        data = bigger;
    }
}
