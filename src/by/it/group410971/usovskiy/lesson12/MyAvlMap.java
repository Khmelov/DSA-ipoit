package by.it.group410971.usovskiy.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // ===== узел АВЛ-дерева =====
    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }
    }

    private Node root;
    private int size;

    // ===== служебные методы АВЛ =====
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);
        n.height = (hl > hr ? hl : hr) + 1;
    }

    private int balanceFactor(Node n) {
        return height(n.right) - height(n.left);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        if (n == null) return null;
        updateHeight(n);
        int bf = balanceFactor(n);

        if (bf == 2) {
            if (balanceFactor(n.right) < 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        if (bf == -2) {
            if (balanceFactor(n.left) > 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        return n;
    }

    private Node findMin(Node n) {
        return n.left == null ? n : findMin(n.left);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    // контейнер для старого значения при put/remove
    private static class Result {
        String oldValue;
        boolean found;
    }

    private Node putNode(Node n, int key, String value, Result r) {
        if (n == null) {
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = putNode(n.left, key, value, r);
        } else if (key > n.key) {
            n.right = putNode(n.right, key, value, r);
        } else {
            r.oldValue = n.value;
            r.found = true;
            n.value = value;
            return n;
        }
        return balance(n);
    }

    private Node removeNode(Node n, int key, Result r) {
        if (n == null) return null;

        if (key < n.key) {
            n.left = removeNode(n.left, key, r);
        } else if (key > n.key) {
            n.right = removeNode(n.right, key, r);
        } else {
            r.oldValue = n.value;
            r.found = true;

            Node left = n.left;
            Node right = n.right;
            if (right == null) return left;
            Node min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }
        return balance(n);
    }

    // ===== обязательные методы по заданию =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = { true };
        inOrder(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        inOrder(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inOrder(n.right, sb, first);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null key");
        Result r = new Result();
        root = putNode(root, key, value, r);
        if (!r.found) size++;
        return r.oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Result r = new Result();
        root = removeNode(root, k, r);
        if (r.found) size--;
        return r.oldValue;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        int k = (Integer) key;
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
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

    // ===== остальные методы Map =====

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
