package by.it.group410972.Pushkarev.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphB {
    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, Integer> color = new HashMap<>();
    static boolean hasCycle = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
        }

        // инициализация цветов
        for (String v : graph.keySet()) {
            color.put(v, 0);
        }

        for (String v : graph.keySet()) {
            if (color.get(v) == 0) {
                dfs(v);
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static void dfs(String v) {
        if (hasCycle) return;

        color.put(v, 1);

        for (String next : graph.get(v)) {
            if (color.get(next) == 0) {
                dfs(next);
            } else if (color.get(next) == 1) {
                hasCycle = true;
                return;
            }
        }

        color.put(v, 2);
    }
}
