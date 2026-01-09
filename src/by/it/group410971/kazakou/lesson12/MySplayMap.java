package by.it.group410971.kazakou.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder out = new StringBuilder();
        out.append('{');
        appendInOrder(root, out);
        if (out.length() > 1) {
            out.setLength(out.length() - 2);
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node current = root;
        Node parent = null;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp == 0) {
                String old = current.value;
                current.value = value;
                return old;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        Node node = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap map = new MySplayMap();
        collectHead(root, toKey, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap map = new MySplayMap();
        collectTail(root, fromKey, map);
        return map;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return max(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return findLower(key, false);
    }

    @Override
    public Integer floorKey(Integer key) {
        return findLower(key, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findHigher(key, true);
    }

    @Override
    public Integer higherKey(Integer key) {
        return findHigher(key, false);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    private Node findNode(Object key) {
        Integer target = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = target.compareTo(current.key);
            if (cmp == 0) {
                return current;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = min(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }
        Node replacement = node.left != null ? node.left : node.right;
        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node.parent.left == node) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.parent.left == node) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
        }
    }

    private Node min(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node max(Node node) {
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    private void appendInOrder(Node node, StringBuilder out) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, out);
        out.append(node.key).append('=').append(node.value).append(", ");
        appendInOrder(node.right, out);
    }

    private void collectHead(Node node, Integer toKey, MySplayMap map) {
        if (node == null) {
            return;
        }
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            collectHead(node.left, toKey, map);
            map.put(node.key, node.value);
            collectHead(node.right, toKey, map);
        } else {
            collectHead(node.left, toKey, map);
        }
    }

    private void collectTail(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) {
            return;
        }
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            collectTail(node.left, fromKey, map);
            map.put(node.key, node.value);
            collectTail(node.right, fromKey, map);
        } else {
            collectTail(node.right, fromKey, map);
        }
    }

    private Integer findLower(Integer key, boolean inclusive) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node current = root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                candidate = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return candidate;
    }

    private Integer findHigher(Integer key, boolean inclusive) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node current = root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                candidate = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return candidate;
    }
}
