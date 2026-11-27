package by.it.group510972.bahutskaya.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

// Словарь: ключи по порядку, можно взять кусок меньше или больше заданного.
public class TaskB implements SortedMap<Integer, String> {

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
        TaskB map = new TaskB();
        copyRange(root, fromKey, toKey, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        TaskB map = new TaskB();
        copyBelow(root, toKey, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        TaskB map = new TaskB();
        copyFrom(root, fromKey, map);
        return map;
    }

    private void copyBelow(Node node, int toKey, TaskB map) {
        if (node == null) {
            return;
        }
        if (node.key < toKey) {
            copyBelow(node.left, toKey, map);
            map.put(node.key, node.value);
            copyBelow(node.right, toKey, map);
        } else {
            copyBelow(node.left, toKey, map);
        }
    }

    private void copyFrom(Node node, int fromKey, TaskB map) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            copyFrom(node.left, fromKey, map);
            map.put(node.key, node.value);
            copyFrom(node.right, fromKey, map);
        } else {
            copyFrom(node.right, fromKey, map);
        }
    }

    private void copyRange(Node node, int fromKey, int toKey, TaskB map) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            copyRange(node.left, fromKey, toKey, map);
        }
        if (node.key >= fromKey && node.key < toKey) {
            map.put(node.key, node.value);
        }
        if (node.key < toKey) {
            copyRange(node.right, fromKey, toKey, map);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }
}
