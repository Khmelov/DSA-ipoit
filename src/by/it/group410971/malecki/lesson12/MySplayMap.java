package by.it.group410971.malecki.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;

        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    private SplayNode rotateRight(SplayNode x) {
        SplayNode y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private SplayNode rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private SplayNode splay(SplayNode node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                return node;
            }

            if (key.compareTo(node.left.key) < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key.compareTo(node.left.key) > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return (node.left == null) ? node : rotateRight(node);
        } else {
            if (node.right == null) {
                return node;
            }

            if (key.compareTo(node.right.key) < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (key.compareTo(node.right.key) > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new SplayNode(key, value);
            size = 1;
            return null;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        SplayNode newNode = new SplayNode(key, value);
        if (cmp < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        root = newNode;
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }

        root = splay(root, (Integer) key);
        return root.key.equals(key) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private boolean containsValue(SplayNode node, String value) {
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

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }

        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) {
            return null;
        }

        String oldValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            SplayNode newRoot = root.right;
            root = splay(root.left, (Integer) key);
            root.right = newRoot;
        }

        size--;
        return oldValue;
    }

    private SplayNode min(SplayNode node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode max(SplayNode node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) return null;
        SplayNode node = min(root);
        return node != null ? node.key : null;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) return null;
        SplayNode node = max(root);
        return node != null ? node.key : null;
    }

    private Integer lowerKey(SplayNode node, Integer key) {
        Integer result = null;
        while (node != null) {
            if (key.compareTo(node.key) <= 0) {
                node = node.left;
            } else {
                result = node.key;
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lowerKey(root, key);
    }

    private Integer floorKey(SplayNode node, Integer key) {
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                result = node.key;
                node = node.right;
            } else {
                return node.key;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        return floorKey(root, key);
    }

    private Integer ceilingKey(SplayNode node, Integer key) {
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                result = node.key;
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node.key;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return ceilingKey(root, key);
    }

    private Integer higherKey(SplayNode node, Integer key) {
        Integer result = null;
        while (node != null) {
            if (key.compareTo(node.key) >= 0) {
                node = node.right;
            } else {
                result = node.key;
                node = node.left;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        return higherKey(root, key);
    }

    private void headMapHelper(SplayNode node, Integer toKey, NavigableMap<Integer, String> result) {
        if (node != null) {
            headMapHelper(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
                headMapHelper(node.right, toKey, result);
            }
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    private void tailMapHelper(SplayNode node, Integer fromKey, NavigableMap<Integer, String> result) {
        if (node != null) {
            tailMapHelper(node.right, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
                tailMapHelper(node.left, fromKey, result);
            }
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
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

    private void toStringHelper(SplayNode node, StringBuilder sb) {
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

    // Methods not required for test C
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
    public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override
    public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override
    public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> firstEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> lastEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> pollLastEntry() { return null; }
}