package by.it.group410972.sheshtakauskas.lesson01.lesson12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    @Override
    public String toString() {
        List<String> elements = new ArrayList<>();
        inorder(root, elements);
        return "{" + String.join(", ", elements) + "}";
    }

    private void inorder(Node node, List<String> elements) {
        if (node == null) return;
        inorder(node.left, elements);
        elements.add(node.key + "=" + node.value);
        inorder(node.right, elements);
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    private Node lowerNode(Integer key, boolean inclusive) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                res = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    private Node higherNode(Integer key, boolean inclusive) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = lowerNode(key, false);
        return n == null ? null : n.key;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = lowerNode(key, true);
        return n == null ? null : n.key;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = higherNode(key, true);
        return n == null ? null : n.key;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(key, false);
        return n == null ? null : n.key;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

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
        MySplayMap map = new MySplayMap();
        inorderAdd(root, map, true, toKey);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        inorderAdd(root, map, false, fromKey);
        return map;
    }

    private void inorderAdd(Node node, MySplayMap map, boolean isHeadMap, Integer keyBound) {
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
        Node cur = root;
        if (cur == null) return null;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        Node cur = root;
        if (cur == null) return null;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return cur.key;
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
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
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


    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        if (last != null) splay(last);
        return null;
    }

    @Override
    public String get(Object key) {
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node cur = root;
        Node parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        Node node = new Node(key, value);
        node.parent = parent;
        if (cmp < 0) parent.left = node;
        else parent.right = node;
        size++;
        splay(node);
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode((Integer) key);
        if (node == null) return null;
        splay(node);
        String val = node.value;
        if (node.left == null) transplant(node, node.right);
        else if (node.right == null) transplant(node, node.left);
        else {
            Node min = node.right;
            while (min.left != null) min = min.left;
            if (min.parent != node) {
                transplant(min, min.right);
                min.right = node.right;
                min.right.parent = min;
            }
            transplant(node, min);
            min.left = node.left;
            min.left.parent = min;
        }
        size--;
        return val;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
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
