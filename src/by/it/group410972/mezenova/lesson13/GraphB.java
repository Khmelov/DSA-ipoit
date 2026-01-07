package by.it.group410972.mezenova.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }

        boolean hasCycle = hasCycle(graph, vertices);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> visited = new HashMap<>();
        // 0 - не посещена, 1 - в процессе посещения, 2 - полностью обработана

        for (String vertex : vertices) {
            visited.put(vertex, 0);
        }

        for (String vertex : vertices) {
            if (visited.get(vertex) == 0) {
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph,
                                       Map<String, Integer> visited) {
        visited.put(vertex, 1);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (visited.get(neighbor) == 0) {
                    if (dfsHasCycle(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        visited.put(vertex, 2);
        return false;
    }
}