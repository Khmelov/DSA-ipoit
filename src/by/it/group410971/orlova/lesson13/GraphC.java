package by.it.a_khmelev.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        // Парсинг графа
        Map<String, List<String>> graph = new HashMap<>();
        if (!input.isEmpty()) {
            String[] edges = input.split(", ");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                if (parts.length == 2) {
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                    graph.putIfAbsent(to, new ArrayList<>());
                }
            }
        }

        // Находим компоненты сильной связности с помощью алгоритма Косарайю
        List<Set<String>> components = kosaraju(graph);

        // Преобразуем Set в List и сортируем
        List<List<String>> sortedComponents = new ArrayList<>();
        for (Set<String> component : components) {
            List<String> list = new ArrayList<>(component);
            Collections.sort(list);
            sortedComponents.add(list);
        }

        // Сортируем компоненты для вывода
        // В примере: C (1), ABDHI (5), E (1), FGK (3)
        // Порядок не по размеру и не по алфавиту первой вершины
        // Нужно сохранить порядок, который дает алгоритм Косарайю

        // Выводим результат
        for (List<String> component : sortedComponents) {
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    private static List<Set<String>> kosaraju(Map<String, List<String>> graph) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Первый проход DFS (прямой граф)
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsForward(vertex, graph, visited, stack);
            }
        }

        // Транспонируем граф
        Map<String, List<String>> transposed = transpose(graph);

        // Второй проход DFS (транспонированный граф)
        visited.clear();
        List<Set<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new HashSet<>();
                dfsBackward(vertex, transposed, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfsForward(String vertex, Map<String, List<String>> graph,
                                   Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsForward(neighbor, graph, visited, stack);
            }
        }

        stack.push(vertex);
    }

    private static Map<String, List<String>> transpose(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();

        // Инициализируем все вершины
        for (String vertex : graph.keySet()) {
            transposed.put(vertex, new ArrayList<>());
        }

        // Транспонируем ребра
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                transposed.get(to).add(from);
            }
        }

        return transposed;
    }

    private static void dfsBackward(String vertex, Map<String, List<String>> graph,
                                    Set<String> visited, Set<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsBackward(neighbor, graph, visited, component);
            }
        }
    }
}