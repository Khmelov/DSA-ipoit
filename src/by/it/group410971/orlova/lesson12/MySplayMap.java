package by.it.a_khmelev.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;
        SplayNode parent;
        
        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private SplayNode root;
    private int size;
    
    public MySplayMap() {
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
            root = new SplayNode(key, value);
            size = 1;
            return null;
        }
        
        // Находим узел или позицию для вставки
        SplayNode node = findNode(key);
        if (node.key.equals(key)) {
            // Ключ уже существует - заменяем значение
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        } else {
            // Вставляем новый узел
            SplayNode newNode = new SplayNode(key, value);
            if (key.compareTo(node.key) < 0) {
                node.left = newNode;
                newNode.parent = node;
            } else {
                node.right = newNode;
                newNode.parent = node;
            }
            size++;
            splay(newNode);
            return null;
        }
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        
        Integer k = (Integer) key;
        SplayNode node = findNode(k);
        if (node == null || !node.key.equals(k)) {
            return null;
        }
        
        String oldValue = node.value;
        
        // Удаляем узел
        if (node.left == null) {
            // Нет левого ребенка
            transplant(node, node.right);
        } else if (node.right == null) {
            // Нет правого ребенка
            transplant(node, node.left);
        } else {
            // Есть оба ребенка
            SplayNode successor = minimum(node.right);
            if (successor.parent != node) {
                transplant(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }
            transplant(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
        }
        
        size--;
        if (node.parent != null) {
            splay(node.parent);
        }
        
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
        
        SplayNode node = findNode((Integer) key);
        if (node != null && node.key.equals(key)) {
            splay(node);
            return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        
        SplayNode node = findNode((Integer) key);
        boolean found = (node != null && node.key.equals(key));
        if (found) {
            splay(node);
        }
        return found;
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
        SplayNode node = minimum(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        SplayNode node = maximum(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        SplayNode node = lowerNode(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        SplayNode node = floorNode(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        SplayNode node = ceilingNode(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        SplayNode node = higherNode(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    /////////////////////////////////////////////////////////////////////////
    //////            Методы Splay-дерева (splaying)                 ///////
    /////////////////////////////////////////////////////////////////////////

    private void splay(SplayNode x) {
        if (x == null) return;
        
        while (x.parent != null) {
            if (x.parent.parent == null) {
                // Zig
                if (x == x.parent.left) {
                    rotateRight(x.parent);
                } else {
                    rotateLeft(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                // Zig-zig (левый-левый)
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                // Zig-zig (правый-правый)
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                // Zig-zag (левый-правый)
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                // Zig-zag (правый-левый)
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
        root = x;
    }

    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        if (y == null) return;
        
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(SplayNode x) {
        SplayNode y = x.left;
        if (y == null) return;
        
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        
        y.right = x;
        x.parent = y;
    }

    /////////////////////////////////////////////////////////////////////////
    //////            Вспомогательные методы дерева                  ///////
    /////////////////////////////////////////////////////////////////////////

    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        SplayNode last = null;
        
        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return last;
    }

    private boolean containsValue(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }
        
        if ((value == null && node.value == null) || 
            (value != null && value.equals(node.value))) {
            splay(node);
            return true;
        }
        
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private SplayNode minimum(SplayNode node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode maximum(SplayNode node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void transplant(SplayNode u, SplayNode v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private SplayNode lowerNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;
        
        while (current != null) {
            if (current.key.compareTo(key) < 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    private SplayNode floorNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;
        
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    private SplayNode ceilingNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;
        
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp == 0) {
                return current;
            } else if (cmp > 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    private SplayNode higherNode(Integer key) {
        SplayNode current = root;
        SplayNode result = null;
        
        while (current != null) {
            if (current.key.compareTo(key) > 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////      Методы NavigableMap (необязательные для C)           ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap headMap = new MySplayMap();
        fillHeadMap(root, toKey, inclusive, headMap);
        return headMap;
    }
    
    private void fillHeadMap(SplayNode node, Integer toKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        
        int cmp = toKey.compareTo(node.key);
        
        if (cmp > 0 || (inclusive && cmp == 0)) {
            fillHeadMap(node.left, toKey, inclusive, map);
            map.put(node.key, node.value);
            fillHeadMap(node.right, toKey, inclusive, map);
        } else {
            fillHeadMap(node.left, toKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap tailMap = new MySplayMap();
        fillTailMap(root, fromKey, inclusive, tailMap);
        return tailMap;
    }
    
    private void fillTailMap(SplayNode node, Integer fromKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        
        int cmp = fromKey.compareTo(node.key);
        
        if (cmp < 0 || (inclusive && cmp == 0)) {
            fillTailMap(node.left, fromKey, inclusive, map);
            map.put(node.key, node.value);
            fillTailMap(node.right, fromKey, inclusive, map);
        } else {
            fillTailMap(node.right, fromKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, 
                                                Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }
        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        
        MySplayMap subMap = new MySplayMap();
        fillSubMap(root, fromKey, fromInclusive, toKey, toInclusive, subMap);
        return subMap;
    }
    
    private void fillSubMap(SplayNode node, Integer fromKey, boolean fromInclusive,
                           Integer toKey, boolean toInclusive, MySplayMap map) {
        if (node == null) return;
        
        int cmpFrom = fromKey.compareTo(node.key);
        int cmpTo = toKey.compareTo(node.key);
        
        if (cmpFrom < 0 || (fromInclusive && cmpFrom == 0)) {
            fillSubMap(node.left, fromKey, fromInclusive, toKey, toInclusive, map);
        }
        
        if ((cmpFrom < 0 || (fromInclusive && cmpFrom == 0)) &&
            (cmpTo > 0 || (toInclusive && cmpTo == 0))) {
            map.put(node.key, node.value);
        }
        
        if (cmpTo > 0 || (toInclusive && cmpTo == 0)) {
            fillSubMap(node.right, fromKey, fromInclusive, toKey, toInclusive, map);
        }
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
    public Entry<Integer, String> lowerEntry(Integer key) {
        Integer lowerKey = lowerKey(key);
        if (lowerKey == null) return null;
        return new SimpleEntry(lowerKey, get(lowerKey));
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Integer floorKey = floorKey(key);
        if (floorKey == null) return null;
        return new SimpleEntry(floorKey, get(floorKey));
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Integer ceilingKey = ceilingKey(key);
        if (ceilingKey == null) return null;
        return new SimpleEntry(ceilingKey, get(ceilingKey));
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Integer higherKey = higherKey(key);
        if (higherKey == null) return null;
        return new SimpleEntry(higherKey, get(higherKey));
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        if (size == 0) return null;
        Integer firstKey = firstKey();
        return new SimpleEntry(firstKey, get(firstKey));
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (size == 0) return null;
        Integer lastKey = lastKey();
        return new SimpleEntry(lastKey, get(lastKey));
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        if (size == 0) return null;
        Integer firstKey = firstKey();
        String value = remove(firstKey);
        return new SimpleEntry(firstKey, value);
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        if (size == 0) return null;
        Integer lastKey = lastKey();
        String value = remove(lastKey);
        return new SimpleEntry(lastKey, value);
    }

    /////////////////////////////////////////////////////////////////////////
    //////            Остальные методы Map интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
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
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        collectKeys(root, keys);
        return keys;
    }
    
    private void collectKeys(SplayNode node, Set<Integer> keys) {
        if (node != null) {
            collectKeys(node.left, keys);
            keys.add(node.key);
            collectKeys(node.right, keys);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        collectValues(root, values);
        return values;
    }
    
    private void collectValues(SplayNode node, List<String> values) {
        if (node != null) {
            collectValues(node.left, values);
            values.add(node.value);
            collectValues(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new HashSet<>();
        collectEntries(root, entries);
        return entries;
    }
    
    private void collectEntries(SplayNode node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            collectEntries(node.left, entries);
            entries.add(new SimpleEntry(node.key, node.value));
            collectEntries(node.right, entries);
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
}