package by.it.group410971.kondratenya.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг графа
        Map<String, List<String>> graph = parseGraph(input);

        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input.trim().isEmpty()) {
            return graph;
        }

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

        return graph;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        // Получаем все вершины
        List<String> vertices = new ArrayList<>(graph.keySet());

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                if (dfsHasCycle(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(vertex)) {
            return true; // Найден цикл
        }

        if (visited.contains(vertex)) {
            return false;
        }

        visited.add(vertex);
        recursionStack.add(vertex);

        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (dfsHasCycle(neighbor, graph, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(vertex);
        return false;
    }
}