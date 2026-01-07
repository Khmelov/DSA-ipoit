package by.it.group410972.mezenova.lesson11;

import java.util.*;

class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

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

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((E) a, (E) b);
        } else {
            return ((Comparable<? super E>) a).compareTo((E) b);
        }
    }

    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("unchecked")
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }


    private int binarySearchForNull() {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (elements[mid] != null) {
                high = mid - 1;
            } else {
                while (mid > 0 && elements[mid - 1] == null) {
                    mid--;
                }
                return mid;
            }
        }
        return -(low + 1);
    }

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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return containsNull();
        }

        int index = binarySearch(o);
        return index >= 0;
    }

    private boolean containsNull() {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);

        if (e == null) {
            return addNull();
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false;
        }

        int insertionPoint = -index - 1;

        if (insertionPoint < size) {
            System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        }

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    private boolean addNull() {
        ensureCapacity(size + 1);

        for (int i = 0; i < size; i++) {
            if (elements[i] == null) {
                return false;
            }
        }

        int insertionPoint = 0;
        while (insertionPoint < size && compare(elements[insertionPoint], null) <= 0) {
            insertionPoint++;
        }

        if (insertionPoint < size) {
            System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        }

        elements[insertionPoint] = null;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return removeNull();
        }

        int index = binarySearch(o);
        if (index < 0) {
            return false;
        }

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null;
        return true;
    }

    private boolean removeNull() {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null) {
                int numMoved = size - i - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, i + 1, elements, i, numMoved);
                }
                elements[--size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
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
        if (c.isEmpty()) {
            return false;
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
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty() || size == 0) {
            return false;
        }

        Object[] newElements = new Object[elements.length];
        int newSize = 0;
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = newElements;
            size = newSize;
        }

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

        Object[] newElements = new Object[elements.length];
        int newSize = 0;
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = newElements;
            size = newSize;
        }

        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturned = -1;

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
                lastReturned = currentIndex;
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(elements[lastReturned]);
                currentIndex = lastReturned;
                lastReturned = -1;
            }
        };
    }

    // Остальные методы интерфейса Set

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
}