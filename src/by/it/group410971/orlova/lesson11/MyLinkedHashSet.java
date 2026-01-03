package by.it.a_khmelev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    
    // Узел для хеш-таблицы (цепочки коллизий)
    private static class HashNode<E> {
        final int hash;
        final E key;
        HashNode<E> next; // следующий в цепочке коллизий
        
        HashNode(int hash, E key, HashNode<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }
    
    // Узел для поддержания порядка добавления
    private static class LinkedNode<E> {
        final E key;
        LinkedNode<E> before; // предыдущий по порядку добавления
        LinkedNode<E> after;  // следующий по порядку добавления
        
        LinkedNode(E key) {
            this.key = key;
        }
    }
    
    private HashNode<E>[] table;
    private LinkedNode<E> head; // первый добавленный элемент
    private LinkedNode<E> tail; // последний добавленный элемент
    private int size;
    private int threshold;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
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
        this.table = (HashNode<E>[]) new HashNode[initialCapacity];
        this.threshold = (int)(initialCapacity * loadFactor);
        this.size = 0;
        this.head = null;
        this.tail = null;
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
        
        LinkedNode<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.key);
            first = false;
            current = current.after;
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
        if (size > 0) {
            size = 0;
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            head = null;
            tail = null;
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
        
        HashNode<E> prev = null;
        HashNode<E> current = table[index];
        
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                
                // Удаляем из linked-списка
                removeFromLinked(current.key);
                
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
        
        HashNode<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
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
        HashNode<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && element.equals(current.key)) {
                // Элемент уже существует
                return false;
            }
            current = current.next;
        }
        
        // Добавляем в хеш-таблицу (в начало цепочки)
        table[index] = new HashNode<>(hash, element, table[index]);
        
        // Добавляем в linked-список (в конец для сохранения порядка)
        addToLinked(element);
        
        size++;
        
        if (size > threshold) {
            resize();
        }
        
        return true;
    }

    private void addToLinked(E element) {
        LinkedNode<E> newNode = new LinkedNode<>(element);
        
        if (tail == null) {
            // Первый элемент
            head = tail = newNode;
        } else {
            // Добавляем в конец
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }
    }

    private void removeFromLinked(E element) {
        // Ищем узел в linked-списке
        LinkedNode<E> current = head;
        while (current != null) {
            if ((element == null && current.key == null) || 
                (element != null && element.equals(current.key))) {
                
                // Обновляем связи
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }
                
                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
                }
                
                // Очищаем ссылки
                current.before = null;
                current.after = null;
                break;
            }
            current = current.after;
        }
    }

    private boolean addNull(boolean replace) {
        int index = 0;
        
        // Проверяем, существует ли null
        HashNode<E> current = table[index];
        while (current != null) {
            if (current.key == null) {
                return false;
            }
            current = current.next;
        }
        
        // Добавляем null
        table[index] = new HashNode<>(0, null, table[index]);
        addToLinked(null);
        size++;
        
        if (size > threshold) {
            resize();
        }
        
        return true;
    }

    private boolean containsNull() {
        int index = 0;
        HashNode<E> current = table[index];
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
        HashNode<E> prev = null;
        HashNode<E> current = table[index];
        
        while (current != null) {
            if (current.key == null) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                
                // Удаляем из linked-списка
                removeFromLinked(null);
                
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
        return h ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        HashNode<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        
        if (oldCapacity >= MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        
        int newCapacity = oldCapacity << 1;
        if (newCapacity > MAX_CAPACITY) {
            newCapacity = MAX_CAPACITY;
        }
        
        HashNode<E>[] newTable = (HashNode<E>[]) new HashNode[newCapacity];
        threshold = (int)(newCapacity * loadFactor);
        
        // Перераспределяем элементы
        for (int i = 0; i < oldCapacity; i++) {
            HashNode<E> current = oldTable[i];
            while (current != null) {
                HashNode<E> next = current.next;
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
            private LinkedNode<E> currentNode = null;
            private LinkedNode<E> nextNode = head;
            private LinkedNode<E> lastReturned = null;
            
            @Override
            public boolean hasNext() {
                return nextNode != null;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = nextNode;
                currentNode = nextNode;
                nextNode = nextNode.after;
                return currentNode.key;
            }
            
            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                
                // Удаляем элемент
                MyLinkedHashSet.this.remove(lastReturned.key);
                lastReturned = null;
                
                // Обновляем ссылки итератора
                if (currentNode != null && currentNode.after == nextNode) {
                    nextNode = currentNode.after;
                }
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        
        LinkedNode<E> current = head;
        while (current != null) {
            result[index++] = current.key;
            current = current.after;
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (element != null) {
                h += element.hashCode();
            }
        }
        return h;
    }
}