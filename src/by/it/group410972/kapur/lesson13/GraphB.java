package by.it.group410972.kapur.lesson13;

import java.util.*;

public class GraphB {

  private static boolean hasCycle(Map<String, Set<String>> graph) {
    Map<String, Integer> color = new HashMap<>();
    for (String v : graph.keySet()) color.put(v, 0);

    for (String v : graph.keySet()) {
      if (color.get(v) == 0) {
        if (dfs(v, graph, color)) return true;
      }
    }
    return false;
  }

  private static boolean dfs(String v, Map<String, Set<String>> graph, Map<String, Integer> color) {
    color.put(v, 1); // серый
    for (String neighbor : graph.get(v)) {
      int c = color.get(neighbor);
      if (c == 1) return true; // цикл
      if (c == 0 && dfs(neighbor, graph, color)) return true;
    }
    color.put(v, 2); // черный
    return false;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();

    Map<String, Set<String>> graph = new HashMap<>();
    for (String e : input.split(",")) {
      String[] p = e.trim().split("->");
      String from = p[0].trim();
      String to = p[1].trim();

      graph.putIfAbsent(from, new TreeSet<>());
      graph.putIfAbsent(to, new TreeSet<>());
      graph.get(from).add(to);
    }

    System.out.println(hasCycle(graph) ? "yes" : "no");
  }
}
