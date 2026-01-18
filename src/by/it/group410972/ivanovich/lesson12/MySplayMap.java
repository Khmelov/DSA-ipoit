package by.it.group410972.ivanovich.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left, right, parent;
        int size;

        SplayNode(Integer key, String value, SplayNode parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.size = 1;
        }
    }

    private SplayNode root;
    private int size;

    public MySplayMap() {
        this.root = null;
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
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
        if (oldValue[0] == null) {
            size++;
        }
        // Поднимаем вставленный/найденный узел в корень
        splay(findNode(key));
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
        SplayNode node = findNode(key);
        if (node == null) {
            return null;
        }

        // Поднимаем удаляемый узел в корень
        splay(node);

        String removedValue = node.value;

        // Если нет левого поддерева
        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        }
        // Если нет правого поддерева
        else if (node.right == null) {
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        }
        // Если есть оба поддерева
        else {
            SplayNode maxLeft = max(node.left);
            splay(maxLeft); // Поднимаем максимум левого поддерева в корень

            // Присоединяем правое поддерево удаляемого узла
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;
            updateSize(root);
        }

        size--;
        return removedValue;
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
        SplayNode node = findNode(key);
        if (node != null) {
            splay(node); // Поднимаем найденный узел в корень
            return node.value;
        }
        return null;
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
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        SplayNode minNode = min(root);
        splay(minNode);
        return minNode.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        SplayNode maxNode = max(root);
        splay(maxNode);
        return maxNode.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        // В SortedMap интерфейсе headMap не включает toKey
        return createHeadMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        // В SortedMap интерфейсе tailMap включает fromKey
        return createTailMap(fromKey, true);
    }

    @Override
    public Integer lowerKey(Integer key) {
        SplayNode node = lowerNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        SplayNode node = floorNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode node = ceilingNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        SplayNode node = higherNode(key);
        return node == null ? null : node.key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        // В NavigableMap интерфейсе можем указать, включать toKey или нет
        return createHeadMap(toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        // В NavigableMap интерфейсе можем указать, включать fromKey или нет
        return createTailMap(fromKey, inclusive);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        // Создаем новую карту для поддиапазона
        MySplayMap result = new MySplayMap();
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
    //////////////////////// Вспомогательные методы Splay-дерева ////////////
    /////////////////////////////////////////////////////////////////////////

    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private void updateSize(SplayNode node) {
        if (node != null) {
            node.size = 1 + size(node.left) + size(node.right);
        }
    }

    private int size(SplayNode node) {
        return node == null ? 0 : node.size;
    }

    private SplayNode rotateRight(SplayNode y) {
        SplayNode x = y.left;
        if (x != null) {
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            x.parent = y.parent;
            x.right = y;
        }

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        if (x != null) {
            y.parent = x;
        }

        updateSize(y);
        updateSize(x);
        return x;
    }

    private SplayNode rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            y.left = x;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        if (y != null) {
            x.parent = y;
        }

        updateSize(x);
        updateSize(y);
        return y;
    }

    private void splay(SplayNode x) {
        if (x == null) return;

        while (x.parent != null) {
            if (x.parent.parent == null) {
                // Zig
                if (x == x.parent.left) {
                    x = rotateRight(x.parent);
                } else {
                    x = rotateLeft(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                // Zig-zig (правый-правый)
                rotateRight(x.parent.parent);
                x = rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                // Zig-zig (левый-левый)
                rotateLeft(x.parent.parent);
                x = rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                // Zig-zag (левый-правый)
                x = rotateLeft(x.parent);
                x = rotateRight(x.parent);
            } else {
                // Zig-zag (правый-левый)
                x = rotateRight(x.parent);
                x = rotateLeft(x.parent);
            }
        }
        root = x;
    }

    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private SplayNode put(SplayNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new SplayNode(key, value, null);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) {
                node.left = new SplayNode(key, value, node);
                updateSize(node);
                return node;
            }
            node.left = put(node.left, key, value, oldValue);
            node.left.parent = node;
        } else if (cmp > 0) {
            if (node.right == null) {
                node.right = new SplayNode(key, value, node);
                updateSize(node);
                return node;
            }
            node.right = put(node.right, key, value, oldValue);
            node.right.parent = node;
        } else {
            // Ключ уже существует
            oldValue[0] = node.value;
            node.value = value;
        }

        updateSize(node);
        return node;
    }

    private boolean containsValue(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            splay(node);
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private SplayNode min(SplayNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode max(SplayNode node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private SplayNode lowerNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                current = current.left;
            } else {
                result = current;
                current = current.right;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private SplayNode floorNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                result = current;
                current = current.right;
            } else {
                result = current;
                break;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private SplayNode ceilingNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0) {
                current = current.right;
            } else if (cmp < 0) {
                result = current;
                current = current.left;
            } else {
                result = current;
                break;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private SplayNode higherNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp >= 0) {
                current = current.right;
            } else {
                result = current;
                current = current.left;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private NavigableMap<Integer, String> createHeadMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        createHeadMap(root, toKey, inclusive, result);
        return result;
    }

    private void createHeadMap(SplayNode node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        int cmp = toKey.compareTo(node.key);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            // Добавляем левое поддерево и текущий узел
            addAll(node.left, result);
            result.put(node.key, node.value);
            createHeadMap(node.right, toKey, inclusive, result);
        } else {
            createHeadMap(node.left, toKey, inclusive, result);
        }
    }

    private NavigableMap<Integer, String> createTailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        createTailMap(root, fromKey, inclusive, result);
        return result;
    }

    private void createTailMap(SplayNode node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        int cmp = fromKey.compareTo(node.key);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            // Добавляем правое поддерево и текущий узел
            createTailMap(node.left, fromKey, inclusive, result);
            result.put(node.key, node.value);
            addAll(node.right, result);
        } else {
            createTailMap(node.right, fromKey, inclusive, result);
        }
    }

    private void subMap(SplayNode node, Integer fromKey, Integer toKey, MySplayMap result) {
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

    private void addAll(SplayNode node, MySplayMap map) {
        if (node == null) {
            return;
        }
        addAll(node.left, map);
        map.put(node.key, node.value);
        addAll(node.right, map);
    }
}