package by.it.group410972.kapur.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

  private static final int DEFAULT_CAPACITY = 16;

  private Node<E>[] table;
  private int size;

  @SuppressWarnings("unchecked")
  public MyHashSet() {
    table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
  }

  // ======= ВНУТРЕННИЙ УЗЕЛ =======
  private static class Node<E> {
    E value;
    Node<E> next;

    Node(E value, Node<E> next) {
      this.value = value;
      this.next = next;
    }
  }

  // ======= ВСПОМОГАТЕЛЬНЫЙ МЕТОД =======
  private int index(Object o) {
    if (o == null) return 0;
    return (o.hashCode() & 0x7fffffff) % table.length;
  }

  // ======= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =======

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
      current = current.next;
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
      current = current.next;
    }

    table[idx] = new Node<>(e, table[idx]);
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
        if (prev == null) {
          table[idx] = current.next;
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

  // ======= toString =======
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    boolean first = true;
    for (Node<E> bucket : table) {
      Node<E> current = bucket;
      while (current != null) {
        if (!first) {
          sb.append(", ");
        }
        sb.append(current.value);
        first = false;
        current = current.next;
      }
    }

    sb.append("]");
    return sb.toString();
  }

  // ======= НЕОБЯЗАТЕЛЬНЫЕ (минимальные заглушки) =======

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

  @Override
  public boolean containsAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }
}
