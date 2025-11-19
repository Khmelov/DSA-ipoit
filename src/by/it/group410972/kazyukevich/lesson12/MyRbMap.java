package by.it.group410972.kazyukevich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color = RED;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    // ---------------- Map methods ----------------
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            root.color = BLACK;
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                String old = current.value;
                current.value = value;
                return old;
            }
        }

        Node node = new Node(key, value, parent);
        if (cmp < 0) parent.left = node;
        else parent.right = node;

        fixInsert(node);
        size++;
        return null;
    }

    private void fixInsert(Node x) {
        while (x != root && x.parent.color == RED) {
            Node parent = x.parent;
            Node grand = parent.parent;
            if (grand == null) break; // root's parent null
            if (parent == grand.left) {
                Node uncle = grand.right;
                if (uncle != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;
                    x = grand;
                } else {
                    if (x == parent.right) {
                        x = parent;
                        rotateLeft(x);
                        parent = x.parent;
                        grand = parent.parent;
                    }
                    parent.color = BLACK;
                    grand.color = RED;
                    rotateRight(grand);
                }
            } else {
                Node uncle = grand.left;
                if (uncle != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;
                    x = grand;
                } else {
                    if (x == parent.left) {
                        x = parent;
                        rotateRight(x);
                        parent = x.parent;
                        grand = parent.parent;
                    }
                    parent.color = BLACK;
                    grand.color = RED;
                    rotateLeft(grand);
                }
            }
        }
        root.color = BLACK;
    }

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

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
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
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public String toString() {
        TreeMap<Integer, String> map = new TreeMap<>();
        fillToMap(root, map);
        return map.toString();
    }

    private void fillToMap(Node node, Map<Integer, String> map) {
        if (node == null) return;
        fillToMap(node.left, map);
        map.put(node.key, node.value);
        fillToMap(node.right, map);
    }

    // ---------------- SortedMap methods ----------------
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        addHeadMap(root, toKey, map);
        return map;
    }

    private void addHeadMap(Node node, Integer toKey, MyRbMap map) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
            addHeadMap(node.left, toKey, map);
            addHeadMap(node.right, toKey, map);
        } else {
            addHeadMap(node.left, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        addTailMap(root, fromKey, map);
        return map;
    }

    private void addTailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            addTailMap(node.left, fromKey, map);
            addTailMap(node.right, fromKey, map);
        } else {
            addTailMap(node.right, fromKey, map);
        }
    }

    // --------------- unused SortedMap methods ----------------
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public String remove(Object key) {
        Node node = root;
        Integer k = (Integer) key;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else {
                String oldValue = node.value;
                deleteNode(node);
                size--;
                return oldValue;
            }
        }
        return null;
    }

    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = minimum(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }

        Node replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) root = replacement;
            else if (node == node.parent.left) node.parent.left = replacement;
            else node.parent.right = replacement;

            node.left = node.right = node.parent = null;

            if (!node.color==RED) fixDelete(replacement);
        } else if (node.parent == null) {
            root = null;
        } else {
            if (!node.color==RED) fixDelete(node);
            if (node.parent != null) {
                if (node == node.parent.left) node.parent.left = null;
                else if (node == node.parent.right) node.parent.right = null;
                node.parent = null;
            }
        }
    }

    private void fixDelete(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node w = rightOf(parentOf(x));
                if (colorOf(w) == RED) {
                    setColor(w, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    w = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(w)) == BLACK && colorOf(rightOf(w)) == BLACK) {
                    setColor(w, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(w)) == BLACK) {
                        setColor(leftOf(w), BLACK);
                        setColor(w, RED);
                        rotateRight(w);
                        w = rightOf(parentOf(x));
                    }
                    setColor(w, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(w), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node w = leftOf(parentOf(x));
                if (colorOf(w) == RED) {
                    setColor(w, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    w = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(w)) == BLACK && colorOf(leftOf(w)) == BLACK) {
                    setColor(w, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(w)) == BLACK) {
                        setColor(rightOf(w), BLACK);
                        setColor(w, RED);
                        rotateLeft(w);
                        w = leftOf(parentOf(x));
                    }
                    setColor(w, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(w), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Вспомогательные методы для цвета и родителя
    private boolean colorOf(Node n) { return n == null ? BLACK : n.color; }
    private Node parentOf(Node n) { return n == null ? null : n.parent; }
    private Node leftOf(Node n) { return n == null ? null : n.left; }
    private Node rightOf(Node n) { return n == null ? null : n.right; }
    private void setColor(Node n, boolean c) { if (n != null) n.color = c; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }
}
