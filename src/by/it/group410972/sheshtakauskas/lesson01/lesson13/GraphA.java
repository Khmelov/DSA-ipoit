package by.it.group410972.sheshtakauskas.lesson01.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new TreeSet<>());
            graph.putIfAbsent(to, new TreeSet<>());

            if (graph.get(from).add(to)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }

            inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : inDegree.keySet()) {
            if (inDegree.get(v) == 0) {
                queue.add(v);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            String v = queue.poll();
            result.append(v).append(" ");

            for (String next : graph.get(v)) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        System.out.print(result.toString().trim());
    }
}
