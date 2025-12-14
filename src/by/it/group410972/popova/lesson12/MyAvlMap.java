package by.it.group410972.popova.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return height(n.right) - height(n.left);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;
        x.right = y;
        y.left = T;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;
        y.left = x;
        x.right = T;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf < -1) {
            if (balanceFactor(n.left) > 0)
                n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (bf > 1) {
            if (balanceFactor(n.right) < 0)
                n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, Integer key, String value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(n.key) < 0)
            n.left = insert(n.left, key, value);
        else if (key.compareTo(n.key) > 0)
            n.right = insert(n.right, key, value);
        else
            n.value = value;
        return balance(n);
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        root = insert(root, key, value);
        return old;
    }

    private Node findMin(Node n) {
        return n.left == null ? n : findMin(n.left);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    private Node remove(Node n, Integer key) {
        if (n == null) return null;
        if (key.compareTo(n.key) < 0)
            n.left = remove(n.left, key);
        else if (key.compareTo(n.key) > 0)
            n.right = remove(n.right, key);
        else {
            size--;
            if (n.right == null) return n.left;
            Node min = findMin(n.right);
            min.right = removeMin(n.right);
            min.left = n.left;
            return balance(min);
        }
        return balance(n);
    }

    @Override
    public String remove(Object key) {
        String old = get((Integer) key);
        root = remove(root, (Integer) key);
        return old;
    }

    private String get(Node n, Integer key) {
        if (n == null) return null;
        if (key.compareTo(n.key) < 0)
            return get(n.left, key);
        else if (key.compareTo(n.key) > 0)
            return get(n.right, key);
        else
            return n.value;
    }

    @Override
    public String get(Object key) {
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove last ", "
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
