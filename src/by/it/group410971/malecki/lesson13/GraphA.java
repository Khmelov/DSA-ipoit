package by.it.group410971.malecki.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг графа
        Map<String, List<String>> graph = parseGraph(input);

        // Топологическая сортировка
        List<String> sorted = topologicalSort(graph);

        // Вывод результата
        for (int i = 0; i < sorted.size(); i++) {
            System.out.print(sorted.get(i));
            if (i < sorted.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        // Удаляем пробелы и разделяем по запятым
        String[] edges = input.split("\\s*,\\s*");

        for (String edge : edges) {
            // Разделяем на вершины
            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0];
            String to = vertices[1];

            // Добавляем в граф
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Сортируем списки смежности для лексикографического порядка
        for (List<String> list : graph.values()) {
            Collections.sort(list);
        }

        return graph;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();

        // Получаем все вершины и сортируем их
        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices); // лексикографический порядок

        // Важно: для корректного порядка нужно обходить вершины в определенном порядке
        // Используем алгоритм Кана
        return topologicalSortKahn(graph, vertices);
    }

    // Алгоритм Кана для топологической сортировки
    private static List<String> topologicalSortKahn(Map<String, List<String>> graph, List<String> vertices) {
        List<String> result = new ArrayList<>();

        // Вычисляем входящие степени вершин
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : vertices) {
            for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Очередь вершин с нулевой входящей степенью
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }
}