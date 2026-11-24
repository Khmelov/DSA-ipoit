package by.it.group510971.rudko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

//Отображение Integer -> String на основе splay-дерева (самоперестраивающееся дерево поиска):
//найденный/добавленный ключ поднимается к корню (splay), поэтому частые запросы работают быстрее.
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        final int key;
        String value;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append("}").toString();
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;
        if (root == null) {
            root = new Node(k, value);
            size++;
            return null;
        }
        root = splay(root, k);
        if (root.key == k) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(k, value);
        if (k < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) return null;
        int k = toKey(key);
        root = splay(root, k);
        if (root.key != k) return null;
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            root = splay(root.left, k);   //наибольший ключ левого поддерева поднимется в корень
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node n = find(toKey(key));
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(toKey(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return maximum(root).key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////  поиск ближайших ключей (без перестроения дерева, обходом от корня)  //////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Integer lowerKey(Integer key) {
        int k = key;
        Node x = root, best = null;
        while (x != null) {
            if (x.key < k) {
                best = x;
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        int k = key;
        Node x = root, best = null;
        while (x != null) {
            if (x.key <= k) {
                best = x;
                if (x.key == k) break;
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        int k = key;
        Node x = root, best = null;
        while (x != null) {
            if (x.key >= k) {
                best = x;
                if (x.key == k) break;
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        int k = key;
        Node x = root, best = null;
        while (x != null) {
            if (x.key > k) {
                best = x;
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return best == null ? null : best.key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, null, true, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, fromKey, inclusive, null, true, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet не реализован");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values не реализован");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet не реализован");
    }

    //методы, возвращающие Map.Entry и представления, в этой работе не реализованы
    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("lowerEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("floorEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("ceilingEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("higherEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("firstEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("lastEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("pollFirstEntry не реализован");
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("pollLastEntry не реализован");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("descendingMap не реализован");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("navigableKeySet не реализован");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("descendingKeySet не реализован");
    }

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    private static int toKey(Object key) {
        return (Integer) key;
    }

    //сплэй: поднимает узел с ключом key (или последний посещенный узел пути) в корень
    private static Node splay(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) {
            if (h.left == null) return h;
            if (key < h.left.key) {                     //зиг-зиг
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (key > h.left.key) {              //зиг-заг
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) h.left = rotateLeft(h.left);
            }
            return (h.left == null) ? h : rotateRight(h);
        } else if (key > h.key) {
            if (h.right == null) return h;
            if (key > h.right.key) {                    //зиг-зиг
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            } else if (key < h.right.key) {             //зиг-заг
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) h.right = rotateRight(h.right);
            }
            return (h.right == null) ? h : rotateLeft(h);
        }
        return h;
    }

    private static Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private static Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    private Node find(int key) {
        if (root == null) return null;
        root = splay(root, key);
        return root.key == key ? root : null;
    }

    private static Node minimum(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private static Node maximum(Node x) {
        while (x.right != null) x = x.right;
        return x;
    }

    private static void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    private static boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    //копирует пары с ключами в заданном диапазоне (null - без границы) в result
    private static void copyRange(Node n, Integer from, boolean fromInc, Integer to, boolean toInc, MySplayMap result) {
        if (n == null) return;
        boolean aboveFrom = from == null || (fromInc ? n.key >= from : n.key > from);
        boolean belowTo = to == null || (toInc ? n.key <= to : n.key < to);
        if (aboveFrom) copyRange(n.left, from, fromInc, to, toInc, result);
        if (aboveFrom && belowTo) result.put(n.key, n.value);
        if (belowTo) copyRange(n.right, from, fromInc, to, toInc, result);
    }
}
