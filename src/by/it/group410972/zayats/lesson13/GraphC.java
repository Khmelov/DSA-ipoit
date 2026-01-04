package by.it.group410972.zayats.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        // 1. Построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);

            revGraph.putIfAbsent(from, new ArrayList<>());
            revGraph.putIfAbsent(to, new ArrayList<>());
            revGraph.get(to).add(from);
        }

        // Лексикографический порядок соседей
        for (List<String> neighbors : graph.values()) Collections.sort(neighbors);
        for (List<String> neighbors : revGraph.values()) Collections.sort(neighbors);

        // 2. DFS для топологической сортировки
        Set<String> visited = new HashSet<>();
        List<String> topo = new ArrayList<>();

        for (String v : graph.keySet()) {
            if (!visited.contains(v)) dfsTopo(v, graph, visited, topo);
        }

        Collections.reverse(topo); // порядок обратный завершению DFS

        // 3. DFS по обратному графу для выделения КСО
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        for (String v : topo) {
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfsComponent(v, revGraph, visited, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                sccList.add(component);
            }
        }

        // 4. Вывод
        for (List<String> comp : sccList) {
            for (String node : comp) System.out.print(node);
            System.out.println();
        }
    }

    private static void dfsTopo(String v, Map<String, List<String>> graph, Set<String> visited, List<String> topo) {
        visited.add(v);
        for (String neighbor : graph.get(v)) {
            if (!visited.contains(neighbor)) dfsTopo(neighbor, graph, visited, topo);
        }
        topo.add(v);
    }

    private static void dfsComponent(String v, Map<String, List<String>> graph, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String neighbor : graph.get(v)) {
            if (!visited.contains(neighbor)) dfsComponent(neighbor, graph, visited, comp);
        }
    }
}
