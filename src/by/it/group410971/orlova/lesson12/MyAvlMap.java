package by.it.a_khmelev.lesson12;

import java.util.Map;

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

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        // Удаляем последнюю запятую и пробел
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        String[] removedValue = new String[1];
        root = remove(root, (Integer) key, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        AvlNode node = find(root, (Integer) key);
        return (node != null) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }

        return find(root, (Integer) key) != null;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы АВЛ-дерева           ///////
    /////////////////////////////////////////////////////////////////////////

    private int height(AvlNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int balanceFactor(AvlNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        y.left = x;
        x.right = T2;

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

    private AvlNode put(AvlNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new AvlNode(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            // Ключ уже существует
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private AvlNode find(AvlNode node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return find(node.left, key);
        } else if (cmp > 0) {
            return find(node.right, key);
        } else {
            return node;
        }
    }

    private AvlNode findMin(AvlNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private AvlNode remove(AvlNode node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            // Найден узел для удаления
            removedValue[0] = node.value;

            // Узел с одним или без детей
            if (node.left == null || node.right == null) {
                AvlNode temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    // Нет детей
                    node = null;
                } else {
                    // Один ребенок
                    node = temp;
                }
            } else {
                // Узел с двумя детьми
                AvlNode temp = findMin(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key, new String[1]);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Методы, которые можно не имплементировать        ////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(AvlNode node, Object value) {
        if (node == null) {
            return false;
        }

        if ((value == null && node.value == null) ||
            (value != null && value.equals(node.value))) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public java.util.Set<Integer> keySet() {
        java.util.Set<Integer> keys = new java.util.HashSet<>();
        collectKeys(root, keys);
        return keys;
    }

    private void collectKeys(AvlNode node, java.util.Set<Integer> keys) {
        if (node != null) {
            collectKeys(node.left, keys);
            keys.add(node.key);
            collectKeys(node.right, keys);
        }
    }

    @Override
    public java.util.Collection<String> values() {
        java.util.Collection<String> values = new java.util.ArrayList<>();
        collectValues(root, values);
        return values;
    }

    private void collectValues(AvlNode node, java.util.Collection<String> values) {
        if (node != null) {
            collectValues(node.left, values);
            values.add(node.value);
            collectValues(node.right, values);
        }
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        java.util.Set<Entry<Integer, String>> entries = new java.util.HashSet<>();
        collectEntries(root, entries);
        return entries;
    }

    private void collectEntries(AvlNode node, java.util.Set<Entry<Integer, String>> entries) {
        if (node != null) {
            collectEntries(node.left, entries);
            entries.add(new SimpleEntry(node.key, node.value));
            collectEntries(node.right, entries);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }

        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) {
            return false;
        }

        try {
            for (Entry<Integer, String> e : entrySet()) {
                Integer key = e.getKey();
                String value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key))) {
                        return false;
                    }
                } else {
                    if (!value.equals(m.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Entry<Integer, String> entry : entrySet()) {
            h += entry.hashCode();
        }
        return h;
    }

    // Простая реализация Entry для внутреннего использования
    private static class SimpleEntry implements Entry<Integer, String> {
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
            String old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> e = (Entry<?, ?>) o;
            return key.equals(e.getKey()) && value.equals(e.getValue());
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    // Дополнительные методы для отладки и тестирования

    public boolean isBalanced() {
        return checkBalance(root);
    }

    private boolean checkBalance(AvlNode node) {
        if (node == null) {
            return true;
        }

        int balance = balanceFactor(node);
        if (Math.abs(balance) > 1) {
            return false;
        }

        return checkBalance(node.left) && checkBalance(node.right);
    }

    public int getHeight() {
        return height(root);
    }
}