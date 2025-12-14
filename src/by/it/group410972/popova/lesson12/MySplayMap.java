package by.it.group410972.popova.lesson12;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        root = null;
        size = 0;
    }

    // Вспомогательные методы для splay-дерева

    private void rotate(Node x) {
        Node parent = x.parent;
        if (parent == null) return;

        Node grandparent = parent.parent;

        if (parent.left == x) {
            // Right rotation
            Node b = x.right;
            x.right = parent;
            parent.left = b;
            if (b != null) b.parent = parent;
        } else {
            // Left rotation
            Node b = x.left;
            x.left = parent;
            parent.right = b;
            if (b != null) b.parent = parent;
        }

        x.parent = grandparent;
        parent.parent = x;

        if (grandparent != null) {
            if (grandparent.left == parent) {
                grandparent.left = x;
            } else {
                grandparent.right = x;
            }
        }
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node parent = x.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig
                rotate(x);
            } else if ((grandparent.left == parent && parent.left == x) ||
                    (grandparent.right == parent && parent.right == x)) {
                // Zig-zig
                rotate(parent);
                rotate(x);
            } else {
                // Zig-zag
                rotate(x);
                rotate(x);
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node last = null;

        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);

            if (cmp == 0) {
                splay(current);
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (last != null) {
            splay(last);
        }
        return null;
    }

    private Node findClosest(Integer key, boolean less, boolean inclusive) {
        Node current = root;
        Node best = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);

            if (cmp == 0) {
                if (inclusive) {
                    best = current;
                }
                if (less) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            } else if (cmp < 0) {
                if (!less) {
                    best = current;
                }
                current = current.left;
            } else {
                if (less) {
                    best = current;
                }
                current = current.right;
            }
        }

        return best;
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;

        inOrderTraversal(node.left, sb);
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp == 0) {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node newNode = new Node(key, value, parent);
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;

        Node node = findNode(key);
        if (node == null) return null;

        String removedValue = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        }
        else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        }
        else {
            Node maxLeft = node.left;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            if (maxLeft.parent != node) {
                maxLeft.parent.right = maxLeft.left;
                if (maxLeft.left != null) {
                    maxLeft.left.parent = maxLeft.parent;
                }
                maxLeft.left = node.left;
                node.left.parent = maxLeft;
            }

            maxLeft.right = node.right;
            node.right.parent = maxLeft;
            maxLeft.parent = null;
            root = maxLeft;
        }

        size--;
        if (root != null) {
            splay(root);
        }
        return removedValue;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;

        Node node = findNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        if (!(valueObj instanceof String)) return false;
        String value = (String) valueObj;

        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
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
        throw new UnsupportedOperationException("headMap not implemented");
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        throw new UnsupportedOperationException("tailMap not implemented");
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = findClosest(key, true, false);
        return node != null ? node.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findClosest(key, true, true);
        return node != null ? node.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findClosest(key, false, true);
        return node != null ? node.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findClosest(key, false, false);
        return node != null ? node.key : null;
    }


    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // natural ordering
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
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
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super Integer, ? super String> action) {
        forEachRecursive(root, action);
    }

    private void forEachRecursive(Node node, BiConsumer<? super Integer, ? super String> action) {
        if (node == null) return;
        forEachRecursive(node.left, action);
        action.accept(node.key, node.value);
        forEachRecursive(node.right, action);
    }

    @Override
    public void replaceAll(BiFunction<? super Integer, ? super String, ? extends String> function) {
        replaceAllRecursive(root, function);
    }

    private void replaceAllRecursive(Node node, BiFunction<? super Integer, ? super String, ? extends String> function) {
        if (node == null) return;
        replaceAllRecursive(node.left, function);
        node.value = function.apply(node.key, node.value);
        replaceAllRecursive(node.right, function);
    }

    @Override
    public String putIfAbsent(Integer key, String value) {
        String currentValue = get(key);
        if (currentValue == null) {
            currentValue = put(key, value);
        }
        return currentValue;
    }

    @Override
    public boolean replace(Integer key, String oldValue, String newValue) {
        Node node = findNode(key);
        if (node != null && Objects.equals(node.value, oldValue)) {
            node.value = newValue;
            return true;
        }
        return false;
    }

    @Override
    public String replace(Integer key, String value) {
        Node node = findNode(key);
        if (node != null) {
            String oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        return null;
    }

    @Override
    public String computeIfAbsent(Integer key, Function<? super Integer, ? extends String> mappingFunction) {
        Node node = findNode(key);
        if (node != null) {
            return node.value;
        }
        String newValue = mappingFunction.apply(key);
        if (newValue != null) {
            put(key, newValue);
        }
        return newValue;
    }

    @Override
    public String computeIfPresent(Integer key, BiFunction<? super Integer, ? super String, ? extends String> remappingFunction) {
        Node node = findNode(key);
        if (node != null) {
            String newValue = remappingFunction.apply(key, node.value);
            if (newValue != null) {
                node.value = newValue;
                return newValue;
            } else {
                remove(key);
                return null;
            }
        }
        return null;
    }

    @Override
    public String compute(Integer key, BiFunction<? super Integer, ? super String, ? extends String> remappingFunction) {
        Node node = findNode(key);
        String oldValue = node != null ? node.value : null;
        String newValue = remappingFunction.apply(key, oldValue);

        if (newValue != null) {
            put(key, newValue);
            return newValue;
        } else if (node != null) {
            remove(key);
        }
        return null;
    }

    @Override
    public String merge(Integer key, String value, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        Node node = findNode(key);
        String oldValue = node != null ? node.value : null;
        String newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);

        if (newValue != null) {
            put(key, newValue);
            return newValue;
        } else if (node != null) {
            remove(key);
        }
        return null;
    }
}