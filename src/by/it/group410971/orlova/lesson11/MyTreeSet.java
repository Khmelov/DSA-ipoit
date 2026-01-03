package by.it.a_khmelev.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private Object[] elements;
    private int size;
    private Comparator<? super E> comparator;

    public MyTreeSet() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public MyTreeSet(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyTreeSet(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    public MyTreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
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
    @SuppressWarnings("unchecked")
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        
        // Поиск позиции для вставки
        int index = binarySearch(element);
        
        if (index >= 0) {
            // Элемент уже существует
            return false;
        }
        
        // Индекс для вставки (преобразуем отрицательный результат binarySearch)
        int insertIndex = -(index + 1);
        
        // Проверяем необходимость расширения массива
        ensureCapacity(size + 1);
        
        // Сдвигаем элементы вправо, чтобы освободить место
        if (insertIndex < size) {
            System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        }
        
        // Вставляем элемент
        elements[insertIndex] = element;
        size++;
        
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }
        
        int index = binarySearch((E) element);
        if (index < 0) {
            // Элемент не найден
            return false;
        }
        
        // Удаляем элемент, сдвигая остальные влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        
        elements[--size] = null; // Очищаем последний элемент
        
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }
        
        return binarySearch((E) element) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        
        for (Object element : c) {
            if (element == null) {
                throw new NullPointerException();
            }
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        
        boolean modified = false;
        // Создаем новый массив с элементами, которые нужно оставить
        Object[] newElements = new Object[elements.length];
        int newSize = 0;
        
        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            if (!c.contains(element)) {
                newElements[newSize++] = element;
            } else {
                modified = true;
            }
        }
        
        elements = newElements;
        size = newSize;
        
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        
        boolean modified = false;
        // Создаем новый массив только с элементами из коллекции c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;
        
        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            if (c.contains(element)) {
                newElements[newSize++] = element;
            } else {
                modified = true;
            }
        }
        
        elements = newElements;
        size = newSize;
        
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;
        
        while (low <= high) {
            int mid = (low + high) >>> 1;
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
        
        // ключ не найден, возвращаем -(insertion point) - 1
        return -(low + 1);
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) e1;
            return comparable.compareTo(e2);
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int oldCapacity = elements.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1); // Увеличиваем на 50%
            
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            
            if (newCapacity > MAX_ARRAY_SIZE) {
                newCapacity = hugeCapacity(minCapacity);
            }
            
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Методы, которые можно не имплементировать        ////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                
                try {
                    MyTreeSet.this.remove(elements[lastRet]);
                    cursor = lastRet;
                    lastRet = -1;
                } catch (IndexOutOfBoundsException e) {
                    throw new java.util.ConcurrentModificationException();
                }
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

    // Эти методы не требуются для задания, но нужны для интерфейса
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        
        Set<?> s = (Set<?>) o;
        if (s.size() != size()) {
            return false;
        }
        
        return containsAll(s);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            Object element = elements[i];
            if (element != null) {
                hashCode += element.hashCode();
            }
        }
        return hashCode;
    }
    
    // Дополнительные методы для TreeSet
    
    @SuppressWarnings("unchecked")
    public E first() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[0];
    }
    
    @SuppressWarnings("unchecked")
    public E last() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[size - 1];
    }
    
    @SuppressWarnings("unchecked")
    public E lower(E e) {
        int index = binarySearch(e);
        if (index < 0) {
            index = -index - 1;
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
            index = -index - 1;
        } else {
            index++; // Найденный элемент не подходит, нужен следующий
        }
        if (index < size) {
            return (E) elements[index];
        }
        return null;
    }
}