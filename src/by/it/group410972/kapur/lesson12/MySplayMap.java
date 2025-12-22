package by.it.group410972.kapur.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

  private Node root;
  private int size;

  private static class Node {
    Integer key;
    String value;
    Node left, right;

    Node(Integer key, String value) {
      this.key = key;
      this.value = value;
    }
  }

  // ================= SPLAY =================

  private Node rotateRight(Node x) {
    Node y = x.left;
    x.left = y.right;
    y.right = x;
    return y;
  }

  private Node rotateLeft(Node x) {
    Node y = x.right;
    x.right = y.left;
    y.left = x;
    return y;
  }

  private Node splay(Node root, Integer key) {
    if (root == null || key.equals(root.key))
      return root;

    int cmp = key.compareTo(root.key);

    if (cmp < 0) {
      if (root.left == null) return root;
      int cmp2 = key.compareTo(root.left.key);

      if (cmp2 < 0) {
        root.left.left = splay(root.left.left, key);
        root = rotateRight(root);
      } else if (cmp2 > 0) {
        root.left.right = splay(root.left.right, key);
        if (root.left.right != null)
          root.left = rotateLeft(root.left);
      }
      return root.left == null ? root : rotateRight(root);
    } else {
      if (root.right == null) return root;
      int cmp2 = key.compareTo(root.right.key);

      if (cmp2 > 0) {
        root.right.right = splay(root.right.right, key);
        root = rotateLeft(root);
      } else if (cmp2 < 0) {
        root.right.left = splay(root.right.left, key);
        if (root.right.left != null)
          root.right = rotateRight(root.right);
      }
      return root.right == null ? root : rotateLeft(root);
    }
  }

  // ================= PUT =================

  @Override
  public String put(Integer key, String value) {
    if (root == null) {
      root = new Node(key, value);
      size = 1;
      return null;
    }

    root = splay(root, key);
    int cmp = key.compareTo(root.key);

    if (cmp == 0) {
      String old = root.value;
      root.value = value;
      return old;
    }

    Node n = new Node(key, value);
    if (cmp < 0) {
      n.right = root;
      n.left = root.left;
      root.left = null;
    } else {
      n.left = root;
      n.right = root.right;
      root.right = null;
    }
    root = n;
    size++;
    return null;
  }

  // ================= GET =================

  @Override
  public String get(Object key) {
    if (root == null) return null;
    root = splay(root, (Integer) key);
    return root.key.equals(key) ? root.value : null;
  }

  // ================= REMOVE =================

  @Override
  public String remove(Object key) {
    if (root == null) return null;

    root = splay(root, (Integer) key);
    if (!root.key.equals(key)) return null;

    String old = root.value;
    if (root.left == null) {
      root = root.right;
    } else {
      Node right = root.right;
      root = splay(root.left, root.key);
      root.right = right;
    }
    size--;
    return old;
  }

  // ================= CONTAINS =================

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

  // ================= SIZE =================

  @Override public int size() { return size; }
  @Override public boolean isEmpty() { return size == 0; }

  @Override
  public void clear() {
    root = null;
    size = 0;
  }

  // ================= FIRST / LAST =================

  @Override
  public Integer firstKey() {
    Node n = root;
    while (n.left != null) n = n.left;
    root = splay(root, n.key);
    return n.key;
  }

  @Override
  public Integer lastKey() {
    Node n = root;
    while (n.right != null) n = n.right;
    root = splay(root, n.key);
    return n.key;
  }

  // ================= HEAD / TAIL =================

  @Override
  public NavigableMap<Integer, String> headMap(Integer toKey) {
    MySplayMap map = new MySplayMap();
    head(root, toKey, map);
    return map;
  }

  private void head(Node n, Integer key, MySplayMap map) {
    if (n == null) return;
    if (n.key.compareTo(key) < 0) {
      map.put(n.key, n.value);
      head(n.left, key, map);
      head(n.right, key, map);
    } else {
      head(n.left, key, map);
    }
  }

  @Override
  public NavigableMap<Integer, String> tailMap(Integer fromKey) {
    MySplayMap map = new MySplayMap();
    tail(root, fromKey, map);
    return map;
  }

  private void tail(Node n, Integer key, MySplayMap map) {
    if (n == null) return;
    if (n.key.compareTo(key) >= 0) {
      map.put(n.key, n.value);
      tail(n.left, key, map);
      tail(n.right, key, map);
    } else {
      tail(n.right, key, map);
    }
  }

  // ================= NAVIGATION =================

  @Override
  public Integer lowerKey(Integer key) {
    return findBound(key, false, false);
  }

  @Override
  public Integer floorKey(Integer key) {
    return findBound(key, true, false);
  }

  @Override
  public Integer ceilingKey(Integer key) {
    return findBound(key, true, true);
  }

  @Override
  public Integer higherKey(Integer key) {
    return findBound(key, false, true);
  }

  private Integer findBound(Integer key, boolean inclusive, boolean greater) {
    Node n = root;
    Integer res = null;

    while (n != null) {
      int cmp = key.compareTo(n.key);
      if (cmp == 0) {
        if (inclusive) return n.key;
        n = greater ? n.right : n.left;
      } else if (cmp < 0) {
        if (greater) res = n.key;
        n = n.left;
      } else {
        if (!greater) res = n.key;
        n = n.right;
      }
    }
    return res;
  }

  // ================= toString =================

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

  // ================= UNUSED =================

  @Override public Comparator<? super Integer> comparator() { return null; }
  @Override public Set<Integer> keySet() { return null; }
  @Override public Collection<String> values() { return null; }
  @Override public Set<Entry<Integer, String>> entrySet() { return null; }
  @Override public void putAll(Map<? extends Integer, ? extends String> m) {}

  @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
  @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
  @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
  @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
  @Override public Entry<Integer, String> firstEntry() { return null; }
  @Override public Entry<Integer, String> lastEntry() { return null; }
  @Override public Entry<Integer, String> pollFirstEntry() { return null; }
  @Override public Entry<Integer, String> pollLastEntry() { return null; }
  @Override public NavigableMap<Integer, String> descendingMap() { return null; }
  @Override public NavigableSet<Integer> navigableKeySet() { return null; }
  @Override public NavigableSet<Integer> descendingKeySet() { return null; }
  @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
  @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
  @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
  @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
}
