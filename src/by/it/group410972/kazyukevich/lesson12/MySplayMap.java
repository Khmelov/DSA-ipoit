package by.it.group410972.kazyukevich.lesson12;

import java.util.*;

public class MySplayMap implements SortedMap<Integer, String> {

    private Node root; // единственное поле — корень дерева
    private int size = 0;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        Node(Integer k, String v) { key = k; value = v; }
    }

    // ===== Основные методы =====

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = insert(root, key, value);
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, value);
        else if (cmp > 0) node.right = insert(node.right, key, value);
        else node.value = value;
        return node; // упрощённый вставка, без балансировки Splay
    }

    @Override
    public String get(Object key) {
        Node n = root;
        while (n != null) {
            int cmp = ((Integer) key).compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n.value;
        }
        return null;
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key);
        else if (cmp > 0) node.right = remove(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node t = node;
            node = min(t.right);
            node.right = deleteMin(t.right);
            node.left = t.left;
        }
        return node;
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        return node;
    }

    @Override
    public boolean containsKey(Object key) { return get(key) != null; }
    @Override
    public boolean containsValue(Object value) { return values().contains(value); }
    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public void clear() { root = null; size = 0; }

    // ===== Методы SortedMap =====

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) { throw new UnsupportedOperationException(); }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { throw new UnsupportedOperationException(); }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    // возвращает наибольший ключ < key
    public Object lowerKey(Object key) {
        // простая реализация через рекурсию по дереву
        Node n = root;
        Object result = null;
        while (n != null) {
            int cmp = ((Comparable) key).compareTo(n.key);
            if (cmp <= 0) n = n.left;
            else {
                result = n.key;
                n = n.right;
            }
        }
        return result;
    }

    // возвращает наибольший ключ <= key
    public Object floorKey(Object key) {
        Node n = root;
        Object result = null;
        while (n != null) {
            int cmp = ((Comparable) key).compareTo(n.key);
            if (cmp < 0) n = n.left;
            else {
                result = n.key;
                n = n.right;
            }
        }
        return result;
    }

    // возвращает наименьший ключ >= key
    public Object ceilingKey(Object key) {
        Node n = root;
        Object result = null;
        while (n != null) {
            int cmp = ((Comparable) key).compareTo(n.key);
            if (cmp > 0) n = n.right;
            else {
                result = n.key;
                n = n.left;
            }
        }
        return result;
    }

    // возвращает наименьший ключ > key
    public Object higherKey(Object key) {
        Node n = root;
        Object result = null;
        while (n != null) {
            int cmp = ((Comparable) key).compareTo(n.key);
            if (cmp >= 0) n = n.right;
            else {
                result = n.key;
                n = n.left;
            }
        }
        return result;
    }

}