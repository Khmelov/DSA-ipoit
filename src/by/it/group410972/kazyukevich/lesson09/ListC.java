package by.it.group410972.kazyukevich.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        E[] newArr = (E[]) new Object[size + 1];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        newArr[size] = e;
        elements = newArr;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) return;
        E[] newArr = (E[]) new Object[size + 1];
        for (int i = 0; i < index; i++) {
            newArr[i] = elements[i];
        }
        newArr[index] = element;
        for (int i = index; i < size; i++) {
            newArr[i + 1] = elements[i];
        }
        elements = newArr;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.isEmpty()) return false;
        E[] newArr = (E[]) new Object[size + c.size()];
        int i = 0;
        for (; i < size; i++) {
            newArr[i] = elements[i];
        }
        for (E e : c) {
            newArr[i++] = e;
        }
        elements = newArr;
        size += c.size();
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null || c.isEmpty() || index < 0 || index > size) return false;
        E[] newArr = (E[]) new Object[size + c.size()];
        int i = 0;
        // копируем левую часть
        for (; i < index; i++) {
            newArr[i] = elements[i];
        }
        // вставляем элементы коллекции
        for (E e : c) {
            newArr[i++] = e;
        }
        // копируем оставшиеся элементы
        for (int j = index; j < size; j++) {
            newArr[i++] = elements[j];
        }
        elements = newArr;
        size += c.size();
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) return null;
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) return null;
        E removed = elements[index];
        E[] newArr = (E[]) new Object[size - 1];
        for (int i = 0; i < index; i++) {
            newArr[i] = elements[i];
        }
        for (int i = index + 1; i < size; i++) {
            newArr[i - 1] = elements[i];
        }
        elements = newArr;
        size--;
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public void clear() {
        elements = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;
        int newSize = 0;
        E[] newArr = (E[]) new Object[size];

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newArr[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            E[] resized = (E[]) new Object[newSize];
            for (int i = 0; i < newSize; i++) {
                resized[i] = newArr[i];
            }
            elements = resized;
            size = newSize;
        }

        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        E[] newArr = (E[]) new Object[size];

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newArr[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            E[] resized = (E[]) new Object[newSize];
            for (int i = 0; i < newSize; i++) {
                resized[i] = newArr[i];
            }
            elements = resized;
            size = newSize;
        }

        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> subList(int fromIndex, int toIndex) {
        ListC<E> sub = new ListC<>();
        for (int i = fromIndex; i < toIndex && i < size; i++) {
            sub.add(elements[i]);
        }
        return sub;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                return elements[cursor++];
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            int cursor = index;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                return elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                return elements[--cursor];
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
                ListC.this.remove(--cursor);
            }

            @Override
            public void set(E e) {
                elements[cursor - 1] = e;
            }

            @Override
            public void add(E e) {
                ListC.this.add(cursor++, e);
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = elements[i];
        }
        return arr;
    }

}