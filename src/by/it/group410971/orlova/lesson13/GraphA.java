package by.it.a_khmelev.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг графа
        Map<String, List<String>> graph = parseGraph(input);

        // Выполнение топологической сортировки
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

        // Если строка пустая, возвращаем пустой граф
        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        // Удаляем пробелы в начале и конце
        input = input.trim();

        // Разделяем по запятой для получения списка рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Удаляем возможные пробелы в начале и конце
            edge = edge.trim();

            // Разделяем на вершину-источник и вершину-назначение
            String[] parts = edge.split(" -> ");

            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                // Добавляем ребро в граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                // Убедимся, что обе вершины есть в графе (даже если у них нет исходящих рёбер)
                graph.putIfAbsent(to, new ArrayList<>());
            }
        }

        return graph;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();

        if (graph.isEmpty()) {
            return result;
        }

        // Вычисляем полустепень захода для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();

        // Инициализируем степени захода нулями для всех вершин
        for (String vertex : graph.keySet()) {
            inDegree.put(vertex, 0);
        }

        // Вычисляем степени захода
        for (String vertex : graph.keySet()) {
            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Очередь вершин с нулевой степенью захода
        // Используем PriorityQueue для лексикографического порядка при равнозначности
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой степенью захода в очередь
        for (String vertex : graph.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        // Алгоритм Кана для топологической сортировки
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем степень захода для всех соседей
            if (graph.get(current) != null) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    // Если степень захода стала 0, добавляем в очередь
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Проверка на наличие циклов
        if (result.size() != graph.size()) {
            // Если есть цикл, все равно возвращаем сортировку, но без проверки
            // или можно выбросить исключение
            // throw new IllegalArgumentException("Graph has a cycle! Topological sort not possible.");
        }

        return result;
    }
}