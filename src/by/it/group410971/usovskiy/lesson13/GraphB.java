package by.it.group410971.usovskiy.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            // пустой граф — циклов нет
            System.out.println("no");
            return;
        }

        // граф (списки смежности) и входящие степени
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // парсим рёбра
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
            // убедимся, что from тоже есть в карте степеней (может быть 0)
            indegree.putIfAbsent(from, indegree.getOrDefault(from, 0));
        }

        // Алгоритм Кана: если удаётся "снять" все вершины — цикла нет.
        Deque<String> q = new ArrayDeque<>();
        for (String v : vertices) {
            if (indegree.getOrDefault(v, 0) == 0) {
                q.add(v);
            }
        }

        int visited = 0;
        while (!q.isEmpty()) {
            String v = q.removeFirst();
            visited++;

            List<String> neigh = graph.get(v);
            if (neigh == null) continue;

            for (String u : neigh) {
                int d = indegree.get(u) - 1;
                indegree.put(u, d);
                if (d == 0) {
                    q.add(u);
                }
            }
        }

        // если количество снятых вершин меньше общего — есть цикл
        if (visited == vertices.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}
