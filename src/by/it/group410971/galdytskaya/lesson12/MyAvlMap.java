package by.it.group410971.galdytskaya.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Iterator;

@SuppressWarnings("unchecked")
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

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
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
        if (node == null) return null;

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = balanceFactor(node);

        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        } else if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }
        return node;
    }

    private Node put(Node node, Integer key, String value, Holder<String> oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            oldValue.value = node.value;
            node.value = value;
            return node;
        } else if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else {
            node.right = put(node.right, key, value, oldValue);
        }
        return balance(node);
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node remove(Node node, Integer key, Holder<String> oldValue, Holder<Boolean> removed) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, oldValue, removed);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, oldValue, removed);
        } else {
            removed.value = true;
            oldValue.value = node.value;
            size--;
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key, new Holder<>(), new Holder<>());
            }
        }
        return balance(node);
    }

    private String get(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return node.value;
            else if (cmp < 0) node = node.left;
            else node = node.right;
        }
        return null;
    }

    private boolean containsKey(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return true;
            else if (cmp < 0) node = node.left;
            else node = node.right;
        }
        return false;
    }

    private void inOrderTraverse(Node node, StringBuilder sb, boolean first) {
        if (node == null) return;
        inOrderTraverse(node.left, sb, first);
        if (!first && sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inOrderTraverse(node.right, sb, false);
    }

    private static class Holder<T> {
        T value;
        Holder() {}
        Holder(T val) { this.value = val; }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringInOrder(root, sb, new boolean[]{false});
        sb.append("}");
        return sb.toString();
    }

    private void toStringInOrder(Node node, StringBuilder sb, boolean[] isFirst) {
        if (node == null) return;
        toStringInOrder(node.left, sb, isFirst);
        if (isFirst[0]) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        isFirst[0] = true;
        toStringInOrder(node.right, sb, isFirst);
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Holder<String> oldValue = new Holder<>();
        root = put(root, key, value, oldValue);
        return oldValue.value;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Holder<String> oldValue = new Holder<>();
        Holder<Boolean> removed = new Holder<>(false);
        root = remove(root, (Integer) key, oldValue, removed);
        return removed.value ? oldValue.value : null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey(root, (Integer) key);
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

    @Override public void putAll(Map<? extends Integer, ? extends String> m) { for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());}
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException();}
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException();}
    @Override public Collection<String> values() { throw new UnsupportedOperationException();}
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException();}
}
