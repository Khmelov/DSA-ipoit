package by.it.group410972.ivanovich.lesson12;

import java.util.Map;
import java.util.NoSuchElementException;

public class MyAvlMap implements Map<Integer, String> {

    private static class AvlNode {
        Integer key;
        String value;
        AvlNode left;
        AvlNode right;
        int height;

        AvlNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }

    private AvlNode root;
    private int size;

    public MyAvlMap() {
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
        root = putNode(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
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
        String[] removedValue = new String[1];
        root = removeNode(root, key, removedValue);
        if (removedValue[0] != null) {
            size--;
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
        AvlNode node = findNode(root, key);
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
        return findNode(root, key) != null;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////
    //////////////////////// Вспомогательные методы АВЛ-дерева //////////////
    /////////////////////////////////////////////////////////////////////////

    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private int height(AvlNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AvlNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private AvlNode putNode(AvlNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new AvlNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putNode(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = putNode(node.right, key, value, oldValue);
        } else {
            // Ключ уже существует, обновляем значение
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        // Обновляем высоту текущего узла
        node.height = max(height(node.left), height(node.right)) + 1;

        // Получаем баланс фактор
        int balance = getBalance(node);

        // LL Case
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rotateRight(node);
        }

        // RR Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return rotateLeft(node);
        }

        // LR Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // RL Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AvlNode findNode(AvlNode node, Integer key) {
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

    private AvlNode minValueNode(AvlNode node) {
        AvlNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private AvlNode removeNode(AvlNode root, Integer key, String[] removedValue) {
        if (root == null) {
            return null;
        }

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = removeNode(root.left, key, removedValue);
        } else if (cmp > 0) {
            root.right = removeNode(root.right, key, removedValue);
        } else {
            // Узел для удаления найден
            removedValue[0] = root.value;

            // Узел с одним потомком или без потомков
            if (root.left == null || root.right == null) {
                AvlNode temp = (root.left != null) ? root.left : root.right;

                // Нет потомков
                if (temp == null) {
                    root = null;
                } else {
                    // Один потомок
                    root = temp;
                }
            } else {
                // Узел с двумя потомками
                AvlNode temp = minValueNode(root.right);
                root.key = temp.key;
                root.value = temp.value;
                root.right = removeNode(root.right, temp.key, new String[1]);
            }
        }

        // Если дерево было из одного узла
        if (root == null) {
            return null;
        }

        // Обновляем высоту текущего узла
        root.height = max(height(root.left), height(root.right)) + 1;

        // Получаем баланс фактор
        int balance = getBalance(root);

        // Балансировка

        // LL Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rotateRight(root);
        }

        // LR Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        // RR Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return rotateLeft(root);
        }

        // RL Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private boolean containsValue(AvlNode node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }
}