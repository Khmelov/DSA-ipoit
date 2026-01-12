package by.it.group410971.tishuk.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;
    private int size = 0;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            height = 1;
        }
    }

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

    private Node rebalance(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = balanceFactor(node);

        // Left heavy
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        // Right heavy
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null) size++;
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value, oldValue);
        else if (cmp > 0) node.right = put(node.right, key, value, oldValue);
        else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    @Override
    public String get(Object key) {
        Node node = root;
        while (node != null) {
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp == 0) return node.value;
            node = cmp < 0 ? node.left : node.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String remove(Object key) {
        String[] oldValue = new String[1];
        root = remove(root, (Integer) key, oldValue);
        if (oldValue[0] != null) size--;
        return oldValue[0];
    }

    private Node remove(Node node, Integer key, String[] oldValue) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key, oldValue);
        else if (cmp > 0) node.right = remove(node.right, key, oldValue);
        else {
            oldValue[0] = node.value;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node minNode = getMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key, new String[1]);
        }
        return rebalance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void inOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        inOrder(node.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;
        inOrder(node.right, sb, first);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean[] first = {true};
        inOrder(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    // --- Методы интерфейса Map, которые не обязательны для этого задания ---
    @Override
    public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
