package by.it.group410971.kondratenya.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг графа
        Map<String, List<String>> graph = parseGraph(input);

        // Находим компоненты сильной связности
        List<List<String>> scc = kosaraju(graph);

        // Сортируем компоненты и вершины в них
        sortComponents(scc);

        // Вывод результата
        for (int i = 0; i < scc.size(); i++) {
            for (String vertex : scc.get(i)) {
                System.out.print(vertex);
            }
            if (i < scc.size() - 1) {
                System.out.println();
            }
        }
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

    private static List<List<String>> kosaraju(Map<String, List<String>> graph) {
        // Шаг 1: Первый проход DFS для определения порядка выхода
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        // Важно: сортируем вершины перед обходом для детерминированного порядка
        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, order);
            }
        }

        // Шаг 2: Транспонирование графа
        Map<String, List<String>> transposed = transposeGraph(graph);

        // Шаг 3: Второй проход DFS в обратном порядке
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, transposed, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, List<String> order) {
        visited.add(vertex);

        // Получаем соседей и сортируем их
        List<String> neighbors = new ArrayList<>(graph.getOrDefault(vertex, new ArrayList<>()));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, order);
            }
        }

        order.add(vertex);
    }

    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            transposed.putIfAbsent(from, new ArrayList<>());

            for (String to : entry.getValue()) {
                transposed.putIfAbsent(to, new ArrayList<>());
                transposed.get(to).add(from);
            }
        }

        return transposed;
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> transposed,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        // Получаем соседей и сортируем их
        List<String> neighbors = new ArrayList<>(transposed.getOrDefault(vertex, new ArrayList<>()));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, transposed, visited, component);
            }
        }
    }

    private static void sortComponents(List<List<String>> scc) {
        // Сортируем вершины внутри каждой компоненты
        for (List<String> component : scc) {
            Collections.sort(component);
        }

        // Сортируем компоненты по первой вершине, но с учетом того,
        // что компоненты должны идти в порядке обхода графа
        // Для сохранения порядка Косарайю не сортируем компоненты между собой
        // Вместо этого сортируем внутри них
    }
}