package by.it.group410971.ratkevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private Object[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            Object[] newHeap = new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        // Пытаемся сравнить как Comparable
        try {
            Comparable<Object> comparableA = (Comparable<Object>) a;
            return comparableA.compareTo(b);
        } catch (ClassCastException e) {
            // Если элементы не Comparable, используем hashCode для порядка
            return Integer.compare(a.hashCode(), b.hashCode());
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }

            // Меняем местами
            Object temp = heap[index];
            heap[index] = heap[parent];
            heap[parent] = temp;
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;

            if (right < size && compare(heap[right], heap[left]) < 0) {
                smallest = right;
            }

            if (compare(heap[index], heap[smallest]) <= 0) {
                break;
            }

            Object temp = heap[index];
            heap[index] = heap[smallest];
            heap[smallest] = temp;
            index = smallest;
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

        // НЕ СОРТИРУЕМ! Выводим в порядке кучи (как PriorityQueue)
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
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
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            return false;
        }
        ensureCapacity();
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = (E) heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (size == 0) {
            return null;
        }
        return (E) heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        for (E item : c) {
            if (offer(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty() || size == 0) {
            return false;
        }

        boolean modified = false;
        int newSize = 0;

        // Фильтруем элементы
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся позиции
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        // Перестраиваем кучу
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null || size == 0) {
            return false;
        }

        boolean modified = false;
        int newSize = 0;

        // Фильтруем элементы
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся позиции
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        // Перестраиваем кучу
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы (заглушки)                 ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        // Находим индекс элемента
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        // Заменяем удаляемый элемент последним
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        // Восстанавливаем свойства кучи
        if (index < size) {
            siftUp(index);
            siftDown(index);
        }

        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    return null;
                }
                return (E) heap[current++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) toArray();
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}