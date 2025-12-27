package by.it.group410971.malecki.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        boolean color;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    private boolean isRed(RbNode node) {
        return node != null && node.color == RED;
    }

    private RbNode rotateLeft(RbNode h) {
        RbNode x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private RbNode rotateRight(RbNode h) {
        RbNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(RbNode h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    private RbNode put(RbNode node, Integer key, String value) {
        if (node == null) {
            size++;
            return new RbNode(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        // Balance the tree
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    private RbNode min(RbNode node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private RbNode max(RbNode node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private String get(RbNode node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    private boolean containsKey(RbNode node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey(root, (Integer) key);
    }

    private boolean containsValue(RbNode node, String value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private void toStringHelper(RbNode node, StringBuilder sb) {
        if (node != null) {
            toStringHelper(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            toStringHelper(node.right, sb);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringHelper(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private SortedMap<Integer, String> headMap(RbNode node, Integer toKey, SortedMap<Integer, String> result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
                headMap(node.right, toKey, result);
            }
        }
        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        return headMap(root, toKey, result);
    }

    private SortedMap<Integer, String> tailMap(RbNode node, Integer fromKey, SortedMap<Integer, String> result) {
        if (node != null) {
            tailMap(node.right, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
                tailMap(node.left, fromKey, result);
            }
        }
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        return tailMap(root, fromKey, result);
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) return null;
        RbNode node = min(root);
        return node != null ? node.key : null;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) return null;
        RbNode node = max(root);
        return node != null ? node.key : null;
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
    public void clear() {
        root = null;
        size = 0;
    }

    // Simple implementation of remove for testing
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String oldValue = get(key);
        if (oldValue != null) {
            // Simple implementation: just mark as removed
            // In real R-B tree, this would be more complex
            root = removeHelper(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    private RbNode removeHelper(RbNode node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeHelper(node.left, key);
        } else if (cmp > 0) {
            node.right = removeHelper(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            RbNode minNode = min(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = removeHelper(node.right, minNode.key);
        }

        return node;
    }

    // Methods not required for test B
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
}