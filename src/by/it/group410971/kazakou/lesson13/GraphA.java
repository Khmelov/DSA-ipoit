package by.it.group410971.kazakou.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) throws Exception {
        String input = readAll();
        if (input.isBlank()) {
            return;
        }
        Map<String, TreeSet<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
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
            graph.computeIfAbsent(from, k -> new TreeSet<>());
            graph.computeIfAbsent(to, k -> new TreeSet<>());
            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            }
            indegree.putIfAbsent(from, indegree.getOrDefault(from, 0));
        }

        TreeSet<String> zero = new TreeSet<>();
        for (String v : vertices) {
            if (indegree.getOrDefault(v, 0) == 0) {
                zero.add(v);
            }
        }

        StringBuilder out = new StringBuilder();
        while (!zero.isEmpty()) {
            String v = zero.pollFirst();
            if (out.length() > 0) {
                out.append(' ');
            }
            out.append(v);
            for (String to : graph.getOrDefault(v, new TreeSet<>())) {
                int deg = indegree.get(to) - 1;
                indegree.put(to, deg);
                if (deg == 0) {
                    zero.add(to);
                }
            }
        }
        System.out.print(out);
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
