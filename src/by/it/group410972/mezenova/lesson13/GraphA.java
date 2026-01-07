package by.it.group410972.mezenova.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            inDegree.put(to, inDegree.get(to) + 1);
        }

        List<String> result = topologicalSort(graph, inDegree);

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            if (graph.containsKey(vertex)) {
                List<String> neighbors = graph.get(vertex);

                Collections.sort(neighbors);

                for (String neighbor : neighbors) {
                    int degree = inDegree.get(neighbor) - 1;
                    inDegree.put(neighbor, degree);

                    if (degree == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        if (result.size() != inDegree.size()) {
            throw new RuntimeException("Граф содержит цикл!");
        }

        return result;
    }
}