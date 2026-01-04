package by.it.group410972.zayats.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;
    private int size;

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

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // --- Вспомогательные методы AVL ---
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        // Левый тяжелый
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        // Правый тяжелый
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    // --- Map методы ---

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = insert(root, key, value);
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);

        if (key < node.key) node.left = insert(node.left, key, value);
        else if (key > node.key) node.right = insert(node.right, key, value);
        else node.value = value; // обновляем значение

        return balance(node);
    }

    @Override
    public String get(Object key) {
        Node n = root;
        Integer k = (Integer) key;
        while (n != null) {
            if (k.equals(n.key)) return n.value;
            else if (k < n.key) n = n.left;
            else n = n.right;
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
            root = delete(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    private Node delete(Node node, Integer key) {
        if (node == null) return null;

        if (key < node.key) node.left = delete(node.left, key);
        else if (key > node.key) node.right = delete(node.right, key);
        else {
            // один или ноль детей
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // два ребенка: ищем минимальный узел в правом поддереве
            Node min = node.right;
            while (min.left != null) min = min.left;

            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key);
        }
        return balance(node);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убираем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toString(node.right, sb);
    }

    // --- Не реализуемые методы Map ---
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
