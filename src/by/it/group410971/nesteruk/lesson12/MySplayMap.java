package by.it.group410971.nesteruk.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

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

    private Node root;
    private int size;

    public MySplayMap() {
        this.size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        Node node = findNode(key);
        if (node != null && key.equals(node.key)) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        }

        root = insert(root, key, value, null);
        size++;
        splay(findNode(key));
        return null;
    }

    private Node insert(Node node, Integer key, String value, Node parent) {
        if (node == null) {
            return new Node(key, value, parent);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value, node);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value, node);
        }
        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node == null || !k.equals(node.key)) {
            return null;
        }

        String oldValue = node.value;
        root = delete(root, k);
        size--;
        return oldValue;
    }

    private Node delete(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node min = minNode(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key);
        }
        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node != null && k.equals(node.key)) {
            splay(node);
            return node.value;
        }
        return null;
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
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node min = minNode(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node max = maxNode(root);
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        headMap(node.left, toKey, inclusive, result);

        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }

        if (cmp < 0) {
            headMap(node.right, toKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        tailMap(node.right, fromKey, inclusive, result);

        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }

        if (cmp > 0 || (inclusive && cmp == 0)) {
            tailMap(node.left, fromKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) return;

        int cmpFrom = node.key.compareTo(fromKey);
        int cmpTo = node.key.compareTo(toKey);

        if (cmpFrom > 0 || (fromInclusive && cmpFrom == 0)) {
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, result);
        }

        if ((cmpFrom > 0 || (fromInclusive && cmpFrom == 0)) &&
                (cmpTo < 0 || (toInclusive && cmpTo == 0))) {
            result.put(node.key, node.value);
        }

        if (cmpTo < 0 || (toInclusive && cmpTo == 0)) {
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, result);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        Integer lowerKey = lowerKey(key);
        return lowerKey == null ? null : new SimpleEntry(lowerKey, get(lowerKey));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        Integer floorKey = floorKey(key);
        return floorKey == null ? null : new SimpleEntry(floorKey, get(floorKey));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        Integer ceilingKey = ceilingKey(key);
        return ceilingKey == null ? null : new SimpleEntry(ceilingKey, get(ceilingKey));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        Integer higherKey = higherKey(key);
        return higherKey == null ? null : new SimpleEntry(higherKey, get(higherKey));
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        if (root == null) return null;
        Integer firstKey = firstKey();
        return new SimpleEntry(firstKey, get(firstKey));
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        if (root == null) return null;
        Integer lastKey = lastKey();
        return new SimpleEntry(lastKey, get(lastKey));
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        if (root == null) return null;
        Integer firstKey = firstKey();
        Map.Entry<Integer, String> entry = firstEntry();
        remove(firstKey);
        return entry;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        if (root == null) return null;
        Integer lastKey = lastKey();
        Map.Entry<Integer, String> entry = lastEntry();
        remove(lastKey);
        return entry;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        MySplayMap result = new MySplayMap();
        descendingMap(root, result);
        return result;
    }

    private void descendingMap(Node node, MySplayMap result) {
        if (node == null) return;
        descendingMap(node.right, result);
        result.put(node.key, node.value);
        descendingMap(node.left, result);
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        TreeSet<Integer> keySet = new TreeSet<>();
        inOrderKeys(root, keySet);
        return keySet;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        TreeSet<Integer> keySet = new TreeSet<>(Collections.reverseOrder());
        inOrderKeys(root, keySet);
        return keySet;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        return navigableKeySet();
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValues(root, values);
        return values;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Entry::getKey));
        inOrderEntries(root, entries);
        return entries;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    private Node findNode(Integer key) {
        return findNode(root, key);
    }

    private Node findNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return findNode(node.left, key);
        if (cmp > 0) return findNode(node.right, key);
        return node;
    }

    private Node lowerNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerNode(node.left, key);
        } else {
            Node right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    private Node floorNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp < 0) return floorNode(node.left, key);

        Node right = floorNode(node.right, key);
        return right != null ? right : node;
    }

    private Node ceilingNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp > 0) return ceilingNode(node.right, key);

        Node left = ceilingNode(node.left, key);
        return left != null ? left : node;
    }

    private Node higherNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return higherNode(node.right, key);
        } else {
            Node left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }

    private Node minNode(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node maxNode(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void splay(Node node) {
        while (node != null && node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        if (rightChild == null) return;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        if (leftChild == null) return;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) return;

        inOrderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderToString(node.right, sb);
    }

    private void inOrderKeys(Node node, Set<Integer> keySet) {
        if (node == null) return;
        inOrderKeys(node.left, keySet);
        keySet.add(node.key);
        inOrderKeys(node.right, keySet);
    }

    private void inOrderValues(Node node, List<String> values) {
        if (node == null) return;
        inOrderValues(node.left, values);
        values.add(node.value);
        inOrderValues(node.right, values);
    }

    private void inOrderEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node == null) return;
        inOrderEntries(node.left, entries);
        entries.add(new SimpleEntry(node.key, node.value));
        inOrderEntries(node.right, entries);
    }

    private static class SimpleEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private String value;

        SimpleEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}