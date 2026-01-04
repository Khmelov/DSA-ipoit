package by.it.group410972.zayats.lesson12;




import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    public MySplayMap() {
        root = null;
        size = 0;
    }

    // --- Splay операции ---
    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
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
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    private Node findNode(Integer key) {
        Node n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) { splay(n); return n; }
            else if (cmp < 0) n = n.left;
            else n = n.right;
        }
        return null;
    }

    // --- Map методы ---
    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return n != null ? n.value : null;
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
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }
        Node n = root;
        Node parent = null;
        int cmp = 0;
        while (n != null) {
            parent = n;
            cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else {
                String old = n.value;
                n.value = value;
                splay(n);
                return old;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode((Integer) key);
        if (node == null) return null;
        splay(node);
        String old = node.value;
        if (node.left == null) replace(node, node.right);
        else if (node.right == null) replace(node, node.left);
        else {
            Node min = node.right;
            while (min.left != null) min = min.left;
            if (min.parent != node) {
                replace(min, min.right);
                min.right = node.right;
                min.right.parent = min;
            }
            replace(node, min);
            min.left = node.left;
            min.left.parent = min;
        }
        size--;
        return old;
    }

    private void replace(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    // --- Размеры ---
    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public void clear() { root = null; size = 0; }

    // --- Sorted/Navigable методы ---
    private Node minimum(Node n) { while (n.left != null) n = n.left; return n; }
    private Node maximum(Node n) { while (n.right != null) n = n.right; return n; }

    @Override
    public Integer firstKey() { Node n = minimum(root); return n != null ? n.key : null; }
    @Override
    public Integer lastKey() { Node n = maximum(root); return n != null ? n.key : null; }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp <= 0) n = n.left;
            else { res = n; n = n.right; }
        }
        return res != null ? res.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else { res = n; n = n.right; }
        }
        return res != null ? res.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp > 0) n = n.right;
            else { res = n; n = n.left; }
        }
        return res != null ? res.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp >= 0) n = n.right;
            else { res = n; n = n.left; }
        }
        return res != null ? res.key : null;
    }

    // --- Диапазоны (headMap, tailMap, subMap) ---
    private void addRange(Node n, Integer from, Integer to, MySplayMap map) {
        if (n == null) return;
        if (from == null || n.key.compareTo(from) >= 0) addRange(n.left, from, to, map);
        if ((from == null || n.key.compareTo(from) >= 0) && (to == null || n.key.compareTo(to) < 0)) {
            map.put(n.key, n.value);
        }
        if (to == null || n.key.compareTo(to) < 0) addRange(n.right, from, to, map);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        addRange(root, null, toKey, map);
        return map;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        addRange(root, fromKey, null, map);
        return map;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MySplayMap map = new MySplayMap();
        addRange(root, fromKey, toKey, map);
        return map;
    }

    // --- toString ---
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
    private void toString(Node n, StringBuilder sb) {
        if (n == null) return;
        toString(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        toString(n.right, sb);
    }

    // --- Заглушки NavigableMap ---
    @Override public String putIfAbsent(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
    @Override public boolean replace(Integer key, String oldValue, String newValue) { throw new UnsupportedOperationException(); }
    @Override public String replace(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}
