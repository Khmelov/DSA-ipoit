package by.it.group410972.ivanovich.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = parseInput(input);
        List<List<String>> sccs = findStronglyConnectedComponents(graph);

        // Вывод компонент сильной связности
        for (List<String> component : sccs) {
            Collections.sort(component);
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
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

    // Метод для нахождения компонент сильной связности (алгоритм Косарайю)
    private static List<List<String>> findStronglyConnectedComponents(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS для заполнения стека порядка завершения обработки
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Транспонируем граф
        Map<String, List<String>> transposedGraph = transposeGraph(graph);

        // Второй проход DFS в порядке, обратном порядку завершения
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, transposedGraph, visited, component);
                sccs.add(component);
            }
        }

        // Сортируем компоненты в нужном порядке (истоки -> стоки)
        sortComponentsByTopologicalOrder(sccs, graph);

        return sccs;
    }

    // Первый проход DFS для заполнения стека
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        stack.push(vertex);
    }

    // Транспонирование графа (обращение направления всех рёбер)
    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();

        // Инициализируем все вершины
        for (String vertex : graph.keySet()) {
            transposed.put(vertex, new ArrayList<>());
        }

        // Добавляем обратные рёбра
        for (String vertex : graph.keySet()) {
            for (String neighbor : graph.get(vertex)) {
                transposed.get(neighbor).add(vertex);
            }
        }

        return transposed;
    }

    // Второй проход DFS для нахождения компонент
    private static void dfsSecondPass(String vertex, Map<String, List<String>> transposedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String neighbor : transposedGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, transposedGraph, visited, component);
            }
        }
    }

    // Сортировка компонент в порядке топологической сортировки конденсации графа
    private static void sortComponentsByTopologicalOrder(List<List<String>> components,
                                                         Map<String, List<String>> graph) {
        // Создаем конденсацию графа
        Map<String, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }

        // Создаем граф конденсации
        int n = components.size();
        boolean[][] condensationGraph = new boolean[n][n];

        for (String vertex : graph.keySet()) {
            int fromComp = vertexToComponent.get(vertex);
            for (String neighbor : graph.get(vertex)) {
                int toComp = vertexToComponent.get(neighbor);
                if (fromComp != toComp) {
                    condensationGraph[fromComp][toComp] = true;
                }
            }
        }

        // Топологическая сортировка конденсации
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (condensationGraph[i][j]) {
                    inDegree[j]++;
                }
            }
        }

        // Порядок компонент по топологической сортировке
        List<Integer> order = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);

            for (int j = 0; j < n; j++) {
                if (condensationGraph[current][j]) {
                    inDegree[j]--;
                    if (inDegree[j] == 0) {
                        queue.add(j);
                    }
                }
            }
        }

        // Переупорядочиваем компоненты
        List<List<String>> sortedComponents = new ArrayList<>();
        for (int compIndex : order) {
            sortedComponents.add(components.get(compIndex));
        }

        components.clear();
        components.addAll(sortedComponents);
    }
}