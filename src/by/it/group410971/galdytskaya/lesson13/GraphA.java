package by.it.group410971.galdytskaya.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        // парсим граф из строки во множество рёбер
        Map<String, Set<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to); // создание TreeSet для упорядочивания смежных вершин
        }

        // построение инцидентности для подсчёта входящих ребер
        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : vertices) {
            inDegree.put(v, 0);
        }

        for (Set<String> neighbors : graph.values()) {
            for (String w : neighbors) {
                inDegree.put(w, inDegree.get(w) + 1);
            }
        }

        // создание очереди с приоритетом
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices) {
            if (inDegree.get(v) == 0) {
                queue.offer(v);
            }
        }

        List<String> topoOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            String v = queue.poll();
            topoOrder.add(v);

            if (graph.containsKey(v)) {
                for (String w : graph.get(v)) {
                    inDegree.put(w, inDegree.get(w) - 1);
                    if (inDegree.get(w) == 0) {
                        queue.offer(w);
                    }
                }
            }
        }

        if (topoOrder.size() == vertices.size()) {
            System.out.println(String.join(" ", topoOrder));
        } else {
            System.out.println("Graph has cycle, no topological order");
        }
    }
}