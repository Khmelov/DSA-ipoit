package by.it.group410972.mezenova.lesson13;

import java.util.*;

public class GraphC {
    private Map<String, List<String>> graph = new HashMap<>();
    private Map<String, List<String>> reverseGraph = new HashMap<>();
    private Set<String> visited = new HashSet<>();
    private List<String> order = new ArrayList<>();
    private List<Set<String>> components = new ArrayList<>();

    public void addEdge(String from, String to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        graph.putIfAbsent(to, new ArrayList<>());
        reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        reverseGraph.putIfAbsent(from, new ArrayList<>());
    }

    private void dfs(String node) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) dfs(neighbor);
        }
        order.add(node);
    }

    private void reverseDfs(String node, Set<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : reverseGraph.get(node)) {
            if (!visited.contains(neighbor)) reverseDfs(neighbor, component);
        }
    }

    public void findSCC() {
        visited.clear();
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) dfs(node);
        }
        visited.clear();
        Collections.reverse(order);
        for (String node : order) {
            if (!visited.contains(node)) {
                Set<String> component = new TreeSet<>();
                reverseDfs(node, component);
                components.add(component);
            }
        }
    }

    public void printComponents() {
        for (Set<String> comp : components) {
            comp.forEach(System.out::print);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        GraphC g = new GraphC();
        String[] edges = line.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            g.addEdge(parts[0], parts[1]);
        }
        g.findSCC();
        g.printComponents();
    }
}