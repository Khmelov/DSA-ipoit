package by.it.group410972.kulesh.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        Map<String, List<String>> graph = parseGraph(input);


        List<String> sorted = topologicalSort(graph);


        for (int i = 0; i < sorted.size(); i++) {
            System.out.print(sorted.get(i));
            if (i < sorted.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();


        String[] edges = input.split("\\s*,\\s*");

        for (String edge : edges) {

            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0];
            String to = vertices[1];


            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }


        for (List<String> list : graph.values()) {
            Collections.sort(list);
        }

        return graph;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();


        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);


        return topologicalSortKahn(graph, vertices);
    }


    private static List<String> topologicalSortKahn(Map<String, List<String>> graph, List<String> vertices) {
        List<String> result = new ArrayList<>();


        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : vertices) {
            for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }


        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }
}