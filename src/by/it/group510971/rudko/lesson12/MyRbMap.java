package by.it.group510971.rudko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

//Отображение Integer -> String на основе красно-черного дерева (алгоритм CLRS, с общим листом NIL).
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        boolean red;
        Node left, right, parent;

        Node(int key, String value, boolean red) {
            this.key = key;
            this.value = value;
            this.red = red;
        }
    }

    private final Node NIL;     //общий "лист" - всегда черный
    private Node root;
    private int size;

    public MyRbMap() {
        NIL = new Node(0, null, false);
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }

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
        root = NIL;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;
        Node y = NIL, x = root;
        while (x != NIL) {
            y = x;
            if (k == x.key) {
                String old = x.value;
                x.value = value;
                return old;
            }
            x = (k < x.key) ? x.left : x.right;
        }
        Node z = new Node(k, value, true);
        z.left = z.right = NIL;
        z.parent = y;
        if (y == NIL) {
            root = z;
        } else if (k < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }
        size++;
        insertFixup(z);
        return null;
    }

    @Override
    public String remove(Object key) {
        Node z = find(toKey(key));
        if (z == NIL) return null;
        String old = z.value;
        deleteNode(z);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node n = find(toKey(key));
        return n == NIL ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(toKey(key)) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, null, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, null, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
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
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, toKey, result);
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

    /////////////////////////////////////////////////////////////////////////
    //////                     служебные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    private static int toKey(Object key) {
        return (Integer) key;
    }

    private Node find(int key) {
        Node x = root;
        while (x != NIL && x.key != key) {
            x = (key < x.key) ? x.left : x.right;
        }
        return x;
    }

    private Node minimum(Node x) {
        while (x.left != NIL) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        while (x.right != NIL) x = x.right;
        return x;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) {
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
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    //восстановление свойств после вставки z (красного)
    private void insertFixup(Node z) {
        while (z.parent.red) {
            Node p = z.parent;
            Node g = p.parent;
            if (p == g.left) {
                Node uncle = g.right;
                if (uncle.red) {
                    p.red = false;
                    uncle.red = false;
                    g.red = true;
                    z = g;
                } else {
                    if (z == p.right) {
                        z = p;
                        rotateLeft(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node uncle = g.left;
                if (uncle.red) {
                    p.red = false;
                    uncle.red = false;
                    g.red = true;
                    z = g;
                } else {
                    if (z == p.left) {
                        z = p;
                        rotateRight(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.red = false;
    }

    //заменяет поддерево u поддеревом v
    private void transplant(Node u, Node v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yWasRed = y.red;
        Node x;
        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yWasRed = y.red;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.red = z.red;
        }
        if (!yWasRed) deleteFixup(x);
    }

    //восстановление свойств после удаления: x несет "лишний" черный
    private void deleteFixup(Node x) {
        while (x != root && !x.red) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.red && !w.right.red) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (!w.right.red) {
                        w.left.red = false;
                        w.red = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    w.right.red = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (!w.right.red && !w.left.red) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (!w.left.red) {
                        w.right.red = false;
                        w.red = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    w.left.red = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.red = false;
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == NIL) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == NIL) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    //копирует в result пары с ключами в [from, to); null означает отсутствие границы
    private void copyRange(Node n, Integer from, Integer to, MyRbMap result) {
        if (n == NIL) return;
        if (from == null || n.key >= from) {
            copyRange(n.left, from, to, result);
        }
        if ((from == null || n.key >= from) && (to == null || n.key < to)) {
            result.put(n.key, n.value);
        }
        if (to == null || n.key < to) {
            copyRange(n.right, from, to, result);
        }
    }
}
