package by.it.group410971.galdytskaya.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Set<String>> reverseGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if(parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new HashSet<>()).add(from);
        }

        for (String v : vertices) {
            graph.putIfAbsent(v, new HashSet<>());
            reverseGraph.putIfAbsent(v, new HashSet<>());
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        for (String v : sortedVertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, reverseGraph, visited, component);
                Collections.sort(component);
                sccList.add(component);
            }
        }

        for (List<String> comp : sccList) {
            StringBuilder sb = new StringBuilder();
            for (String vertex : comp) {
                sb.append(vertex);
            }
            System.out.println(sb);
        }
    }

    private static void dfs1(String v, Map<String, Set<String>> graph, Set<String> visited, Deque<String> stack) {
        visited.add(v);
        List<String> neighbors = new ArrayList<>(graph.getOrDefault(v, Collections.emptySet()));
        Collections.sort(neighbors);
        for (String w : neighbors) {
            if (!visited.contains(w)) {
                dfs1(w, graph, visited, stack);
            }
        }
        stack.push(v);
    }

    private static void dfs2(String v, Map<String, Set<String>> reverseGraph, Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        List<String> neighbors = new ArrayList<>(reverseGraph.getOrDefault(v, Collections.emptySet()));
        Collections.sort(neighbors);
        for (String w : neighbors) {
            if (!visited.contains(w)) {
                dfs2(w, reverseGraph, visited, component);
            }
        }
    }
}
