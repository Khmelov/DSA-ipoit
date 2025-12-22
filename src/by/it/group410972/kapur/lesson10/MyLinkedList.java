package by.it.group410972.kapur.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

  private static class Node<E> {
    E item;
    Node<E> prev;
    Node<E> next;

    Node(E item, Node<E> prev, Node<E> next) {
      this.item = item;
      this.prev = prev;
      this.next = next;
    }
  }

  private Node<E> first;
  private Node<E> last;
  private int size = 0;

  // ====================== BASIC HELPERS ======================
  private void linkFirst(E e) {
    Node<E> f = first;
    Node<E> newNode = new Node<>(e, null, f);
    first = newNode;
    if (f == null)
      last = newNode;
    else
      f.prev = newNode;
    size++;
  }

  private void linkLast(E e) {
    Node<E> l = last;
    Node<E> newNode = new Node<>(e, l, null);
    last = newNode;
    if (l == null)
      first = newNode;
    else
      l.next = newNode;
    size++;
  }

  private E unlink(Node<E> node) {
    Node<E> prev = node.prev;
    Node<E> next = node.next;

    if (prev == null)
      first = next;
    else
      prev.next = next;

    if (next == null)
      last = prev;
    else
      next.prev = prev;

    size--;
    return node.item;
  }

  private Node<E> node(int index) {
    if (index < 0 || index >= size)
      throw new IndexOutOfBoundsException();

    Node<E> x;
    if (index < (size >> 1)) {
      x = first;
      for (int i = 0; i < index; i++) x = x.next;
    } else {
      x = last;
      for (int i = size - 1; i > index; i--) x = x.prev;
    }
    return x;
  }

  // ====================== REQUIRED METHODS ======================

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    Node<E> x = first;
    while (x != null) {
      sb.append(x.item);
      x = x.next;
      if (x != null) sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
  }

  @Override
  public boolean add(E e) {
    addLast(e);
    return true;
  }

  public E remove(int index) {
    return unlink(node(index));
  }

  @Override
  public boolean remove(Object o) {
    Node<E> x = first;
    if (o == null) {
      while (x != null) {
        if (x.item == null) {
          unlink(x);
          return true;
        }
        x = x.next;
      }
    } else {
      while (x != null) {
        if (o.equals(x.item)) {
          unlink(x);
          return true;
        }
        x = x.next;
      }
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  // -------- addFirst / addLast --------
  @Override
  public void addFirst(E e) {
    linkFirst(e);
  }

  @Override
  public void addLast(E e) {
    linkLast(e);
  }

  // -------- get / element --------
  @Override
  public E element() {
    return getFirst();
  }

  @Override
  public E getFirst() {
    if (first == null) throw new NoSuchElementException();
    return first.item;
  }

  @Override
  public E getLast() {
    if (last == null) throw new NoSuchElementException();
    return last.item;
  }

  // -------- poll --------
  @Override
  public E poll() {
    return pollFirst();
  }

  @Override
  public E pollFirst() {
    if (first == null) return null;
    return unlink(first);
  }

  @Override
  public E pollLast() {
    if (last == null) return null;
    return unlink(last);
  }

  // ====================== UNUSED BUT MUST EXIST FOR Deque ======================
  @Override public boolean offer(E e) { return add(e); }
  @Override public boolean offerFirst(E e) { addFirst(e); return true; }
  @Override public boolean offerLast(E e) { addLast(e); return true; }

  @Override public E remove() { return removeFirst(); }
  @Override public E removeFirst() { return getFirstAfterCheck(); }
  @Override public E removeLast() { return getLastAfterCheck(); }

  private E getFirstAfterCheck() {
    if (first == null) throw new NoSuchElementException();
    return unlink(first);
  }

  private E getLastAfterCheck() {
    if (last == null) throw new NoSuchElementException();
    return unlink(last);
  }

  @Override public E peek() { return peekFirst(); }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {

  }

  @Override
  public void push(E e) {

  }

  @Override
  public E pop() {
    return null;
  }

  @Override public E peekFirst() { return first == null ? null : first.item; }
  @Override public E peekLast() { return last == null ? null : last.item; }

  @Override
  public boolean removeFirstOccurrence(Object o) {
    return false;
  }

  @Override
  public boolean removeLastOccurrence(Object o) {
    return false;
  }

  @Override public boolean isEmpty() { return size == 0; }

  @Override public Iterator<E> iterator() {
    return new Iterator<>() {
      Node<E> cur = first;
      public boolean hasNext() { return cur != null; }
      public E next() {
        if (cur == null) throw new NoSuchElementException();
        E v = cur.item;
        cur = cur.next;
        return v;
      }
    };
  }

  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return null;
  }

  // остальные методы Deque можно оставить пустыми/unsupported,
  // тест их не вызывает
  @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
