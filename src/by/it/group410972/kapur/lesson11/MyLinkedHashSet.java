package by.it.group410972.kapur.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

  private static final int DEFAULT_CAPACITY = 16;

  private Node<E>[] table;
  private int size;

  private Node<E> head;
  private Node<E> tail;

  @SuppressWarnings("unchecked")
  public MyLinkedHashSet() {
    table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
  }

  // ======= УЗЕЛ =======
  private static class Node<E> {
    E value;
    Node<E> bucketNext;
    Node<E> orderNext;

    Node(E value) {
      this.value = value;
    }
  }

  private int index(Object o) {
    if (o == null) return 0;
    return (o.hashCode() & 0x7fffffff) % table.length;
  }

  // ======= БАЗОВЫЕ МЕТОДЫ =======

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void clear() {
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
    head = tail = null;
    size = 0;
  }

  @Override
  public boolean contains(Object o) {
    int idx = index(o);
    Node<E> current = table[idx];
    while (current != null) {
      if (o == null ? current.value == null : o.equals(current.value)) {
        return true;
      }
      current = current.bucketNext;
    }
    return false;
  }

  @Override
  public boolean add(E e) {
    int idx = index(e);
    Node<E> current = table[idx];

    while (current != null) {
      if (e == null ? current.value == null : e.equals(current.value)) {
        return false;
      }
      current = current.bucketNext;
    }

    Node<E> node = new Node<>(e);

    // вставка в хеш-таблицу
    node.bucketNext = table[idx];
    table[idx] = node;

    // вставка в порядок добавления
    if (head == null) {
      head = tail = node;
    } else {
      tail.orderNext = node;
      tail = node;
    }

    size++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    int idx = index(o);
    Node<E> current = table[idx];
    Node<E> prev = null;

    while (current != null) {
      if (o == null ? current.value == null : o.equals(current.value)) {

        // удаление из бакета
        if (prev == null) {
          table[idx] = current.bucketNext;
        } else {
          prev.bucketNext = current.bucketNext;
        }

        // удаление из порядка
        removeFromOrder(current);

        size--;
        return true;
      }
      prev = current;
      current = current.bucketNext;
    }
    return false;
  }

  private void removeFromOrder(Node<E> target) {
    if (head == target) {
      head = head.orderNext;
      if (tail == target) tail = null;
      return;
    }

    Node<E> prev = head;
    while (prev != null && prev.orderNext != target) {
      prev = prev.orderNext;
    }

    if (prev != null) {
      prev.orderNext = target.orderNext;
      if (tail == target) {
        tail = prev;
      }
    }
  }

  // ======= BULK-ОПЕРАЦИИ =======

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E e : c) {
      if (add(e)) modified = true;
    }
    return modified;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean modified = false;
    for (Object o : c) {
      if (remove(o)) modified = true;
    }
    return modified;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean modified = false;
    Node<E> current = head;

    while (current != null) {
      Node<E> next = current.orderNext;
      if (!c.contains(current.value)) {
        remove(current.value);
        modified = true;
      }
      current = next;
    }
    return modified;
  }

  // ======= toString =======

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    Node<E> current = head;
    while (current != null) {
      sb.append(current.value);
      current = current.orderNext;
      if (current != null) {
        sb.append(", ");
      }
    }

    sb.append("]");
    return sb.toString();
  }

  // ======= НЕ ИСПОЛЬЗУЮТСЯ В ТЕСТЕ =======

  @Override
  public Iterator<E> iterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    throw new UnsupportedOperationException();
  }
}
