package by.it.group410971.nesteruk.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());

            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reverseGraph.putIfAbsent(from, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reverseGraph, visited, component);
                Collections.sort(component);
                sccList.add(component);
            }
        }

        Map<List<String>, List<List<String>>> componentGraph = buildComponentGraph(sccList, graph);
        List<List<String>> sortedComponents = topologicalSortComponents(componentGraph, sccList);

        for (List<String> component : sortedComponents) {
            System.out.println(String.join("", component));
        }
    }

    private static Map<List<String>, List<List<String>>> buildComponentGraph(List<List<String>> sccList, Map<String, List<String>> graph) {
        Map<List<String>, List<List<String>>> componentGraph = new HashMap<>();
        Map<String, List<String>> nodeToComponent = new HashMap<>();

        for (List<String> component : sccList) {
            componentGraph.put(component, new ArrayList<>());
            for (String node : component) {
                nodeToComponent.put(node, component);
            }
        }

        for (List<String> component : sccList) {
            for (String node : component) {
                for (String neighbor : graph.get(node)) {
                    List<String> neighborComponent = nodeToComponent.get(neighbor);
                    if (!component.equals(neighborComponent) && !componentGraph.get(component).contains(neighborComponent)) {
                        componentGraph.get(component).add(neighborComponent);
                    }
                }
            }
        }

        return componentGraph;
    }

    private static List<List<String>> topologicalSortComponents(Map<List<String>, List<List<String>>> componentGraph, List<List<String>> sccList) {
        Map<List<String>, Integer> inDegree = new HashMap<>();
        for (List<String> component : sccList) {
            inDegree.put(component, 0);
        }

        for (List<String> component : sccList) {
            for (List<String> neighbor : componentGraph.get(component)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<List<String>> queue = new LinkedList<>();
        for (List<String> component : sccList) {
            if (inDegree.get(component) == 0) {
                queue.offer(component);
            }
        }

        List<List<String>> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            List<String> component = queue.poll();
            result.add(component);

            for (List<String> neighbor : componentGraph.get(component)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }

    private static void dfsFirstPass(String node, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }
        stack.push(node);
    }

    private static void dfsSecondPass(String node, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : reverseGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reverseGraph, visited, component);
            }
        }
    }
}