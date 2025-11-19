package by.it.group410972.kazyukevich.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyAvlMap implements Map<Integer, String> {

    private class Node {
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

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private Node balance(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int bf = balanceFactor(node);

        if (bf > 1) { // left heavy
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1) { // right heavy
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = put(root, key, value);
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);
        if (key < node.key) node.left = put(node.left, key, value);
        else if (key > node.key) node.right = put(node.right, key, value);
        else node.value = value;
        return balance(node);
    }

    @Override
    public String get(Object key) {
        Node node = root;
        while (node != null) {
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;
        if (key < node.key) node.left = remove(node.left, key);
        else if (key > node.key) node.right = remove(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = node.right;
            while (min.left != null) min = min.left;
            node.key = min.key;
            node.value = min.value;
            node.right = remove(node.right, min.key);
        }
        return balance(node);
    }

    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public void clear() { root = null; size = 0; }

    private void toString(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        toString(node.left, sb, first);
        if (!first[0]) sb.append(", "); else first[0] = false;
        sb.append(node.key).append("=").append(node.value);
        toString(node.right, sb, first);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }

    // Остальные методы интерфейса Map можно оставить с UnsupportedOperationException
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
