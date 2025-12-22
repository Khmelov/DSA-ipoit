package by.it.group410972.kapur.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

  private E[] elements;
  private int head = 0;
  private int tail = 0;
  private int size = 0;

  @SuppressWarnings("unchecked")
  public MyArrayDeque() {
    elements = (E[]) new Object[16];
  }

  private int capacity() {
    return elements.length;
  }

  private void grow() {
    int oldCap = capacity();
    int newCap = oldCap * 2;

    @SuppressWarnings("unchecked")
    E[] newArr = (E[]) new Object[newCap];

    for (int i = 0; i < size; i++) {
      newArr[i] = elements[(head + i) % oldCap];
    }

    elements = newArr;
    head = 0;
    tail = size;
  }

  private void ensureCapacity() {
    if (size == capacity()) grow();
  }

  private int dec(int i) {
    return (i == 0) ? capacity() - 1 : i - 1;
  }

  private int inc(int i) {
    return (i + 1) % capacity();
  }

  // ====================== toString ======================
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < size; i++) {
      sb.append(elements[(head + i) % capacity()]);
      if (i < size - 1) sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
  }

  // ====================== size ======================
  @Override
  public int size() {
    return size;
  }

  // ====================== ADD ======================
  @Override
  public boolean add(E e) {
    addLast(e);
    return true;
  }

  @Override
  public void addFirst(E e) {
    ensureCapacity();
    head = dec(head);
    elements[head] = e;
    size++;
  }

  @Override
  public void addLast(E e) {
    ensureCapacity();
    elements[tail] = e;
    tail = inc(tail);
    size++;
  }

  // ====================== GET ======================
  @Override
  public E element() {
    return getFirst();
  }

  @Override
  public E getFirst() {
    if (size == 0) throw new NoSuchElementException();
    return elements[head];
  }

  @Override
  public E getLast() {
    if (size == 0) throw new NoSuchElementException();
    return elements[dec(tail)];
  }

  // ====================== POLL ======================
  @Override
  public E poll() {
    return pollFirst();
  }

  @Override
  public E pollFirst() {
    if (size == 0) return null;
    E value = elements[head];
    elements[head] = null;
    head = inc(head);
    size--;
    return value;
  }

  @Override
  public E pollLast() {
    if (size == 0) return null;
    tail = dec(tail);
    E value = elements[tail];
    elements[tail] = null;
    size--;
    return value;
  }

  // ====================== REQUIRED FOR Deque COMPILATION ======================
  @Override public boolean offer(E e) { add(e); return true; }
  @Override public boolean offerFirst(E e) { addFirst(e); return true; }
  @Override public boolean offerLast(E e) { addLast(e); return true; }

  @Override public E remove() { return removeFirst(); }
  @Override public E removeFirst() {
    E e = pollFirst();
    if (e == null) throw new NoSuchElementException();
    return e;
  }
  @Override public E removeLast() {
    E e = pollLast();
    if (e == null) throw new NoSuchElementException();
    return e;
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

  @Override
  public boolean remove(Object o) {
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

  @Override public E peekFirst() { return size == 0 ? null : elements[head]; }
  @Override public E peekLast() { return size == 0 ? null : elements[dec(tail)]; }

  @Override
  public boolean removeFirstOccurrence(Object o) {
    return false;
  }

  @Override
  public boolean removeLastOccurrence(Object o) {
    return false;
  }

  @Override public boolean isEmpty() { return size == 0; }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {
      int i = 0;
      public boolean hasNext() { return i < size; }
      public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        return elements[(head + i++) % capacity()];
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

  @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}

