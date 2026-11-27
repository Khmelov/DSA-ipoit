package by.it.group510972.bahutskaya.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Есть ли в графе цикл.
public class TaskB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = parseGraph(input);
        boolean hasCycle = hasCycle(graph);
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
            if (vertices.length < 2 || vertices[0].isEmpty() || vertices[1].isEmpty()) {
                continue;
            }
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
        for (String vertex : graph.keySet()) {
            if (visit(vertex, graph, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean visit(String vertex, Map<String, List<String>> graph, Set<String> visited, Set<String> recursionStack) {
        // Вершина уже есть в текущем пути — это цикл
        if (recursionStack.contains(vertex)) {
            return true;
        }
        // Эту вершину уже проверяли
        if (visited.contains(vertex)) {
            return false;
        }
        recursionStack.add(vertex);
        for (String next : graph.getOrDefault(vertex, List.of())) {
            if (visit(next, graph, visited, recursionStack)) {
                return true;
            }
        }
        // Уходим из вершины и помечаем её проверенной
        recursionStack.remove(vertex);
        visited.add(vertex);
        return false;
    }
}
