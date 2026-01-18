package by.it.group410972.ivanovich.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private Comparator<? super E> comparator;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<? super E>) e1).compareTo(e2);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
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

    @Override
    public boolean add(Object obj) {
        return addElement((E) obj);
    }

    @Override
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }

        int index = binarySearch((E) obj);
        if (index < 0) {
            return false; // элемент не найден
        }

        // Удаляем элемент, сдвигая остальные влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // очищаем последний элемент
        return true;
    }

    @Override
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        return binarySearch((E) obj) >= 0;
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
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        // Создаем новый массив, содержащий только те элементы, которых нет в c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (!c.contains(element)) {
                newElements[newSize++] = element;
            }
        }

        boolean modified = (newSize != size);
        elements = newElements;
        size = newSize;

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        // Создаем новый массив, содержащий только те элементы, которые есть в c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (c.contains(element)) {
                newElements[newSize++] = element;
            }
        }

        boolean modified = (newSize != size);
        elements = newElements;
        size = newSize;

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (currentIndex == 0) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(elements[currentIndex - 1]);
                currentIndex--;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /////////////////////////////////////////////////////////////////////////
    //////////////////////// Вспомогательные методы /////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private boolean addElement(E element) {
        if (element == null) {
            throw new NullPointerException("TreeSet does not permit null elements");
        }

        // Ищем место для вставки
        int index = binarySearch(element);
        if (index >= 0) {
            return false; // элемент уже существует
        }

        // Индекс для вставки (дополнение до положительного)
        int insertIndex = -(index + 1);

        // Увеличиваем емкость при необходимости
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо, освобождая место
        if (insertIndex < size) {
            System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        }

        // Вставляем новый элемент
        elements[insertIndex] = element;
        size++;
        return true;
    }

    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // безопасное деление на 2
            @SuppressWarnings("unchecked")
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // ключ найден
            }
        }
        return -(low + 1); // ключ не найден, возвращаем позицию для вставки
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    // Дополнительные методы для работы с TreeSet
    @SuppressWarnings("unchecked")
    public E first() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[0];
    }

    @SuppressWarnings("unchecked")
    public E last() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[size - 1];
    }

    @SuppressWarnings("unchecked")
    public E lower(E e) {
        int index = binarySearch(e);
        if (index < 0) {
            index = -(index + 1);
        }
        if (index > 0) {
            return (E) elements[index - 1];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public E higher(E e) {
        int index = binarySearch(e);
        if (index < 0) {
            index = -(index + 1);
        } else {
            index++;
        }
        if (index < size) {
            return (E) elements[index];
        }
        return null;
    }
}