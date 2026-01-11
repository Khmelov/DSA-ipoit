package by.it.group410972.stankevich.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        Map<String, List<String>> graph = parseGraph(input);


        List<List<String>> scc = kosaraju(graph);


        sortComponents(scc);

        for (int i = 0; i < scc.size(); i++) {
            for (String vertex : scc.get(i)) {
                System.out.print(vertex);
            }
            if (i < scc.size() - 1) {
                System.out.println();
            }
        }
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input.trim().isEmpty()) {
            return graph;
        }


        String[] edges = input.split("\\s*,\\s*");

        for (String edge : edges) {

            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0];
            String to = vertices[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        return graph;
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph) {

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();


        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, order);
            }
        }


        Map<String, List<String>> transposed = transposeGraph(graph);


        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, transposed, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, List<String> order) {
        visited.add(vertex);


        List<String> neighbors = new ArrayList<>(graph.getOrDefault(vertex, new ArrayList<>()));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, order);
            }
        }

        order.add(vertex);
    }

    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            transposed.putIfAbsent(from, new ArrayList<>());

            for (String to : entry.getValue()) {
                transposed.putIfAbsent(to, new ArrayList<>());
                transposed.get(to).add(from);
            }
        }

        return transposed;
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> transposed,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);


        List<String> neighbors = new ArrayList<>(transposed.getOrDefault(vertex, new ArrayList<>()));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, transposed, visited, component);
            }
        }
    }

    private static void sortComponents(List<List<String>> scc) {

        for (List<String> component : scc) {
            Collections.sort(component);
        }


    }
}
