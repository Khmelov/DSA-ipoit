package by.it.group410971.usovskiy.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class GraphA {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        // граф: списки смежности
        Map<String, List<String>> graph = new HashMap<>();
        // количество входящих рёбер
        Map<String, Integer> indegree = new HashMap<>();
        // множество всех вершин
        Set<String> vertices = new HashSet<>();

        // парсим рёбра: "u -> v"
        String[] edges = line.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] uv = edge.split("->");
            if (uv.length != 2) continue;

            String from = uv[0].trim();
            String to   = uv[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            // убедимся, что источник тоже есть в карте степеней
            indegree.putIfAbsent(from, indegree.getOrDefault(from, 0));
        }

        // очередь вершин с нулевой степенью — в лексикографическом порядке
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.getOrDefault(v, 0) == 0) {
                pq.add(v);
            }
        }

        List<String> order = new ArrayList<>();

        // Алгоритм Кана
        while (!pq.isEmpty()) {
            String v = pq.poll();
            order.add(v);

            List<String> neighbours = graph.get(v);
            if (neighbours == null) continue;

            for (String u : neighbours) {
                int d = indegree.get(u) - 1;
                indegree.put(u, d);
                if (d == 0) {
                    pq.add(u);
                }
            }
        }

        // вывод результата
        System.out.println(String.join(" ", order));
    }
}
