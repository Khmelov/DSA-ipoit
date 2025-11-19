package by.it.group410972.kazyukevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyTreeSet() {
        data = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= data.length) {
            E[] newData = (E[]) new Comparable[data.length * 2];
            for (int i = 0; i < size; i++) newData[i] = data[i];
            data = newData;
        }
    }

    private int findIndex(E element) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = element.compareTo(data[mid]);
            if (cmp == 0) return mid;
            else if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -left - 1; // если нет, возвращаем отрицательное место вставки
    }

    @Override
    public boolean add(E e) {
        int idx = findIndex(e);
        if (idx >= 0) return false; // уже есть
        int insertPos = -idx - 1;
        ensureCapacity();
        for (int i = size; i > insertPos; i--) data[i] = data[i - 1];
        data[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return findIndex((E) o) >= 0;
    }

    @Override
    public boolean remove(Object o) {
        int idx = findIndex((E) o);
        if (idx < 0) return false;
        for (int i = idx; i < size - 1; i++) data[i] = data[i + 1];
        data[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int index = 0;
            @Override
            public boolean hasNext() { return index < size; }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return data[index++];
            }
        };
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) while (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = data[i];
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] arr = a.length >= size ? a : (T[]) new Object[size];
        for (int i = 0; i < size; i++) arr[i] = (T) data[i];
        if (arr.length > size) arr[size] = null;
        return arr;
    }

    @Override public int hashCode() { int h=0; for (E e:data) if(e!=null) h+=e.hashCode(); return h; }
    @Override public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof Set)) return false;
        Set<?> s=(Set<?>)o;
        if(s.size()!=size) return false;
        return containsAll(s);
    }
}
