package by.it.group410972.popova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<Integer, List<Integer>> graph = new HashMap<>();
        Set<Integer> vertices = new TreeSet<>();

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

        Map<Integer, Integer> indegree = new HashMap<>();
        for (int v : vertices) indegree.put(v, 0);
        for (List<Integer> adj : graph.values()) {
            for (int to : adj) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int v : vertices) {
            if (indegree.get(v) == 0) pq.add(v);
        }

        List<Integer> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            int v = pq.poll();
            result.add(v);
            if (graph.containsKey(v)) {
                for (int to : graph.get(v)) {
                    indegree.put(to, indegree.get(to) - 1);
                    if (indegree.get(to) == 0) pq.add(to);
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(" ");
        }
    }
}
