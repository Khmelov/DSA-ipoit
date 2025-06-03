package by.it.group410972.pahodnia.lesson03;

import java.io.InputStream;
import java.util.*;

public class A_Huffman {
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        if (inputStream == null) {
            System.err.println("Input file not found!");
            return;
        }

        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();

        try {
            String result = instance.encode(inputStream);
            long finishTime = System.currentTimeMillis();

            // Выводим результаты
            System.out.printf("%d %d\n", codes.size(), result.length());
            for (Map.Entry<Character, String> entry : codes.entrySet()) {
                System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
            }
            System.out.println(result);
            System.out.printf("Execution time: %d ms\n", finishTime - startTime);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    String encode(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next(); // Читаем только одно слово
        scanner.close();

        // Подсчитываем частоту символов
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // Создаем приоритетную очередь для узлов дерева Хаффмана
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Строим дерево Хаффмана
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        // Генерируем коды для символов
        if (!priorityQueue.isEmpty()) {
            Node root = priorityQueue.poll();
            root.fillCodes("");
        }

        // Кодируем строку
        StringBuilder encodedString = new StringBuilder();
        for (char c : s.toCharArray()) {
            encodedString.append(codes.get(c));
        }

        return encodedString.toString();
    }

    static abstract class Node implements Comparable<Node> {
        private final int frequency;

        private Node(int frequency) {
            this.frequency = frequency;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequency, o.frequency);
        }
    }

    static private class InternalNode extends Node {
        Node left, right;

        InternalNode(Node left, Node right) {
            super(left.frequency + right.frequency);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            if (left != null) left.fillCodes(code + "0");
            if (right != null) right.fillCodes(code + "1");
        }
    }
    static private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequency, char symbol) {
            super(frequency);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }
}
