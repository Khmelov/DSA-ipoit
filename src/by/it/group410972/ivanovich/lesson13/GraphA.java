package by.it.group410972.ivanovich.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = parseInput(input);
        List<String> topologicalOrder = topologicalSort(graph);

        // Вывод результата
        for (int i = 0; i < topologicalOrder.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(topologicalOrder.get(i));
        }
    }

    // Метод для парсинга входной строки
    private static Map<String, List<String>> parseInput(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        // Разделяем по запятой для получения пар "вершина -> соседи"
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            // Разделяем на вершину и её соседей
            String[] parts = edge.split("\\s*->\\s*");
            String vertex = parts[0].trim();
            String neighbor = parts[1].trim();

            // Добавляем в граф
            graph.putIfAbsent(vertex, new ArrayList<>());
            graph.get(vertex).add(neighbor);

            // Убедимся, что сосед тоже есть в графе (даже если у него нет исходящих рёбер)
            graph.putIfAbsent(neighbor, new ArrayList<>());
        }

        return graph;
    }

    // Метод для топологической сортировки
    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        Map<String, Integer> inDegree = new HashMap<>();
        List<String> result = new ArrayList<>();

        // Инициализация степеней входа
        for (String vertex : graph.keySet()) {
            inDegree.putIfAbsent(vertex, 0);
        }

        // Вычисление степеней входа
        for (String vertex : graph.keySet()) {
            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Приоритетная очередь для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой степенью входа
        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        // Алгоритм Кана
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем степени входа соседей
            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Проверка на наличие цикла
        if (result.size() != graph.size()) {
            throw new IllegalArgumentException("Граф содержит цикл, топологическая сортировка невозможна");
        }

        return result;
    }
}