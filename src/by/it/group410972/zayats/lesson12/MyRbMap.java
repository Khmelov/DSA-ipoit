package by.it.group410972.zayats.lesson12;


import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    public MyRbMap() {
        root = null;
        size = 0;
    }

    // --- Вспомогательные методы ---
    private boolean isRed(Node n) { return n != null && n.color == RED; }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    private void fixAfterInsert(Node x) {
        x.color = RED;
        while (x != null && x != root && isRed(x.parent)) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right;
                if (isRed(y)) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) { x = x.parent; rotateLeft(x); }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left;
                if (isRed(y)) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) { x = x.parent; rotateRight(x); }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    // --- Map методы ---
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size++;
            return null;
        }
        Node current = root, parent = null;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else { String old = current.value; current.value = value; return old; }
        }
        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) parent.left = newNode; else parent.right = newNode;
        fixAfterInsert(newNode);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = root;
        Integer k = (Integer) key;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp == 0) return n.value;
            else if (cmp < 0) n = n.left;
            else n = n.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) { return get(key) != null; }

    @Override
    public boolean containsValue(Object value) { return containsValue(root, value); }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // --- Удаление узла ---
    @Override
    public String remove(Object key) {
        Node node = root;
        Integer k = (Integer) key;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp == 0) break;
            else if (cmp < 0) node = node.left;
            else node = node.right;
        }
        if (node == null) return null;
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) { if (x != null) x.parent = y; }
            else { transplant(y, y.right); y.right = z.right; y.right.parent = y; }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == BLACK) fixAfterDelete(x, z.parent);
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node n) { while (n.left != null) n = n.left; return n; }

    private void fixAfterDelete(Node x, Node parent) {
        while ((x != root) && (x == null || x.color == BLACK)) {
            if (parent != null && x == parent.left) {
                Node w = parent.right;
                if (isRed(w)) { w.color = BLACK; parent.color = RED; rotateLeft(parent); w = parent.right; }
                if ((w.left == null || !isRed(w.left)) && (w.right == null || !isRed(w.right))) { w.color = RED; x = parent; parent = x.parent; }
                else {
                    if (w.right == null || !isRed(w.right)) { if (w.left != null) w.left.color = BLACK; w.color = RED; rotateRight(w); w = parent.right; }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    break;
                }
            } else {
                Node w = parent.left;
                if (isRed(w)) { w.color = BLACK; parent.color = RED; rotateRight(parent); w = parent.left; }
                if ((w.right == null || !isRed(w.right)) && (w.left == null || !isRed(w.left))) { w.color = RED; x = parent; parent = x.parent; }
                else {
                    if (w.left == null || !isRed(w.left)) { if (w.right != null) w.right.color = BLACK; w.color = RED; rotateLeft(w); w = parent.left; }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    break;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    // --- Очистка и размеры ---
    @Override
    public void clear() { root = null; size = 0; }
    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }

    // --- SortedMap ---
    @Override
    public Integer firstKey() { Node n = root; if (n == null) return null; while (n.left != null) n = n.left; return n.key; }
    @Override
    public Integer lastKey() { Node n = root; if (n == null) return null; while (n.right != null) n = n.right; return n.key; }
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) { MyRbMap map = new MyRbMap(); addHeadMap(root, toKey, map); return map; }
    private void addHeadMap(Node node, Integer toKey, MyRbMap map) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) { map.put(node.key, node.value); addHeadMap(node.left, toKey, map); addHeadMap(node.right, toKey, map); }
        else addHeadMap(node.left, toKey, map);
    }
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { MyRbMap map = new MyRbMap(); addTailMap(root, fromKey, map); return map; }
    private void addTailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) { map.put(node.key, node.value); addTailMap(node.left, fromKey, map); addTailMap(node.right, fromKey, map); }
        else addTailMap(node.right, fromKey, map);
    }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { MyRbMap map = new MyRbMap(); addSubMap(root, fromKey, toKey, map); return map; }
    private void addSubMap(Node node, Integer fromKey, Integer toKey, MyRbMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) map.put(node.key, node.value);
        if (node.key.compareTo(fromKey) > 0) addSubMap(node.left, fromKey, toKey, map);
        if (node.key.compareTo(toKey) < 0) addSubMap(node.right, fromKey, toKey, map);
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() { return null; }

    // --- toString ---
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toString(node.right, sb);
    }

    // --- Заглушки для Map ---
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}
