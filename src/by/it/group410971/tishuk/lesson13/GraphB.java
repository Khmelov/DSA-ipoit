package by.it.group410971.tishuk.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        // --- построение графа ---
        Map<Integer, Set<Integer>> graph = new TreeMap<>();

        String[] parts = input.split(",");
        for (String part : parts) {
            part = part.trim(); // "1 -> 2"
            String[] arrow = part.split("->");
            if (arrow.length != 2) continue;

            int from = Integer.parseInt(arrow[0].trim());
            int to = Integer.parseInt(arrow[1].trim());

            graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new HashSet<>()); // вершина без исходящих ребер
        }

        // --- проверка на циклы ---
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();
        boolean hasCycle = false;

        for (int node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        // --- вывод ---
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(int node, Map<Integer, Set<Integer>> graph,
                               Set<Integer> visited, Set<Integer> recStack) {
        visited.add(node);
        recStack.add(node);

        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, graph, visited, recStack)) return true;
            } else if (recStack.contains(neighbor)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }
}
