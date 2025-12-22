package by.it.group410972.kapur.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

  private Node root;
  private int size;

  private static class Node {
    Integer key;
    String value;
    Node left;
    Node right;
    int height = 1;

    Node(Integer key, String value) {
      this.key = key;
      this.value = value;
    }
  }

  // ------------------- UTIL -------------------

  private int height(Node n) {
    return n == null ? 0 : n.height;
  }

  private int balance(Node n) {
    return n == null ? 0 : height(n.right) - height(n.left);
  }

  private void fixHeight(Node n) {
    int hl = height(n.left);
    int hr = height(n.right);
    n.height = Math.max(hl, hr) + 1;
  }

  private Node rotateRight(Node p) {
    Node q = p.left;
    p.left = q.right;
    q.right = p;
    fixHeight(p);
    fixHeight(q);
    return q;
  }

  private Node rotateLeft(Node q) {
    Node p = q.right;
    q.right = p.left;
    p.left = q;
    fixHeight(q);
    fixHeight(p);
    return p;
  }

  private Node balanceNode(Node n) {
    fixHeight(n);

    if (balance(n) == 2) {
      if (balance(n.right) < 0)
        n.right = rotateRight(n.right);
      return rotateLeft(n);
    }
    if (balance(n) == -2) {
      if (balance(n.left) > 0)
        n.left = rotateLeft(n.left);
      return rotateRight(n);
    }
    return n;
  }

  // ------------------- PUT -------------------

  @Override
  public String put(Integer key, String value) {
    String old = get(key);
    root = put(root, key, value);
    if (old == null) size++;
    return old;
  }

  private Node put(Node n, Integer key, String value) {
    if (n == null)
      return new Node(key, value);

    int cmp = key.compareTo(n.key);
    if (cmp < 0)
      n.left = put(n.left, key, value);
    else if (cmp > 0)
      n.right = put(n.right, key, value);
    else
      n.value = value;

    return balanceNode(n);
  }

  // ------------------- GET -------------------

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

  // ------------------- REMOVE -------------------

  @Override
  public String remove(Object key) {
    String old = get(key);
    if (old != null) {
      root = remove(root, (Integer) key);
      size--;
    }
    return old;
  }

  private Node remove(Node n, Integer key) {
    if (n == null) return null;

    int cmp = key.compareTo(n.key);
    if (cmp < 0)
      n.left = remove(n.left, key);
    else if (cmp > 0)
      n.right = remove(n.right, key);
    else {
      Node l = n.left;
      Node r = n.right;
      if (r == null) return l;
      Node min = findMin(r);
      min.right = removeMin(r);
      min.left = l;
      return balanceNode(min);
    }
    return balanceNode(n);
  }

  private Node findMin(Node n) {
    return n.left != null ? findMin(n.left) : n;
  }

  private Node removeMin(Node n) {
    if (n.left == null)
      return n.right;
    n.left = removeMin(n.left);
    return balanceNode(n);
  }

  // ------------------- CONTAINS -------------------

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  // ------------------- SIZE -------------------

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
    root = null;
    size = 0;
  }

  // ------------------- toString -------------------

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    inorder(root, sb);
    if (sb.length() > 1)
      sb.setLength(sb.length() - 2);
    sb.append("}");
    return sb.toString();
  }

  private void inorder(Node n, StringBuilder sb) {
    if (n == null) return;
    inorder(n.left, sb);
    sb.append(n.key).append("=").append(n.value).append(", ");
    inorder(n.right, sb);
  }

  // ------------------- UNUSED MAP METHODS -------------------

  @Override public boolean containsValue(Object value) { return false; }
  @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
  @Override public Set<Integer> keySet() { return null; }
  @Override public Collection<String> values() { return null; }
  @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}
