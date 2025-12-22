package by.it.group410972.kapur.lesson10;

import java.util.Collection;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

  private E[] heap = (E[]) new Object[16];
  private int size = 0;

  // ===================== UTILS =====================

  private int compare(E a, E b) {
    return ((Comparable<E>) a).compareTo(b);
  }

  private void ensureCapacity() {
    if (size >= heap.length) {
      E[] newHeap = (E[]) new Object[heap.length * 2];
      System.arraycopy(heap, 0, newHeap, 0, size);
      heap = newHeap;
    }
  }

  private void swap(int i, int j) {
    E tmp = heap[i];
    heap[i] = heap[j];
    heap[j] = tmp;
  }

  private void siftUp(int i) {
    while (i > 0) {
      int p = (i - 1) / 2;
      if (compare(heap[i], heap[p]) >= 0) break;
      swap(i, p);
      i = p;
    }
  }

  private void siftDown(int i) {
    while (true) {
      int l = i * 2 + 1;
      int r = i * 2 + 2;
      int m = i;

      if (l < size && compare(heap[l], heap[m]) < 0) m = l;
      if (r < size && compare(heap[r], heap[m]) < 0) m = r;

      if (m == i) break;
      swap(i, m);
      i = m;
    }
  }

  private void heapify() {
    for (int i = size / 2 - 1; i >= 0; i--) {
      siftDown(i);
    }
  }

  // ===================== REQUIRED =====================

  @Override
  public boolean add(E e) {
    if (e == null) throw new NullPointerException();
    ensureCapacity();
    heap[size] = e;
    siftUp(size++);
    return true;
  }

  @Override
  public boolean offer(E e) {
    return add(e);
  }

  @Override
  public E remove() {
    if (size == 0) throw new IllegalStateException();
    return poll();
  }

  @Override
  public E poll() {
    if (size == 0) return null;
    E res = heap[0];
    heap[0] = heap[--size];
    heap[size] = null;
    if (size > 0) siftDown(0);
    return res;
  }

  @Override
  public E element() {
    if (size == 0) throw new IllegalStateException();
    return heap[0];
  }

  @Override
  public E peek() {
    return size == 0 ? null : heap[0];
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
  public void clear() {
    for (int i = 0; i < size; i++) heap[i] = null;
    size = 0;
  }

  @Override
  public boolean contains(Object o) {
    for (int i = 0; i < size; i++) {
      if (heap[i].equals(o)) return true;
    }
    return false;
  }

  // ===================== BULK =====================

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean changed = false;
    for (E e : c) {
      add(e);
      changed = true;
    }
    return changed;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    int newSize = 0;
    for (int i = 0; i < size; i++) {
      if (!c.contains(heap[i])) {
        heap[newSize++] = heap[i];
      }
    }
    boolean changed = newSize != size;
    for (int i = newSize; i < size; i++) heap[i] = null;
    size = newSize;
    heapify();
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    int newSize = 0;
    for (int i = 0; i < size; i++) {
      if (c.contains(heap[i])) {
        heap[newSize++] = heap[i];
      }
    }
    boolean changed = newSize != size;
    for (int i = newSize; i < size; i++) heap[i] = null;
    size = newSize;
    heapify();
    return changed;
  }

  // ===================== OTHER =====================

  @Override
  public String toString() {
    if (size == 0) return "[]";
    StringBuilder sb = new StringBuilder("[");
    sb.append(heap[0]);
    for (int i = 1; i < size; i++) {
      sb.append(", ").append(heap[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  // ======= UNUSED Queue methods (not tested) =======

  @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
  @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
  @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
  @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
