package by.it.a_khmelev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    
    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    // Узел односвязного списка
    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAX_CAPACITY) {
            initialCapacity = MAX_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        
        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.threshold = (int)(initialCapacity * loadFactor);
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (size > 0) {
            size = 0;
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        return addInternal(element, false);
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return removeNull();
        }
        
        int hash = hash(element);
        int index = (table.length - 1) & hash;
        
        Node<E> prev = null;
        Node<E> current = table[index];
        
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        
        return false;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return containsNull();
        }
        
        int hash = hash(element);
        int index = (table.length - 1) & hash;
        
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key);
                first = false;
                current = current.next;
            }
        }
        
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    private boolean addInternal(E element, boolean replace) {
        if (element == null) {
            return addNull(replace);
        }
        
        int hash = hash(element);
        int index = (table.length - 1) & hash;
        
        // Проверяем, существует ли уже такой элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                if (replace) {
                    // Для put-like операций заменяем значение
                    // Для HashSet просто возвращаем false
                    return false;
                }
                // Элемент уже существует
                return false;
            }
            current = current.next;
        }
        
        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(hash, element, table[index]);
        size++;
        
        if (size > threshold) {
            resize();
        }
        
        return true;
    }

    private boolean addNull(boolean replace) {
        int index = 0; // null всегда идет в bucket 0
        
        // Проверяем, существует ли null
        Node<E> current = table[index];
        while (current != null) {
            if (current.key == null) {
                if (replace) {
                    return false;
                }
                return false;
            }
            current = current.next;
        }
        
        // Добавляем null
        table[index] = new Node<>(0, null, table[index]);
        size++;
        
        if (size > threshold) {
            resize();
        }
        
        return true;
    }

    private boolean containsNull() {
        int index = 0;
        Node<E> current = table[index];
        while (current != null) {
            if (current.key == null) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private boolean removeNull() {
        int index = 0;
        Node<E> prev = null;
        Node<E> current = table[index];
        
        while (current != null) {
            if (current.key == null) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        
        return false;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        // Дополнительное перемешивание для снижения коллизий
        return h ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        
        if (oldCapacity >= MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        
        int newCapacity = oldCapacity << 1; // Удваиваем размер
        if (newCapacity > MAX_CAPACITY) {
            newCapacity = MAX_CAPACITY;
        }
        
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        threshold = (int)(newCapacity * loadFactor);
        
        // Перераспределяем элементы
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = (newCapacity - 1) & current.hash;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        
        table = newTable;
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Методы, которые можно не имплементировать        ////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private Node<E> currentNode = null;
            private Node<E> nextNode = findNextNode();
            
            private Node<E> findNextNode() {
                // Если есть текущий узел, переходим к следующему
                if (currentNode != null) {
                    currentNode = currentNode.next;
                    if (currentNode != null) {
                        return currentNode;
                    }
                }
                
                // Ищем следующий непустой bucket
                while (currentIndex < table.length) {
                    if (table[currentIndex] != null) {
                        currentNode = table[currentIndex];
                        currentIndex++;
                        return currentNode;
                    }
                    currentIndex++;
                }
                
                return null;
            }
            
            @Override
            public boolean hasNext() {
                return nextNode != null;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                E result = nextNode.key;
                nextNode = findNextNode();
                return result;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                result[index++] = current.key;
                current = current.next;
            }
        }
        
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
        }
        
        Object[] result = toArray();
        System.arraycopy(result, 0, a, 0, size);
        
        if (a.length > size) {
            a[size] = null;
        }
        
        return a;
    }

    @Override
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
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // Эти методы не требуются для задания, но нужны для интерфейса
    
    @Override
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
        int h = 0;
        for (E element : this) {
            if (element != null) {
                h += element.hashCode();
            }
        }
        return h;
    }
}