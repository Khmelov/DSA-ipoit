package by.it.group410972.popova.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

          String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }


        Set<String> visited = new HashSet<>();
        Stack<String> order = new Stack<>();
        for (String v : vertices) {
            if (!visited.contains(v)) dfs1(v, graph, visited, order);
        }

        Map<String, List<String>> revGraph = new HashMap<>();
        for (String u : graph.keySet()) {
            for (String v : graph.get(u)) {
                revGraph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revGraph, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            for (String v : comp) System.out.print(v);
            System.out.println();
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, Stack<String> order) {
        visited.add(v);
        if (graph.containsKey(v)) {
            for (String to : graph.get(v)) {
                if (!visited.contains(to)) dfs1(to, graph, visited, order);
            }
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> revGraph, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        if (revGraph.containsKey(v)) {
            for (String to : revGraph.get(v)) {
                if (!visited.contains(to)) dfs2(to, revGraph, visited, comp);
            }
        }
    }
}
