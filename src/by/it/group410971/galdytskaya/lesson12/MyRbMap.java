package by.it.group410971.galdytskaya.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;
        int size;

        Node(Integer key, String value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node root;

    public MyRbMap() {
        root = null;
    }

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
    }

    private int size(Node x) {
        return x == null ? 0 : x.size;
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        return get(root, (Integer) key);
    }

    private String get(Node x, Integer key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED, 1);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node h) {
        h.color = RED;
        if (h.left != null) h.left.color = BLACK;
        if (h.right != null) h.right.color = BLACK;
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!containsKey(key)) return null;
        String oldValue = get(key);
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = remove(root, (Integer) key);
        if (!isEmpty()) root.color = BLACK;
        return oldValue;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left != null ? h.left.left : null))
                h = moveRedLeft(h);
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right != null ? h.right.left : null))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node min = min(h.right);
                h.key = min.key;
                h.value = min.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = remove(h.right, key);
            }
        }
        return balance(h);
    }

    private Node deleteMin(Node h) {
        if (h.left == null)
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return balance(h);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // natural ordering
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, toKey, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, MyRbMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            headMap(node.left, toKey, map);
            map.put(node.key, node.value);
            headMap(node.right, toKey, map);
        } else {
            headMap(node.left, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, fromKey, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp < 0) {
            tailMap(node.right, fromKey, map);
        } else {
            tailMap(node.left, fromKey, map);
            map.put(node.key, node.value);
            tailMap(node.right, fromKey, map);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return max(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        LinkedHashSet<Entry<Integer, String>> set = new LinkedHashSet<>();
        fillEntrySet(root, set);
        return set;
    }

    private void fillEntrySet(Node node, Set<Entry<Integer, String>> set) {
        if (node == null) return;
        fillEntrySet(node.left, set);
        set.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        fillEntrySet(node.right, set);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        List<Entry<Integer, String>> list = new ArrayList<>(entrySet());
        for (int i = 0; i < list.size(); i++) {
            Entry<Integer, String> e = list.get(i);
            sb.append(e.getKey()).append("=").append(e.getValue());
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) throw new NullPointerException("Map is null");
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    // Unsupported optional operations
    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }
}
