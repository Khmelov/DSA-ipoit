package by.it.group410971.lukashonok.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GraphC {

    public static void main(String[] args) throws Exception {
        String input = readAll();
        if (input.isBlank()) {
            return;
        }
        Map<String, TreeSet<String>> graph = new HashMap<>();
        Map<String, TreeSet<String>> reverse = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        for (String part : input.split(",")) {
            String edge = part.trim();
            if (edge.isEmpty()) {
                continue;
            }
            int arrow = edge.indexOf("->");
            if (arrow < 0) {
                continue;
            }
            String from = edge.substring(0, arrow).trim();
            String to = edge.substring(arrow + 2).trim();
            vertices.add(from);
            vertices.add(to);
            graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new TreeSet<>());
            reverse.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
            reverse.computeIfAbsent(from, k -> new TreeSet<>());
        }

        TreeSet<String> ordered = new TreeSet<>(vertices);
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String v : ordered) {
            if (!visited.contains(v)) {
                dfsOrder(v, graph, visited, order);
            }
        }

        visited.clear();
        StringBuilder out = new StringBuilder();
        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (visited.contains(v)) {
                continue;
            }
            List<String> component = new ArrayList<>();
            dfsCollect(v, reverse, visited, component);
            component.sort(String::compareTo);
            for (String node : component) {
                out.append(node);
            }
            out.append('\n');
        }
        if (out.length() > 0) {
            out.setLength(out.length() - 1);
        }
        System.out.print(out);
    }

    private static void dfsOrder(String v, Map<String, TreeSet<String>> graph, Set<String> visited, List<String> order) {
        visited.add(v);
        for (String to : graph.getOrDefault(v, new TreeSet<>())) {
            if (!visited.contains(to)) {
                dfsOrder(to, graph, visited, order);
            }
        }
        order.add(v);
    }

    private static void dfsCollect(String v, Map<String, TreeSet<String>> graph, Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        for (String to : graph.getOrDefault(v, new TreeSet<>())) {
            if (!visited.contains(to)) {
                dfsCollect(to, graph, visited, component);
            }
        }
    }

    private static String readAll() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString().trim();
    }
}
