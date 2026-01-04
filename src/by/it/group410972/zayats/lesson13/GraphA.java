package by.it.group410972.zayats.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Считываем строку вида "0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1"
        String input = sc.nextLine();

        // Построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            // Добавляем вершины
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            // Считаем in-degree
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
        }

        // Лексикографический порядок: используем PriorityQueue
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : graph.keySet()) {
            if (inDegree.get(v) == 0) {
                queue.add(v);
            }
        }

        List<String> topoOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            String v = queue.poll();
            topoOrder.add(v);
            for (String neighbor : graph.get(v)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Проверка на наличие цикла
        if (topoOrder.size() != graph.size()) {
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна");
        } else {
            System.out.println(String.join(" ", topoOrder));
        }
    }
}
