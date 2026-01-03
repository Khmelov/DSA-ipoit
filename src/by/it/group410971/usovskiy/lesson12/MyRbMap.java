package by.it.group410971.usovskiy.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Set;

public class MyRbMap implements SortedMap<Integer, String> {

    // ======== узел красно-черного дерева ========
    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean red; // true = красный, false = черный

        Node(Integer key, String value, Node parent, boolean red) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.red = red;
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

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЕРЕВА ====================

    // сравнение ключей (натуральный порядок Integer)
    private int compare(int k1, int k2) {
        return Integer.compare(k1, k2);
    }

    private static boolean colorOf(Node n) {
        return n != null && n.red;
    }

    private static Node parentOf(Node n) {
        return n == null ? null : n.parent;
    }

    private static Node leftOf(Node n) {
        return n == null ? null : n.left;
    }

    private static Node rightOf(Node n) {
        return n == null ? null : n.right;
    }

    private static void setColor(Node n, boolean red) {
        if (n != null) n.red = red;
    }

    private void rotateLeft(Node p) {
        if (p == null) return;
        Node r = p.right;
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

    private void rotateRight(Node p) {
        if (p == null) return;
        Node l = p.left;
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

    private Node findNode(int key) {
        Node n = root;
        while (n != null) {
            int cmp = compare(key, n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n;
        }
        return null;
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

    private Node successor(Node n) {
        if (n == null) return null;
        if (n.right != null) {
            n = n.right;
            while (n.left != null) n = n.left;
            return n;
        }
        Node p = n.parent;
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }

    // ====================== ВСТАВКА ======================

    private void fixAfterInsertion(Node x) {
        x.red = true;
        while (x != null && x != root && colorOf(parentOf(x))) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x))); // дядя
                if (colorOf(y)) {
                    setColor(parentOf(x), false);
                    setColor(y, false);
                    setColor(parentOf(parentOf(x)), true);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), false);
                    setColor(parentOf(parentOf(x)), true);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x))); // зеркально
                if (colorOf(y)) {
                    setColor(parentOf(x), false);
                    setColor(y, false);
                    setColor(parentOf(parentOf(x)), true);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), false);
                    setColor(parentOf(parentOf(x)), true);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.red = false;
    }

    // ====================== УДАЛЕНИЕ ======================

    private void fixAfterDeletion(Node x) {
        while (x != root && !colorOf(x)) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib)) {
                    setColor(sib, false);
                    setColor(parentOf(x), true);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (!colorOf(leftOf(sib)) && !colorOf(rightOf(sib))) {
                    setColor(sib, true);
                    x = parentOf(x);
                } else {
                    if (!colorOf(rightOf(sib))) {
                        setColor(leftOf(sib), false);
                        setColor(sib, true);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), false);
                    setColor(rightOf(sib), false);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // зеркальный случай
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib)) {
                    setColor(sib, false);
                    setColor(parentOf(x), true);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (!colorOf(rightOf(sib)) && !colorOf(leftOf(sib))) {
                    setColor(sib, true);
                    x = parentOf(x);
                } else {
                    if (!colorOf(leftOf(sib))) {
                        setColor(rightOf(sib), false);
                        setColor(sib, true);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), false);
                    setColor(leftOf(sib), false);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        if (x != null) x.red = false;
    }

    private void deleteNode(Node p) {
        if (p == null) return;

        // если у узла два ребёнка — заменяем его преемником
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        // теперь у p максимум один непустой ребёнок
        Node replacement = (p.left != null) ? p.left : p.right;

        if (replacement != null) { // один ребёнок
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;

            if (!p.red) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) { // корень и лист
            root = null;
        } else { // лист
            if (!p.red) {
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) p.parent.left = null;
                else if (p == p.parent.right) p.parent.right = null;
                p.parent = null;
            }
        }
    }

    // ====================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ MAP/SortedMap ======================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = { true };
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null key");
        if (root == null) {
            root = new Node(key, value, null, false); // корень – черный
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
                return old;
            }
        }
        Node x = new Node(key, value, parent, true); // новый – красный
        if (cmp < 0) parent.left = x;
        else parent.right = x;
        fixAfterInsertion(x);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        if (n == null) return null;
        String old = n.value;
        deleteNode(n);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
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

    // ====================== SortedMap-методы ======================

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        headMapRec(root, toKey, res);
        return res;
    }

    private void headMapRec(Node n, int toKey, MyRbMap res) {
        if (n == null) return;
        if (n.key >= toKey) {
            headMapRec(n.left, toKey, res);
        } else {
            headMapRec(n.left, toKey, res);
            res.put(n.key, n.value);
            headMapRec(n.right, toKey, res);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        tailMapRec(root, fromKey, res);
        return res;
    }

    private void tailMapRec(Node n, int fromKey, MyRbMap res) {
        if (n == null) return;
        if (n.key < fromKey) {
            tailMapRec(n.right, fromKey, res);
        } else {
            tailMapRec(n.left, fromKey, res);
            res.put(n.key, n.value);
            tailMapRec(n.right, fromKey, res);
        }
    }

    @Override
    public Integer firstKey() {
        Node f = firstNode();
        if (f == null) throw new NoSuchElementException();
        return f.key;
    }

    @Override
    public Integer lastKey() {
        Node l = lastNode();
        if (l == null) throw new NoSuchElementException();
        return l.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        // null – значит используется естественный порядок Integer
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        // В задании не требуется, можно сделать через head/tail
        if (fromKey == null || toKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        subMapRec(root, fromKey, toKey, res);
        return res;
    }

    private void subMapRec(Node n, int fromKey, int toKey, MyRbMap res) {
        if (n == null) return;
        if (n.key >= toKey) {
            subMapRec(n.left, fromKey, toKey, res);
        } else if (n.key < fromKey) {
            subMapRec(n.right, fromKey, toKey, res);
        } else {
            subMapRec(n.left, fromKey, toKey, res);
            res.put(n.key, n.value);
            subMapRec(n.right, fromKey, toKey, res);
        }
    }

    // ====================== Остальные методы Map (заглушки) ======================

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
