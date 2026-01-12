package by.it.group410971.tishuk.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        // --- построение графа ---
        Map<String, Set<String>> graph = new TreeMap<>();
        Map<String, Set<String>> reverseGraph = new TreeMap<>();

        String[] parts = input.split(",");
        for (String part : parts) {
            part = part.trim();
            String[] arrow = part.split("->");
            if (arrow.length != 2) continue;

            String from = arrow[0].trim();
            String to = arrow[1].trim();

            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new TreeSet<>()); // вершина без исходящих ребер

            reverseGraph.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
            reverseGraph.computeIfAbsent(from, k -> new TreeSet<>());
        }

        // --- первый проход DFS для топологического порядка ---
        Set<String> visited = new HashSet<>();
        LinkedList<String> finishOrder = new LinkedList<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, finishOrder);
            }
        }

        // --- второй проход DFS по обратному графу ---
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        while (!finishOrder.isEmpty()) {
            String node = finishOrder.removeLast();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsCollect(node, reverseGraph, visited, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                sccList.add(component);
            }
        }

        // --- вывод ---
        for (List<String> component : sccList) {
            StringBuilder sb = new StringBuilder();
            for (String v : component) sb.append(v);
            System.out.println(sb);
        }
    }

    private static void dfs(String node, Map<String, Set<String>> graph,
                            Set<String> visited, LinkedList<String> finishOrder) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) dfs(neighbor, graph, visited, finishOrder);
        }
        finishOrder.add(node);
    }

    private static void dfsCollect(String node, Map<String, Set<String>> graph,
                                   Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) dfsCollect(neighbor, graph, visited, component);
        }
    }
}
