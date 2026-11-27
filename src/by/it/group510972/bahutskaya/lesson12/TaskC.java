package by.it.group510972.bahutskaya.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

// Словарь, умеет найти ближайший ключ: меньше, не больше, не меньше, больше.
public class TaskC implements NavigableMap<Integer, String> {

    private static final class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height = 1;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void update(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t = x.right;
        x.right = y;
        y.left = t;
        update(y);
        update(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t = y.left;
        y.left = x;
        x.right = t;
        update(x);
        update(y);
        return y;
    }

    private Node rebalance(Node node) {
        update(node);
        int factor = balance(node);
        if (factor > 1) {
            if (balance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (factor < -1) {
            if (balance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        append(root, sb, new boolean[]{true});
        sb.append('}');
        return sb.toString();
    }

    private void append(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) {
            return;
        }
        append(node.left, sb, first);
        if (!first[0]) {
            sb.append(", ");
        }
        first[0] = false;
        sb.append(node.key).append('=').append(node.value);
        append(node.right, sb, first);
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = new String[1];
        boolean[] created = new boolean[1];
        root = put(root, key, value, old, created);
        if (created[0]) {
            size++;
        }
        return old[0];
    }

    private Node put(Node node, int key, String value, String[] old, boolean[] created) {
        if (node == null) {
            created[0] = true;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = put(node.left, key, value, old, created);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, old, created);
        } else {
            old[0] = node.value;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer number) || get(number) == null && !containsKey(number)) {
            return null;
        }
        String old = get(number);
        root = delete(root, number);
        size--;
        return old;
    }

    private Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return rebalance(node);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer number)) {
            return null;
        }
        Node node = root;
        while (node != null) {
            if (number < node.key) {
                node = node.left;
            } else if (number > node.key) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer number)) {
            return false;
        }
        Node node = root;
        while (node != null) {
            if (number < node.key) {
                node = node.left;
            } else if (number > node.key) {
                node = node.right;
            } else {
                return true;
            }
        }
        return false;
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
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        TaskC map = new TaskC();
        copyRange(root, fromKey, fromInclusive, toKey, toInclusive, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        TaskC map = new TaskC();
        copyBelow(root, toKey, inclusive, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        TaskC map = new TaskC();
        copyFrom(root, fromKey, inclusive, map);
        return map;
    }

    private void copyBelow(Node node, int toKey, boolean inclusive, TaskC map) {
        if (node == null) {
            return;
        }
        copyBelow(node.left, toKey, inclusive, map);
        if (node.key < toKey || (inclusive && node.key == toKey)) {
            map.put(node.key, node.value);
            copyBelow(node.right, toKey, inclusive, map);
        }
    }

    private void copyFrom(Node node, int fromKey, boolean inclusive, TaskC map) {
        if (node == null) {
            return;
        }
        if (node.key > fromKey || (inclusive && node.key == fromKey)) {
            copyFrom(node.left, fromKey, inclusive, map);
            map.put(node.key, node.value);
        }
        copyFrom(node.right, fromKey, inclusive, map);
    }

    private void copyRange(Node node, int fromKey, boolean fromInclusive, int toKey, boolean toInclusive, TaskC map) {
        if (node == null) {
            return;
        }
        copyRange(node.left, fromKey, fromInclusive, toKey, toInclusive, map);
        boolean fromOk = node.key > fromKey || (fromInclusive && node.key == fromKey);
        boolean toOk = node.key < toKey || (toInclusive && node.key == toKey);
        if (fromOk && toOk) {
            map.put(node.key, node.value);
        }
        copyRange(node.right, fromKey, fromInclusive, toKey, toInclusive, map);
    }

    @Override
    public Integer firstKey() {
        Entry<Integer, String> entry = firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }

    @Override
    public Integer lastKey() {
        Entry<Integer, String> entry = lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyOf(lowerEntry(key));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOf(floorEntry(key));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOf(ceilingEntry(key));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOf(higherEntry(key));
    }

    private Integer keyOf(Entry<Integer, String> entry) {
        return entry == null ? null : entry.getKey();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return bound(key, true, false);
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return bound(key, true, true);
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return bound(key, false, true);
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return bound(key, false, false);
    }

    private Entry<Integer, String> bound(int key, boolean lower, boolean inclusive) {
        Node node = root;
        Node best = null;
        while (node != null) {
            if (lower) {
                if (node.key < key || (inclusive && node.key == key)) {
                    best = node;
                    node = node.right;
                } else {
                    node = node.left;
                }
            } else if (node.key > key || (inclusive && node.key == key)) {
                best = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (best == null) {
            return null;
        }
        int found = best.key;
        String value = best.value;
        return new SimpleEntry<>(found, value);
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        if (root == null) {
            return null;
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return new SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (root == null) {
            return null;
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return new SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Entry<Integer, String> entry = firstEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Entry<Integer, String> entry = lastEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
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

    private static final class SimpleEntry<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry<?, ?> other)) {
                return false;
            }
            return java.util.Objects.equals(key, other.getKey()) && java.util.Objects.equals(value, other.getValue());
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hashCode(key) ^ java.util.Objects.hashCode(value);
        }
    }
}
