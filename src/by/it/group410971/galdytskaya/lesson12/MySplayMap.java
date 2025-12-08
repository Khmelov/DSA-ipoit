package by.it.group410971.galdytskaya.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node splay(Node root, Integer key) {
        if (root == null) return null;

        int cmp1 = key.compareTo(root.key);
        if (cmp1 == 0) return root;

        if (cmp1 < 0) {
            if (root.left == null) return root;
            int cmp2 = key.compareTo(root.left.key);
            if (cmp2 < 0) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            } else if (cmp2 > 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }
            return root.left == null ? root : rightRotate(root);
        } else {
            if (root.right == null) return root;
            int cmp2 = key.compareTo(root.right.key);
            if (cmp2 > 0) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            } else if (cmp2 < 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            }
            return root.right == null ? root : leftRotate(root);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
        Node newNode = new Node(key, value);
        if (cmp < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
            root = newNode;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
            root = newNode;
        }
        size++;
        return null;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (root == null) return null;
        root = splay(root, key);
        if (root.key.equals(key)) return root.value;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (root == null) return null;
        root = splay(root, key);
        if (!root.key.equals(key)) return null;
        String removedValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSubtree = root.right;
            root = root.left;
            root = splay(root, key);
            root.right = rightSubtree;
        }
        size--;
        return removedValue;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValueInternal(root, (String) value);
    }

    private boolean containsValueInternal(Node node, String value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueInternal(node.left, value) || containsValueInternal(node.right, value);
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringInOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void toStringInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringInOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toStringInOrder(node.right, sb);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap res = new MySplayMap();
        headMapFill(root, toKey, res);
        return res;
    }

    private void headMapFill(Node node, Integer toKey, MySplayMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            map.put(node.key, node.value);
            headMapFill(node.left, toKey, map);
            headMapFill(node.right, toKey, map);
        } else {
            headMapFill(node.left, toKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap res = new MySplayMap();
        tailMapFill(root, fromKey, res);
        return res;
    }

    private void tailMapFill(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            map.put(node.key, node.value);
            tailMapFill(node.left, fromKey, map);
            tailMapFill(node.right, fromKey, map);
        } else {
            tailMapFill(node.right, fromKey, map);
        }
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp <= 0) {
                cur = cur.left;
            } else {
                res = cur.key;
                cur = cur.right;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp == 0) {
                return key;
            } else {
                res = cur.key;
                cur = cur.right;
            }
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp > 0) {
                cur = cur.right;
            } else {
                res = cur.key;
                cur = cur.left;
            }
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp >= 0) {
                cur = cur.right;
            } else {
                res = cur.key;
                cur = cur.left;
            }
        }
        return res;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap subMap = new MySplayMap();
        for (Map.Entry<Integer, String> entry : entrySetInternal()) {
            Integer k = entry.getKey();
            boolean fromCond = fromKey == null || (fromInclusive ? k >= fromKey : k > fromKey);
            boolean toCond = toKey == null || (toInclusive ? k <= toKey : k < toKey);
            if (fromCond && toCond) {
                subMap.put(k, entry.getValue());
            }
        }
        return subMap;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    private List<Map.Entry<Integer, String>> entrySetInternal() {
        List<Map.Entry<Integer, String>> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }

    private void inorder(Node node, List<Map.Entry<Integer, String>> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        inorder(node.right, list);
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) throw new NullPointerException("Map is null");
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

}
