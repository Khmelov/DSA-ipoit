package by.it.group410972.ivanovich.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = parseInput(input);

        if (hasCycle(graph)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
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

    // Метод для проверки наличия циклов с использованием DFS
    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Вспомогательный метод DFS для обнаружения циклов
    private static boolean dfs(String vertex, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в стеке рекурсии - найден цикл
        if (recursionStack.contains(vertex)) {
            return true;
        }

        // Если вершина уже посещена и обработана полностью
        if (visited.contains(vertex)) {
            return false;
        }

        // Помечаем вершину как посещённую и добавляем в стек рекурсии
        visited.add(vertex);
        recursionStack.add(vertex);

        // Рекурсивно проверяем всех соседей
        for (String neighbor : graph.get(vertex)) {
            if (dfs(neighbor, graph, visited, recursionStack)) {
                return true;
            }
        }

        // Удаляем вершину из стека рекурсии после обработки всех соседей
        recursionStack.remove(vertex);
        return false;
    }
}