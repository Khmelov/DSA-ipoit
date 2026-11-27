package by.it.group510972.bahutskaya.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

// Частые буквы получают короткий код. Это код Хаффмана.

public class TaskA {

    static private final Map<Character, String> codes = new TreeMap<>();
    private static int nextOrder;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        long startTime = System.currentTimeMillis();
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        codes.clear();
        nextOrder = 0;
        // Считаем, сколько раз встречается каждая буква
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char symbol = s.charAt(i);
            count.put(symbol, count.getOrDefault(symbol, 0) + 1);
        }

        // Кладём буквы в очередь и склеиваем два самых редких узла
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : new TreeMap<>(count).entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }
        Node root = priorityQueue.poll();
        if (count.size() == 1) {
            root.fillCodes("0");
        } else {
            root.fillCodes("");
        }
        // Заменяем каждую букву её кодом
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(codes.get(s.charAt(i)));
        }

        return sb.toString();
    }

    abstract class Node implements Comparable<Node> {
        private final int frequence;
        private final int order;

        private Node(int frequence) {
            this.frequence = frequence;
            this.order = nextOrder++;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            int byFrequency = Integer.compare(frequence, o.frequence);
            if (byFrequency != 0) {
                return byFrequency;
            }
            return Integer.compare(order, o.order);
        }
    }

    private class InternalNode extends Node {
        Node left;
        Node right;

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }

    }

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }

}
