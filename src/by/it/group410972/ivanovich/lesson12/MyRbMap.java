package by.it.group410972.ivanovich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left, right;
        boolean color;
        int size;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = 1;
        }
    }

    private RbNode root;

    public MyRbMap() {
        this.root = null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
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
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK; // Корень всегда черный
        return oldValue[0];
    }

    @Override
    public String remove(Object keyObj) {
        if (keyObj == null) {
            return null;
        }
        if (!(keyObj instanceof Integer)) {
            return null;
        }

        Integer key = (Integer) keyObj;
        if (!containsKey(key)) {
            return null;
        }

        String[] removedValue = new String[1];
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = remove(root, key, removedValue);
        if (root != null) {
            root.color = BLACK;
        }
        return removedValue[0];
    }

    @Override
    public String get(Object keyObj) {
        if (keyObj == null) {
            return null;
        }
        if (!(keyObj instanceof Integer)) {
            return null;
        }

        Integer key = (Integer) keyObj;
        RbNode node = get(root, key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (keyObj == null) {
            return false;
        }
        if (!(keyObj instanceof Integer)) {
            return false;
        }

        Integer key = (Integer) keyObj;
        return get(root, key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(RbNode node) {
        return node == null ? 0 : node.size;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException("toKey cannot be null");
        }

        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey cannot be null");
        }

        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // используем естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException("Keys cannot be null");
        }
        if (fromKey.compareTo(toKey) >= 0) {
            throw new IllegalArgumentException("fromKey must be less than toKey");
        }

        MyRbMap result = new MyRbMap();
        subMap(root, fromKey, toKey, result);
        return result;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////      Методы которые не реализуем       /////////////////
    /////////////////////////////////////////////////////////////////////////

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

    /////////////////////////////////////////////////////////////////////////
    //////////////////////// Вспомогательные методы КЧ-дерева ///////////////
    /////////////////////////////////////////////////////////////////////////

    private void inOrderTraversal(RbNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
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
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private RbNode rotateRight(RbNode h) {
        RbNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(RbNode h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private RbNode put(RbNode h, Integer key, String value, String[] oldValue) {
        if (h == null) {
            return new RbNode(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value, oldValue);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value, oldValue);
        } else {
            // Ключ уже существует
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        // Балансировка
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    private RbNode get(RbNode node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    private boolean containsValue(RbNode node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Удаление
    private RbNode moveRedLeft(RbNode h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private RbNode moveRedRight(RbNode h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private RbNode balance(RbNode h) {
        if (isRed(h.right)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    private RbNode removeMin(RbNode h) {
        if (h.left == null) {
            return null;
        }

        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = removeMin(h.left);
        return balance(h);
    }

    private RbNode min(RbNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private RbNode max(RbNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private RbNode remove(RbNode h, Integer key, String[] removedValue) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = remove(h.left, key, removedValue);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && h.right == null) {
                removedValue[0] = h.value;
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                RbNode minNode = min(h.right);
                removedValue[0] = h.value;
                h.key = minNode.key;
                h.value = minNode.value;
                h.right = removeMin(h.right);
            } else {
                h.right = remove(h.right, key, removedValue);
            }
        }
        return balance(h);
    }

    // Методы для headMap, tailMap, subMap
    private void headMap(RbNode node, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        int cmp = toKey.compareTo(node.key);
        if (cmp <= 0) {
            // Ключ меньше или равен toKey, идем влево
            headMap(node.left, toKey, result);
        } else {
            // Ключ больше toKey, добавляем левое поддерево и идем вправо
            addAll(node.left, result);
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        }
    }

    private void tailMap(RbNode node, Integer fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        int cmp = fromKey.compareTo(node.key);
        if (cmp > 0) {
            // Ключ меньше fromKey, идем вправо
            tailMap(node.right, fromKey, result);
        } else {
            // Ключ больше или равен fromKey, добавляем правое поддерево и идем влево
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            addAll(node.right, result);
        }
    }

    private void subMap(RbNode node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        int cmpFrom = fromKey.compareTo(node.key);
        int cmpTo = toKey.compareTo(node.key);

        if (cmpFrom < 0) {
            // Ключ больше fromKey, проверяем левое поддерево
            subMap(node.left, fromKey, toKey, result);
        }

        if (cmpFrom <= 0 && cmpTo > 0) {
            // Ключ в диапазоне [fromKey, toKey)
            result.put(node.key, node.value);
        }

        if (cmpTo > 0) {
            // Ключ меньше toKey, проверяем правое поддерево
            subMap(node.right, fromKey, toKey, result);
        }
    }

    private void addAll(RbNode node, MyRbMap map) {
        if (node == null) {
            return;
        }
        addAll(node.left, map);
        map.put(node.key, node.value);
        addAll(node.right, map);
    }
}