package by.it.group410972.kazyukevich.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private E[] elements;
    private int size;
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
    private void grow() {
        int newCapacity = elements.length * 2;
        E[] newArr = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    @Override

    public boolean add(E e) {
        if (elements == null) {
            elements = (E[]) new Object[10]; // ← инициализация при первом вызове
        } else if (size == elements.length) {
            E[] newArr = (E[]) new Object[elements.length * 2]; // ← расширение
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
        elements[size++] = e;
        return true;
    }


    @Override
    public E remove(int index) {
        checkIndex(index);
        E removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (size == elements.length) grow();
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;

    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = elements[index];
        elements[index] = element;
        return old;
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
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
    }


    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return null;
    }


    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) return i;
        }
        return -1;
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
            add(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        int count = c.size();
        while (elements.length < size + count) grow();
        // сдвигаем вправо
        for (int i = size - 1; i >= index; i--) {
            elements[i + count] = elements[i];
        }
        // вставляем новые
        int i = index;
        for (E e : c) {
            elements[i++] = e;
        }
        size += count;
        return count > 0;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(elements[i]);
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArr = (T[]) new Object[size];
            for (int i = 0; i < size; i++) newArr[i] = (T) elements[i];
            return newArr;
        }
        for (int i = 0; i < size; i++) a[i] = (T) elements[i];
        if (a.length > size) a[size] = null;
        return a;
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
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
        return new Iterator<E>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[pos++];
            }
        };
    }

}
