package by.it.group410972.Pushkarev.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

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

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        inorderAdd(root, map, true, toKey);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        inorderAdd(root, map, false, fromKey);
        return map;
    }

    private void inorderAdd(Node node, MyRbMap map, boolean isHeadMap, Integer keyBound) {
        if (node == null) return;
        inorderAdd(node.left, map, isHeadMap, keyBound);
        if (isHeadMap) {
            if (node.key.compareTo(keyBound) < 0) map.put(node.key, node.value);
        } else {
            if (node.key.compareTo(keyBound) >= 0) map.put(node.key, node.value);
        }
        inorderAdd(node.right, map, isHeadMap, keyBound);
    }


    @Override
    public Integer firstKey() {
        return root == null ? null : minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        return root == null ? null : maxNode(root).key;
    }

    private Node minNode(Node node) { while (node.left != null) node = node.left; return node; }
    private Node maxNode(Node node) { while (node.right != null) node = node.right; return node; }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null) r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null) root = r;
            else if (p.parent.left == p) p.parent.left = r;
            else p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null) root = l;
            else if (p.parent.right == p) p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    private static boolean colorOf(Node n) { return n == null ? BLACK : n.color; }
    private static Node parentOf(Node n) { return n == null ? null : n.parent; }
    private static void setColor(Node n, boolean c) { if (n != null) n.color = c; }
    private static Node leftOf(Node n) { return n == null ? null : n.left; }
    private static Node rightOf(Node n) { return n == null ? null : n.right; }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size++;
            return null;
        }

        Node parent = null;
        Node cur = root;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                return old;
            }
            cur = cmp < 0 ? cur.left : cur.right;
        }

        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;

        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = root;
        while (node != null) {
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp == 0) break;
            node = cmp < 0 ? node.left : node.right;
        }
        if (node == null) return null;

        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    private void deleteNode(Node p) {
        // Простейшая реализация без балансировки для начала
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) root = replacement;
            else if (p == p.parent.left) p.parent.left = replacement;
            else p.parent.right = replacement;
            p.left = p.right = p.parent = null;
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p == p.parent.left) p.parent.left = null;
            else if (p == p.parent.right) p.parent.right = null;
            p.parent = null;
        }
    }

    private Node successor(Node t) {
        if (t == null) return null;
        else if (t.right != null) {
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void inorder(Node node, StringBuilder sb) {
        if (node != null) {
            inorder(node.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(node.key).append("=").append(node.value);
            inorder(node.right, sb);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}
