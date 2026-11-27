package by.it.group510972.bahutskaya.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

// Группы вершин, из которых можно ходить друг к другу. Сначала исток, потом сток.
public class TaskC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverse = new HashMap<>();
        parseGraph(input, graph, reverse);
        System.out.println(components(graph, reverse));
    }

    private static void parseGraph(String input, Map<String, List<String>> graph, Map<String, List<String>> reverse) {
        if (input.trim().isEmpty()) {
            return;
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

            // Прямое ребро и обратное, оно нужно для второго обхода
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
            reverse.putIfAbsent(to, new ArrayList<>());
            reverse.putIfAbsent(from, new ArrayList<>());
            reverse.get(to).add(from);
        }
    }

    private static String components(Map<String, List<String>> graph, Map<String, List<String>> reverse) {
        // Запоминаем, в каком порядке вершины заканчивают обход
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String vertex : graph.keySet()) {
            finish(vertex, graph, visited, order);
        }

        // По обратным рёбрам собираем компоненты
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> componentOf = new HashMap<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);
            if (visited.contains(vertex)) {
                continue;
            }
            List<String> component = new ArrayList<>();
            collect(vertex, reverse, visited, component);
            Collections.sort(component);
            int id = components.size();
            components.add(component);
            for (String member : component) {
                componentOf.put(member, id);
            }
        }

        // Сжимаем каждую компоненту в одну вершину
        int count = components.size();
        List<Set<Integer>> componentGraph = new ArrayList<>();
        int[] indegree = new int[count];
        for (int i = 0; i < count; i++) {
            componentGraph.add(new HashSet<>());
        }
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            int from = componentOf.get(entry.getKey());
            for (String next : entry.getValue()) {
                int to = componentOf.get(next);
                if (from != to && componentGraph.get(from).add(to)) {
                    indegree[to]++;
                }
            }
        }

        // Выводим компоненты от истока к стоку
        PriorityQueue<Integer> ready = new PriorityQueue<>((left, right) -> componentName(components.get(left)).compareTo(componentName(components.get(right))));
        for (int i = 0; i < count; i++) {
            if (indegree[i] == 0) {
                ready.add(i);
            }
        }
        StringBuilder out = new StringBuilder();
        while (!ready.isEmpty()) {
            int id = ready.poll();
            if (out.length() > 0) {
                out.append('\n');
            }
            out.append(componentName(components.get(id)));
            for (int next : componentGraph.get(id)) {
                indegree[next]--;
                if (indegree[next] == 0) {
                    ready.add(next);
                }
            }
        }
        return out.toString();
    }

    private static String componentName(List<String> component) {
        StringBuilder name = new StringBuilder();
        for (String vertex : component) {
            name.append(vertex);
        }
        return name.toString();
    }

    private static void finish(String vertex, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        if (!visited.add(vertex)) {
            return;
        }
        for (String next : graph.getOrDefault(vertex, List.of())) {
            finish(next, graph, visited, order);
        }
        // Вершина готова, когда все исходящие уже пройдены
        order.add(vertex);
    }

    private static void collect(String vertex, Map<String, List<String>> reverse, Set<String> visited, List<String> component) {
        if (!visited.add(vertex)) {
            return;
        }
        component.add(vertex);
        for (String next : reverse.getOrDefault(vertex, List.of())) {
            collect(next, reverse, visited, component);
        }
    }
}
