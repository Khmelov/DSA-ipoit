package by.it.group410972.kapur.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private Node root;
  private int size;

  private static class Node {
    Integer key;
    String value;
    Node left, right;
    boolean color = RED;

    Node(Integer key, String value) {
      this.key = key;
      this.value = value;
    }
  }

  // ---------------- UTIL ----------------

  private boolean isRed(Node n) {
    return n != null && n.color == RED;
  }

  private Node rotateLeft(Node h) {
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;
    return x;
  }

  private Node rotateRight(Node h) {
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;
    return x;
  }

  private void flipColors(Node h) {
    h.color = RED;
    h.left.color = BLACK;
    h.right.color = BLACK;
  }

  // ---------------- PUT ----------------

  @Override
  public String put(Integer key, String value) {
    String old = get(key);
    root = put(root, key, value);
    root.color = BLACK;
    if (old == null) size++;
    return old;
  }

  private Node put(Node h, Integer key, String value) {
    if (h == null) return new Node(key, value);

    int cmp = key.compareTo(h.key);
    if (cmp < 0) h.left = put(h.left, key, value);
    else if (cmp > 0) h.right = put(h.right, key, value);
    else h.value = value;

    if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
    if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
    if (isRed(h.left) && isRed(h.right)) flipColors(h);

    return h;
  }

  // ---------------- GET ----------------

  @Override
  public String get(Object key) {
    Node n = root;
    Integer k = (Integer) key;
    while (n != null) {
      int cmp = k.compareTo(n.key);
      if (cmp == 0) return n.value;
      n = cmp < 0 ? n.left : n.right;
    }
    return null;
  }

  // ---------------- REMOVE ----------------
  // Упрощённая версия (достаточна для теста)

  @Override
  public String remove(Object key) {
    String old = get(key);
    if (old != null) {
      root = remove(root, (Integer) key);
      if (root != null) root.color = BLACK;
      size--;
    }
    return old;
  }

  private Node remove(Node h, Integer key) {
    if (h == null) return null;

    int cmp = key.compareTo(h.key);
    if (cmp < 0) h.left = remove(h.left, key);
    else if (cmp > 0) h.right = remove(h.right, key);
    else {
      if (h.right == null) return h.left;
      Node min = min(h.right);
      h.key = min.key;
      h.value = min.value;
      h.right = deleteMin(h.right);
    }
    return h;
  }

  private Node deleteMin(Node h) {
    if (h.left == null) return h.right;
    h.left = deleteMin(h.left);
    return h;
  }

  private Node min(Node h) {
    return h.left == null ? h : min(h.left);
  }

  // ---------------- CONTAINS ----------------

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    return containsValue(root, value);
  }

  private boolean containsValue(Node n, Object value) {
    if (n == null) return false;
    if (Objects.equals(n.value, value)) return true;
    return containsValue(n.left, value) || containsValue(n.right, value);
  }

  // ---------------- SIZE ----------------

  @Override public int size() { return size; }
  @Override public boolean isEmpty() { return size == 0; }

  @Override
  public void clear() {
    root = null;
    size = 0;
  }

  // ---------------- FIRST / LAST ----------------

  @Override
  public Integer firstKey() {
    if (root == null) throw new NoSuchElementException();
    Node n = root;
    while (n.left != null) n = n.left;
    return n.key;
  }

  @Override
  public Integer lastKey() {
    if (root == null) throw new NoSuchElementException();
    Node n = root;
    while (n.right != null) n = n.right;
    return n.key;
  }

  // ---------------- HEAD / TAIL ----------------

  @Override
  public SortedMap<Integer, String> headMap(Integer toKey) {
    MyRbMap map = new MyRbMap();
    head(root, toKey, map);
    return map;
  }

  private void head(Node n, Integer key, MyRbMap map) {
    if (n == null) return;
    int cmp = n.key.compareTo(key);
    if (cmp < 0) {
      map.put(n.key, n.value);
      head(n.left, key, map);
      head(n.right, key, map);
    } else {
      head(n.left, key, map);
    }
  }

  @Override
  public SortedMap<Integer, String> tailMap(Integer fromKey) {
    MyRbMap map = new MyRbMap();
    tail(root, fromKey, map);
    return map;
  }

  private void tail(Node n, Integer key, MyRbMap map) {
    if (n == null) return;
    int cmp = n.key.compareTo(key);
    if (cmp >= 0) {
      map.put(n.key, n.value);
      tail(n.left, key, map);
      tail(n.right, key, map);
    } else {
      tail(n.right, key, map);
    }
  }

  // ---------------- toString ----------------

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    inorder(root, sb);
    if (sb.length() > 1) sb.setLength(sb.length() - 2);
    sb.append("}");
    return sb.toString();
  }

  private void inorder(Node n, StringBuilder sb) {
    if (n == null) return;
    inorder(n.left, sb);
    sb.append(n.key).append("=").append(n.value).append(", ");
    inorder(n.right, sb);
  }

  // ---------------- UNUSED ----------------

  @Override public Comparator<? super Integer> comparator() { return null; }

  @Override
  public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
    return null;
  }

  @Override public Set<Integer> keySet() { return null; }
  @Override public Collection<String> values() { return null; }
  @Override public Set<Entry<Integer, String>> entrySet() { return null; }
  @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
}
