package by.it.group510972.bahutskaya.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

// Ставим вершины так, чтобы стрелки шли вперёд. Если без разницы — по алфавиту.
public class TaskA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        parseGraph(input, graph, indegree);
        System.out.println(String.join(" ", order(graph, indegree)));
    }

    private static void parseGraph(String input, Map<String, List<String>> graph, Map<String, Integer> indegree) {
        if (input.trim().isEmpty()) {
            return;
        }

        // Удаляем пробелы и разделяем по запятым
        String[] edges = input.split("\\s*,\\s*");
        for (String edge : edges) {
            // Разделяем на вершины
            String[] vertices = edge.split("\\s*->\\s*");
            if (vertices.length < 2 || vertices[0].isEmpty() || vertices[1].isEmpty()) {
                continue;
            }
            String from = vertices[0];
            String to = vertices[1];

            // Добавляем ребро и считаем, сколько стрелок входит в вершину
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
            indegree.putIfAbsent(from, 0);
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        }
    }

    private static List<String> order(Map<String, List<String>> graph, Map<String, Integer> indegree) {
        // Сначала вершины, в которые никто не входит
        PriorityQueue<String> ready = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                ready.add(entry.getKey());
            }
        }

        // Снимаем вершину и уменьшаем счётчик у соседей
        List<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String vertex = ready.poll();
            order.add(vertex);
            for (String next : graph.getOrDefault(vertex, List.of())) {
                int degree = indegree.get(next) - 1;
                indegree.put(next, degree);
                if (degree == 0) {
                    ready.add(next);
                }
            }
        }
        return order;
    }
}
