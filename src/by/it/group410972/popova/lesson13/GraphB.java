package by.it.group410972.popova.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<Integer, List<Integer>> graph = new HashMap<>();
        Set<Integer> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            int from = Integer.parseInt(parts[0].trim());
            int to = Integer.parseInt(parts[1].trim());

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        boolean hasCycle = containsCycle(graph, vertices);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean containsCycle(Map<Integer, List<Integer>> graph, Set<Integer> vertices) {
        Map<Integer, Integer> state = new HashMap<>();

        for (int v : vertices) {
            state.put(v, 0);
        }

        for (int v : vertices) {
            if (state.get(v) == 0) {
                if (dfs(v, graph, state)) return true;
            }
        }
        return false;
    }

    private static boolean dfs(int v, Map<Integer, List<Integer>> graph, Map<Integer, Integer> state) {
        state.put(v, 1);
        if (graph.containsKey(v)) {
            for (int to : graph.get(v)) {
                if (state.get(to) == 1) return true; // цикл найден
                if (state.get(to) == 0 && dfs(to, graph, state)) return true;
            }
        }
        state.put(v, 2);
        return false;
    }
}
