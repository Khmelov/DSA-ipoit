package by.it.a_khmelev.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private Object[] queue;
    private int size;
    private Comparator<? super E> comparator;

    public MyPriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public MyPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;
        this.size = 0;
    }

    public MyPriorityQueue(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c instanceof MyPriorityQueue) {
            MyPriorityQueue<? extends E> pq = (MyPriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator;
            initFromCollection(pq);
        } else {
            this.comparator = null;
            initFromCollection(c);
        }
    }

    private void initFromCollection(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if (a.getClass() != Object[].class) {
            a = java.util.Arrays.copyOf(a, a.length, Object[].class);
        }

        if (c instanceof MyPriorityQueue) {
            // Уже отсортирована как куча
            queue = a;
            size = a.length;
        } else {
            // Нужно построить кучу
            queue = a;
            size = a.length;
            heapify();
        }
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
            sb.append(queue[i]);
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
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new java.util.NoSuchElementException();
        }
        return x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (queue[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(queue[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        int i = size;
        if (i >= queue.length) {
            grow(i + 1);
        }
        size = i + 1;
        if (i == 0) {
            queue[0] = element;
        } else {
            siftUp(i, element);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) queue[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new java.util.NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object element : c) {
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
        if (c == this) {
            throw new IllegalArgumentException();
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
        // Простой, но неэффективный способ
        Object[] newQueue = new Object[queue.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = (E) queue[i];
            if (!c.contains(element)) {
                newQueue[newSize++] = element;
            } else {
                modified = true;
            }
        }

        queue = newQueue;
        size = newSize;
        heapify(); // Перестроить кучу

        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        boolean modified = false;
        Object[] newQueue = new Object[queue.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = (E) queue[i];
            if (c.contains(element)) {
                newQueue[newSize++] = element;
            } else {
                modified = true;
            }
        }

        queue = newQueue;
        size = newSize;
        heapify(); // Перестроить кучу

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы кучи                 ///////
    /////////////////////////////////////////////////////////////////////////

    private void siftUp(int k, E x) {
        if (comparator != null) {
            siftUpUsingComparator(k, x);
        } else {
            siftUpComparable(k, x);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    private void siftDown(int k, E x) {
        if (comparator != null) {
            siftDownUsingComparator(k, x);
        } else {
            siftDownComparable(k, x);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1; // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                    ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0) {
                c = queue[child = right];
            }
            if (key.compareTo((E) c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                    comparator.compare((E) c, (E) queue[right]) > 0) {
                c = queue[child = right];
            }
            if (comparator.compare(x, (E) c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                (oldCapacity + 2) :
                (oldCapacity >> 1));
        // overflow-conscious code
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        queue = java.util.Arrays.copyOf(queue, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
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
                return (E) queue[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                // Удаление через итератор неэффективно для кучи
                // Упрощенная реализация
                Object[] newQueue = new Object[queue.length];
                int newSize = 0;
                for (int i = 0; i < size; i++) {
                    if (i != lastRet) {
                        newQueue[newSize++] = queue[i];
                    }
                }
                queue = newQueue;
                size = newSize;
                heapify();
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(queue, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(queue, size, a.getClass());
        }
        System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) {
            return false;
        } else {
            removeAt(i);
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (queue[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private E removeAt(int i) {
        int s = --size;
        if (s == i) {
            // Удаление последнего элемента
            queue[i] = null;
        } else {
            E moved = (E) queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
                if (queue[i] != moved) {
                    return moved;
                }
            }
        }
        return null;
    }

    // Методы интерфейса Queue, которые не требуются в задании

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Queue)) {
            return false;
        }
        // Упрощенная проверка
        Queue<?> q = (Queue<?>) o;
        if (size != q.size()) {
            return false;
        }
        // Проверка через итератор
        Iterator<E> it1 = iterator();
        Iterator<?> it2 = q.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            E e1 = it1.next();
            Object e2 = it2.next();
            if (!(e1 == null ? e2 == null : e1.equals(e2))) {
                return false;
            }
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }
}