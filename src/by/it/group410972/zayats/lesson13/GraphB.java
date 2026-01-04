package by.it.group410972.zayats.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        // Построение графа
        Map<Integer, List<Integer>> graph = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Статусы вершин: 0 = не посещена, 1 = в стеке DFS, 2 = полностью обработана
        Map<Integer, Integer> status = new HashMap<>();
        for (Integer v : graph.keySet()) {
            status.put(v, 0);
        }

        boolean hasCycle = false;
        for (Integer v : graph.keySet()) {
            if (status.get(v) == 0) {
                if (dfsCycle(v, graph, status)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsCycle(int v, Map<Integer, List<Integer>> graph, Map<Integer, Integer> status) {
        status.put(v, 1); // помечаем вершину как в стеке DFS
        for (Integer neighbor : graph.get(v)) {
            if (status.get(neighbor) == 1) return true; // цикл найден
            if (status.get(neighbor) == 0 && dfsCycle(neighbor, graph, status)) return true;
        }
        status.put(v, 2); // обработана
        return false;
    }
}
