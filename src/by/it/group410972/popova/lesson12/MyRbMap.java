package by.it.group410972.popova.lesson12;

import java.util.Map;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;


public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

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

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    private boolean isRed(Node n) { return n != null && n.color == RED; }
    private boolean isBlack(Node n) { return n == null || n.color == BLACK; }

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

    private void fixInsert(Node z) {
        while (isRed(z.parent)) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (isRed(y)) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (isRed(y)) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String put(Integer key, String value) {
        Node parent = null;
        Node current = root;
        while (current != null) {
            parent = current;
            if (key.compareTo(current.key) < 0) current = current.left;
            else if (key.compareTo(current.key) > 0) current = current.right;
            else {
                String old = current.value;
                current.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value, RED, parent);
        if (parent == null) root = newNode;
        else if (key.compareTo(parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        fixInsert(newNode);
        size++;
        return null;
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
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node n, String value) {
        if (n == null) return false;
        if (value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public String remove(Object key) {
        Node n = root;
        Node parent = null;
        while (n != null) {
            int cmp = ((Integer) key).compareTo(n.key);
            if (cmp < 0) { parent = n; n = n.left; }
            else if (cmp > 0) { parent = n; n = n.right; }
            else break;
        }
        if (n == null) return null;
        String old = n.value;
        // простой случай: удаление листа
        if (n.left == null && n.right == null) {
            if (parent == null) root = null;
            else if (parent.left == n) parent.left = null;
            else parent.right = null;
        }
        // остальные случаи можно доработать
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

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
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    @Override
    public Integer firstKey() {
        Node n = root;
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = root;
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        collectHead(root, sub, toKey);
        return sub;
    }

    private void collectHead(Node n, MyRbMap sub, Integer toKey) {
        if (n == null) return;
        collectHead(n.left, sub, toKey);
        if (n.key < toKey) sub.put(n.key, n.value);
        collectHead(n.right, sub, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        collectTail(root, sub, fromKey);
        return sub;
    }

    private void collectTail(Node n, MyRbMap sub, Integer fromKey) {
        if (n == null) return;
        collectTail(n.left, sub, fromKey);
        if (n.key >= fromKey) sub.put(n.key, n.value);
        collectTail(n.right, sub, fromKey);
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
