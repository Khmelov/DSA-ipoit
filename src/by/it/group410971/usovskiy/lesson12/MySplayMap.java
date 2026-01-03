package by.it.group410971.usovskiy.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    // ===== узел splay-дерева =====
    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
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
    }

    private Node root;
    private int size;

    // ========= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ДЕРЕВА =========

    private int compare(int k1, int k2) {
        return Integer.compare(k1, k2);
    }

    private void rotateLeft(Node x) {
        if (x == null) return;
        Node y = x.right;
        if (y == null) return;

        x.right = y.left;
        if (y.left != null) y.left.parent = x;

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

    private void rotateRight(Node x) {
        if (x == null) return;
        Node y = x.left;
        if (y == null) return;

        x.left = y.right;
        if (y.right != null) y.right.parent = x;

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

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // zig
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                // zig-zig (лево-лево)
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                // zig-zig (право-право)
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                // zig-zag (лево-право)
                rotateLeft(p);
                rotateRight(g);
            } else {
                // zig-zag (право-лево)
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x;
    }

    private Node firstNode() {
        Node n = root;
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node lastNode() {
        Node n = root;
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }

    private Node findNode(int key, boolean doSplay) {
        Node x = root;
        Node last = null;
        while (x != null) {
            last = x;
            int cmp = compare(key, x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else break;
        }
        if (doSplay && last != null) splay(last);
        return (x != null && x.key == key) ? x : null;
    }

    // ========= УДАЛЕНИЕ КОРНЯ =========
    private void removeRoot() {
        if (root == null) return;
        Node left = root.left;
        Node right = root.right;
        if (left != null) left.parent = null;
        if (right != null) right.parent = null;

        if (left == null) {
            root = right;
        } else if (right == null) {
            root = left;
        } else {
            // ищем максимум в левом поддереве
            Node x = left;
            while (x.right != null) x = x.right;
            // поднимаем его в корень левого дерева
            splayInSubtree(left, x);
            // теперь x – корень левого дерева
            root = x;
            root.right = right;
            right.parent = root;
        }
    }

    // локальный splay в поддереве (когда root ещё не заменён)
    private void splayInSubtree(Node subtreeRoot, Node x) {
        root = subtreeRoot;
        splay(x);
    }

    // ========= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ Map/NavigableMap =========

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null key");
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node p = root;
        Node parent = null;
        int cmp = 0;
        while (p != null) {
            parent = p;
            cmp = compare(key, p.key);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                String old = p.value;
                p.value = value;
                splay(p);
                return old;
            }
        }
        Node x = new Node(key, value, parent);
        if (cmp < 0) parent.left = x;
        else parent.right = x;
        splay(x);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key, true);
        return n == null ? null : n.value;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key, true);
        if (n == null) return null;
        String old = n.value;
        // n уже в корне после splay
        removeRoot();
        size--;
        return old;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node n = findNode((Integer) key, true);
        return n != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
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

    // ========= Навигационные методы по ключам =========

    private Node lowerNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (key <= cur.key) {
                cur = cur.left;
            } else {
                res = cur;
                cur = cur.right;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    private Node floorNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else {
                res = cur;
                cur = cur.right;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    private Node ceilingNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (key > cur.key) {
                cur = cur.right;
            } else {
                res = cur;
                cur = cur.left;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    private Node higherNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (key >= cur.key) {
                cur = cur.right;
            } else {
                res = cur;
                cur = cur.left;
            }
        }
        if (res != null) splay(res);
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = lowerNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = floorNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = ceilingNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(key);
        return n == null ? null : n.key;
    }

    // ========= first / last =========

    @Override
    public Integer firstKey() {
        Node n = firstNode();
        if (n == null) throw new NoSuchElementException();
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = lastNode();
        if (n == null) throw new NoSuchElementException();
        splay(n);
        return n.key;
    }

    // ========= диапазоны: headMap/tailMap/subMap =========

    private void rangeRec(Node n,
                          Integer fromKey, boolean fromIncl, boolean hasFrom,
                          Integer toKey, boolean toIncl, boolean hasTo,
                          MySplayMap res) {
        if (n == null) return;
        rangeRec(n.left, fromKey, fromIncl, hasFrom, toKey, toIncl, hasTo, res);

        boolean ok = true;
        if (hasFrom) {
            int cmp = compare(n.key, fromKey);
            if (cmp < 0 || (!fromIncl && cmp == 0)) ok = false;
        }
        if (hasTo) {
            int cmp = compare(n.key, toKey);
            if (cmp > 0 || (!toIncl && cmp == 0)) ok = false;
        }
        if (ok) res.put(n.key, n.value);

        rangeRec(n.right, fromKey, fromIncl, hasFrom, toKey, toIncl, hasTo, res);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        // SortedMap.headMap — строго меньше toKey
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        rangeRec(root, null, false, false, toKey, inclusive, true, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        // SortedMap.tailMap — >= fromKey
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        rangeRec(root, fromKey, inclusive, true, null, false, false, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        // SortedMap.subMap — [fromKey, toKey)
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        MySplayMap res = new MySplayMap();
        rangeRec(root, fromKey, fromInclusive, true, toKey, toInclusive, true, res);
        return res;
    }

    // ========= toString() — по возрастанию ключей =========

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = {true};
        inOrderToString(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inOrderToString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        inOrderToString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inOrderToString(n.right, sb, first);
    }

    // ========= Остальные методы NavigableMap/Map =========

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок Integer
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        if (k == null) return null;
        String v = get(k);
        return new Node(k, v, null);
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        if (k == null) return null;
        String v = get(k);
        return new Node(k, v, null);
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        if (k == null) return null;
        String v = get(k);
        return new Node(k, v, null);
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        if (k == null) return null;
        String v = get(k);
        return new Node(k, v, null);
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        Node n = firstNode();
        if (n == null) return null;
        return n;
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        Node n = lastNode();
        if (n == null) return null;
        return n;
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Node n = firstNode();
        if (n == null) return null;
        String val = remove(n.key);
        return new Node(n.key, val, null);
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Node n = lastNode();
        if (n == null) return null;
        String val = remove(n.key);
        return new Node(n.key, val, null);
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
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
}
