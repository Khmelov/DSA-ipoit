package by.it.group410972.kapur.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

  private Object[] data = new Object[10];
  private int size = 0;

  // ======= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =======

  private int compare(E a, E b) {
    return ((Comparable<E>) a).compareTo(b);
  }

  private void ensureCapacity() {
    if (size >= data.length) {
      Object[] newData = new Object[data.length * 2];
      for (int i = 0; i < size; i++) {
        newData[i] = data[i];
      }
      data = newData;
    }
  }

  private int indexOf(Object o) {
    for (int i = 0; i < size; i++) {
      if (o.equals(data[i])) {
        return i;
      }
    }
    return -1;
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
    for (int i = 0; i < size; i++) {
      data[i] = null;
    }
    size = 0;
  }

  @Override
  public boolean contains(Object o) {
    return indexOf(o) >= 0;
  }

  @Override
  public boolean add(E e) {
    if (contains(e)) {
      return false;
    }

    ensureCapacity();

    int i = size - 1;
    while (i >= 0 && compare((E) data[i], e) > 0) {
      data[i + 1] = data[i];
      i--;
    }

    data[i + 1] = e;
    size++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    int idx = indexOf(o);
    if (idx < 0) {
      return false;
    }

    for (int i = idx; i < size - 1; i++) {
      data[i] = data[i + 1];
    }

    data[size - 1] = null;
    size--;
    return true;
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
    int i = 0;

    while (i < size) {
      if (!c.contains(data[i])) {
        remove(data[i]);
        modified = true;
      } else {
        i++;
      }
    }
    return modified;
  }

  // ======= toString =======

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    for (int i = 0; i < size; i++) {
      sb.append(data[i]);
      if (i < size - 1) {
        sb.append(", ");
      }
    }

    sb.append("]");
    return sb.toString();
  }

  // ======= НЕ ИСПОЛЬЗУЕТСЯ В ТЕСТЕ =======

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
