package by.it.group510971.rudko.lesson13;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

//Поиск циклов в орграфе: если все вершины удается выстроить в топологический порядок, цикла нет.
public class GraphB {

    public static void main(String[] args) {
        Map<String, List<String>> graph = GraphReader.read(new Scanner(System.in));
        System.out.println(hasCycle(graph) ? "yes" : "no");
    }

    static boolean hasCycle(Map<String, List<String>> graph) {
        Map<String, Integer> indegree = new TreeMap<>();
        for (String v : graph.keySet()) {
            indegree.putIfAbsent(v, 0);
        }
        for (List<String> targets : graph.values()) {
            for (String t : targets) {
                indegree.merge(t, 1, Integer::sum);
            }
        }
        Deque<String> ready = new ArrayDeque<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) ready.add(e.getKey());
        }
        int processed = 0;
        while (!ready.isEmpty()) {
            String v = ready.poll();
            processed++;
            for (String t : graph.get(v)) {
                if (indegree.merge(t, -1, Integer::sum) == 0) ready.add(t);
            }
        }
        return processed < graph.size();   //вершины на цикле так и не стали готовыми
    }
}
