package by.it.a_khmelev.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        RbNode parent;
        boolean color;

        RbNode(Integer key, String value, boolean color, RbNode parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
    }

    private RbNode root;
    private int size;

    public MyRbMap() {
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

        if (root == null) {
            root = new RbNode(key, value, BLACK, null);
            size = 1;
            return null;
        }

        RbNode current = root;
        RbNode parent = null;

        // Поиск позиции для вставки
        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Ключ уже существует
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        // Вставка нового узла
        RbNode newNode = new RbNode(key, value, RED, parent);
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Балансировка
        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        RbNode node = getNode((Integer) key);
        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        RbNode node = getNode((Integer) key);
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

        return getNode((Integer) key) != null;
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
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return getFirstNode().key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return getLastNode().key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException("toKey cannot be null");
        }
        return new SubMap(null, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey cannot be null");
        }
        return new SubMap(fromKey, null);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Внутренний класс SubMap                      ///////
    /////////////////////////////////////////////////////////////////////////

    private class SubMap extends AbstractMap<Integer, String>
            implements SortedMap<Integer, String> {

        private final Integer fromKey; // inclusive, может быть null
        private final Integer toKey;   // exclusive, может быть null

        SubMap(Integer fromKey, Integer toKey) {
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        private boolean inRange(Integer key) {
            return (fromKey == null || key.compareTo(fromKey) >= 0) &&
                    (toKey == null || key.compareTo(toKey) < 0);
        }

        @Override
        public String put(Integer key, String value) {
            if (!inRange(key)) {
                throw new IllegalArgumentException("Key out of range");
            }
            return MyRbMap.this.put(key, value);
        }

        @Override
        public String get(Object key) {
            if (!(key instanceof Integer) || !inRange((Integer) key)) {
                return null;
            }
            return MyRbMap.this.get(key);
        }

        @Override
        public boolean containsKey(Object key) {
            if (!(key instanceof Integer) || !inRange((Integer) key)) {
                return false;
            }
            return MyRbMap.this.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            // Нужно проверить только значения в диапазоне
            return entrySet().stream()
                    .anyMatch(entry -> Objects.equals(entry.getValue(), value));
        }

        @Override
        public String remove(Object key) {
            if (!(key instanceof Integer) || !inRange((Integer) key)) {
                return null;
            }
            return MyRbMap.this.remove(key);
        }

        @Override
        public void clear() {
            // Удаляем все ключи в диапазоне
            List<Integer> keysToRemove = new ArrayList<>();
            for (Integer key : keySet()) {
                keysToRemove.add(key);
            }
            for (Integer key : keysToRemove) {
                remove(key);
            }
        }

        @Override
        public int size() {
            int count = 0;
            for (@SuppressWarnings("unused") Integer key : keySet()) {
                count++;
            }
            return count;
        }

        @Override
        public boolean isEmpty() {
            return !keySet().iterator().hasNext();
        }

        @Override
        public Set<Integer> keySet() {
            Set<Integer> keys = new HashSet<>();
            for (Integer key : MyRbMap.this.keySet()) {
                if (inRange(key)) {
                    keys.add(key);
                }
            }
            return keys;
        }

        @Override
        public Set<Entry<Integer, String>> entrySet() {
            Set<Entry<Integer, String>> entries = new HashSet<>();
            for (Entry<Integer, String> entry : MyRbMap.this.entrySet()) {
                if (inRange(entry.getKey())) {
                    entries.add(entry);
                }
            }
            return entries;
        }

        @Override
        public Comparator<? super Integer> comparator() {
            return null; // Используется естественный порядок
        }

        @Override
        public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            if (fromKey == null || toKey == null) {
                throw new NullPointerException();
            }
            if (fromKey.compareTo(toKey) > 0) {
                throw new IllegalArgumentException("fromKey > toKey");
            }

            // Объединяем ограничения
            Integer newFrom = (this.fromKey == null) ? fromKey :
                    Math.max(this.fromKey, fromKey);
            Integer newTo = (this.toKey == null) ? toKey :
                    Math.min(this.toKey, toKey);

            if (newFrom.compareTo(newTo) >= 0) {
                return new SubMap(newFrom, newFrom); // Пустой диапазон
            }

            return new SubMap(newFrom, newTo);
        }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            if (toKey == null) {
                throw new NullPointerException();
            }

            Integer newTo = (this.toKey == null) ? toKey :
                    Math.min(this.toKey, toKey);
            Integer newFrom = this.fromKey;

            if (newFrom != null && newFrom.compareTo(newTo) >= 0) {
                return new SubMap(newFrom, newFrom); // Пустой диапазон
            }

            return new SubMap(newFrom, newTo);
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            if (fromKey == null) {
                throw new NullPointerException();
            }

            Integer newFrom = (this.fromKey == null) ? fromKey :
                    Math.max(this.fromKey, fromKey);
            Integer newTo = this.toKey;

            if (newTo != null && newFrom.compareTo(newTo) >= 0) {
                return new SubMap(newFrom, newFrom); // Пустой диапазон
            }

            return new SubMap(newFrom, newTo);
        }

        @Override
        public Integer firstKey() {
            Iterator<Integer> it = keySet().iterator();
            if (!it.hasNext()) {
                throw new NoSuchElementException();
            }
            return it.next();
        }

        @Override
        public Integer lastKey() {
            // Для эффективности нужно найти максимальный ключ в диапазоне
            Integer last = null;
            for (Integer key : MyRbMap.this.keySet()) {
                if (inRange(key)) {
                    last = key;
                } else if (toKey != null && key.compareTo(toKey) >= 0) {
                    break; // Ключи дальше уже вне диапазона
                }
            }
            if (last == null) {
                throw new NoSuchElementException();
            }
            return last;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Integer key : MyRbMap.this.keySet()) {
                if (inRange(key)) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(key).append("=").append(MyRbMap.this.get(key));
                    first = false;
                } else if (toKey != null && key.compareTo(toKey) >= 0) {
                    break;
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Вспомогательные методы красно-черного дерева        ///////
    /////////////////////////////////////////////////////////////////////////

    private RbNode getNode(Integer key) {
        RbNode current = root;
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

    private boolean containsValue(RbNode node, Object value) {
        if (node == null) {
            return false;
        }

        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private RbNode getFirstNode() {
        if (root == null) {
            return null;
        }
        RbNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private RbNode getLastNode() {
        if (root == null) {
            return null;
        }
        RbNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    private void inOrderTraversal(RbNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////           Методы для балансировки красно-черного дерева   ///////
    /////////////////////////////////////////////////////////////////////////

    private void fixAfterInsertion(RbNode node) {
        while (node != null && node != root && colorOf(parentOf(node)) == RED) {
            if (parentOf(node) == leftOf(parentOf(parentOf(node)))) {
                RbNode y = rightOf(parentOf(parentOf(node)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(node), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    node = parentOf(parentOf(node));
                } else {
                    if (node == rightOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateLeft(node);
                    }
                    setColor(parentOf(node), BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    rotateRight(parentOf(parentOf(node)));
                }
            } else {
                RbNode y = leftOf(parentOf(parentOf(node)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(node), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    node = parentOf(parentOf(node));
                } else {
                    if (node == leftOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateRight(node);
                    }
                    setColor(parentOf(node), BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    rotateLeft(parentOf(parentOf(node)));
                }
            }
        }
        root.color = BLACK;
    }

    private void deleteNode(RbNode node) {
        if (node.left != null && node.right != null) {
            RbNode s = successor(node);
            node.key = s.key;
            node.value = s.value;
            node = s;
        }

        RbNode replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            node.left = node.right = node.parent = null;

            if (colorOf(node) == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (colorOf(node) == BLACK) {
                fixAfterDeletion(node);
            }

            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
    }

    private void fixAfterDeletion(RbNode node) {
        while (node != root && colorOf(node) == BLACK) {
            if (node == leftOf(parentOf(node))) {
                RbNode sib = rightOf(parentOf(node));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(node), RED);
                    rotateLeft(parentOf(node));
                    sib = rightOf(parentOf(node));
                }

                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(node));
                    }

                    setColor(sib, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(node));
                    node = root;
                }
            } else {
                RbNode sib = leftOf(parentOf(node));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(node), RED);
                    rotateRight(parentOf(node));
                    sib = leftOf(parentOf(node));
                }

                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(node));
                    }

                    setColor(sib, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(node));
                    node = root;
                }
            }
        }

        setColor(node, BLACK);
    }

    private RbNode successor(RbNode node) {
        if (node.right != null) {
            RbNode p = node.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            RbNode p = node.parent;
            RbNode ch = node;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private boolean colorOf(RbNode node) {
        return (node == null) ? BLACK : node.color;
    }

    private void setColor(RbNode node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private RbNode parentOf(RbNode node) {
        return (node == null) ? null : node.parent;
    }

    private RbNode leftOf(RbNode node) {
        return (node == null) ? null : node.left;
    }

    private RbNode rightOf(RbNode node) {
        return (node == null) ? null : node.right;
    }

    private void rotateLeft(RbNode p) {
        if (p != null) {
            RbNode r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(RbNode p) {
        if (p != null) {
            RbNode l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            l.parent = p.parent;
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }
            l.right = p;
            p.parent = l;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Методы, которые можно не имплементировать        ////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        fillKeys(root, keys);
        return keys;
    }

    private void fillKeys(RbNode node, Set<Integer> keys) {
        if (node != null) {
            fillKeys(node.left, keys);
            keys.add(node.key);
            fillKeys(node.right, keys);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        fillValues(root, values);
        return values;
    }

    private void fillValues(RbNode node, List<String> values) {
        if (node != null) {
            fillValues(node.left, values);
            values.add(node.value);
            fillValues(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new HashSet<>();
        fillEntries(root, entries);
        return entries;
    }

    private void fillEntries(RbNode node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            fillEntries(node.left, entries);
            entries.add(new SimpleEntry(node.key, node.value));
            fillEntries(node.right, entries);
        }
    }

    // Простая реализация Entry
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}