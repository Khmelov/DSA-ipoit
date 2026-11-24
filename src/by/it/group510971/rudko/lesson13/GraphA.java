package by.it.group510971.rudko.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

//Топологическая сортировка орграфа (алгоритм Кана).
//Из готовых вершин всегда берем лексикографически наименьшую.
public class GraphA {

    public static void main(String[] args) {
        Map<String, List<String>> graph = GraphReader.read(new Scanner(System.in));
        System.out.println(topologicalSort(graph));
    }

    static String topologicalSort(Map<String, List<String>> graph) {
        Map<String, Integer> indegree = new TreeMap<>();
        for (String v : graph.keySet()) {
            indegree.putIfAbsent(v, 0);
        }
        for (List<String> targets : graph.values()) {
            for (String t : targets) {
                indegree.merge(t, 1, Integer::sum);
            }
        }
        PriorityQueue<String> ready = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) ready.add(e.getKey());
        }
        List<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String v = ready.poll();
            order.add(v);
            for (String t : graph.get(v)) {
                int left = indegree.merge(t, -1, Integer::sum);
                if (left == 0) ready.add(t);
            }
        }
        return String.join(" ", order);
    }
}
