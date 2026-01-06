package by.it.group410972.sheshtakauskas.lesson01.lesson13;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GraphC {
    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, List<String>> reverse = new HashMap<>();

    static Set<String> visited = new HashSet<>();
    static Deque<String> order = new ArrayDeque<>();
    static List<List<String>> components = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        String[] edges = input.split(",\\s*");
        for (String e : edges) {
            String[] p = e.split("->");
            String from = p[0];
            String to = p[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            reverse.putIfAbsent(from, new ArrayList<>());
            reverse.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            reverse.get(to).add(from);
        }

        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs1(v);
            }
        }

        visited.clear();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            StringBuilder sb = new StringBuilder();
            for (String s : comp) sb.append(s);
            System.out.println(sb);
        }
    }

    private static void dfs1(String v) {
        visited.add(v);
        for (String to : graph.get(v)) {
            if (!visited.contains(to)) {
                dfs1(to);
            }
        }
        order.push(v);
    }

    private static void dfs2(String v, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String to : reverse.get(v)) {
            if (!visited.contains(to)) {
                dfs2(to, comp);
            }
        }
    }
}
