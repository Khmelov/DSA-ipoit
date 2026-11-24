package by.it.group510971.rudko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

//Отображение Integer -> String на основе АВЛ-дерева (самобалансирующееся дерево поиска по высоте).
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ (кроме интерфейсов и исключений).
public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        final int key;
        String value;
        int height = 1;
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
        Node found = find(root, key);
        if (found != null) {
            String old = found.value;
            found.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node found = find(root, toKey(key));
        if (found == null) return null;
        String old = found.value;
        root = delete(root, found.key);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node found = find(root, toKey(key));
        return found == null ? null : found.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, toKey(key)) != null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
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

    private static Node find(Node n, int key) {
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    private static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private static void update(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private static Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        update(y);
        update(x);
        return x;
    }

    private static Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        return y;
    }

    //пересчитывает высоту узла и восстанавливает баланс (разность высот детей не больше 1)
    private static Node rebalance(Node n) {
        update(n);
        int balance = height(n.left) - height(n.right);
        if (balance > 1) {
            if (height(n.left.left) < height(n.left.right)) {
                n.left = rotateLeft(n.left);   //большой правый поворот
            }
            return rotateRight(n);
        }
        if (balance < -1) {
            if (height(n.right.right) < height(n.right.left)) {
                n.right = rotateRight(n.right); //большой левый поворот
            }
            return rotateLeft(n);
        }
        return n;
    }

    private static Node insert(Node n, int key, String value) {
        if (n == null) return new Node(key, value);
        if (key < n.key) n.left = insert(n.left, key, value);
        else n.right = insert(n.right, key, value);
        return rebalance(n);
    }

    private static Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node min = n.right;                   //преемник - минимум правого поддерева
            while (min.left != null) min = min.left;
            Node replaced = new Node(min.key, min.value);
            replaced.left = n.left;
            replaced.right = delete(n.right, min.key);
            n = replaced;
        }
        return rebalance(n);
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
}
