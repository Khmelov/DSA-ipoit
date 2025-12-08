package by.it.group410971.galdytskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        Map<String, Set<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        }

        boolean hasCycle = false;
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();

        for (String v : vertices) {
            if (!visited.contains(v)) {
                if (dfsCycle(v, graph, visited, inStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsCycle(String node, Map<String, Set<String>> graph,
                                    Set<String> visited, Set<String> inStack) {
        visited.add(node);
        inStack.add(node);

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    if (dfsCycle(neighbor, graph, visited, inStack)) return true;
                } else if (inStack.contains(neighbor)) {
                    return true;
                }
            }
        }

        inStack.remove(node);
        return false;
    }
}
