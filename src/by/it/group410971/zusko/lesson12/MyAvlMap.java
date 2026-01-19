package by.it.group410971.zusko.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private static class AvlNode {
        Integer key;
        String value;
        AvlNode left;
        AvlNode right;
        int height;

        AvlNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AvlNode root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    private int height(AvlNode node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(AvlNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T = x.right;

        x.right = y;
        y.left = T;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T = y.left;

        y.left = x;
        x.right = T;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private AvlNode balance(AvlNode node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AvlNode put(AvlNode node, Integer key, String value) {
        if (node == null) {
            size++;
            return new AvlNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value; // update existing key
        }

        return balance(node);
    }

    private AvlNode minValueNode(AvlNode node) {
        AvlNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private AvlNode remove(AvlNode node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            // Node found
            if (node.left == null || node.right == null) {
                size--;
                AvlNode temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    return null;
                } else {
                    return temp;
                }
            } else {
                // Node with two children
                AvlNode temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key);
            }
        }

        return balance(node);
    }

    private String get(AvlNode node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    private boolean containsKey(AvlNode node, Integer key) {
        if (node == null) {
            return false;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    private void toStringHelper(AvlNode node, StringBuilder sb) {
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return containsKey(root, (Integer) key);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get(root, (Integer) key);
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = put(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        String oldValue = get(key);
        root = remove(root, (Integer) key);
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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

    // Methods not required for test A
    @Override
    public boolean containsValue(Object value) { return false; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
}