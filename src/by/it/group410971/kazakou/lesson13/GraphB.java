package by.it.group410971.kazakou.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GraphB {

    public static void main(String[] args) throws Exception {
        String input = readAll();
        if (input.isBlank()) {
            return;
        }
        Map<String, TreeSet<String>> graph = new HashMap<>();
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
        }

        Map<String, Integer> color = new HashMap<>();
        TreeSet<String> ordered = new TreeSet<>(vertices);
        boolean hasCycle = false;
        for (String v : ordered) {
            if (color.getOrDefault(v, 0) == 0 && dfs(v, graph, color)) {
                hasCycle = true;
                break;
            }
        }
        System.out.print(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v, Map<String, TreeSet<String>> graph, Map<String, Integer> color) {
        color.put(v, 1);
        for (String to : graph.getOrDefault(v, new TreeSet<>())) {
            int state = color.getOrDefault(to, 0);
            if (state == 1) {
                return true;
            }
            if (state == 0 && dfs(to, graph, color)) {
                return true;
            }
        }
        color.put(v, 2);
        return false;
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
